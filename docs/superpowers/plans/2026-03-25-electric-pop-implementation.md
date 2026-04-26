# Electric Pop Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build the Electric Pop KMP UI library from repo init through agent-driven component development pipeline.

**Architecture:** Single Gradle module KMP library (`library/`) with package separation (theme/foundation/composite/chart) + demo app (`demo/`). Compose Multiplatform targets Android (API 24+), iOS, Desktop JVM. Theme wraps Material3 with Electric Pop defaults. Components built via Pixy agent orchestration after infrastructure is complete.

**Tech Stack:** Kotlin 2.3.20, Compose Multiplatform 1.10.3, AGP 9.1.0, Gradle 9.4.1, squircle-shape 5.2.0, Roborazzi 1.59.0, vanniktech maven-publish 0.36.0

**Spec:** `docs/superpowers/specs/2026-03-25-electric-pop-design.md`

---

## File Structure

```
compose-electric-pop/
├── .github/
│   └── workflows/
│       ├── ci.yml                          # Lint, build, test on PR
│       ├── release.yml                     # Publish to Maven Central on tag
│       └── pages.yml                       # Deploy GitHub Pages
├── .execution-history/                     # Execution tracking plans
├── docs/
│   ├── index.html                          # GitHub Pages home
│   └── superpowers/
│       ├── specs/                          # Design specs
│       └── plans/                          # Implementation plans
├── gradle/
│   ├── libs.versions.toml                  # Version catalog
│   └── wrapper/
│       └── gradle-wrapper.properties
├── library/
│   ├── build.gradle.kts
│   └── src/
│       ├── commonMain/kotlin/co/tanay/electricpop/
│       │   ├── theme/
│       │   │   ├── ElectricPopTheme.kt     # Theme composable + CompositionLocals
│       │   │   ├── Color.kt               # Light + dark color schemes
│       │   │   ├── Typography.kt          # Space Grotesk + Manrope type scale
│       │   │   ├── Shape.kt              # Squircle shape tokens
│       │   │   └── Spacing.kt            # Custom spacing scale
│       │   ├── foundation/                # 20 foundation components (one file each)
│       │   ├── composite/                 # 7 composite components (one file each)
│       │   └── chart/                     # 3 chart components (one file each)
│       ├── commonTest/kotlin/co/tanay/electricpop/
│       │   └── theme/
│       │       └── ElectricPopThemeTest.kt
│       ├── androidMain/kotlin/co/tanay/electricpop/
│       │   └── Platform.android.kt
│       ├── iosMain/kotlin/co/tanay/electricpop/
│       │   └── Platform.ios.kt
│       └── desktopMain/kotlin/co/tanay/electricpop/
│           └── Platform.desktop.kt
├── demo/
│   ├── build.gradle.kts
│   └── src/
│       ├── commonMain/kotlin/co/tanay/electricpop/demo/
│       │   ├── App.kt                     # Root app with theme toggle
│       │   ├── CatalogScreen.kt           # Component catalog list
│       │   └── components/                # Per-component demo pages
│       ├── androidMain/
│       │   └── kotlin/co/tanay/electricpop/demo/
│       │       └── MainActivity.kt
│       ├── iosMain/
│       │   └── kotlin/co/tanay/electricpop/demo/
│       │       └── MainViewController.kt
│       └── desktopMain/
│           └── kotlin/co/tanay/electricpop/demo/
│               └── Main.kt
├── AGENTS.md                               # Agent context + instructions
├── CLAUDE.md                               # Claude Code project instructions
├── build.gradle.kts                        # Root build script
├── settings.gradle.kts                     # Module includes + plugin management
├── gradle.properties                       # KMP + Compose flags
└── README.md                               # Project overview
```

---

## Phase 01: Repo Setup

### Task 1: Initialize Gradle project skeleton

**Files:**
- Create: `settings.gradle.kts`
- Create: `build.gradle.kts`
- Create: `gradle.properties`
- Create: `gradle/libs.versions.toml`
- Create: `.gitignore`

- [ ] **Step 1: Create `gradle/libs.versions.toml`**

```toml
[versions]
kotlin = "2.3.20"
compose-multiplatform = "1.10.3"
agp = "9.1.0"
maven-publish = "0.36.0"
squircle-shape = "5.2.0"
roborazzi = "1.59.0"

[libraries]
squircle-shape = { module = "com.stoyanvuchev:squircle-shape", version.ref = "squircle-shape" }
roborazzi = { module = "io.github.takahirom.roborazzi:roborazzi", version.ref = "roborazzi" }
roborazzi-compose = { module = "io.github.takahirom.roborazzi:roborazzi-compose", version.ref = "roborazzi" }

[plugins]
kotlin-multiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
compose-multiplatform = { id = "org.jetbrains.compose", version.ref = "compose-multiplatform" }
compose-compiler = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
android-library = { id = "com.android.library", version.ref = "agp" }
android-application = { id = "com.android.application", version.ref = "agp" }
maven-publish = { id = "com.vanniktech.maven.publish", version.ref = "maven-publish" }
roborazzi = { id = "io.github.takahirom.roborazzi", version.ref = "roborazzi" }
```

