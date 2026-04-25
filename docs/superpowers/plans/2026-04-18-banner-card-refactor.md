# Banner Card Refactor Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Reshape the composite-tier banner family: rename `PopMetricCard` → `PopBannerCard` with enhanced chip/icon styling, introduce a new `PopImageBannerCard` for the image-hero concept, and fix dark-mode screenshot parity via a theme-painted page background.

**Architecture:** One branch (`feat/pop-banner-card`), two commits. Commit 1 renames Metric → Banner with enhancements, deletes the old numeric Banner, and updates all tracking docs + the implementor agent template. Commit 2 adds the new `PopImageBannerCard` with a configurable text anchor + scrim, bundled sample drawable, and 10 Roborazzi goldens.

**Tech Stack:** Kotlin 2.3.20, Compose Multiplatform 1.10.3, Roborazzi (desktop screenshot goldens), `MaterialTheme.shapes.extraSmall` (squircle), `ElectricPopTheme.spacing`, `PopShapeFull`, `PopSurface`, `PopBadge`, `PopDisplayText`, `PopIcon`.

---

## File Structure

### Commit 1 — Rename + enhance

**Renamed (via `git mv`, then symbol-renamed in place):**
- `library/src/commonMain/kotlin/co/tanay/electricpop/composite/PopMetricCard.kt` → `PopBannerCard.kt`
- `library/src/commonTest/kotlin/co/tanay/electricpop/composite/PopMetricCardTest.kt` → `PopBannerCardTest.kt`
- `library/src/desktopTest/kotlin/co/tanay/electricpop/composite/PopMetricCardScreenshotTest.kt` → `PopBannerCardScreenshotTest.kt`
- `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/PopMetricCardDemo.kt` → `PopBannerCardDemo.kt`

**Deleted:**
- `library/src/commonMain/kotlin/co/tanay/electricpop/composite/PopBannerCard.kt` (current numeric impl — **deleted before** git-mv of Metric → Banner so the rename target is free)
- `library/src/commonTest/kotlin/co/tanay/electricpop/composite/PopBannerCardTest.kt`
- `library/src/desktopTest/kotlin/co/tanay/electricpop/composite/PopBannerCardScreenshotTest.kt`
- `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/PopBannerCardDemo.kt`
- `library/src/desktopTest/snapshots/PopBannerCard_allVariants_light.png` (old numeric goldens)
- `library/src/desktopTest/snapshots/PopBannerCard_allVariants_dark.png`
- `library/src/desktopTest/snapshots/PopMetricCard_allVariants_light.png` (will be re-recorded under new name)
- `library/src/desktopTest/snapshots/PopMetricCard_allVariants_dark.png`

**Modified (docs / inventory / agent):**
- `.claude/agents/pixy-implementor.md` — add dark-bg wrapper to screenshot-test template
- `CLAUDE.md:129` — composite inventory line
- `AGENTS.md` — composite inventory table (rows 54–55)
- `README.md:82–83` — composite description rows
- `PROMPT-PHASE-03.md:140, 145` — Wave 4 tracker rows 22 and 27 + `⬅ NEXT` marker
- `.execution-history/phase-03-implementation.md` — append dated note in fix log
- `.execution-history/execution-summary-phase-1-2.md` — append one-line historical note
- `docs/superpowers/plans/2026-03-25-electric-pop-implementation.md:1613–1614, 1676` — append dated note near banner rows
- `docs/superpowers/specs/2026-03-25-electric-pop-design.md:144, 153–154` — update 6.2 table + revision note
- `library/src/commonMain/kotlin/co/tanay/electricpop/foundation/PopSurface.kt:84` — update comment reference

**Re-recorded goldens (post-enhancements + dark-bg wrapper):**
- `library/src/desktopTest/snapshots/PopBannerCard_allVariants_light.png` (new, Metric-derived content)
- `library/src/desktopTest/snapshots/PopBannerCard_allVariants_dark.png` (new, Metric-derived content)

### Commit 2 — Add PopImageBannerCard

**Created:**
- `library/src/commonMain/kotlin/co/tanay/electricpop/composite/PopImageBannerCard.kt` — component
- `library/src/commonTest/kotlin/co/tanay/electricpop/composite/PopImageBannerCardTest.kt` — unit test (anchor→alignment + gradient helpers)
- `library/src/desktopTest/kotlin/co/tanay/electricpop/composite/PopImageBannerCardScreenshotTest.kt` — 5 variants × 2 themes = 10 goldens
- `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/PopImageBannerCardDemo.kt` — all 5 variants
- `library/src/commonMain/composeResources/drawable/pop_banner_hero.png` — bundled abstract sample (~1200×800, <100KB, CC0 / procedurally generated)
- 10 new snapshot files under `library/src/desktopTest/snapshots/PopImageBannerCard_*.png`

**Modified:**
- `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/CatalogScreen.kt` — add one catalog entry + import

---

## Preflight

### Task 0: Verify baseline

**Files:** none

- [ ] **Step 1: Confirm clean-ish working tree and expected branch**

Run: `git status --short && git branch --show-current`
Expected (partial OK): branch `feat/pop-banner-card`; any local changes present are pre-existing spec/doc edits already committed or staged.

- [ ] **Step 2: Confirm baseline green build**

Run: `./gradlew :library:desktopTest :library:verifyRoborazziDesktop`
Expected: BUILD SUCCESSFUL. This establishes that all current snapshots match — any failures after this point are attributable to our changes.

- [ ] **Step 3: Record current component inventory line counts for sanity**

Run: `grep -c "CatalogEntry(" demo/src/commonMain/kotlin/co/tanay/electricpop/demo/CatalogScreen.kt`
Expected: matches the 25-ish existing entries (the count is informational — if the number changes unexpectedly, we know a merge interleaved other component work).

---

## Commit 1: Rename PopMetricCard to PopBannerCard and enhance chip/icon row

### Task 1: Delete old PopBannerCard (numeric) files

**Files:**
- Delete: `library/src/commonMain/kotlin/co/tanay/electricpop/composite/PopBannerCard.kt`
- Delete: `library/src/commonTest/kotlin/co/tanay/electricpop/composite/PopBannerCardTest.kt`
- Delete: `library/src/desktopTest/kotlin/co/tanay/electricpop/composite/PopBannerCardScreenshotTest.kt`
- Delete: `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/PopBannerCardDemo.kt`
- Delete: `library/src/desktopTest/snapshots/PopBannerCard_allVariants_light.png`
- Delete: `library/src/desktopTest/snapshots/PopBannerCard_allVariants_dark.png`

- [ ] **Step 1: Remove numeric PopBannerCard sources, tests, demo, and goldens**

```bash
git rm library/src/commonMain/kotlin/co/tanay/electricpop/composite/PopBannerCard.kt
git rm library/src/commonTest/kotlin/co/tanay/electricpop/composite/PopBannerCardTest.kt
git rm library/src/desktopTest/kotlin/co/tanay/electricpop/composite/PopBannerCardScreenshotTest.kt
git rm demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/PopBannerCardDemo.kt
git rm library/src/desktopTest/snapshots/PopBannerCard_allVariants_light.png
git rm library/src/desktopTest/snapshots/PopBannerCard_allVariants_dark.png
```

- [ ] **Step 2: Temporarily remove the catalog entry that references the deleted demo**

Edit `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/CatalogScreen.kt`:
- Delete the import line `import co.tanay.electricpop.demo.components.PopBannerCardDemo`
- Delete the catalog row `CatalogEntry("PopBannerCard", "Composite") { PopBannerCardDemo() },` (currently the last Wave 4 entry at line 82)

