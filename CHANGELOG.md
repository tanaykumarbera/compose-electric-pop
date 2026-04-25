# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Apache 2.0 LICENSE file at repo root.
- Contributor Covenant 2.1 Code of Conduct.
- CONTRIBUTING guide covering build commands, screenshot recording, and commit conventions.
- GitHub issue and pull request templates.
- Dependabot config for `gradle` and `github-actions` ecosystems (weekly).

### Changed
- Documentation: dropped `PopFeatureCard` from the inventory; its hero-spotlight role is fulfilled by `PopBannerCard`. Inventory is now 26 components (20 foundation + 6 composite + 1 PopChart with three styles).

## [0.1.0] - 2026-04-25

Initial public scaffold release. Contains the full design system implementation:

- **20 foundation components**: PopButton, PopTextField, PopRadioGroup, PopSwitch, PopSlider, PopChip, PopIcon, PopSurface, PopBadge, PopPill, PopIconRow, PopSectionHeader, PopTitleBar, PopDisplayText, PopCodeBlock, PopIconListItem, PopTable, PopStepList, PopBottomBar, PopDropdown.
- **6 composite components**: PopCarouselCard, PopDashboardCard, PopDataRow, PopActionCard, PopBannerCard, PopImageBannerCard.
- **1 chart component** (`PopChart`) with three styles: `PopChartStyle.Line`, `PopChartStyle.Bar`, `PopChartStyle.Donut`.
- ElectricPopTheme with Kinetic Pulse color palette, Space Grotesk + Manrope typography, squircle shapes, and 8dp spacing scale.
- Roborazzi golden screenshots for every component, light + dark.
- Demo catalog app for Android, iOS, and Desktop.

[Unreleased]: https://github.com/tanaykumarbera/compose-electric-pop/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/tanaykumarbera/compose-electric-pop/releases/tag/v0.1.0
