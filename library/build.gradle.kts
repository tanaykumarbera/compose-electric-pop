import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import java.net.URI

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.kover)
    alias(libs.plugins.dokka)
}

val gitCommitSha: String by lazy {
    runCatching {
        ProcessBuilder("git", "rev-parse", "HEAD")
            .directory(rootProject.projectDir)
            .redirectErrorStream(true)
            .start()
            .inputStream
            .bufferedReader()
            .readText()
            .trim()
            .ifBlank { "main" }
    }.getOrDefault("main")
}

dokka {
    moduleName.set("compose-electric-pop")
    dokkaPublications.html {
        suppressInheritedMembers.set(true)
        suppressObviousFunctions.set(true)
    }
    dokkaSourceSets.configureEach {
        documentedVisibilities.set(setOf(VisibilityModifier.Public))
        skipDeprecated.set(false)
        skipEmptyPackages.set(true)
        sourceLink {
            localDirectory.set(rootProject.projectDir)
            remoteUrl.set(URI("https://github.com/tanaykumarbera/compose-electric-pop/blob/$gitCommitSha"))
            remoteLineSuffix.set("#L")
        }
    }
    pluginsConfiguration.html {
        templatesDir.set(layout.projectDirectory.dir("dokka/templates").asFile)
        customStyleSheets.from(layout.projectDirectory.file("dokka/assets/star-button.css"))
        footerMessage.set("Copyright © 2026 @tanaykumarbera · Released under Apache 2.0")
    }
}

// ---------------------------------------------------------------------------
// syncScreenshotKdoc — keeps the screenshot table in each component's KDoc in
// sync with the Roborazzi snapshots on disk. Convention-based, idempotent.
// ---------------------------------------------------------------------------

val screenshotPagesBaseUrl = "https://tanaykumarbera.github.io/compose-electric-pop/snapshots"
val screenshotBlockStart = "<!-- screenshots:start (auto-generated, do not edit) -->"
val screenshotBlockEnd = "<!-- screenshots:end -->"

fun upsertScreenshotBlock(
    source: String,
    component: String,
    tableLines: List<String>,
): String {
    val newline = if (source.contains("\r\n")) "\r\n" else "\n"
    val lines = source.split(newline).toMutableList()

    val funLineIdx = lines.indexOfFirst { it.startsWith("fun $component(") }
    require(funLineIdx >= 0) { "syncScreenshotKdoc: cannot locate `fun $component(`" }

    var i = funLineIdx - 1
    while (i >= 0) {
        val trimmed = lines[i].trim()
        if (trimmed.startsWith("@") || trimmed.isEmpty()) {
            i--
        } else {
            break
        }
    }
    require(i >= 0 && lines[i].trim() == "*/") {
        "syncScreenshotKdoc: no KDoc found above `fun $component(`"
    }
    val kdocEndLine = i

    var j = kdocEndLine - 1
    while (j >= 0 && !lines[j].trim().startsWith("/**")) j--
    require(j >= 0) { "syncScreenshotKdoc: malformed KDoc above `fun $component(`" }
    val kdocStartLine = j

    val indent = lines[kdocStartLine].substringBefore("/**")
    val starPrefix = "$indent * "

    var blockStart = -1
    var blockEnd = -1
    for (k in kdocStartLine..kdocEndLine) {
        if (blockStart == -1 && "<!-- screenshots:start" in lines[k]) blockStart = k
        if (blockStart != -1 && screenshotBlockEnd in lines[k]) {
            blockEnd = k
            break
        }
    }

    val blockLines = buildList {
        add("$starPrefix$screenshotBlockStart")
        tableLines.forEach { add("$starPrefix$it") }
        add("$starPrefix$screenshotBlockEnd")
    }

    return if (blockStart != -1 && blockEnd != -1) {
        for (idx in blockEnd downTo blockStart) lines.removeAt(idx)
        lines.addAll(blockStart, blockLines)
        lines.joinToString(newline)
    } else {
        // Insert before `*/` with a blank ` *` separator
        val insertion = listOf("$indent *") + blockLines
        lines.addAll(kdocEndLine, insertion)
        lines.joinToString(newline)
    }
}