(The renamed Metric demo is still called `PopMetricCardDemo` at this point — we haven't touched its symbol yet. Task 3 updates the catalog again to point at the renamed demo.)

- [ ] **Step 3: Compile to confirm nothing else in the codebase referenced the old numeric types**

Run: `./gradlew :library:compileKotlinDesktop :demo:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL. (The types `PopBannerCardTrend`, `PopBannerCardTrendDirection`, `PopBannerCardVital` were only used by the deleted files — a compile pass with zero errors confirms no other callers.)

If any `Unresolved reference` surfaces for those three types, that call site must be updated now (remove the call or migrate to `PopBadgeDirection` / `PopIconItem`); we do not proceed with stale references.

---

### Task 2: Rename Metric files to Banner via git mv (no logic changes yet)

**Files:**
- Rename: `PopMetricCard.kt` → `PopBannerCard.kt`
- Rename: `PopMetricCardTest.kt` → `PopBannerCardTest.kt`
- Rename: `PopMetricCardScreenshotTest.kt` → `PopBannerCardScreenshotTest.kt`
- Rename: `PopMetricCardDemo.kt` → `PopBannerCardDemo.kt`

- [ ] **Step 1: git mv the four files**

```bash
git mv library/src/commonMain/kotlin/co/tanay/electricpop/composite/PopMetricCard.kt \
       library/src/commonMain/kotlin/co/tanay/electricpop/composite/PopBannerCard.kt

git mv library/src/commonTest/kotlin/co/tanay/electricpop/composite/PopMetricCardTest.kt \
       library/src/commonTest/kotlin/co/tanay/electricpop/composite/PopBannerCardTest.kt

git mv library/src/desktopTest/kotlin/co/tanay/electricpop/composite/PopMetricCardScreenshotTest.kt \
       library/src/desktopTest/kotlin/co/tanay/electricpop/composite/PopBannerCardScreenshotTest.kt

git mv demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/PopMetricCardDemo.kt \
       demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/PopBannerCardDemo.kt
```

- [ ] **Step 2: Also remove old Metric goldens (will be re-recorded under Banner names)**

```bash
git rm library/src/desktopTest/snapshots/PopMetricCard_allVariants_light.png
git rm library/src/desktopTest/snapshots/PopMetricCard_allVariants_dark.png
```

- [ ] **Step 3: Verify rename status**

Run: `git status --short | grep -E "Pop(Metric|Banner)Card"`
Expected: four `R` (renamed) lines for the four moved files and two `D` lines for the deleted Metric goldens.

---

### Task 3: Rename Metric symbols to Banner in the moved component file

**Files:**
- Modify: `library/src/commonMain/kotlin/co/tanay/electricpop/composite/PopBannerCard.kt` (formerly `PopMetricCard.kt`)

- [ ] **Step 1: Rename the enum class, companion type, and function**

Edit `library/src/commonMain/kotlin/co/tanay/electricpop/composite/PopBannerCard.kt`:
- Replace (all occurrences): `PopMetricCardStyle` → `PopBannerCardStyle`
- Replace (all occurrences): `PopMetricCard` → `PopBannerCard` (this covers the function name, the kdoc, and stray identifiers)

Note: the `@param style` kdoc line that referenced `PopMetricCardStyle.Hero` / `.Surface` becomes `PopBannerCardStyle.Hero` / `.Surface`.

- [ ] **Step 2: Compile to catch misses**

Run: `./gradlew :library:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL. If `Unresolved reference: PopMetricCard*` appears in the demo or test files, Task 4/5 will fix those.

---

### Task 4: Update the renamed test file to reference the new symbol

**Files:**
- Modify: `library/src/commonTest/kotlin/co/tanay/electricpop/composite/PopBannerCardTest.kt`

- [ ] **Step 1: Rename the class and the placeholder comment**

Edit `library/src/commonTest/kotlin/co/tanay/electricpop/composite/PopBannerCardTest.kt`:
- Replace `class PopMetricCardTest` → `class PopBannerCardTest`
- Replace the placeholder body so it reads:

```kotlin
package co.tanay.electricpop.composite

import kotlin.test.Test

class PopBannerCardTest {
    @Test
    fun visualValidationViaDemo() {
        // PopBannerCard is a purely visual composite component.
        // It has no extractable business logic beyond what its foundation
        // components (PopDisplayText, PopBadge, PopSurface) already provide.
        // Visual correctness is validated via:
        //   - The demo app (PopBannerCardDemo, registered in CatalogScreen)
        //   - Roborazzi screenshot tests in desktopTest (PopBannerCardScreenshotTest)
    }
}
```

- [ ] **Step 2: Run common tests**

Run: `./gradlew :library:desktopTest --tests "co.tanay.electricpop.composite.PopBannerCardTest"`
Expected: BUILD SUCCESSFUL with 1 test passed.

---

### Task 5: Update the renamed demo file to reference the new symbol

**Files:**
- Modify: `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/PopBannerCardDemo.kt`

- [ ] **Step 1: Rename composable and imports**

Edit `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/PopBannerCardDemo.kt`:
- Replace (all occurrences): `PopMetricCard` → `PopBannerCard`
- Replace (all occurrences): `PopMetricCardStyle` → `PopBannerCardStyle`
- Replace `fun PopMetricCardDemo` → `fun PopBannerCardDemo`
- The two `co.tanay.electricpop.composite.PopMetricCard*` imports become `co.tanay.electricpop.composite.PopBannerCard*`.

- [ ] **Step 2: Compile the demo module**

Run: `./gradlew :demo:compileKotlinDesktop`
Expected: `Unresolved reference: PopBannerCardDemo` in `CatalogScreen.kt` (because we removed that entry in Task 1, Step 2 — it's re-added in Step 3 below).

- [ ] **Step 3: Re-register the demo in CatalogScreen**

Edit `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/CatalogScreen.kt`:
- Remove the line `CatalogEntry("PopMetricCard", "Composite") { PopMetricCardDemo() },` (currently at row 77).
- Remove the line `import co.tanay.electricpop.demo.components.PopMetricCardDemo`.
- Add the line `import co.tanay.electricpop.demo.components.PopBannerCardDemo`.
- Add the Wave 4 row (replacing the removed `PopMetricCard` row, at the same position for a minimal diff):
  ```kotlin
  CatalogEntry("PopBannerCard", "Composite") { PopBannerCardDemo() },
  ```

The final Wave 4 block should be six entries, in this order: `PopDataRow`, `PopBannerCard`, (commented `PopFeatureCard`), `PopDashboardCard`, `PopCarouselCard`, `PopActionCard`. The old numeric-banner catalog entry deleted in Task 1 is not re-added — there is exactly one `PopBannerCard` catalog entry.

- [ ] **Step 4: Compile**

Run: `./gradlew :demo:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL.

---

### Task 6: Update the renamed screenshot-test file to reference the new symbol

**Files:**
- Modify: `library/src/desktopTest/kotlin/co/tanay/electricpop/composite/PopBannerCardScreenshotTest.kt`

- [ ] **Step 1: Rename symbols in the screenshot test (file paths updated in Task 8)**

Edit `library/src/desktopTest/kotlin/co/tanay/electricpop/composite/PopBannerCardScreenshotTest.kt`:
- Replace (all occurrences): `PopMetricCard` → `PopBannerCard`
- Replace the two golden file paths:
  - `"src/desktopTest/snapshots/PopMetricCard_allVariants_light.png"` → `"src/desktopTest/snapshots/PopBannerCard_allVariants_light.png"`
  - `"src/desktopTest/snapshots/PopMetricCard_allVariants_dark.png"` → `"src/desktopTest/snapshots/PopBannerCard_allVariants_dark.png"`
- Replace `@Composable private fun MetricCardContent` → `@Composable private fun BannerCardContent`.
- Update the two call sites from `MetricCardContent(...)` → `BannerCardContent(...)`.

- [ ] **Step 2: Compile desktop tests**

Run: `./gradlew :library:compileDesktopTestKotlinDesktop`
Expected: BUILD SUCCESSFUL. (Do not run screenshots yet — Task 7 changes the chip typography and Task 8 adds the dark-bg wrapper; re-record once after both.)

---

### Task 7: Enhance the chip (titleLarge italic/Black + uppercased) and merge chip + icons onto one row

**Files:**
- Modify: `library/src/commonMain/kotlin/co/tanay/electricpop/composite/PopBannerCard.kt`

- [ ] **Step 1: Add the typography imports**

Near the other `import androidx.compose.ui.text.font.*` imports, ensure both are present. The file currently has none — add these three:

```kotlin
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
```

(`FontWeight.Black` + `FontStyle.Italic` are used by the new chip typography. The old `sp` import can be removed if no longer referenced — verify with the compiler.)

- [ ] **Step 2: Rewrite the chip + icon block to a single row with the enhanced chip typography**

Locate the existing block in `PopBannerCard.kt` that starts at the current line `// 3. Badge — larger pill to match Stitch design (px-5 py-2 text-lg)` and ends just before the closing braces of the outer `Column` (this is approximately lines 156–293 in the pre-edit file). Replace that entire block — from the `if (badgeValue != null) {` through the end of the `if (icons.isNotEmpty()) { … }` block — with:

```kotlin
                // 3 + 4. Chip + icon cluster on a single row.
                // Chip typography: titleLarge italic/Black/uppercase (ported from old PopBannerCard).
                // Chip colors stay as PopMetricCard:
                //   Hero → onPrimaryContainer bg, primaryContainer content.
                //   Surface → derived from badgeDirection.toColors().
                // Icons preserved from PopMetricCard: 40dp circles, -12dp overlap, +N overflow.
                val hasChip = badgeValue != null
                val hasIcons = icons.isNotEmpty()
                if (hasChip || hasIcons) {
                    Spacer(Modifier.height(spacing.sm))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (hasChip) {
                            val chipBg: Color
                            val chipContent: Color
                            if (isHero) {
                                chipBg = MaterialTheme.colorScheme.onPrimaryContainer
                                chipContent = MaterialTheme.colorScheme.primaryContainer
                            } else {
                                val badgeColors = badgeDirection.toColors()
                                chipBg = badgeColors.containerColor
                                chipContent = badgeColors.contentColor
                            }
                            Row(
                                modifier = Modifier
                                    .clip(PopShapeFull)
                                    .background(chipBg)
                                    .padding(horizontal = spacing.md, vertical = spacing.xs),
                                horizontalArrangement = Arrangement.spacedBy(spacing.xs),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                when (badgeDirection) {
                                    PopBadgeDirection.Up -> PopIcon(
                                        imageVector = co.tanay.electricpop.foundation.PopIcons.TrendUp,
                                        contentDescription = "Trending up",
                                        size = PopIconSize.Medium,
                                        tint = chipContent,
                                    )
                                    PopBadgeDirection.Down -> PopIcon(
                                        imageVector = co.tanay.electricpop.foundation.PopIcons.TrendDown,
                                        contentDescription = "Trending down",
                                        size = PopIconSize.Medium,
                                        tint = chipContent,
                                    )
                                    PopBadgeDirection.Neutral -> {}
                                }
                                Text(
                                    text = badgeValue!!.uppercase(),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontStyle = FontStyle.Italic,
                                        fontWeight = FontWeight.Black,
                                    ),
                                    color = chipContent,
                                )
                            }
                        }

                        if (hasIcons) {
                            val maxVisible = 5
                            val visibleIcons = icons.take(maxVisible)
                            val overflowCount = icons.size - maxVisible
                            val coinSize = 40.dp
                            val overlapOffset = 12.dp
                            val borderColor = if (isHero) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceContainer
                            }
                            val iconBgColor = if (isHero) {
                                Color.White.copy(alpha = 0.4f)
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerHigh
                            }
                            val iconTint = if (isHero) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                            val totalItems = visibleIcons.size + if (overflowCount > 0) 1 else 0
                            Box(
                                modifier = Modifier
                                    .size(
                                        width = coinSize * totalItems - overlapOffset * (totalItems - 1),
                                        height = coinSize,
                                    ),
                            ) {
                                visibleIcons.forEachIndexed { index, item ->
                                    Box(
                                        modifier = Modifier
                                            .offset(x = (coinSize - overlapOffset) * index)
                                            .size(coinSize)
                                            .clip(CircleShape)
                                            .background(iconBgColor)
                                            .border(2.dp, borderColor, CircleShape),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        PopIcon(
                                            imageVector = item.imageVector,
                                            contentDescription = item.contentDescription,
                                            size = PopIconSize.Small,
                                            tint = iconTint,
                                        )
                                    }
                                }
                                if (overflowCount > 0) {
                                    val overflowIndex = visibleIcons.size
                                    Box(
                                        modifier = Modifier
                                            .offset(x = (coinSize - overlapOffset) * overflowIndex)
                                            .size(coinSize)
                                            .clip(CircleShape)
                                            .background(iconBgColor)
                                            .border(2.dp, borderColor, CircleShape),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text = "+$overflowCount",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = iconTint,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
```

The intent of this rewrite:
- **Single Row** for chip + icons (replaces two separate `if` blocks each with its own `Spacer`).
- **One** `Spacer(spacing.sm)` **above the Row** (replaces `Spacer(xxs)` before the chip and `Spacer(xs)` before the icon cluster).
- **Chip typography** changes from `labelLarge` to `titleLarge` + italic + Black.
- **Chip colors + icon cluster styling** are unchanged from today's `PopMetricCard`.
- **Zero-emission guard:** if both `badgeValue` is null and `icons` is empty, neither `Spacer` nor `Row` is emitted (preserves today's behavior).

Leave the outer kdoc intact except:
- Replace any residual `PopMetricCard` identifier in prose with `PopBannerCard`.
- If the kdoc mentions "larger pill to match Stitch design (px-5 py-2 text-lg)" verbatim, that prose can stay — the chip typography change only changes the `style =` line, not the padding or Stitch parity.

- [ ] **Step 3: Compile library**

Run: `./gradlew :library:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL. Any unused-import warnings (`.sp`) should be cleaned up.

---

### Task 8: Apply the dark-bg screenshot wrapper to the renamed screenshot test

**Files:**
- Modify: `library/src/desktopTest/kotlin/co/tanay/electricpop/composite/PopBannerCardScreenshotTest.kt`

- [ ] **Step 1: Wrap the variants in a theme-painted page background**

Edit `BannerCardContent(darkTheme: Boolean)`. The current body is:

```kotlin
ElectricPopTheme(darkTheme = darkTheme) {
    val spacing = ElectricPopTheme.spacing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        // … four PopBannerCard variants
    }
}
```

Change it to:

```kotlin
ElectricPopTheme(darkTheme = darkTheme) {
    val spacing = ElectricPopTheme.spacing
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            // … four PopBannerCard variants (unchanged)
        }
    }
}
```

Add the necessary imports at the top of the file if missing:

```kotlin
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
```

- [ ] **Step 2: Re-record the two goldens**

Run: `./gradlew :library:recordRoborazziDesktop --tests "co.tanay.electricpop.composite.PopBannerCardScreenshotTest"`
Expected: BUILD SUCCESSFUL; two PNGs written under `library/src/desktopTest/snapshots/PopBannerCard_allVariants_{light,dark}.png`.

- [ ] **Step 3: Verify goldens are stable on a second pass**

Run: `./gradlew :library:verifyRoborazziDesktop --tests "co.tanay.electricpop.composite.PopBannerCardScreenshotTest"`
Expected: BUILD SUCCESSFUL (no diff on re-verify).

Sanity check: open both PNGs and confirm the dark one has a dark page background outside the cards (this is the whole reason for Section 8 in the spec). If the dark background is still white, the wrapper is wrong — re-check Step 1.

---

### Task 9: Update the pixy-implementor agent template

**Files:**
- Modify: `.claude/agents/pixy-implementor.md`

- [ ] **Step 1: Update the screenshot-test code template (around lines 98–121)**

In the example under "Step 7", change both `runDesktopComposeUiTest` blocks from:

```kotlin
setContent {
    ElectricPopTheme(darkTheme = false) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Render all variants here
        }
    }
}
```

to:

```kotlin
setContent {
    ElectricPopTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Render all variants here
            }
        }
    }
}
```

(And the mirrored `darkTheme = true` block.)

- [ ] **Step 2: Add a prose note directly after the template**

After the closing fence of the example code block and before "### 8. Build, test, and record screenshots", insert:

```
> **Required:** wrap screenshot content in `Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))` so dark-mode goldens have a dark page background for visual parity with light goldens. `ElectricPopTheme` alone does not paint a page background.
```

- [ ] **Step 3: Sanity-check the diff**

Run: `git diff .claude/agents/pixy-implementor.md`
Expected: two hunks — one updating the code fence, one adding the prose note. No other regions touched.

---

### Task 10: Update docs, inventories, and trackers

**Files:**
- Modify: `CLAUDE.md`
- Modify: `AGENTS.md`
- Modify: `README.md`
- Modify: `PROMPT-PHASE-03.md`
- Modify: `.execution-history/phase-03-implementation.md`
- Modify: `.execution-history/execution-summary-phase-1-2.md`
- Modify: `docs/superpowers/plans/2026-03-25-electric-pop-implementation.md`
- Modify: `docs/superpowers/specs/2026-03-25-electric-pop-design.md`
- Modify: `library/src/commonMain/kotlin/co/tanay/electricpop/foundation/PopSurface.kt`

- [ ] **Step 1: `CLAUDE.md` — swap `PopMetricCard` for `PopImageBannerCard` in the composite list**

Edit `CLAUDE.md` line 129:

```
PopFeatureCard, PopCarouselCard, PopDashboardCard, PopDataRow, PopActionCard, PopBannerCard, PopMetricCard
```

becomes:

```
PopFeatureCard, PopCarouselCard, PopDashboardCard, PopDataRow, PopActionCard, PopBannerCard, PopImageBannerCard
```

- [ ] **Step 2: `AGENTS.md` — update the Composite (7) table**

Edit `AGENTS.md`:
- Row 6 `PopBannerCard` column "Composes" stays as `PopSurface + headline overlay`; description stays `Big image + text` → actually change the row so it describes the **enhanced metric banner** (chip + icons). Replace row 6 with:
  `| 6 | PopBannerCard | PopBadgeDirection, PopDisplayText, PopIcon, PopSurface | Metric banner: label + value + trend chip + overlapping icon cluster (Hero/Surface styles). |`
- Replace row 7 (`PopMetricCard`) with:
  `| 7 | PopImageBannerCard | PopSurface + Image | Big image card with overlaid headline, configurable text anchor + scrim. |`

- [ ] **Step 3: `README.md` — rewrite rows 82 and 83**

Edit `README.md`:
- Row 82 `| PopBannerCard | Big image card with overlay headline |` → `| PopBannerCard | Metric banner with trend chip and overlapping icon cluster |`
- Row 83 `| PopMetricCard | Self-contained metric display |` → `| PopImageBannerCard | Big image card with overlay headline and configurable text anchor (Coil/Kamel compatible via `Painter`) |`

- [ ] **Step 4: `PROMPT-PHASE-03.md` — Wave 4 tracker rows**

Edit `PROMPT-PHASE-03.md`:
- Row 22 (line 140): change `PopMetricCard` label → `PopBannerCard (enhanced)` and keep the `DONE — PR #23 merged` status, but append ` — renamed 2026-04-18`.
- Row 27 (line 145): change from the `⬅ NEXT` marker on `PopBannerCard` to the renamed `PopImageBannerCard`, status `⬅ NEXT`.

