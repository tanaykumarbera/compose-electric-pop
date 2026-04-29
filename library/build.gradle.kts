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
}

// ---------------------------------------------------------------------------
// syncScreenshotKdoc — keeps the screenshot table in each component's KDoc in
// sync with the Roborazzi snapshots on disk. Convention-based, idempotent.
// See docs/superpowers/plans/SESSION-RESUME.md (step 4) for the full design.
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

mavenPublishing {
    coordinates("co.tanay", "compose-electric-pop", "0.1.0")

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
