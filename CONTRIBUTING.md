# Contributing to Electric Pop

Thanks for your interest. This guide covers the local setup, the component creation flow, and the conventions reviewers will expect.

## Getting set up

```bash
git clone git@github.com:tanaykumarbera/compose-electric-pop.git
cd compose-electric-pop
./gradlew build
```

Requirements:

- JDK 17 (Zulu / Temurin both work)
- Android SDK with `compileSdk = 36` available (`sdkmanager "platforms;android-36"`)
- Xcode 15+ if you want to build the iOS targets locally (not required for desktop / Android contributions)

The repo uses the Gradle wrapper, so you do not need a system Gradle install.

## Common Gradle commands

```bash
./gradlew :library:build                     # Library only
./gradlew :demo:build                        # Demo only
./gradlew :library:desktopTest               # Fastest test loop (desktop unit tests)
./gradlew :library:allTests                  # All unit tests across targets
./gradlew :library:recordRoborazziDesktop    # Re-record golden screenshots
./gradlew :library:verifyRoborazziDesktop    # Verify screenshots match goldens
./gradlew :demo:run                          # Launch the desktop demo
```

## Project layout

```
library/src/commonMain/kotlin/com/electricpop/
├── theme/        ElectricPopTheme, color, typography, shape, spacing
├── foundation/   20 primitive components
├── composite/    6 composite components
└── chart/        PopChart with PopChartStyle.Line / Bar / Donut

demo/src/commonMain/kotlin/com/electricpop/demo/
├── App.kt              Root demo app
├── CatalogScreen.kt    Component catalog
└── components/         Per-component demo pages
```

See `CLAUDE.md` for the full component-creation SOP and the seven non-negotiable design rules.

## Design rules (non-negotiable)

Every component must follow these:

1. **No-Line Rule** — no 1px borders. Use tonal surface shifts and spacing for separation.
2. **Tonal Shadows** — shadow color matches background, darkened 10%, 32dp blur, no offset.
3. **Ghost Border** — accessibility only. `outlineVariant` at 15% opacity.
4. **Neon Glow** — primary CTAs emit a 15-20% spread of their base color.
5. **Kinetic Interactions** — hover scales 1.05x, active 0.95x, 200ms ease.
6. **Squircle Radii** — use `ElectricPopShapes`, not `RoundedCornerShape`.
7. **Typography Impact** — uppercase, italic, black-weight headlines.

## Coding conventions

- Read colors from `MaterialTheme.colorScheme`. Never hardcode hex.
- Read typography from `MaterialTheme.typography`. Never hardcode `TextStyle`.
- Read spacing from `ElectricPopTheme.spacing`.
- Composites must compose from foundation components, not duplicate their drawing code.
- Use `PopIcons.*` for icons in demos and tests. Do not add `material-icons-core` as a dependency.

## Tests

Unit tests live next to the source under `library/src/commonTest/`. Run `./gradlew :library:desktopTest` for the fast loop.

Tests must exercise the component's actual code, not Kotlin stdlib. If a component is purely visual, write a minimal placeholder test stating that visual validation happens via the demo app and the Roborazzi screenshot tests — do not pad with `String.uppercase()` calls or similar.

`runComposeUiTest` is not available in `commonTest`. UI behavior tests belong in `desktopTest`.

## Screenshot tests

Every component has Roborazzi goldens at `library/src/desktopTest/snapshots/`, one PNG per theme.

When your change affects rendering:

```bash
./gradlew :library:recordRoborazziDesktop      # update goldens
./gradlew :library:verifyRoborazziDesktop      # verify
```

PRs that re-record goldens must explain why in the PR description and include before/after images.

## Demo app

If you add a component, register it in `demo/src/commonMain/kotlin/com/electricpop/demo/CatalogScreen.kt` and add a `*Demo.kt` file under `demo/.../components/` showing every variant.

Verify the demo renders correctly in both light and dark themes before requesting review.

## Branching and commits

- Branch from `main`: `git checkout -b feat/pop-something` or `chore/something`.
- One logical change per PR. Component additions get one branch and one PR each; PRs target `main` directly.
- Commit messages follow [Conventional Commits](https://www.conventionalcommits.org/): `feat(foundation): add PopButton`, `fix(chart): autoscale donut center text`, `chore(docs): drop PopFeatureCard from inventory`.
- Do not include AI tool attribution or `Co-Authored-By` lines for AI assistants in commits.
- Never push directly to `main`. Open a PR.

## Pull request checklist

Before requesting review, confirm:

- [ ] `./gradlew :library:build` passes
- [ ] `./gradlew :library:desktopTest` passes
- [ ] `./gradlew :library:verifyRoborazziDesktop` passes (or goldens re-recorded with explanation)
- [ ] Demo app shows the change in light + dark
- [ ] Public APIs are documented with KDoc
- [ ] Commit message follows Conventional Commits

## Reporting bugs and proposing features

Use the issue templates under `.github/ISSUE_TEMPLATE/`. For visual issues, attach a screenshot or screen recording — it speeds triage considerably.

## Code of Conduct

This project follows the [Contributor Covenant 2.1](CODE_OF_CONDUCT.md). By participating, you agree to its terms.

## License

By contributing, you agree that your contributions will be licensed under the [Apache License 2.0](LICENSE).
