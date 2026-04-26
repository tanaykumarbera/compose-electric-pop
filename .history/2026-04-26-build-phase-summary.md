# Build Phase Summary (Phases 01–03)

**Frozen:** 2026-04-26
**Source:** condensed from `.execution-history/` (now removed; full detail in `git log`).

The library shipped 30 components across 5 build waves between 2026-03-25 and 2026-04-26. This file preserves a few decisions that aren't obvious from code or CLAUDE.md.

## Architecture decisions worth remembering

- **Single Gradle module** with package-level separation. R8 handles tree-shaking at consumer build time. The user explicitly chose this over a multi-module split.
- **Theming via MaterialTheme wrapping.** `ElectricPopTheme` wraps `MaterialTheme` and provides custom `ElectricPopSpacing` via `CompositionLocal`. Consumers get Material3 interop for free.
- **minSdk 24** (Android 7 Nougat). Widest reasonable floor without polyfills.
- **Generic `Pop*` naming.** Library is general-purpose; never name a component after its origin (no `FinanceCard`, no `CryptoWidget`).

## Version-pinning hazards (still relevant)

- `agp = 8.7.3` — AGP 9.x dropped KMP support for `com.android.library`. Don't bump to 9 without verifying KMP compatibility.
- `vanniktech/gradle-maven-publish-plugin = 0.28.0` — 0.36.0+ requires AGP 8.13.0+, which conflicts with KMP.
- Squircle import path is `sv.lib.squircleshape.SquircleShape`, **not** `com.stoyanvuchev.squircleshape.SquircleShape` (Maven coordinate suggests the latter; the actual Kotlin package is the former).
- JDK 21 required everywhere (squircle-shape 5.x compiles to class-file v65).

## Test strategy

Compose UI tests (`runComposeUiTest`) are unavailable in `commonTest` — Skiko native libs aren't on the desktop test classpath. The library uses:

- **Roborazzi screenshot tests** in `desktopTest` for visual validation (light + dark, all variants).
- **Plain Kotlin unit tests** in `commonTest` for components with extractable logic.
- **Honest placeholder tests** for purely visual components (one test per component, documenting that visual validation is via the demo app + screenshot golden).

The reviewer agent enforces an "acid test": would every test still pass if the component source file were deleted? If yes, the test is rejected.

## Agent pipeline lineage

The agent system was rebuilt once after a v1 attempt produced fake tests that only exercised `String.uppercase()`. The v2 pipeline (`pixy-planner`, `pixy-implementor`, `pixy-reviewer`) with scoped tools, model locking, and the acid-test review criterion is what shipped. See `.claude/agents/*.md` for current definitions.

## Pointers

- Full per-component build history: `git log --first-parent main` filtering by `feat/pop-*` branches.
- PRs #1–#46 cover phases 01–03 build work; PR #47 onward is the release-readiness track.