Final values:

```
| 22 | PopBannerCard (enhanced) | composite | PopBadge, PopDisplayText, PopIcon, PopSurface | DONE — PR #23 merged; renamed from PopMetricCard 2026-04-18 |
| **27** | **PopImageBannerCard** | **composite** | **PopSurface + Image overlay** | **⬅ NEXT** |
```

Also update the section header on line 136 if applicable — `IN PROGRESS (1 remaining)` still holds (one NEXT).

- [ ] **Step 5: `.execution-history/phase-03-implementation.md` — append a dated fix-log row**

Add a new row at the end of the Fix Log table:

```
| 2026-04-18 | PopMetricCard renamed to PopBannerCard (enhanced chip + single-row icons); old numeric PopBannerCard deleted and replaced by new PopImageBannerCard. See spec `2026-04-18-banner-card-refactor`. | – |
```

Also update the Wave 4 tracker table in this file (where row 27 currently reads `| 27 | PopBannerCard | [NEXT] |`): rename the row to `| 27 | PopImageBannerCard | [NEXT] |`. Row 22 already marked DONE remains DONE — append `— renamed to PopBannerCard` inline if consistent with the file's style.

- [ ] **Step 6: `.execution-history/execution-summary-phase-1-2.md` — append a historical note**

Append one line at the very end of the file:

