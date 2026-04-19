# Phase 03: Implementation

Prerequisites: Phase 01 and 02 fully complete. Pixy pipeline validated.

## Pre-Component Setup
| ID | Step | Status |
|----|------|--------|
| P1 | Extract dark color scheme from Stitch dark screens | [DONE] |
| P2 | Bundle Space Grotesk + Manrope fonts as compose resources | [DONE] |
| P3 | Replace hand-coded PopIcons with Material Symbols vector resources | [DONE] |

## Wave 1: Core Foundation
| ID | Component | Status |
|----|-----------|--------|
| 1 | PopPill | [DONE] |
| 2 | PopIcon | [DONE] |
| 3 | PopSurface | [DONE] |
| 4 | PopBadge | [DONE] |

## Wave 2: Input Foundation
| ID | Component | Status |
|----|-----------|--------|
| 5 | PopButton | [DONE] |
| 6 | PopTextField | [DONE] |
| 7 | PopSwitch | [DONE] |
| 8 | PopSlider | [DONE] |
| 9 | PopRadioGroup | [DONE] |
| 10 | PopChip | [DONE] |
| 11 | PopDropdown | [DONE] |

## Wave 3: Layout Foundation
| ID | Component | Status |
|----|-----------|--------|
| 12 | PopIconRow | [DONE] |
| 13 | PopSectionHeader | [DONE] — redesigned per Stitch "Stash Hub Card Doc" (PR #14) |
| 14 | PopTitleBar | [DONE] — redesigned per Stitch "Live Ledger Cards Doc" (PR #14) |
| 15 | PopDisplayText | [DONE] |
| 16 | PopIconListItem | [DONE] |
| 17 | PopStepList | [DONE] |
| 18 | PopTable | [DONE] |
| 19 | PopCodeBlock | [DONE] — PR #21 merged |
| 20 | PopBottomBar | [DONE] |

## Wave 4: Composites
| ID | Component | Status |
|----|-----------|--------|
| 21 | PopDataRow | [DONE] — PR #22 merged |
| 22 | PopBannerCard (enhanced) | [DONE] — PR #23 merged — renamed from PopMetricCard 2026-04-18 |
| 23 | PopFeatureCard | [DONE] — PR merged |
| 24 | PopDashboardCard | [DONE] — PR merged |
| 25 | PopCarouselCard | [DONE] — PR merged |
| 26 | PopActionCard | [DONE] — PR #28 open |
| 27 | PopImageBannerCard | [NEXT] |

## Wave 5: Charts
| ID | Component | Status |
|----|-----------|--------|
| 28 | PopLineChart | [PENDING] |
| 29 | PopBarChart | [PENDING] |
| 30 | PopDonutChart | [PENDING] |

## Fix Log
| Date | Fix | PR |
|------|-----|----|
| 2026-04-07 | PopSectionHeader + PopTitleBar redesigned to match Stitch designs | #14 |
| 2026-04-09 | PopCodeBlock copy button bg hardcoded to surfaceContainerHigh — clashed with custom containerColor in dark mode. Fixed to contentColor at 15% opacity | #21 |
| 2026-04-10 | PopMetricCard: increased padding (xl), enlarged badge (labelLarge), replaced icon row with overlapping coin-stack (+N overflow). Stitch HTML used for exact CSS→Compose mapping. Updated pixy-planner to download HTML. | #23 |
| 2026-04-18 | PopMetricCard renamed to PopBannerCard (enhanced chip + single-row icons); old numeric PopBannerCard deleted and replaced by new PopImageBannerCard. See spec `2026-04-18-banner-card-refactor`. | – |
