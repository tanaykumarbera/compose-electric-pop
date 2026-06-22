plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.application)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { target ->
        target.binaries.framework {
            baseName = "DemoApp"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(project(":library"))
            implementation("org.jetbrains.compose.runtime:runtime:1.10.3")
            implementation("org.jetbrains.compose.foundation:foundation:1.10.3")
            implementation("org.jetbrains.compose.material3:material3:1.9.0")
            implementation("org.jetbrains.compose.ui:ui:1.10.3")
            implementation("org.jetbrains.compose.components:components-resources:1.10.3")
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.activity:activity-compose:1.9.3")
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation("org.jetbrains.compose.desktop:desktop-jvm-linux-x64:1.10.3")
            }
        }
    }
}

android {
    namespace = "co.tanay.electricpop.demo"
    compileSdk = 36

    defaultConfig {
        applicationId = "co.tanay.electricpop.demo"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

compose.desktop {
    application {
        mainClass = "co.tanay.electricpop.demo.MainKt"
    }
}

// AGP 8.7.3 lint can't read Kotlin 2.3.20 metadata — disable all lint tasks for demo
tasks.matching { it.name.contains("lint", ignoreCase = true) }.configureEach {
    enabled = false
}

// ---------------------------------------------------------------------------
// checkCatalogRegistration — guardrail asserting every library Pop component is
// reachable from the demo catalog. Mirrors :library:checkScreenshotPresence:
// a file PopXxx.kt counts iff it declares a top-level public `@Composable fun
// PopXxx(`, and must then appear as `CatalogEntry("PopXxx", ...)` in
// CatalogScreen.kt. Allowlist for intentional omissions:
// demo/catalog-allowlist.txt.
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

tasks.register("checkCatalogRegistration") {
    group = "verification"
    description = "Assert every library Pop component has a CatalogEntry in the demo catalog."

    val componentRoot =
        rootProject.layout.projectDirectory.dir("library/src/commonMain/kotlin/co/tanay/electricpop")
    val catalogFile =
        layout.projectDirectory.file("src/commonMain/kotlin/co/tanay/electricpop/demo/CatalogScreen.kt")
    val allowlistFile = layout.projectDirectory.file("catalog-allowlist.txt")
    val tiers = listOf("foundation", "composite", "chart")

    tiers.forEach { tier ->
        inputs.dir(componentRoot.dir(tier)).withPropertyName("components-$tier")
    }
    inputs.files(catalogFile).withPropertyName("catalog")
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

        val catalogText = catalogFile.asFile.readText()

        val components = tiers
            .flatMap { tier -> componentRoot.dir(tier).asFile.listFiles().orEmpty().toList() }
            .filter { it.isFile && it.extension == "kt" && it.name.startsWith("Pop") }
            .mapNotNull { file ->
                val name = file.nameWithoutExtension
                if (declaresPublicComposable(file.readText(), name)) name else null
            }
            .sorted()

        val missing = components
            .filter { it !in allowlist }
            .filterNot { catalogText.contains("CatalogEntry(\"$it\"") }

        if (missing.isNotEmpty()) {
            throw GradleException(
                "Components missing from the demo catalog (CatalogScreen.kt): " +
                    "${missing.joinToString(", ")}. Add a CatalogEntry, or allowlist in " +
                    "demo/catalog-allowlist.txt.",
            )
        }

        logger.lifecycle(
            "checkCatalogRegistration: ${components.size} components verified " +
                "(${allowlist.size} allowlisted)",
        )
    }
}