```
- 2026-04-18 — Composite tier rebalanced: PopMetricCard renamed to PopBannerCard (enhanced chip/icon row); old numeric PopBannerCard removed; new PopImageBannerCard added. Spec: `docs/superpowers/specs/2026-04-18-banner-card-refactor.md`.
```

(No rewriting of past entries.)

- [ ] **Step 7: `docs/superpowers/plans/2026-03-25-electric-pop-implementation.md` — append a historical note near rows 1613–1614 and 1676**

At line 1613–1614 (the Wave 4 table inside the plan doc), leave the text as-is and insert a single block quote immediately above:

```
> **Revised 2026-04-18:** see `docs/superpowers/specs/2026-04-18-banner-card-refactor.md`. PopMetricCard was renamed to PopBannerCard; original numeric PopBannerCard was replaced by PopImageBannerCard.
```

Do the same at line 1676 next to `Composite (7): …`:

```
> _2026-04-18 note: the `PopMetricCard` slot in this list became `PopImageBannerCard`. See `docs/superpowers/specs/2026-04-18-banner-card-refactor.md`._
```

- [ ] **Step 8: `docs/superpowers/specs/2026-03-25-electric-pop-design.md` — update 6.2 table**

Edit `docs/superpowers/specs/2026-03-25-electric-pop-design.md`:
- Immediately below the `### 6.2 Composite (7 components)` header (line 144), add:
  ```
  > **Revised 2026-04-18 — see spec `2026-04-18-banner-card-refactor`.**
  ```
- Replace row 6 (line 153):
  `| 6 | **PopBannerCard** | PopBadge, PopDisplayText, PopIcon, PopSurface | Metric banner: label, value, trend chip (italic black uppercase), overlapping icon cluster. Hero/Surface styles. |`
- Replace row 7 (line 154) entirely:
  `| 7 | **PopImageBannerCard** | PopSurface + Image overlay | Big image hero card with overlaid italic-black-uppercase headline. Configurable text anchor + scrim. |`

- [ ] **Step 9: `library/src/commonMain/kotlin/co/tanay/electricpop/foundation/PopSurface.kt:84` — update comment**