- [ ] **Step 2: Create `settings.gradle.kts`**

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolution {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "compose-electric-pop"

include(":library")
include(":demo")
```

- [ ] **Step 3: Create root `build.gradle.kts`**

```kotlin
plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.maven.publish) apply false
}
```

- [ ] **Step 4: Create `gradle.properties`**

```properties
org.gradle.jvmargs=-Xmx2g -XX:MaxMetaspaceSize=512m
org.gradle.parallel=true
org.gradle.caching=true

kotlin.code.style=official
kotlin.mpp.stability.nowarn=true

android.useAndroidX=true
android.nonTransitiveRClass=true
```

- [ ] **Step 5: Create `.gitignore`**

```
.gradle/
build/
local.properties
*.iml
.idea/
.DS_Store
*.hprof
.superpowers/
.execution-history/
```

- [ ] **Step 6: Initialize Gradle wrapper**

Run: `gradle wrapper --gradle-version 9.4.1`
Expected: `gradle/wrapper/gradle-wrapper.properties` created with `distributionUrl=...gradle-9.4.1-bin.zip`

- [ ] **Step 7: Verify Gradle syncs**

Run: `./gradlew --version`
Expected: Output shows Gradle 9.4.1, Kotlin 2.3.20

- [ ] **Step 8: Commit**

```bash
git add -A
git commit -m "feat: initialize Gradle project skeleton with version catalog"
```

---

### Task 2: Create library module (KMP + Compose)

**Files:**
- Create: `library/build.gradle.kts`
- Create: `library/src/commonMain/kotlin/co/tanay/electricpop/Platform.kt`
- Create: `library/src/androidMain/kotlin/co/tanay/electricpop/Platform.android.kt`
- Create: `library/src/iosMain/kotlin/co/tanay/electricpop/Platform.ios.kt`
- Create: `library/src/desktopMain/kotlin/co/tanay/electricpop/Platform.desktop.kt`
- Create: `library/src/androidMain/AndroidManifest.xml`

- [ ] **Step 1: Create `library/build.gradle.kts`**

```kotlin
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
}

kotlin {
    androidTarget {
        compilations.all {
    compilerOptions {
                jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            }
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
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.squircle.shape)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
            }
        }
    }
}

android {
    namespace = "co.tanay.electricpop"
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
```

- [ ] **Step 2: Create `library/src/androidMain/AndroidManifest.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest />
```

- [ ] **Step 3: Create platform expect/actual stubs**

`library/src/commonMain/kotlin/co/tanay/electricpop/Platform.kt`:
```kotlin
package co.tanay.electricpop

expect val platformName: String
```

`library/src/androidMain/kotlin/co/tanay/electricpop/Platform.android.kt`:
```kotlin
package co.tanay.electricpop

actual val platformName: String = "Android"
```

`library/src/iosMain/kotlin/co/tanay/electricpop/Platform.ios.kt`:
```kotlin
package co.tanay.electricpop

actual val platformName: String = "iOS"
```

`library/src/desktopMain/kotlin/co/tanay/electricpop/Platform.desktop.kt`:
```kotlin
package co.tanay.electricpop

actual val platformName: String = "Desktop"
```

- [ ] **Step 4: Verify library builds**

Run: `./gradlew :library:build`
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add library/
git commit -m "feat: add KMP library module with Android, iOS, Desktop targets"
```

---

### Task 3: Create demo app module

**Files:**
- Create: `demo/build.gradle.kts`
- Create: `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/App.kt`
- Create: `demo/src/androidMain/kotlin/co/tanay/electricpop/demo/MainActivity.kt`
- Create: `demo/src/androidMain/AndroidManifest.xml`
- Create: `demo/src/iosMain/kotlin/co/tanay/electricpop/demo/MainViewController.kt`
- Create: `demo/src/desktopMain/kotlin/co/tanay/electricpop/demo/Main.kt`

- [ ] **Step 1: Create `demo/build.gradle.kts`**

```kotlin
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.application)
}

kotlin {
    androidTarget {
        compilations.all {
    compilerOptions {
                jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            }
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
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

android {
    namespace = "co.tanay.electricpop.demo"
    compileSdk = 35

    defaultConfig {
        applicationId = "co.tanay.electricpop.demo"
        minSdk = 24
        targetSdk = 35
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
```

- [ ] **Step 2: Create `demo/src/androidMain/AndroidManifest.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest>
    <application
        android:label="Electric Pop Demo"
        android:theme="@android:style/Theme.Material.Light.NoActionBar">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

- [ ] **Step 3: Create shared App composable**

`demo/src/commonMain/kotlin/co/tanay/electricpop/demo/App.kt`:
```kotlin
package co.tanay.electricpop.demo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import co.tanay.electricpop.platformName

@Composable
fun App() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Electric Pop Demo — $platformName")
    }
}
```

- [ ] **Step 4: Create platform entry points**

`demo/src/androidMain/kotlin/co/tanay/electricpop/demo/MainActivity.kt`:
```kotlin
package co.tanay.electricpop.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}
```

`demo/src/iosMain/kotlin/co/tanay/electricpop/demo/MainViewController.kt`:
```kotlin
package co.tanay.electricpop.demo

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController { App() }
```

`demo/src/desktopMain/kotlin/co/tanay/electricpop/demo/Main.kt`:
```kotlin
package co.tanay.electricpop.demo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Electric Pop Demo"
    ) {
        App()
    }
}
```

- [ ] **Step 5: Verify demo builds**

Run: `./gradlew :demo:build`
Expected: BUILD SUCCESSFUL

- [ ] **Step 6: Commit**

```bash
git add demo/
git commit -m "feat: add demo app module with Android, iOS, Desktop entry points"
```

---

### Task 4: Set up theme skeleton with design tokens

**Files:**
- Create: `library/src/commonMain/kotlin/co/tanay/electricpop/theme/Color.kt`
- Create: `library/src/commonMain/kotlin/co/tanay/electricpop/theme/Typography.kt`
- Create: `library/src/commonMain/kotlin/co/tanay/electricpop/theme/Shape.kt`
- Create: `library/src/commonMain/kotlin/co/tanay/electricpop/theme/Spacing.kt`
- Create: `library/src/commonMain/kotlin/co/tanay/electricpop/theme/ElectricPopTheme.kt`

- [ ] **Step 1: Create `Color.kt` with light + dark schemes**

```kotlin
package co.tanay.electricpop.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Primary — Electric Lime
val ElectricLime = Color(0xFF4E6300)
val ElectricLimeContainer = Color(0xFFCAFD00)
val OnElectricLime = Color(0xFFE1FF88)
val OnElectricLimeContainer = Color(0xFF4A5E00)
val ElectricLimeDim = Color(0xFF435600)

