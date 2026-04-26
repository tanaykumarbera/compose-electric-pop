plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless)
}

allprojects {
    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)

    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        toolVersion = rootProject.libs.versions.detekt.get()
        config.setFrom(rootProject.layout.projectDirectory.file("detekt.yml"))
        buildUponDefaultConfig = true
        autoCorrect = false
        parallel = true
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "21"
        reports {
            html.required.set(true)
            xml.required.set(true)
            txt.required.set(false)
            sarif.required.set(false)
            md.required.set(false)
        }
    }
}

spotless {
    val ktlintVersion = libs.versions.ktlint.get()
    val ktlintOverrides = mapOf(
        // @Composable functions are PascalCase by convention
        "ktlint_standard_function-naming" to "disabled",
        // Compose code is verbose; relaxing line length keeps signatures readable
        "ktlint_standard_max-line-length" to "disabled",
        // Wildcard imports are idiomatic for Compose (layout.*, material3.*, runtime.*)
        "ktlint_standard_no-wildcard-imports" to "disabled",
    )
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**", "**/.gradle/**")
        ktlint(ktlintVersion).editorConfigOverride(ktlintOverrides)
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude("**/build/**", "**/.gradle/**")
        ktlint(ktlintVersion).editorConfigOverride(ktlintOverrides)
    }
}