tasks.register("syncScreenshotKdoc") {
    group = "documentation"
    description = "Sync screenshot tables in component KDoc from desktopTest snapshots."

    val snapshotsDir = layout.projectDirectory.dir("src/desktopTest/snapshots")
    val componentRoot = layout.projectDirectory.dir("src/commonMain/kotlin/co/tanay/electricpop")
    val tiers = listOf("foundation", "composite", "chart")

    inputs.dir(snapshotsDir).withPropertyName("snapshots")
    tiers.forEach { tier ->
        inputs.dir(componentRoot.dir(tier)).withPropertyName("components-$tier")
    }
    outputs.upToDateWhen { false }

    doLast {
        val snapshotPattern = Regex("""^(Pop\w+)_(\w+)_(light|dark)\.png$""")
        val grouped = sortedMapOf<String, java.util.SortedMap<String, MutableMap<String, String>>>()
        snapshotsDir.asFile.listFiles().orEmpty().asSequence()
            .filter { it.isFile }
            .forEach { file ->
                val match = snapshotPattern.matchEntire(file.name) ?: return@forEach
                val (component, scenario, theme) = match.destructured
                grouped
                    .getOrPut(component) { sortedMapOf() }
                    .getOrPut(scenario) { mutableMapOf() }[theme] = file.name
            }

        val componentFiles = tiers
            .flatMap { tier -> componentRoot.dir(tier).asFile.listFiles().orEmpty().toList() }
            .filter { it.isFile && it.extension == "kt" && it.name.startsWith("Pop") }

        var updated = 0
        var withoutSnapshots = 0
        componentFiles.forEach { file ->
            val component = file.nameWithoutExtension
            val scenarios = grouped[component]
            if (scenarios.isNullOrEmpty()) {
                withoutSnapshots++
                return@forEach
            }

            val tableLines = buildList {
                add("| Scenario | Light | Dark |")
                add("| --- | --- | --- |")
                scenarios.forEach { (scenario, themes) ->
                    val light = themes["light"]?.let { "![]($screenshotPagesBaseUrl/$it)" } ?: "—"
                    val dark = themes["dark"]?.let { "![]($screenshotPagesBaseUrl/$it)" } ?: "—"
                    add("| $scenario | $light | $dark |")
                }
            }

            val original = file.readText()
            val rewritten = upsertScreenshotBlock(original, component, tableLines)
            if (rewritten != original) {
                file.writeText(rewritten)
                updated++
            }
        }

        logger.lifecycle(
            "syncScreenshotKdoc: scanned ${componentFiles.size} files, " +
                "updated $updated, $withoutSnapshots had no matching snapshots",
        )
    }
}

// ---------------------------------------------------------------------------
// checkScreenshotPresence — guardrail asserting every component file has a
// matching pair of light + dark Roborazzi snapshots. Filename-based: a file
// PopXxx.kt is checked iff it declares a top-level public `@Composable fun
// PopXxx(`. Sibling public composables in the same file (e.g. PopIconButton
// in PopButton.kt) are implicitly covered by the parent's snapshot — see
// SESSION-RESUME step 5 decision log. Allowlist at library/screenshot-allowlist.txt.
// ---------------------------------------------------------------------------

fun declaresPublicComposable(source: String, name: String): Boolean {
    val funPattern = Regex("""^fun ${Regex.escape(name)}\s*[(<]""")
    val lines = source.lines()
    val funIdx = lines.indexOfFirst { funPattern.containsMatchIn(it) }
    if (funIdx < 0) return false
    var i = funIdx - 1
    while (i >= 0) {
        val trimmed = lines[i].trim()
        if (trimmed.isEmpty()) {
            i--
            continue
        }
        if (trimmed.startsWith("@Composable")) return true
        if (trimmed.startsWith("@")) {
            i--
            continue
        }
        return false
    }
    return false
}