// Secondary — Neon Magenta
val NeonMagenta = Color(0xFFA400A4)
val NeonMagentaContainer = Color(0xFFFFBDF3)
val OnNeonMagenta = Color(0xFFFFEEF8)
val OnNeonMagentaContainer = Color(0xFF820082)

// Tertiary — Cyber Cyan
val CyberCyan = Color(0xFF006666)
val CyberCyanContainer = Color(0xFF00FFFF)
val OnCyberCyan = Color(0xFFBBFFFE)
val OnCyberCyanContainer = Color(0xFF005D5D)

// Error
val PopError = Color(0xFFB02500)
val PopErrorContainer = Color(0xFFF95630)
val OnPopError = Color(0xFFFFEFEC)
val OnPopErrorContainer = Color(0xFF520C00)

// Surface hierarchy (light)
val PopSurface = Color(0xFFF5F6F7)
val PopSurfaceBright = Color(0xFFF5F6F7)
val PopSurfaceDim = Color(0xFFD1D5D7)
val PopSurfaceContainer = Color(0xFFE6E8EA)
val PopSurfaceContainerHigh = Color(0xFFE0E3E4)
val PopSurfaceContainerHighest = Color(0xFFDADDDF)
val PopSurfaceContainerLow = Color(0xFFEFF1F2)
val PopSurfaceContainerLowest = Color(0xFFFFFFFF)

// Outline
val PopOutline = Color(0xFF757778)
val PopOutlineVariant = Color(0xFFABADAE)

// On-surface
val PopOnBackground = Color(0xFF2C2F30)
val PopOnSurface = Color(0xFF2C2F30)
val PopOnSurfaceVariant = Color(0xFF595C5D)

// Inverse
val PopInverseSurface = Color(0xFF0C0F10)
val PopInverseOnSurface = Color(0xFF9B9D9E)
val PopInversePrimary = Color(0xFFCAFD00)

val ElectricPopLightColorScheme = lightColorScheme(
    primary = ElectricLime,
    onPrimary = OnElectricLime,
    primaryContainer = ElectricLimeContainer,
    onPrimaryContainer = OnElectricLimeContainer,
    secondary = NeonMagenta,
    onSecondary = OnNeonMagenta,
    secondaryContainer = NeonMagentaContainer,
    onSecondaryContainer = OnNeonMagentaContainer,
    tertiary = CyberCyan,
    onTertiary = OnCyberCyan,
    tertiaryContainer = CyberCyanContainer,
    onTertiaryContainer = OnCyberCyanContainer,
    error = PopError,
    onError = OnPopError,
    errorContainer = PopErrorContainer,
    onErrorContainer = OnPopErrorContainer,
    background = PopOnBackground.copy(alpha = 0f).compositeOver(PopSurface),
    onBackground = PopOnBackground,
    surface = PopSurface,
    onSurface = PopOnSurface,
    onSurfaceVariant = PopOnSurfaceVariant,
    surfaceBright = PopSurfaceBright,
    surfaceDim = PopSurfaceDim,
    surfaceContainer = PopSurfaceContainer,
    surfaceContainerHigh = PopSurfaceContainerHigh,
    surfaceContainerHighest = PopSurfaceContainerHighest,
    surfaceContainerLow = PopSurfaceContainerLow,
    surfaceContainerLowest = PopSurfaceContainerLowest,
    outline = PopOutline,
    outlineVariant = PopOutlineVariant,
    inverseSurface = PopInverseSurface,
    inverseOnSurface = PopInverseOnSurface,
    inversePrimary = PopInversePrimary,
    surfaceTint = ElectricLime,
)