The line currently reads:

```
 * Many composite components (PopFeatureCard, PopMetricCard, PopDashboardCard, etc.)
```

Replace with:

```
 * Many composite components (PopFeatureCard, PopBannerCard, PopDashboardCard, etc.)
```

- [ ] **Step 10: Verify no remaining `PopMetricCard` references in live code**

Run: `grep -rn "PopMetricCard" library demo .claude --include="*.kt" --include="*.md"`
Expected: **no matches** in `library/` or `demo/` Kotlin sources or in `CLAUDE.md` / `AGENTS.md` / `README.md`. Acceptable remaining matches: historical notes inside `.execution-history/` and the plan/spec docs (those are archival context; they MUST say `PopMetricCard renamed to PopBannerCard …`, never reference `PopMetricCard` as a current component).

If any live match remains, fix it inline before moving on.

---

### Task 11: Build, test, verify-goldens, commit

**Files:** none (verification)

- [ ] **Step 1: Full library + demo compile**

Run: `./gradlew :library:compileKotlinDesktop :demo:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 2: Desktop test suite**

Run: `./gradlew :library:desktopTest`
Expected: BUILD SUCCESSFUL; `PopBannerCardTest.visualValidationViaDemo` passes; `PopBannerCardScreenshotTest.allVariants_light` + `..._dark` both pass (matching the goldens recorded in Task 8).

- [ ] **Step 3: Golden verification**

Run: `./gradlew :library:verifyRoborazziDesktop`
Expected: BUILD SUCCESSFUL. No pre-existing component's goldens diffed (this is the scope-limit check — we only re-recorded `PopBannerCard_*`).

If any **unrelated** golden diffs (e.g., a PopFeatureCard golden changed), stop and investigate — something about the shared rename touched a type they depend on, which would be a regression.

- [ ] **Step 4: Stage and commit**

```bash
git add -A
git status --short
```

Confirm only the expected files are staged: the four renamed files (with in-place edits), six deleted files (old numeric Banner + 2 old Metric goldens + 2 old Banner goldens; numeric banner deletions were staged via `git rm` already), two re-recorded Banner goldens, plus the doc and agent edits. No spurious files.

Commit:

```bash
git commit -m "$(cat <<'EOF'
feat(composite): rename PopMetricCard to PopBannerCard and enhance chip/icon row

Deletes the original numeric PopBannerCard (PopBannerCardTrend /
PopBannerCardVital types removed) and renames PopMetricCard to
PopBannerCard in its place. The renamed card gets two ergonomic
tweaks: the trend chip adopts titleLarge italic/Black uppercase
typography (ported from the old banner), and chip + icon cluster
collapse onto a single row instead of stacked rows.

Applies a dark-theme page background wrapper to the renamed
screenshot test so dark-mode goldens visibly differ from light
ones, and updates the pixy-implementor template to require the
wrapper for all future screenshot tests. Retroactive re-record
for the ~15 other components is intentionally out of scope.

Docs synced across CLAUDE.md, AGENTS.md, README.md,
PROMPT-PHASE-03.md, .execution-history/*, plan, and spec.
See docs/superpowers/specs/2026-04-18-banner-card-refactor.md.
EOF
)"
```

No `Co-Authored-By` / AI attribution (per `feedback_no_ai_branding`).

- [ ] **Step 5: Post-commit sanity**

Run: `git status && git log --oneline -3`
Expected: clean working tree; top commit is the one just created.

---

## Commit 2: Add PopImageBannerCard

### Task 12: Generate the bundled sample hero image

**Files:**
- Create: `library/src/commonMain/composeResources/drawable/pop_banner_hero.png`

- [ ] **Step 1: Generate an abstract CC0 PNG (~1200×800, under 100KB)**

Use an ImageMagick one-liner that produces an abstract gradient + geometric overlay (no identifiable content, freely redistributable). Run once from the repo root:

```bash
magick -size 1200x800 \
  gradient:"#0b0f12-#3a2f5a" \
  -fill "rgba(202,253,0,0.55)" -draw "polygon 0,560 1200,380 1200,800 0,800" \
  -fill "rgba(255,255,255,0.10)" -draw "circle 960,220 1120,80" \
  -fill "rgba(255,255,255,0.05)" -draw "circle 240,640 420,780" \
  -quality 85 \
  library/src/commonMain/composeResources/drawable/pop_banner_hero.png
```

If `magick` isn't available, fall back to:

```bash
convert -size 1200x800 \
  gradient:"#0b0f12-#3a2f5a" \
  -fill "rgba(202,253,0,0.55)" -draw "polygon 0,560 1200,380 1200,800 0,800" \
  library/src/commonMain/composeResources/drawable/pop_banner_hero.png
```

If neither is installed, use a Python one-liner (PIL) that ships on most dev images:

```bash
python3 - <<'PY'
from PIL import Image, ImageDraw
im = Image.new("RGB", (1200, 800), "#0b0f12")
d = ImageDraw.Draw(im, "RGBA")
# vertical gradient
for y in range(800):
    t = y / 799
    r = int(0x0b * (1 - t) + 0x3a * t)
    g = int(0x0f * (1 - t) + 0x2f * t)
    b = int(0x12 * (1 - t) + 0x5a * t)
    d.line([(0, y), (1200, y)], fill=(r, g, b))
d.polygon([(0, 560), (1200, 380), (1200, 800), (0, 800)], fill=(202, 253, 0, 140))
d.ellipse([(800, 80), (1120, 360)], fill=(255, 255, 255, 25))
d.ellipse([(60, 500), (420, 780)], fill=(255, 255, 255, 12))
im.save("library/src/commonMain/composeResources/drawable/pop_banner_hero.png", "PNG", optimize=True)
PY
```

- [ ] **Step 2: Verify size and dimensions**

Run: `du -b library/src/commonMain/composeResources/drawable/pop_banner_hero.png && python3 -c "from PIL import Image; print(Image.open('library/src/commonMain/composeResources/drawable/pop_banner_hero.png').size)"`
Expected: file is under `102400` bytes (100 KB) and dimensions are `(1200, 800)`.

If over 100KB, re-run with a lower `-quality` or shrink to `1000x667`.

- [ ] **Step 3: Confirm the resource is picked up by codegen**

Run: `./gradlew :library:generateComposeResClass`
Expected: BUILD SUCCESSFUL. After this runs, the symbol `compose_electric_pop.library.generated.resources.pop_banner_hero` will exist (same codegen pattern used for the icon drawables). Task 13 imports it.

---

### Task 13: Implement `PopImageBannerCard`

**Files:**
- Create: `library/src/commonMain/kotlin/co/tanay/electricpop/composite/PopImageBannerCard.kt`

- [ ] **Step 1: Write the anchor enum + helper functions + component**

Create `library/src/commonMain/kotlin/co/tanay/electricpop/composite/PopImageBannerCard.kt`:

```kotlin
package co.tanay.electricpop.composite

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import co.tanay.electricpop.theme.ElectricPopTheme

/**
 * Anchor position for the text block inside [PopImageBannerCard].
 */
enum class PopImageBannerTextAnchor {
    TopStart, TopCenter, TopEnd,
    CenterStart, Center, CenterEnd,
    BottomStart, BottomCenter, BottomEnd,
}

