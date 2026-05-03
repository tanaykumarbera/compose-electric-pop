# Electric Pop

[![Maven Central](https://img.shields.io/maven-central/v/co.tanay/compose-electric-pop.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/co.tanay/compose-electric-pop)
[![CI](https://github.com/tanaykumarbera/compose-electric-pop/actions/workflows/ci.yml/badge.svg)](https://github.com/tanaykumarbera/compose-electric-pop/actions/workflows/ci.yml)
[![Coverage](https://codecov.io/gh/tanaykumarbera/compose-electric-pop/branch/main/graph/badge.svg)](https://codecov.io/gh/tanaykumarbera/compose-electric-pop)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-CAFD00?logo=kotlin&logoColor=black)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.10.3-CAFD00?logo=jetpackcompose&logoColor=black)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![Platforms](https://img.shields.io/badge/Platforms-Android%20%7C%20iOS%20%7C%20Desktop-CAFD00)](#platforms)
[![API docs](https://img.shields.io/badge/API_docs-Dokka-CAFD00)](https://tanaykumarbera.github.io/compose-electric-pop/api/)

A high-energy Compose Multiplatform UI component library.

Bold typography. Kinetic interactions. Neon-saturated design system.

## Platforms

- Android (API 24+)
- iOS (arm64 + simulatorArm64)
- Desktop (JVM)

## Installation

```kotlin
// build.gradle.kts
dependencies {
    implementation("co.tanay:compose-electric-pop:0.0.1")
}
```

## Quick Start

```kotlin
ElectricPopTheme {
    PopButton(onClick = { /* … */ }) {
        Text("Get Started")
    }
}
```

## Theme Customization

Electric Pop ships with an opinionated default theme but supports full customization:

```kotlin
ElectricPopTheme(
    darkTheme = true,
    colorScheme = yourCustomColorScheme,
    typography = yourCustomTypography,
) {
    // Your UI — all Pop components automatically use your theme
}
```

## Components

The library ships **27 components** across three tiers:

- **20 foundation** (`co.tanay.electricpop.foundation`) — buttons, fields, chips, surfaces, icons, typography primitives.
- **6 composite** (`co.tanay.electricpop.composite`) — banner cards, dashboard cards, action cards, carousels.
- **1 chart** (`co.tanay.electricpop.chart`) — `PopChart` with `PopChartStyle.Line`, `Bar`, `Donut`.

For the full component reference — API, parameters, and inline light + dark screenshots — see the published Dokka site:

**📖 <https://tanaykumarbera.github.io/compose-electric-pop/api/>**

## Design System

Electric Pop is built on the "Kinetic Pulse" design language: tonal hierarchy instead of borders, glow instead of grey shadow, type as object. The 7 design rules every component enforces — plus the full theme architecture, color palette, typography, shape, and spacing token tables — are documented in [`DESIGN.md`](DESIGN.md).

Highlights:
- **Colors:** Electric Lime (`#CAFD00`), Neon Magenta (`#FFBDF3`), Cyber Cyan (`#00FFFF`)
- **Typography:** Space Grotesk (headlines), Manrope (body)
- **Shapes:** Squircle corners (continuous curvature, not rounded rectangles)
- **Interactions:** Kinetic hover (1.05×) and active (0.95×) scaling

## License

Apache License 2.0