// Dark scheme — placeholder, must be verified against Stitch dark variant screens
// TODO(Phase 03): Extract exact dark values from Stitch dark screens
val ElectricPopDarkColorScheme = darkColorScheme(
    primary = ElectricLimeContainer,
    onPrimary = OnElectricLimeContainer,
    primaryContainer = ElectricLime,
    onPrimaryContainer = OnElectricLime,
    secondary = NeonMagentaContainer,
    onSecondary = OnNeonMagentaContainer,
    secondaryContainer = NeonMagenta,
    onSecondaryContainer = OnNeonMagenta,
    tertiary = CyberCyanContainer,
    onTertiary = OnCyberCyanContainer,
    tertiaryContainer = CyberCyan,
    onTertiaryContainer = OnCyberCyan,
    error = PopErrorContainer,
    onError = OnPopErrorContainer,
    errorContainer = PopError,
    onErrorContainer = OnPopError,
    surface = PopInverseSurface,
    onSurface = PopInverseOnSurface,
    outline = PopOutlineVariant,
    outlineVariant = PopOutline,
    inverseSurface = PopSurface,
    inverseOnSurface = PopOnSurface,
    inversePrimary = ElectricLime,
)
```

Note: `Color.compositeOver` import: `import androidx.compose.ui.graphics.compositeOver`. If that creates a compilation issue, just use `PopSurface` directly for the `background` field.

- [ ] **Step 2: Create `Typography.kt`**

```kotlin
package co.tanay.electricpop.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// TODO(Phase 03): Bundle Space Grotesk + Manrope as compose resources
// For now, use system defaults with correct weights/styles
val SpaceGrotesk = FontFamily.Default // Replace with bundled font
val Manrope = FontFamily.Default      // Replace with bundled font

val ElectricPopTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        fontSize = 57.sp,
        letterSpacing = (-0.02).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        fontSize = 45.sp,
        letterSpacing = (-0.02).sp,
    ),
    displaySmall = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        fontSize = 36.sp,
        letterSpacing = (-0.02).sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        fontSize = 32.sp,
        letterSpacing = (-0.02).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 14.sp,
        letterSpacing = 0.5.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 11.sp,
        letterSpacing = 0.5.sp,
    ),
)
```

- [ ] **Step 3: Create `Shape.kt`**

```kotlin
package co.tanay.electricpop.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Shapes
import com.stoyanvuchev.squircleshape.SquircleShape

val ElectricPopShapes = Shapes(
    extraSmall = SquircleShape(percent = 25),   // 8dp equivalent
    small = SquircleShape(percent = 35),         // 12dp
    medium = SquircleShape(percent = 50),        // 16dp (md = 1rem)
    large = SquircleShape(percent = 70),         // 32dp (lg = 2rem)
    extraLarge = SquircleShape(percent = 85),    // 48dp (xl = 3rem)
)

val PopShapeFull = CircleShape // pill shape (9999px equivalent)
```

Note: Verify the `com.stoyanvuchev.squircleshape` import path against squircle-shape 5.2.0 docs. The API may use `SquircleShape(cornerSize)` with dp values instead of percent — adjust accordingly.

- [ ] **Step 4: Create `Spacing.kt`**

```kotlin
package co.tanay.electricpop.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class ElectricPopSpacing(
    val xxs: Dp = 4.dp,
    val xs: Dp = 8.dp,
    val sm: Dp = 12.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
    val xxl: Dp = 48.dp,
    val xxxl: Dp = 64.dp,
)

val LocalElectricPopSpacing = staticCompositionLocalOf { ElectricPopSpacing() }
```

- [ ] **Step 5: Create `ElectricPopTheme.kt`**

```kotlin
package co.tanay.electricpop.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography

@Composable
fun ElectricPopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme? = null,
    typography: Typography = ElectricPopTypography,
    shapes: Shapes = ElectricPopShapes,
    spacing: ElectricPopSpacing = ElectricPopSpacing(),
    content: @Composable () -> Unit,
) {
    val colors = colorScheme ?: if (darkTheme) {
        ElectricPopDarkColorScheme
    } else {
        ElectricPopLightColorScheme
    }

    CompositionLocalProvider(
        LocalElectricPopSpacing provides spacing,
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = typography,
            shapes = shapes,
            content = content,
        )
    }
}

object ElectricPopTheme {
    val spacing: ElectricPopSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalElectricPopSpacing.current
}
```

- [ ] **Step 6: Verify theme compiles**

Run: `./gradlew :library:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL

- [ ] **Step 7: Wire theme into demo app**

Update `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/App.kt`:
```kotlin
package co.tanay.electricpop.demo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme

@Composable
fun App() {
    var isDark by remember { mutableStateOf(false) }

    ElectricPopTheme(darkTheme = isDark) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "ELECTRIC POP",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Dark theme", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.width(8.dp))
                Switch(checked = isDark, onCheckedChange = { isDark = it })
            }
        }
    }
}
```

- [ ] **Step 8: Verify demo builds with theme**

Run: `./gradlew :demo:build`
Expected: BUILD SUCCESSFUL

- [ ] **Step 9: Commit**

```bash
git add library/src/commonMain/kotlin/co/tanay/electricpop/theme/ demo/src/commonMain/
git commit -m "feat: add Electric Pop theme system with color, typography, shape, spacing tokens"
```

---

### Task 5: Set up testing infrastructure

**Files:**
- Create: `library/src/commonTest/kotlin/co/tanay/electricpop/theme/ElectricPopThemeTest.kt`

- [ ] **Step 1: Write theme smoke test**