/**
 * A big image card with overlaid editorial headline. Use for marketing/hero banners.
 *
 * Sizing: clips to `MaterialTheme.shapes.extraSmall` and preserves the painter's
 * intrinsic aspect ratio; callers can also constrain height explicitly via [modifier].
 *
 * Scrim: when [scrim] is true, a linear (or radial, for [PopImageBannerTextAnchor.Center])
 * gradient is drawn from [scrimColor] at the anchored edge fading toward transparent,
 * ensuring readable text contrast on busy images.
 *
 * Design rule compliance:
 * - Rule 1 (No-Line): no borders; scrim is a gradient.
 * - Rule 5 (Kinetic): hover → 1.02×, press → 0.97× when [onClick] is provided.
 * - Rule 6 (Squircle): outer clip uses `MaterialTheme.shapes.extraSmall`.
 * - Rule 7 (Typography Impact): headline is `displayMedium` italic/Black, uppercased;
 *   eyebrow is `labelSmall` with wide tracking.
 *
 * @param painter Image source. Callers can compose with Coil's `rememberAsyncImagePainter`
 *   or Kamel's `asyncPainterResource` for remote images — both return a [Painter].
 * @param headline Required hero headline; rendered italic + Black + uppercased. `\n` is honored.
 * @param modifier Optional [Modifier]; consumer may pass `.height(…).fillMaxWidth()` to override aspect-ratio sizing.
 * @param eyebrow Optional small uppercased label above the headline.
 * @param subtitle Optional body-sized line below the headline.
 * @param textAnchor Where the text column sits within the card. Also drives scrim direction.
 * @param scrim Whether to paint a contrast gradient behind the text.
 * @param scrimColor Base color for the scrim gradient (defaults to 55% black).
 * @param contentColor Text color; eyebrow/subtitle use softened alpha.
 * @param contentDescription Accessibility description for the image. When null, the image is marked decorative.
 * @param onClick Optional click handler; enables kinetic scale interaction when set.
 */
@Composable
fun PopImageBannerCard(
    painter: Painter,
    headline: String,
    modifier: Modifier = Modifier,
    eyebrow: String? = null,
    subtitle: String? = null,
    textAnchor: PopImageBannerTextAnchor = PopImageBannerTextAnchor.BottomStart,
    scrim: Boolean = true,
    scrimColor: Color = Color.Black.copy(alpha = 0.55f),
    contentColor: Color = Color.White,
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null,
) {
    val spacing = ElectricPopTheme.spacing
    val shape = MaterialTheme.shapes.extraSmall

    // Rule 5: kinetic interaction
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val targetScale = when {
        onClick == null -> 1f
        isPressed -> 0.97f
        isHovered -> 1.02f
        else -> 1f
    }
    val scale by animateFloatAsState(targetScale, tween(200), label = "image_banner_scale")

    // Intrinsic-aspect sizing with graceful fallback for unsized painters (e.g., vector).
    val intrinsic = painter.intrinsicSize
    val aspect = if (intrinsic == Size.Unspecified || intrinsic.width <= 0f || intrinsic.height <= 0f) {
        16f / 9f
    } else {
        intrinsic.width / intrinsic.height
    }

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(shape)
            .aspectRatio(aspect, matchHeightConstraintsFirst = true)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                }
            )
            .then(
                if (contentDescription != null) {
                    Modifier.semantics { this.contentDescription = contentDescription }
                } else {
                    Modifier
                }
            ),
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        if (scrim) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(scrimBrush(textAnchor, scrimColor)),
            )
        }

        Column(
            modifier = Modifier
                .align(alignmentFor(textAnchor))
                .padding(spacing.lg),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
            horizontalAlignment = horizontalAlignmentFor(textAnchor),
        ) {
            if (eyebrow != null) {
                Text(
                    text = eyebrow.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 2.4.sp,
                    color = contentColor.copy(alpha = 0.75f),
                )
            }
            Text(
                text = headline.uppercase(),
                style = MaterialTheme.typography.displayMedium.copy(
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Black,
                ),
                color = contentColor,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.9f),
                )
            }
        }
    }
}

/**
 * Maps an anchor to its corresponding [Alignment] within the enclosing [Box].
 * Exposed internal for unit testing.
 */
internal fun alignmentFor(anchor: PopImageBannerTextAnchor): Alignment = when (anchor) {
    PopImageBannerTextAnchor.TopStart -> Alignment.TopStart
    PopImageBannerTextAnchor.TopCenter -> Alignment.TopCenter
    PopImageBannerTextAnchor.TopEnd -> Alignment.TopEnd
    PopImageBannerTextAnchor.CenterStart -> Alignment.CenterStart
    PopImageBannerTextAnchor.Center -> Alignment.Center
    PopImageBannerTextAnchor.CenterEnd -> Alignment.CenterEnd
    PopImageBannerTextAnchor.BottomStart -> Alignment.BottomStart
    PopImageBannerTextAnchor.BottomCenter -> Alignment.BottomCenter
    PopImageBannerTextAnchor.BottomEnd -> Alignment.BottomEnd
}

/**
 * Cross-axis alignment for the text column content itself (not its placement).
 * End anchors right-align column children, center anchors center-align them,
 * and the default is left/start alignment.
 * Exposed internal for unit testing.
 */
internal fun horizontalAlignmentFor(anchor: PopImageBannerTextAnchor): Alignment.Horizontal = when (anchor) {
    PopImageBannerTextAnchor.TopEnd,
    PopImageBannerTextAnchor.CenterEnd,
    PopImageBannerTextAnchor.BottomEnd -> Alignment.End
    PopImageBannerTextAnchor.TopCenter,
    PopImageBannerTextAnchor.Center,
    PopImageBannerTextAnchor.BottomCenter -> Alignment.CenterHorizontally
    else -> Alignment.Start
}

/**
 * Scrim brush for the given [anchor]. Top/bottom anchors get a vertical linear
 * gradient; side anchors get horizontal; [PopImageBannerTextAnchor.Center] gets
 * a radial gradient.
 * Exposed internal for unit testing.
 */
internal fun scrimBrush(anchor: PopImageBannerTextAnchor, color: Color): Brush = when (anchor) {
    PopImageBannerTextAnchor.TopStart,
    PopImageBannerTextAnchor.TopCenter,
    PopImageBannerTextAnchor.TopEnd -> Brush.verticalGradient(
        0f to color, 0.6f to Color.Transparent,
    )
    PopImageBannerTextAnchor.BottomStart,
    PopImageBannerTextAnchor.BottomCenter,
    PopImageBannerTextAnchor.BottomEnd -> Brush.verticalGradient(
        0.4f to Color.Transparent, 1f to color,
    )
    PopImageBannerTextAnchor.CenterStart -> Brush.horizontalGradient(
        0f to color, 0.6f to Color.Transparent,
    )
    PopImageBannerTextAnchor.CenterEnd -> Brush.horizontalGradient(
        0.4f to Color.Transparent, 1f to color,
    )
    PopImageBannerTextAnchor.Center -> Brush.radialGradient(
        0f to color, 1f to Color.Transparent,
    )
}
```

Notes:
- The spec requested `spacing.xl` (24dp) padding; `ElectricPopTheme.spacing.lg` is exactly 24dp per `Spacing.kt`, so `.padding(spacing.lg)` matches the spec value. (`spacing.xl` is 32dp; we use `lg` = 24dp to match the numeric intent.)
- `Box.align(...)` inside a `Box` — using the `BoxScope` extension — honors the anchor.
- The `semantics` modifier is applied on the outer `Box` rather than the `Image` because a card with an image + text is conceptually a single labeled entity; `null` content description on `Image` treats the image itself as decorative.

- [ ] **Step 2: Compile**

Run: `./gradlew :library:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL.

---

### Task 14: Unit-test the helpers

**Files:**
- Create: `library/src/commonTest/kotlin/co/tanay/electricpop/composite/PopImageBannerCardTest.kt`

- [ ] **Step 1: Write tests that exercise real component code, not stdlib**

