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