```kotlin
package co.tanay.electricpop.theme

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ElectricPopThemeTest {

    @Test
    fun lightColorScheme_hasPrimaryElectricLime() {
        assertEquals(ElectricLime, ElectricPopLightColorScheme.primary)
    }

    @Test
    fun darkColorScheme_differFromLight() {
        assertNotEquals(
            ElectricPopLightColorScheme.surface,
            ElectricPopDarkColorScheme.surface,
        )
    }

    @Test
    fun spacing_hasCorrectDefaults() {
        val spacing = ElectricPopSpacing()
        assertEquals(4.dp, spacing.xxs)
        assertEquals(64.dp, spacing.xxxl)
    }
}
```

Note: Add `import androidx.compose.ui.unit.dp` for the dp references.

- [ ] **Step 2: Run tests**

Run: `./gradlew :library:allTests`
Expected: 3 tests pass

- [ ] **Step 3: Commit**

```bash
git add library/src/commonTest/
git commit -m "test: add theme smoke tests for color schemes and spacing"
```

---

### Task 6: Set up CI pipeline

**Files:**
- Create: `.github/workflows/ci.yml`
- Create: `.github/workflows/release.yml`

- [ ] **Step 1: Create CI workflow**

`.github/workflows/ci.yml`:
```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

concurrency:
  group: ci-${{ github.ref }}
  cancel-in-progress: true

jobs:
  build:
    runs-on: macos-latest
    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4
        with:
          distribution: zulu
          java-version: 17

      - uses: gradle/actions/setup-gradle@v4

      - name: Lint
        run: ./gradlew lint --continue

      - name: Build library
        run: ./gradlew :library:build

      - name: Build demo
        run: ./gradlew :demo:build

      - name: Run tests
        run: ./gradlew :library:allTests

      - name: Upload test reports
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: test-reports
          path: "**/build/reports/tests/"
```

- [ ] **Step 2: Create release workflow**

`.github/workflows/release.yml`:
```yaml
name: Release

on:
  push:
    tags: ["v*"]

jobs:
  publish:
    runs-on: macos-latest
    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4
        with:
          distribution: zulu
          java-version: 17

      - uses: gradle/actions/setup-gradle@v4

      - name: Build & test
        run: ./gradlew :library:build :library:allTests

      - name: Publish to Maven Central
        run: ./gradlew :library:publishAllPublicationsToMavenCentral --no-configuration-cache
        env:
          ORG_GRADLE_PROJECT_mavenCentralUsername: ${{ secrets.MAVEN_CENTRAL_USERNAME }}
          ORG_GRADLE_PROJECT_mavenCentralPassword: ${{ secrets.MAVEN_CENTRAL_PASSWORD }}
          ORG_GRADLE_PROJECT_signingInMemoryKey: ${{ secrets.GPG_SIGNING_KEY }}
          ORG_GRADLE_PROJECT_signingInMemoryKeyId: ${{ secrets.GPG_KEY_ID }}
          ORG_GRADLE_PROJECT_signingInMemoryKeyPassword: ${{ secrets.GPG_KEY_PASSWORD }}

      - name: Create GitHub Release
        uses: softprops/action-gh-release@v2
        with:
          generate_release_notes: true
```

- [ ] **Step 3: Commit**

```bash
git add .github/
git commit -m "ci: add CI and release GitHub Actions workflows"
```

---

### Task 7: Set up GitHub Pages

**Files:**
- Create: `docs/index.html`
- Create: `.github/workflows/pages.yml`

- [ ] **Step 1: Create landing page**