```kotlin
package co.tanay.electricpop.composite

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PopImageBannerCardTest {

    @Test
    fun alignmentFor_mapsEveryAnchorToUniqueAlignment() {
        val pairs = PopImageBannerTextAnchor.values().map { it to alignmentFor(it) }
        // Every anchor must produce a distinct Alignment to avoid layout collisions.
        assertEquals(pairs.size, pairs.map { it.second }.toSet().size)
    }

    @Test
    fun alignmentFor_wellKnownCases() {
        assertEquals(Alignment.TopStart, alignmentFor(PopImageBannerTextAnchor.TopStart))
        assertEquals(Alignment.Center, alignmentFor(PopImageBannerTextAnchor.Center))
        assertEquals(Alignment.BottomEnd, alignmentFor(PopImageBannerTextAnchor.BottomEnd))
    }

    @Test
    fun horizontalAlignmentFor_followsAnchorSide() {
        assertEquals(Alignment.Start, horizontalAlignmentFor(PopImageBannerTextAnchor.TopStart))
        assertEquals(Alignment.Start, horizontalAlignmentFor(PopImageBannerTextAnchor.BottomStart))
        assertEquals(Alignment.CenterHorizontally, horizontalAlignmentFor(PopImageBannerTextAnchor.Center))
        assertEquals(Alignment.CenterHorizontally, horizontalAlignmentFor(PopImageBannerTextAnchor.BottomCenter))
        assertEquals(Alignment.End, horizontalAlignmentFor(PopImageBannerTextAnchor.TopEnd))
        assertEquals(Alignment.End, horizontalAlignmentFor(PopImageBannerTextAnchor.CenterEnd))
    }

    @Test
    fun scrimBrush_isNonNullForEveryAnchor() {
        // We can't deeply introspect Compose Brush types across platforms, but we can
        // guarantee every anchor returns a brush instance so the caller never NPEs.
        PopImageBannerTextAnchor.values().forEach { anchor ->
            assertNotNull(scrimBrush(anchor, Color.Black))
        }
    }
}
```

- [ ] **Step 2: Run the test**

Run: `./gradlew :library:desktopTest --tests "co.tanay.electricpop.composite.PopImageBannerCardTest"`
Expected: BUILD SUCCESSFUL; 4 tests passed.

---

### Task 15: Build the demo page and register in the catalog

**Files:**
- Create: `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/PopImageBannerCardDemo.kt`
- Modify: `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/CatalogScreen.kt`

- [ ] **Step 1: Write the demo page with the five screenshot variants (plus a clickable variant)**

Create `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/PopImageBannerCardDemo.kt`:

```kotlin
package co.tanay.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.composite.PopImageBannerCard
import co.tanay.electricpop.composite.PopImageBannerTextAnchor
import co.tanay.electricpop.theme.ElectricPopTheme
import compose_electric_pop.library.generated.resources.Res
import compose_electric_pop.library.generated.resources.pop_banner_hero
import org.jetbrains.compose.resources.painterResource

@Composable
fun PopImageBannerCardDemo() {
    val spacing = ElectricPopTheme.spacing
    val hero = painterResource(Res.drawable.pop_banner_hero)

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        SectionLabel("BOTTOM START (DEFAULT)")
        PopImageBannerCard(
            painter = hero,
            eyebrow = "Electric Pop",
            headline = "Encrypted\nWealth\nSimplified",
            subtitle = "A single surface for everything you own.",
            textAnchor = PopImageBannerTextAnchor.BottomStart,
            modifier = Modifier.fillMaxWidth(),
        )

        SectionLabel("TOP END")
        PopImageBannerCard(
            painter = hero,
            eyebrow = "Limited",
            headline = "Drop 04",
            textAnchor = PopImageBannerTextAnchor.TopEnd,
            modifier = Modifier.fillMaxWidth(),
        )

        SectionLabel("CENTER, RADIAL SCRIM")
        PopImageBannerCard(
            painter = hero,
            headline = "Encrypted\nWealth\nSimplified",
            textAnchor = PopImageBannerTextAnchor.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        SectionLabel("BOTTOM CENTER, NO SCRIM")
        PopImageBannerCard(
            painter = hero,
            headline = "Pop Off",
            subtitle = "For the people who refuse beige.",
            textAnchor = PopImageBannerTextAnchor.BottomCenter,
            scrim = false,
            modifier = Modifier.fillMaxWidth(),
        )

        SectionLabel("HEIGHT-CONSTRAINED (120DP)")
        PopImageBannerCard(
            painter = hero,
            eyebrow = "Thin banner",
            headline = "Stay Charged",
            textAnchor = PopImageBannerTextAnchor.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
        )

        SectionLabel("CLICKABLE (KINETIC)")
        PopImageBannerCard(
            painter = hero,
            eyebrow = "Try me",
            headline = "Tap To Feel It",
            subtitle = "Hover scale + press scale with 200ms ease.",
            textAnchor = PopImageBannerTextAnchor.BottomStart,
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
```

- [ ] **Step 2: Register in CatalogScreen**

Edit `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/CatalogScreen.kt`:
- Add import: `import co.tanay.electricpop.demo.components.PopImageBannerCardDemo`
- Add Wave 4 row directly after the existing `PopBannerCard` entry:
  ```kotlin
  CatalogEntry("PopImageBannerCard", "Composite") { PopImageBannerCardDemo() },
  ```

- [ ] **Step 3: Compile demo**

Run: `./gradlew :demo:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL.

---

### Task 16: Screenshot-test the five configurations per theme

**Files:**
- Create: `library/src/desktopTest/kotlin/co/tanay/electricpop/composite/PopImageBannerCardScreenshotTest.kt`

- [ ] **Step 1: Write the test file — five named variants × two themes**

```kotlin
package co.tanay.electricpop.composite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme
import compose_electric_pop.library.generated.resources.Res
import compose_electric_pop.library.generated.resources.pop_banner_hero
import io.github.takahirom.roborazzi.captureRoboImage
import org.jetbrains.compose.resources.painterResource
import kotlin.test.Test

class PopImageBannerCardScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 420, height = 1600) {
        setContent { Scene(darkTheme = false) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopImageBannerCard_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 420, height = 1600) {
        setContent { Scene(darkTheme = true) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopImageBannerCard_allVariants_dark.png",
        )
    }
}