tasks.register("checkScreenshotPresence") {
    group = "verification"
    description = "Assert each Pop component file has matching light + dark Roborazzi snapshots."

    val snapshotsDir = layout.projectDirectory.dir("src/desktopTest/snapshots")
    val componentRoot = layout.projectDirectory.dir("src/commonMain/kotlin/co/tanay/electricpop")
    val allowlistFile = layout.projectDirectory.file("screenshot-allowlist.txt")
    val tiers = listOf("foundation", "composite", "chart")

    inputs.dir(snapshotsDir).withPropertyName("snapshots")
    tiers.forEach { tier ->
        inputs.dir(componentRoot.dir(tier)).withPropertyName("components-$tier")
    }
    inputs.files(allowlistFile).withPropertyName("allowlist").optional(true)

    doLast {
        val allowlist = if (allowlistFile.asFile.exists()) {
            allowlistFile.asFile.readLines()
                .map { it.substringBefore('#').trim() }
                .filter { it.isNotEmpty() }
                .toSet()
        } else {
            emptySet()
        }

        val snapshotNames = snapshotsDir.asFile.listFiles().orEmpty()
            .filter { it.isFile && it.extension == "png" }
            .map { it.name }

        val componentFiles = tiers
            .flatMap { tier -> componentRoot.dir(tier).asFile.listFiles().orEmpty().toList() }
            .filter { it.isFile && it.extension == "kt" && it.name.startsWith("Pop") }

        val components = componentFiles
            .mapNotNull { file ->
                val name = file.nameWithoutExtension
                if (declaresPublicComposable(file.readText(), name)) name else null
            }
            .sorted()

        val misses = mutableListOf<String>()
        components.forEach { component ->
            if (component in allowlist) return@forEach
            val prefix = "${component}_"
            val hasLight = snapshotNames.any { it.startsWith(prefix) && it.endsWith("_light.png") }
            val hasDark = snapshotNames.any { it.startsWith(prefix) && it.endsWith("_dark.png") }
            when {
                !hasLight && !hasDark -> misses += "$component (no _light, no _dark)"
                !hasLight -> misses += "$component (no _light)"
                !hasDark -> misses += "$component (no _dark)"
            }
        }

        if (misses.isNotEmpty()) {
            throw GradleException("Missing snapshots: ${misses.joinToString(", ")}")
        }

        logger.lifecycle(
            "checkScreenshotPresence: ${components.size} components verified " +
                "(${allowlist.size} allowlisted)",
        )
    }
}

// ---------------------------------------------------------------------------
// generateSkillManifest / checkSkillManifest — keep the consumer Agent Skill's
// component cheat-sheet (skills/electric-pop/reference/components.md) in sync
// with the public component source. Same convention-based, drift-guarded shape
// as syncScreenshotKdoc / checkScreenshotPresence above. The whole file is
// generated, so checkSkillManifest compares the full rendered output.
// ---------------------------------------------------------------------------

val skillApiBaseUrl = "https://tanaykumarbera.github.io/compose-electric-pop/api/"
val skillManifestTiers = listOf("foundation" to "Foundation", "composite" to "Composite", "chart" to "Chart")