`docs/index.html`:
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Electric Pop — Compose Multiplatform UI Library</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@700;900&family=Manrope:wght@400;600;800&display=swap');
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Manrope', sans-serif; background: #0C0F10; color: #F5F6F7; }
        .hero { min-height: 100vh; display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 2rem; text-align: center; }
        h1 { font-family: 'Space Grotesk', sans-serif; font-weight: 900; font-style: italic; font-size: clamp(3rem, 8vw, 6rem); text-transform: uppercase; letter-spacing: -0.02em; color: #CAFD00; }
        .subtitle { font-size: 1.25rem; color: #9B9D9E; margin-top: 1rem; max-width: 600px; }
        .badge { display: inline-block; background: #CAFD00; color: #4A5E00; padding: 0.5rem 1.5rem; border-radius: 9999px; font-weight: 800; margin-top: 2rem; text-decoration: none; }
        .badge:hover { transform: scale(1.05); transition: transform 0.2s; }
        .features { display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 1.5rem; padding: 4rem 2rem; max-width: 960px; margin: 0 auto; }
        .card { background: #1a1d1e; border-radius: 2rem; padding: 2rem; }
        .card h3 { font-family: 'Space Grotesk', sans-serif; font-weight: 700; color: #CAFD00; margin-bottom: 0.5rem; }
        .card p { color: #9B9D9E; line-height: 1.6; }
        .platforms { color: #595C5D; text-align: center; padding: 2rem; font-size: 0.9rem; }
    </style>
</head>
<body>
    <div class="hero">
        <h1>Electric Pop</h1>
        <p class="subtitle">A high-energy Compose Multiplatform UI component library. Bold typography, kinetic interactions, and a neon-saturated design system.</p>
        <a class="badge" href="https://github.com/tanaykumarbera/compose-electric-pop/wiki">View Components &rarr;</a>
    </div>
    <div class="features">
        <div class="card">
            <h3>30 Components</h3>
            <p>Foundation elements, composite cards, and data visualization — all themed with the Kinetic Pulse aesthetic.</p>
        </div>
        <div class="card">
            <h3>Light + Dark</h3>
            <p>Full light and dark color schemes derived from Electric Lime, Neon Magenta, and Cyber Cyan.</p>
        </div>
        <div class="card">
            <h3>Multiplatform</h3>
            <p>Android, iOS, and Desktop JVM. Single dependency, one API surface.</p>
        </div>
    </div>
    <p class="platforms">Android (API 24+) &middot; iOS &middot; Desktop JVM &middot; Compose Multiplatform</p>
</body>
</html>
```

- [ ] **Step 2: Create Pages workflow**

`.github/workflows/pages.yml`:
```yaml
name: Deploy Pages

on:
  push:
    branches: [main]
    paths: ["docs/**"]

permissions:
  pages: write
  id-token: write

jobs:
  deploy:
    environment:
      name: github-pages
      url: ${{ steps.deployment.outputs.page_url }}
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/configure-pages@v4
      - uses: actions/upload-pages-artifact@v3
        with:
          path: docs/
      - id: deployment
        uses: actions/deploy-pages@v4
```

- [ ] **Step 3: Commit**

```bash
git add docs/index.html .github/workflows/pages.yml
git commit -m "feat: add GitHub Pages landing page and deployment workflow"
```

---

## Phase 02: SOP & Agents

### Task 8: Create component development SOP

**Files:**
- Create: `CLAUDE.md`

- [ ] **Step 1: Write CLAUDE.md with component creation SOP**

```markdown
# Electric Pop — Project Instructions

## Overview
Electric Pop is a Compose Multiplatform UI component library. Single module, package separation.

## Component Creation SOP

### 1. Plan
- Check spec: `docs/superpowers/specs/2026-03-25-electric-pop-design.md`
- Identify component tier: foundation / composite / chart
- Reference Stitch design: https://stitch.withgoogle.com/projects/7983075619754946215

### 2. Implement
- File: `library/src/commonMain/kotlin/co/tanay/electricpop/{tier}/{ComponentName}.kt`
- One file per component
- Components MUST read colors/typography from MaterialTheme, not hardcoded values
- Components MUST use ElectricPopTheme.spacing for spacing values
- Shapes MUST use squircle shapes from ElectricPopShapes
- Follow the 7 design rules (no borders, tonal shadows, neon glow, kinetic interactions, squircle radii, typography impact, ghost border fallback)

### 3. Test
- Unit test: `library/src/commonTest/kotlin/co/tanay/electricpop/{tier}/{ComponentName}Test.kt`
- Test all variants and states
- Run: `./gradlew :library:allTests`

### 4. Demo
- Add demo page: `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/{ComponentName}Demo.kt`
- Register in catalog: update `CatalogScreen.kt`
- Show all variants with sample data
- Verify light + dark themes

### 5. Commit
- Format: `feat(component): add PopComponentName with variants`
- One component per PR
- PR title: `feat: add PopComponentName`

## Build Commands
- Build all: `./gradlew build`
- Library only: `./gradlew :library:build`
- Demo only: `./gradlew :demo:build`
- Tests: `./gradlew :library:allTests`
- Desktop demo: `./gradlew :demo:run`

## Design Rules (non-negotiable)
1. No 1px borders — use tonal surface shifts
2. Tonal shadows only — bg-tinted, 32px blur, 0 offset
3. Ghost border at 15% outline_variant opacity (accessibility only)
4. Neon glow on primary CTAs — 15-20% opacity spread
5. Kinetic interactions — hover 1.05x, active 0.95x
6. Squircle radii — continuous curvature via squircle-shape lib
7. Headlines — uppercase, italic, black weight, -0.02em tracking
```

- [ ] **Step 2: Commit**

```bash
git add CLAUDE.md
git commit -m "docs: add CLAUDE.md with component development SOP and build commands"
```

---

### Task 9: Create AGENTS.md and agent definitions

**Files:**
- Create: `AGENTS.md`

- [ ] **Step 1: Write AGENTS.md**

```markdown
# Electric Pop — Agent Guide

## Project Context
Electric Pop is a Compose Multiplatform UI library implementing the "Kinetic Pulse" design system.
- **Repo:** github.com/tanaykumarbera/compose-electric-pop
- **Design:** Stitch project 7983075619754946215
- **Spec:** docs/superpowers/specs/2026-03-25-electric-pop-design.md
- **Targets:** Android (API 24+), iOS, Desktop JVM
- **Stack:** Kotlin 2.3.20, Compose Multiplatform 1.10.3, AGP 9.1.0

## Agent Hierarchy

### Pixy (Orchestrator) — Opus
The brain. Takes a requirement and orchestrates the full development cycle.
Does NOT write code. Only delegates.

**Workflow:**
1. Receive component requirement
2. Delegate to Planner for implementation plan
3. Delegate to Implementor to execute plan
4. Delegate to Reviewer to review implementation
5. If issues found: delegate back to Implementor with review feedback
6. Loop max 3 times. If errors repeat, STOP and ask human for support
7. When approved: create PR

**Invocation:**
```
Agent(subagent_type="general-purpose", model="opus", prompt="[Pixy prompt with full context]")
```

### Planner — Opus
Creates detailed, step-by-step implementation plans for a single component.

**Input:** Component name, spec reference, Stitch screen references
**Output:** Step-by-step plan with exact file paths, code snippets, test cases
**Must include:** All variants, light + dark theme support, demo page, tests

### Implementor — Sonnet
Executes implementation plans. Writes code, runs tests, fixes build errors.

**Input:** Implementation plan from Planner
**Output:** Working code committed to branch
**Rules:**
- Follow CLAUDE.md SOP exactly
- One component per branch: `feat/pop-{component-name}`
- Run tests after each step
- Stop if same error appears twice

### Reviewer — Opus
Reviews implementation against plan and design spec.

**Input:** Branch diff, original plan, design spec
**Output:** Approved | Issues Found (with specific fixes needed)
**Checks:**
- All variants implemented
- Design rules followed (7 rules from spec section 5)
- Tests cover all variants and states
- Demo page shows all variants
- Light + dark theme both work
- No hardcoded colors/sizes — must use theme tokens

## Error Handling
- If Implementor fails same step twice → Pixy stops and asks human
- If Reviewer rejects 3 times → Pixy stops and asks human
- Never enter blind hallucination loops
```

- [ ] **Step 2: Commit**

```bash
git add AGENTS.md
git commit -m "docs: add AGENTS.md with Pixy orchestrator and agent hierarchy"
```

---

### Task 10: Create demo app catalog scaffold

**Files:**
- Create: `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/CatalogScreen.kt`
- Modify: `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/App.kt`

- [ ] **Step 1: Create CatalogScreen**

```kotlin
package co.tanay.electricpop.demo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class CatalogEntry(
    val name: String,
    val tier: String,
    val content: @Composable () -> Unit,
)

// Components register themselves here as they are implemented
val catalogEntries = listOf<CatalogEntry>(
    // Will be populated as components are built via Pixy
    // Example:
    // CatalogEntry("PopButton", "Foundation") { PopButtonDemo() }
)

@Composable
fun CatalogScreen(onSelect: (CatalogEntry) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                text = "ELECTRIC POP",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Component Catalog",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(24.dp))
        }

        if (catalogEntries.isEmpty()) {
            item {
                Text(
                    text = "No components yet. Run Pixy to build them.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        items(catalogEntries) { entry ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(entry) },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(entry.name, style = MaterialTheme.typography.titleMedium)
                        Text(entry.tier, style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
```

- [ ] **Step 2: Update App.kt with navigation and theme toggle**

```kotlin
package co.tanay.electricpop.demo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme

@Composable
fun App() {
    var isDark by remember { mutableStateOf(false) }
    var selectedEntry by remember { mutableStateOf<CatalogEntry?>(null) }

    ElectricPopTheme(darkTheme = isDark) {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (selectedEntry != null) {
                        TextButton(onClick = { selectedEntry = null }) {
                            Text("< Back")
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            selectedEntry!!.name,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        if (isDark) "Dark" else "Light",
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Spacer(Modifier.width(8.dp))
                    Switch(checked = isDark, onCheckedChange = { isDark = it })
                }
            },
        ) { padding ->
            Box(Modifier.padding(padding)) {
                val entry = selectedEntry
                if (entry != null) {
                    entry.content()
                } else {
                    CatalogScreen(onSelect = { selectedEntry = it })
                }
            }
        }
    }
}
```

- [ ] **Step 3: Verify demo builds**

Run: `./gradlew :demo:build`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Commit**

```bash
git add demo/src/commonMain/
git commit -m "feat: add demo app catalog scaffold with theme toggle and navigation"
```

---

### Task 11: Create execution tracking infrastructure

**Files:**
- Create: `.execution-history/phase-01-repo-setup.md`
- Create: `.execution-history/phase-02-sop-agents.md`
- Create: `.execution-history/phase-03-implementation.md`

- [ ] **Step 1: Create Phase 01 tracking**

```markdown
# Phase 01: Repo Setup

| ID | Step | Status |
|----|------|--------|
| 1 | Init Gradle project skeleton | [PENDING] |
| 2 | Create library module (KMP + Compose) | [PENDING] |
| 3 | Create demo app module | [PENDING] |
| 4 | Set up theme skeleton with design tokens | [PENDING] |
| 5 | Set up testing infrastructure | [PENDING] |
| 6 | Set up CI pipeline | [PENDING] |
| 7 | Set up GitHub Pages | [PENDING] |
```

- [ ] **Step 2: Create Phase 02 tracking**

```markdown
# Phase 02: SOP & Agents

| ID | Step | Status |
|----|------|--------|
| 8 | Create component development SOP (CLAUDE.md) | [PENDING] |
| 9 | Create AGENTS.md and agent definitions | [PENDING] |
| 10 | Create demo app catalog scaffold | [PENDING] |
| 11 | Create execution tracking infrastructure | [PENDING] |
```

- [ ] **Step 3: Create Phase 03 tracking**

```markdown
# Phase 03: Implementation

Prerequisites: Phase 01 and 02 fully complete.

| ID | Step | Status |
|----|------|--------|
| 12 | Extract dark color scheme from Stitch dark screens | [PENDING] |
| 13 | Bundle Space Grotesk + Manrope fonts as compose resources | [PENDING] |
| 14-33 | Foundation components (20) via Pixy | [PENDING] |
| 34-40 | Composite components (7) via Pixy | [PENDING] |
| 41-43 | Chart components (3) via Pixy | [PENDING] |

## Component Checklist

### Foundation
| ID | Component | Status |
|----|-----------|--------|
| 14 | PopButton | [PENDING] |
| 15 | PopTextField | [PENDING] |
| 16 | PopRadioGroup | [PENDING] |
| 17 | PopSwitch | [PENDING] |
| 18 | PopSlider | [PENDING] |
| 19 | PopChip | [PENDING] |
| 20 | PopIcon | [PENDING] |
| 21 | PopSurface | [PENDING] |
| 22 | PopBadge | [PENDING] |
| 23 | PopPill | [PENDING] |
| 24 | PopIconRow | [PENDING] |
| 25 | PopSectionHeader | [PENDING] |
| 26 | PopTitleBar | [PENDING] |
| 27 | PopDisplayText | [PENDING] |
| 28 | PopCodeBlock | [PENDING] |
| 29 | PopIconListItem | [PENDING] |
| 30 | PopTable | [PENDING] |
| 31 | PopStepList | [PENDING] |
| 32 | PopBottomBar | [PENDING] |
| 33 | PopDropdown | [PENDING] |

> **Revised 2026-04-18:** see `docs/superpowers/specs/2026-04-18-banner-card-refactor.md`. PopMetricCard was renamed to PopBannerCard; original numeric PopBannerCard was replaced by PopImageBannerCard.

### Composite
| ID | Component | Status |
|----|-----------|--------|
| 34 | PopFeatureCard | [PENDING] |
| 35 | PopCarouselCard | [PENDING] |
| 36 | PopDashboardCard | [PENDING] |
| 37 | PopDataRow | [PENDING] |
| 38 | PopActionCard | [PENDING] |
| 39 | PopBannerCard | [PENDING] |
| 40 | PopMetricCard | [PENDING] |

### Chart
| ID | Component | Status |
|----|-----------|--------|
| 41 | PopLineChart | [PENDING] |
| 42 | PopBarChart | [PENDING] |
| 43 | PopDonutChart | [PENDING] |
```

- [ ] **Step 4: Commit**

```bash
git add .execution-history/
git commit -m "docs: add execution tracking plans for all phases"
```

---

### Task 12: Create README

**Files:**
- Create: `README.md`

- [ ] **Step 1: Write README**

```markdown
# Electric Pop

A high-energy Compose Multiplatform UI component library.

Bold typography. Kinetic interactions. Neon-saturated design system.

## Platforms

- Android (API 24+)
- iOS
- Desktop (JVM)

## Installation

```gradle
// In your build.gradle.kts
dependencies {
    implementation("co.tanay:compose-electric-pop:0.1.0")
}
```

## Quick Start

```kotlin
ElectricPopTheme {
    PopButton(onClick = { }) {
        Text("Get Started")
    }
}
```

## Components

**Foundation (20):** PopButton, PopTextField, PopRadioGroup, PopSwitch, PopSlider, PopChip, PopIcon, PopSurface, PopBadge, PopPill, PopIconRow, PopSectionHeader, PopTitleBar, PopDisplayText, PopCodeBlock, PopIconListItem, PopTable, PopStepList, PopBottomBar, PopDropdown

**Composite (7):** PopFeatureCard, PopCarouselCard, PopDashboardCard, PopDataRow, PopActionCard, PopBannerCard, PopMetricCard

> _2026-04-18 note: the `PopMetricCard` slot in this list became `PopImageBannerCard`. See `docs/superpowers/specs/2026-04-18-banner-card-refactor.md`._

**Chart (3):** PopLineChart, PopBarChart, PopDonutChart

## Theming

Electric Pop ships with an opinionated default theme but supports full customization:

```kotlin
ElectricPopTheme(
    darkTheme = true,
    colorScheme = customColorScheme,
    typography = customTypography,
) {
    // Your UI
}
```

## License

Apache License 2.0
```

- [ ] **Step 2: Commit**

```bash
git add README.md
git commit -m "docs: add README with installation, quick start, and component list"
```

---

## Execution Notes

- **Phase 01 (Tasks 1-7):** Must complete before Phase 02. Sets up the compilable project.
- **Phase 02 (Tasks 8-12):** Must complete before Phase 03. Sets up the development pipeline.
- **Phase 03 (IDs 12-43 in execution tracking):** Driven by Pixy agent. Each component follows: Planner → Implementor → Reviewer → fix loop. Starts with dark scheme extraction (ID 12) and font bundling (ID 13), then components (IDs 14-43).
- **Git strategy:** Keep committing locally. User will force push to remote when satisfied.
- **Do NOT pull from remote.** Remote has different content from a past attempt.
- **Squircle import path:** Verify `com.stoyanvuchev.squircleshape.SquircleShape` against actual library API in squircle-shape 5.2.0. Adjust if needed.
- **Font bundling:** Deferred to Phase 03 step 13. Typography uses `FontFamily.Default` as placeholder.
- **Dark scheme:** Placeholder inversion in Phase 01. Verified extraction from Stitch dark screens in Phase 03 step 12.