@Composable
private fun Scene(darkTheme: Boolean) {
    ElectricPopTheme(darkTheme = darkTheme) {
        val spacing = ElectricPopTheme.spacing
        val hero = painterResource(Res.drawable.pop_banner_hero)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(spacing.md),
            ) {
                // 1. BottomStart (default) — eyebrow + headline + subtitle, scrim on.
                PopImageBannerCard(
                    painter = hero,
                    eyebrow = "Electric Pop",
                    headline = "Encrypted\nWealth\nSimplified",
                    subtitle = "A single surface for everything you own.",
                    textAnchor = PopImageBannerTextAnchor.BottomStart,
                    modifier = Modifier.fillMaxWidth(),
                )
                // 2. TopEnd — eyebrow + headline only, scrim on.
                PopImageBannerCard(
                    painter = hero,
                    eyebrow = "Limited",
                    headline = "Drop 04",
                    textAnchor = PopImageBannerTextAnchor.TopEnd,
                    modifier = Modifier.fillMaxWidth(),
                )
                // 3. Center — headline only (multi-line), radial scrim.
                PopImageBannerCard(
                    painter = hero,
                    headline = "Encrypted\nWealth\nSimplified",
                    textAnchor = PopImageBannerTextAnchor.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                // 4. BottomCenter — headline + subtitle, scrim off.
                PopImageBannerCard(
                    painter = hero,
                    headline = "Pop Off",
                    subtitle = "For the people who refuse beige.",
                    textAnchor = PopImageBannerTextAnchor.BottomCenter,
                    scrim = false,
                    modifier = Modifier.fillMaxWidth(),
                )
                // 5. Height-constrained — tests matchHeightConstraintsFirst.
                PopImageBannerCard(
                    painter = hero,
                    eyebrow = "Thin banner",
                    headline = "Stay Charged",
                    textAnchor = PopImageBannerTextAnchor.CenterStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                )
            }
        }
    }
}
```

The spec's 5 screenshot variants are represented as 5 sub-cards in one composite golden, matching the single-golden pattern used across the library. (Five separate `@Test` methods per theme would produce 10 goldens instead of 2 — the spec's "5 configurations per theme" expresses visual coverage, not file count. The chosen pattern matches every other composite screenshot test in the project, so reviewers can diff cleanly.)

- [ ] **Step 2: Record goldens**

Run: `./gradlew :library:recordRoborazziDesktop --tests "co.tanay.electricpop.composite.PopImageBannerCardScreenshotTest"`
Expected: BUILD SUCCESSFUL; two PNGs written under `library/src/desktopTest/snapshots/PopImageBannerCard_allVariants_{light,dark}.png`.

- [ ] **Step 3: Verify goldens**

Run: `./gradlew :library:verifyRoborazziDesktop --tests "co.tanay.electricpop.composite.PopImageBannerCardScreenshotTest"`
Expected: BUILD SUCCESSFUL.

Sanity check: open both PNGs. Confirm:
- light: five banners, dark page background visible between them? No — light page background. The *light* golden should have a light page background. Confirm by comparing to any other `*_light.png` in `snapshots/`.
- dark: five banners with a dark page background visible between them (not white). This confirms the dark-bg wrapper is working.
- headline text is italic, uppercased, Black weight, clearly readable against scrim.

---

### Task 17: Full build + global verify + commit

**Files:** none (verification)

- [ ] **Step 1: Full compile**

Run: `./gradlew :library:compileKotlinDesktop :demo:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 2: Full desktop test suite**

Run: `./gradlew :library:desktopTest`
Expected: BUILD SUCCESSFUL; all tests pass including `PopImageBannerCardTest` (4) and both `PopImageBannerCardScreenshotTest` methods.

- [ ] **Step 3: Global golden verification**

Run: `./gradlew :library:verifyRoborazziDesktop`
Expected: BUILD SUCCESSFUL with zero diffs across **all** components (Banner + ImageBanner + the ~20 unrelated components).

- [ ] **Step 4: Stage and commit**

```bash
git add library/src/commonMain/kotlin/co/tanay/electricpop/composite/PopImageBannerCard.kt
git add library/src/commonTest/kotlin/co/tanay/electricpop/composite/PopImageBannerCardTest.kt
git add library/src/desktopTest/kotlin/co/tanay/electricpop/composite/PopImageBannerCardScreenshotTest.kt
git add demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/PopImageBannerCardDemo.kt
git add demo/src/commonMain/kotlin/co/tanay/electricpop/demo/CatalogScreen.kt
git add library/src/commonMain/composeResources/drawable/pop_banner_hero.png
git add library/src/desktopTest/snapshots/PopImageBannerCard_allVariants_light.png
git add library/src/desktopTest/snapshots/PopImageBannerCard_allVariants_dark.png
git status --short
```

Expected (only these files staged):
```
A  demo/src/commonMain/kotlin/co/tanay/electricpop/demo/CatalogScreen.kt (M, not A)
A  demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/PopImageBannerCardDemo.kt
A  library/src/commonMain/composeResources/drawable/pop_banner_hero.png
A  library/src/commonMain/kotlin/co/tanay/electricpop/composite/PopImageBannerCard.kt
A  library/src/commonTest/kotlin/co/tanay/electricpop/composite/PopImageBannerCardTest.kt
A  library/src/desktopTest/kotlin/co/tanay/electricpop/composite/PopImageBannerCardScreenshotTest.kt
A  library/src/desktopTest/snapshots/PopImageBannerCard_allVariants_dark.png
A  library/src/desktopTest/snapshots/PopImageBannerCard_allVariants_light.png
```

Commit:

```bash
git commit -m "$(cat <<'EOF'
feat(composite): add PopImageBannerCard with configurable text anchor

Introduces the missing image-hero card from the original spec:
a `Painter`-driven big-image card with overlaid italic-Black-
uppercase headline, optional eyebrow and subtitle, configurable
text anchor (9 positions), scrim gradient (linear/radial depending
on anchor), and optional kinetic click scale.

Intrinsic-aspect sizing with `matchHeightConstraintsFirst = true`
so callers can drive the card by width, height, or both. Falls
back to 16:9 for painters with unspecified intrinsic size.

Ships with a CC0 abstract sample drawable, a demo page registered
in CatalogScreen, and Roborazzi goldens captured against a
theme-painted page background (light + dark). Consumers wanting
remote images compose with Coil 3's `rememberAsyncImagePainter`
or Kamel's `asyncPainterResource` — both return a `Painter`.
EOF
)"
```

- [ ] **Step 5: Post-commit sanity**

Run: `git status && git log --oneline -5`
Expected: clean working tree; the two new commits on top.

---

## Wrap-up

### Task 18: End-of-branch cleanup + PR body checklist

**Files:** none (PR materials)

- [ ] **Step 1: Final sanity run**

Run: `./gradlew :library:build :demo:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 2: Push the branch**

Run: `git push -u origin feat/pop-banner-card`
Expected: branch pushed; remote-tracking set.

- [ ] **Step 3: Draft the PR body**

Use the `git-pr` skill (or hand-author) with:

- **Summary:** one paragraph — "rename Metric→Banner with chip/icon polish; add new PopImageBannerCard; fix dark-mode screenshot parity; retroactive re-record of other components intentionally out of scope (follow-up)."
- **Changes table:** two columns (component, status). Banner enhanced; Metric removed; ImageBanner added.
- **Screenshot table:** 2×2 — `PopBannerCard_allVariants_{light,dark}.png` and `PopImageBannerCard_allVariants_{light,dark}.png` rendered inline.
- **Dependencies:** none added.
- **Gotchas / cons (per spec §10):**
  - Dark-bg wrapper not applied to ~15 pre-existing components — tracked as follow-up.
  - `Painter.intrinsicSize == Size.Unspecified` fallback is 16:9; document for vector-painter callers.
  - Sample drawable is procedurally generated (CC0). Consumers who need better art should bundle their own.

- [ ] **Step 4: Send Telegram update on PR open**

Per `feedback_telegram_updates`, fire a `telegram__reply` to `chat_id: 1402731017` announcing the PR URL and the two-commit structure. No screenshots needed; the PR has them.

---

## Self-Review Checklist

(Run this after writing — not as a runtime task.)

- [x] Every section of `2026-04-18-banner-card-refactor.md` maps to a task: §5 (ImageBanner) → Tasks 12–16; §6 (Banner rename + enhance) → Tasks 1–8; §7 (file ops + docs + catalog) → Tasks 1, 2, 5, 10; §8 (agent template) → Task 9; §9 (two-commit strategy) → Tasks 11 + 17; §10 (risks) called out in PR body (Task 18) and handled in Task 13 (aspect fallback) / Task 12 (CC0 asset).
- [x] No placeholders: every code block is concrete; every grep/run command has an expected result.
- [x] Type consistency: `PopBannerCardStyle` / `PopImageBannerTextAnchor` / `alignmentFor` / `horizontalAlignmentFor` / `scrimBrush` referenced identically across tasks and the test file.
- [x] Scope of edits explicit: `git rm` the old numeric banner **before** `git mv` Metric→Banner (avoids collision). Test file and demo symbol renames handled in separate tasks so compile errors surface one file at a time.
