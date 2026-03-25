plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
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
    }
}

android {
    namespace = "com.electricpop"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

mavenPublishing {
    coordinates("com.electricpop", "electric-pop", "0.1.0")

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