// Extract (summary, paramNames) from the KDoc above the top-level declaration of
// `component` (a fun / object / data class / enum, etc.) in `source`.
fun extractComponentDoc(source: String, component: String): Pair<String, List<String>> {
    val lines = source.lines()
    val declRegex = Regex(
        """^\s*(fun|object|class|data class|enum class|sealed class|interface|val|var)\s+${Regex.escape(component)}\b""",
    )
    val declIdx = lines.indexOfFirst { declRegex.containsMatchIn(it) }
    if (declIdx < 0) return "" to emptyList()

    // Walk up past annotations / blank lines to the KDoc close `*/`.
    var i = declIdx - 1
    while (i >= 0 && (lines[i].trim().isEmpty() || lines[i].trim().startsWith("@"))) i--
    if (i < 0 || lines[i].trim() != "*/") return "" to emptyList()
    val kdocEnd = i
    var j = kdocEnd - 1
    while (j >= 0 && !lines[j].trim().startsWith("/**")) j--
    if (j < 0) return "" to emptyList()
    val kdocStart = j

    val content = (kdocStart..kdocEnd).map { idx ->
        lines[idx].trim().removePrefix("/**").removePrefix("*/").removePrefix("*").trim()
    }

    val params = content
        .mapNotNull { Regex("""^@param\s+(\w+)""").find(it)?.groupValues?.get(1) }
        .filter { it != "modifier" }

    val summaryLines = buildList {
        for (line in content) {
            if (line.startsWith("@")) break
            if (line.isEmpty()) {
                if (isNotEmpty()) break else continue
            }
            add(line)
        }
    }
    var summary = summaryLines.joinToString(" ")
        .replace(Regex("""\[([^\]]+)]"""), "$1") // KDoc links [X] -> X
        .replace(Regex("""\s+"""), " ")
        .trim()
    val dot = summary.indexOf(". ")
    if (dot >= 0) summary = summary.substring(0, dot + 1)
    summary = summary.replace("|", "\\|")
    return summary to params
}

fun buildSkillManifest(componentRoot: Directory, snapshotsDir: Directory): String {
    val snapshotPattern = Regex("""^(Pop\w+)_(\w+)_(light|dark)\.png$""")
    val snapshots = sortedMapOf<String, java.util.SortedMap<String, MutableMap<String, String>>>()
    snapshotsDir.asFile.listFiles().orEmpty().asSequence()
        .filter { it.isFile }
        .forEach { file ->
            val m = snapshotPattern.matchEntire(file.name) ?: return@forEach
            val (component, scenario, theme) = m.destructured
            snapshots.getOrPut(component) { sortedMapOf() }
                .getOrPut(scenario) { mutableMapOf() }[theme] = file.name
        }

    val sb = StringBuilder()
    sb.appendLine("<!-- Generated by ./gradlew :library:generateSkillManifest — do not edit by hand. -->")
    sb.appendLine("# Electric Pop — Component Reference")
    sb.appendLine()
    sb.appendLine(
        "Authoritative API (full signatures, every parameter, inline light + dark " +
            "screenshots): <$skillApiBaseUrl>",
    )

    skillManifestTiers.forEach { (tier, display) ->
        val files = componentRoot.dir(tier).asFile.listFiles().orEmpty()
            .filter { it.isFile && it.extension == "kt" && it.name.startsWith("Pop") }
            .sortedBy { it.name }
        sb.appendLine()
        sb.appendLine("## $display (${files.size})")
        sb.appendLine()
        sb.appendLine("| Component | Summary | Key params | Preview |")
        sb.appendLine("| --- | --- | --- | --- |")
        files.forEach { file ->
            val component = file.nameWithoutExtension
            val (summary, params) = extractComponentDoc(file.readText(), component)
            val keyParams = params.take(4).joinToString(", ") { "`$it`" }.ifEmpty { "—" }
            val scenario = snapshots[component]?.entries?.firstOrNull()?.value
            val preview = if (scenario != null) {
                val light = scenario["light"]?.let { "[light]($screenshotPagesBaseUrl/$it)" }
                val dark = scenario["dark"]?.let { "[dark]($screenshotPagesBaseUrl/$it)" }
                listOfNotNull(light, dark).joinToString(" · ").ifEmpty { "—" }
            } else {
                "—"
            }
            sb.appendLine("| `$component` | ${summary.ifEmpty { "—" }} | $keyParams | $preview |")
        }
    }
    return sb.toString()
}

val skillManifestFile = rootProject.layout.projectDirectory.file("skills/electric-pop/reference/components.md")
val skillComponentRoot = layout.projectDirectory.dir("src/commonMain/kotlin/co/tanay/electricpop")
val skillSnapshotsDir = layout.projectDirectory.dir("src/desktopTest/snapshots")

