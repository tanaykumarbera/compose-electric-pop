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
    implementation("com.electricpop:electric-pop:0.1.0")
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

## Components (30)

### Foundation (20)

| Component | Description |
|-----------|-------------|
| PopButton | Primary, Secondary, Ghost buttons × XL/Large/Small + Icon variant |
| PopTextField | Text input with label, error state, password toggle |
| PopRadioGroup | Radio selection with tonal shift |
| PopSwitch | On/Off toggle with kinetic animation |
| PopSlider | Range slider with value display |
| PopChip | Category tags in primary/secondary/tertiary colors |
| PopIcon | Material Symbols wrapper |
| PopSurface | Themed container with squircle corners and tonal shadows |
| PopBadge | Directional badge (up/down/neutral) with value |
| PopPill | Small label badges (Active, Locked, Live, etc.) |
| PopIconRow | Dynamic horizontal icon cluster |
| PopSectionHeader | Section header with accent label and line |
| PopTitleBar | Title with inline badge |
| PopDisplayText | Large emphasized text with fractional part |
| PopCodeBlock | Pre-formatted code with copy header |
| PopIconListItem | Icon + description list item |
| PopTable | Label-value table with alternating tonal rows |
| PopStepList | Numbered items with icons |
| PopBottomBar | Glassmorphic navigation bar |
| PopDropdown | Selector with expand affordance |

### Composite (6)

| Component | Description |
|-----------|-------------|
| PopCarouselCard | Horizontal scroll card strip |
| PopDashboardCard | Data overview card with status pills and data rows |
| PopDataRow | Icon + title + value row with tonal separation |
| PopActionCard | Card with input fields and action buttons |
| PopBannerCard | Hero / metric banner with trend chip and overlapping icon cluster |
| PopImageBannerCard | Big image card with overlay headline and configurable text anchor |

### Chart (3)

| Component | Description |
|-----------|-------------|
| PopLineChart | Trend line with glow on active points |
| PopBarChart | Comparative bars with active scaling |
| PopDonutChart | Circular gauge with center text |

## Design System

Based on the "Kinetic Pulse" aesthetic:

- **Colors:** Electric Lime (#CAFD00), Neon Magenta (#FFBDF3), Cyber Cyan (#00FFFF)
- **Typography:** Space Grotesk (headlines), Manrope (body)
- **Shapes:** Squircle corners (continuous curvature)
- **Interactions:** Kinetic hover/active animations

## License

Apache License 2.0