tasks.register("generateSkillManifest") {
    group = "documentation"
    description = "Generate skills/electric-pop/reference/components.md from public component source."
    skillManifestTiers.forEach { (tier, _) ->
        inputs.dir(skillComponentRoot.dir(tier)).withPropertyName("components-$tier")
    }
    inputs.dir(skillSnapshotsDir).withPropertyName("snapshots")
    outputs.file(skillManifestFile)
    doLast {
        val rendered = buildSkillManifest(skillComponentRoot, skillSnapshotsDir)
        skillManifestFile.asFile.parentFile.mkdirs()
        skillManifestFile.asFile.writeText(rendered)
        logger.lifecycle("generateSkillManifest: wrote ${skillManifestFile.asFile.relativeTo(rootProject.projectDir)}")
    }
}

tasks.register("checkSkillManifest") {
    group = "verification"
    description = "Assert the Agent Skill component cheat-sheet is in sync with component source."
    skillManifestTiers.forEach { (tier, _) ->
        inputs.dir(skillComponentRoot.dir(tier)).withPropertyName("components-$tier")
    }
    inputs.dir(skillSnapshotsDir).withPropertyName("snapshots")
    inputs.files(skillManifestFile).withPropertyName("manifest").optional(true)
    doLast {
        val rendered = buildSkillManifest(skillComponentRoot, skillSnapshotsDir)
        val current = skillManifestFile.asFile.takeIf { it.exists() }?.readText()
        if (current != rendered) {
            throw GradleException(
                "skills/electric-pop/reference/components.md is stale. " +
                    "Run ./gradlew :library:generateSkillManifest and commit the result.",
            )
        }
        logger.lifecycle("checkSkillManifest: component cheat-sheet is in sync.")
    }
}

kover {
    reports {
        filters {
            excludes {
                packages("co.tanay.electricpop.demo.*")
            }
        }
    }
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
        publishLibraryVariants("release")
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { target ->
        target.binaries.framework {
            baseName = "ElectricPop"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.compose.runtime:runtime:1.10.3")
            implementation("org.jetbrains.compose.foundation:foundation:1.10.3")
            implementation("org.jetbrains.compose.material3:material3:1.9.0")
            implementation("org.jetbrains.compose.ui:ui:1.10.3")
            implementation("org.jetbrains.compose.components:components-resources:1.10.3")
            implementation(libs.squircle.shape)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        val desktopMain by getting {
            dependencies {
                implementation("org.jetbrains.compose.desktop:desktop:1.10.3")
            }
        }
        val desktopTest by getting {
            dependencies {
                implementation(libs.roborazzi.compose.desktop)
                implementation(compose.desktop.currentOs)
                implementation("org.jetbrains.compose.ui:ui-test:1.10.3")
                implementation("org.jetbrains.compose.ui:ui-test-junit4:1.10.3")
            }
        }
    }
}

compose.resources {
    publicResClass = true
}

android {
    namespace = "co.tanay.electricpop"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

version = (findProperty("VERSION_NAME") as? String)?.takeIf { it.isNotBlank() } ?: "0.0.0-SNAPSHOT"

mavenPublishing {
    coordinates(groupId = "co.tanay", artifactId = "compose-electric-pop")
    publishToMavenCentral(automaticRelease = false)
    signAllPublications()

    pom {
        name.set("Electric Pop")
        description.set("A high-energy Compose Multiplatform UI component library")
        url.set("https://github.com/tanaykumarbera/compose-electric-pop")

        licenses {
            license {
                name.set("Apache License 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0")
            }
        }

        developers {
            developer {
                id.set("tanaykumarbera")
                name.set("Tanay Kumar Bera")
            }
        }

        scm {
            url.set("https://github.com/tanaykumarbera/compose-electric-pop")
            connection.set("scm:git:git://github.com/tanaykumarbera/compose-electric-pop.git")
            developerConnection.set("scm:git:ssh://github.com/tanaykumarbera/compose-electric-pop.git")
        }
    }
}
