# Session Resume — Electric Pop Release Track

> Cross-session **temporary work state**. Eventually deleted entirely (step 6) — git history preserves it. Each PR below has a checkbox; mark after merge.
> Master design ref (kept while track is active, retired in step 6): `docs/superpowers/specs/2026-03-25-electric-pop-design.md`.
> Build-phase retrospective lives at `.history/2026-04-26-build-phase-summary.md` (created in step 1).

## Where we are

- **Library is feature-complete.** 20 foundation + 6 composite + 1 `PopChart` (3 styles) — all built, tested, screenshot-verified, demo-registered.
- **Track:** repo cleanup → publishable Pages reference site → Maven Central. **Not in a hurry to publish; clean over fast.**
- **First publish target:** `0.0.1` (signals pre-stable; revised down from earlier 0.1.0 default). Coords: `co.tanay:compose-electric-pop:0.0.1`.
- **Source package:** `co.tanay.electricpop.{foundation,composite,chart,theme}`. Demo: `co.tanay.electricpop.demo`.
- **JDK:** 21 everywhere (squircle-shape 5.x is class-file v65).
- **CI:** matrix split — `ubuntu-latest` for desktop+android, `macos-latest` for iOS framework build. Codecov upload wired.
- **Pages:** `tanaykumarbera.github.io/compose-electric-pop` already publishing from `docs/`. We extend it.

## Already merged

| PR | Branch | What it did |
|----|--------|-------------|
| #45 | `chore/ci-matrix` | Split CI desktop+android (ubuntu) + iOS (macos), JDK 17 → 21, fix iOS `JvmName` import |
| #46 | `chore/coverage-kover` | Kover 0.9.8 + Codecov upload |

## In flight

| PR | Branch | What it does |
|----|--------|--------------|
| #47 | `chore/rename-package` | `com.electricpop.*` → `co.tanay.electricpop.*` (128 files); coords `co.tanay:compose-electric-pop`; demo `applicationId` `co.tanay.electricpop.demo`. Awaiting CI matrix run + merge. |

PR #47 used `mv` + `git add -A` rather than `git mv`. Git's rename-detection still recorded all 128 file moves as renames at 85–100% similarity (`R085`–`R100` in `git log --name-status`), so `git blame` and `git log --follow <file>` track history correctly. Next time, `git mv` would just make the diff display less noisy.

> **Note:** `chore/docs-prune` (step 1 below) was branched off `chore/rename-package` while #47 is still open. Once #47 merges, this PR rebases cleanly to just the doc-prune diff.

---

## Release-track checklist

Each item is a single-purpose branch and PR. Cross off after merge. Do not skip ahead — the sequence is dependency-ordered.

### [x] 1. `chore/docs-prune` — PR #48 (open)

Dropped historical plans/specs that the library shipped past. Git history preserves them.

**Deleted:**
- `exec.log.md`, `PROMPT-PHASE-03.md`, `user-requirements.md`
- `docs/superpowers/plans/2026-03-25-electric-pop-implementation.md`
- `docs/superpowers/plans/2026-04-18-banner-card-refactor.md` + matching spec
- `docs/superpowers/plans/2026-04-19-pixy-agent-optimization-plan.md`
- `docs/superpowers/plans/pop-donut-chart.md`
- `docs/superpowers/plans/pop-chart.md` + `agent-team-optimization-prompt.md` (were untracked)
- `.execution-history/` (3 files) — replaced by a one-page summary

**Kept:** `docs/superpowers/specs/2026-03-25-electric-pop-design.md` (master design ref while track is active), `docs/superpowers/plans/repo-cleanup-plan.md` (full plan), `SESSION-RESUME.md` (this file).

**Added:**
- `.history/README.md` — folder marker + purpose
- `.history/2026-04-26-build-phase-summary.md` — distilled retrospective from `.execution-history/`

**Repointed:**
- `AGENTS.md:17`, `CLAUDE.md:10` — "Plan:" line → release-track docs
- design spec line 146 — banner-card-refactor link replaced with inline rename note
- this file — corrected stale "#47 already merged" entry; #47 is in flight

**Done when:** PR #48 merged.

---

### [x] 2. `chore/detekt-spotless` — PR #49 (open)

Detekt 1.23.6 + Spotless 6.25.0 + ktlint 1.3.1 wired as a required ubuntu CI check.

**Configured:**
- `detekt.yml` generated from `--generate-config` defaults; Compose-unfriendly rules deactivated with inline rationale (MagicNumber, FunctionNaming, MaxLineLength, LongMethod, LongParameterList, MatchingDeclarationName, TooManyFunctions, WildcardImport).
- Structural rules kept active with raised thresholds: CyclomaticComplexMethod 15→25, ReturnCount 2→5 + `excludeGuardClauses`, LoopWithTooManyJumpStatements 1→2, NestedBlockDepth 4→6.
- `.editorconfig` holds ktlint overrides; Spotless also passes them via `editorConfigOverride` (Spotless 6.x doesn't always honor the file).
- Root `build.gradle.kts` applies Detekt to all subprojects and points the default `detekt` task at every `src/` dir (KMP source sets aren't auto-discovered).

**Findings fixed (real bugs):**
- `PopSurface.contentColor` was unused; now wired via `CompositionLocalProvider(LocalContentColor provides contentColor)` so the documented children-can-read contract is honored.
- `PopChart.drawLineChart`: dropped leftover `density` parameter.
- `PopChart.drawBarChart`: `@Suppress("CyclomaticComplexMethod", "NestedBlockDepth", "LoopWithTooManyJumpStatements")` — math-heavy single function, splitting would obscure the algorithm.

**Commits in PR:**
- `chore: add Detekt 1.23.6 + Spotless 6.25.0 tooling`
- `style: apply spotless`
- `chore: fix detekt findings`
- `ci: gate ubuntu job on detekt + spotlessCheck`

**Done when:** PR #49 merged.

---

### [ ] 3. `feat/dokka-pages`

Stand up Dokka HTML output and serve it from GitHub Pages alongside the existing site. **No screenshot embeds yet** — that's step 4.

**Library setup (`library/build.gradle.kts`):**
- Add Dokka 2.0.0 plugin
- Configure `dokkaHtml`: module name `compose-electric-pop`, source link to GitHub at the current commit, public symbols only
- Skip custom CSS / logo for v1 (defer to a polish pass after the track lands)

**Pages workflow (`.github/workflows/pages.yml`):**
- Expand trigger paths to `["docs/**", "library/src/**", "**/build.gradle.kts", "gradle/libs.versions.toml"]`
- Set up JDK 21
- Run `./gradlew :library:dokkaHtml`
- Upload artifact = `docs/` ∪ `library/build/dokka/html/` merged. Layout:
  - `/` → existing `docs/index.html`
  - `/api/` → Dokka HTML output

**Update `docs/index.html`:** add a link to `/api/` so the landing page surfaces the reference.

**Done when:** push to main rebuilds Dokka; `tanaykumarbera.github.io/compose-electric-pop/api/` shows the API reference for all 27 components.

---

### [ ] 4. `feat/screenshot-codegen`

Convention-based KDoc rewriter so screenshots appear inline in every component's reference page — **zero hand maintenance**.

**Snapshot naming (already enforced by Roborazzi tests):**
`{ComponentName}_{scenarioName}_{light|dark}.png`. Most components have one scenario (`allVariants` / `allPresets` / `allTones`); `PopDropdown` (`allVariants` + `expandedTrigger`) and `PopSurface` (`allTones` + `ghostBorder`) have two. Codegen emits one row per scenario.

**KDoc block (delimited, code-managed):**
```kotlin
/**
 * Existing component description...
 *
 * <!-- screenshots:start (auto-generated, do not edit) -->
 * | Scenario | Light | Dark |
 * | --- | --- | --- |
 * | allVariants | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopButton_allVariants_light.png) | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopButton_allVariants_dark.png) |
 * <!-- screenshots:end -->
 */
```

**Why absolute URLs to Pages:** Dokka renders them directly; IntelliJ Quick Documentation popup also renders them; no relative-path gymnastics from `commonMain/.../*.kt` up to `desktopTest/snapshots/`. The URL is stable — repo isn't getting renamed.

**Snapshots on the Pages site:** Pages workflow (extended in this PR) copies `library/src/desktopTest/snapshots/*.png` to `<publish-artifact>/snapshots/`. Path stays in lockstep with the URLs in KDoc.

**Gradle task `:library:syncScreenshotKdoc`:**
- Walk `library/src/desktopTest/snapshots/*.png`
- Group by component name with regex `^(Pop\w+)_(\w+)_(light|dark)\.png$`
- For each component file in `library/src/commonMain/kotlin/co/tanay/electricpop/{foundation,composite,chart}/*.kt`:
  - Locate the primary public composable's KDoc block
  - Find or insert the delimited block
  - Replace contents with the generated table (sorted by scenario name)
- Idempotent: running twice produces no diff

**Pre-commit hook:** `scripts/install-hooks.sh` installs `.git/hooks/pre-commit` that runs the task and aborts the commit if it produces a diff. Document in CONTRIBUTING.md.

**CI step:** runs `./gradlew syncScreenshotKdoc` and asserts `git diff --exit-code library/src/commonMain` is clean. Fails the build when snapshots and KDoc diverge.

**Done when:** every component file has an up-to-date screenshot block; rerunning `syncScreenshotKdoc` is a no-op; the Pages reference site renders both light and dark images for each component.

---

### [ ] 5. `feat/screenshot-presence-check`

Companion guardrail: every public Pop composable must have at least one matching pair of light + dark snapshots.

**Gradle task `:library:checkScreenshotPresence`:**
- Scan `library/src/commonMain/kotlin/co/tanay/electricpop/{foundation,composite,chart}/*.kt`
- For each top-level public `@Composable fun Pop\w+\(` (skip `private`/`internal` helpers and trivial overload variants)
- Assert at least one `{Component}_*_light.png` AND `{Component}_*_dark.png` exists in `library/src/desktopTest/snapshots/`
- On miss, fail with: `Missing snapshots: PopXxx (no _light), PopYyy (no _dark)`

**Allowlist:** `library/screenshot-allowlist.txt`, one component name per line, for any component that legitimately has no screenshot. Empty for now.

**Wire into CI matrix:** `./gradlew checkScreenshotPresence` on the ubuntu job. Required check.

**Done when:** CI fails on a hypothetical PR that adds a `Pop*` composable without snapshots.

---

### [ ] 6. `chore/scrub-plan-refs`

Final cleanup. Pages reference site is the source of truth for component details. Eliminate `docs/superpowers/` from the repo entirely — extract anything still load-bearing, then delete the rest. `.history/` already exists (created in step 1) and is the only place that references the retired tree.

**Extract (load-bearing):**
- `docs/superpowers/specs/2026-03-25-electric-pop-design.md` → top-level `DESIGN.md` (~200 lines covering the 7 design rules + theme philosophy + color/typography token list). The rest of the spec is historical.

**Delete (working state, git preserves):**
- `docs/superpowers/plans/SESSION-RESUME.md` (this file) — checklist complete; no archival value
- `docs/superpowers/plans/repo-cleanup-plan.md` — same reason
- `docs/superpowers/specs/2026-03-25-electric-pop-design.md` — content extracted to `DESIGN.md`
- The empty `docs/superpowers/{plans,specs}/` directories themselves

**Rewrite `AGENTS.md`:**
- Drop the 30-row component inventory tables — repoint to `https://tanaykumarbera.github.io/compose-electric-pop/api/`
- Drop the release-track / SESSION-RESUME / repo-cleanup-plan references
- Add a one-line `.history/` index pointer for archive lookups
- Trim to a focused agent operating manual: `/build-component` workflow, agent dispatch rules, MCP tool usage. Design rules link to `DESIGN.md`.

**Rewrite `CLAUDE.md`:**
- Drop the component table (now in Pages)
- Drop spec/plan path references — repoint to `DESIGN.md` and Pages
- Keep: build commands, component creation SOP, Stitch reference, MCP guidance, Telegram chat_id

**Rewrite `README.md`:**
- Drop the component table — repoint to Pages reference site
- Keep: install snippet, quick start, theme customization, platform list, link to `DESIGN.md`

**Done when:** `docs/superpowers/` no longer exists in the working tree; `grep -rn "docs/superpowers" --exclude-dir=.history --exclude-dir=.git` returns empty; `DESIGN.md` exists at repo root and is linked from AGENTS / CLAUDE / README.

---

### [ ] 7. `chore/readme-badges`

Add status badges. **No Maven Central badge yet** — that lights up in step 9.

- CI: `https://github.com/tanaykumarbera/compose-electric-pop/actions/workflows/ci.yml/badge.svg`
- Codecov: `https://codecov.io/gh/tanaykumarbera/compose-electric-pop/branch/main/graph/badge.svg`
- Kotlin: shields.io static `Kotlin-2.3.20`
- Compose Multiplatform: shields.io static `Compose%20MP-1.10.3`
- Platforms: shields.io static `platforms-Android%20%7C%20iOS%20%7C%20Desktop`
- License: shields.io derived from `LICENSE`

**Done when:** README renders 6 badges, all green.

---

### [ ] 8. `chore/release-please`

Wire automated release flow. Conventional Commits become required.

**Add `.github/workflows/release-please.yml`:**
- Trigger on push to main
- `release-type: simple` (versions live in `gradle.properties`)
- Auto-generates `CHANGELOG.md` and a version-bump PR

**Add `.github/workflows/pr-title-lint.yml`:** semantic-pull-request action; required check.

**Update `CONTRIBUTING.md`:** Conventional Commits cheat sheet + link to spec.

**Tweak `release.yml`:** thread tag version into gradle build:
```bash
./gradlew :library:publishAllPublicationsToMavenCentral -PVERSION_NAME=${GITHUB_REF_NAME#v}
```

**Done when:** a Conventional Commit on main produces a release PR; PR-title lint blocks non-conventional titles.

---

### [ ] 9. `feat/maven-central` — **USER-BLOCKED**

Final piece. Cannot start until Tanay completes:

1. Claim `co.tanay` namespace on Sonatype Central Portal — verify ownership of `tanay.co` via DNS TXT record
2. Generate a GPG signing key (`gpg --full-generate-key`, RSA 4096, no expiry or 2-year)
3. Add 5 GitHub repo secrets:
   - `MAVEN_CENTRAL_USERNAME`
   - `MAVEN_CENTRAL_PASSWORD`
   - `GPG_SIGNING_KEY` (ASCII-armored private key, full block)
   - `GPG_KEY_ID` (last 8 hex of fingerprint)
   - `GPG_KEY_PASSWORD`

Once those exist, the PR adds to `library/build.gradle.kts`:

```kotlin
mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)
    signAllPublications()
    coordinates("co.tanay", "compose-electric-pop", project.findProperty("VERSION_NAME") as String? ?: "0.0.1-SNAPSHOT")
    pom { /* license, dev, scm — already present */ }
}
```

**Update `gradle.properties`:** `VERSION_NAME=0.0.1`.

**Add Maven Central badge to `README.md`:** shields.io maven-central query for `co.tanay/compose-electric-pop`.

**Done when:** 0.0.1 published to Central; install snippet in README resolves for a downstream user; badge resolves green.

---

## Resolved decisions

- **Detekt/Spotless mode:** required from step 2 onward (not advisory).
- **First public version:** `0.0.1` (pre-stable signal; supersedes the earlier 0.1.0 default).
- **"Wiki" destination:** Dokka HTML on GitHub Pages, NOT GitHub repo Wiki product.
- **Screenshot integration:** in-KDoc markdown table via codegen; absolute URLs to the Pages-hosted `/snapshots/` directory; no source-mutation outside the delimited block.
- **Conventional Commits PR-title lint:** required from step 8 onward (not deferred indefinitely).
- **`docs/superpowers/` fate:** end state has zero references to that tree. Step 6 extracts design rules to `DESIGN.md` and deletes the folder; `.history/` (created in step 1) holds only the build-phase summary, not the working-state files.
- **Step ordering rationale:** Detekt/Spotless before Dokka so generated KDoc blocks already conform to formatter style. Dokka site before screenshot codegen so the Pages publish pipeline is proven independently. Codegen before presence-check so edge cases surface before the gate goes live. Plan-refs scrub before badges so README repointing happens with everything in place. Maven Central is last and isolated — sit on it indefinitely until DNS/GPG are ready.

## Notes for next session

- **Next up: step 3** (`feat/dokka-pages`). Branch off `main` once #49 merges. Stand up Dokka HTML and serve from GitHub Pages alongside `docs/`. **No screenshot embeds yet** — that's step 4. Pages workflow trigger paths expand to `["docs/**", "library/src/**", "**/build.gradle.kts", "gradle/libs.versions.toml"]`; artifact = `docs/` ∪ `library/build/dokka/html/` mapped to `/api/`.
- Use **GitHub MCP** (`mcp__plugin_github_github__*`), not `gh` CLI (not installed).
- Use `ToolSearch(query="select:<tool>", max_results=1)` to load deferred MCP tool schemas before calling.
- Telegram `chat_id 1402731017` — notify on PR creation, step completion, blockers, and decision-needed checkpoints. Do **not** notify for sub-step build output.
- `co.tanay.electricpop.demo` package isn't published; Kover excludes `co.tanay.electricpop.demo.*` from coverage (`library/build.gradle.kts`).
- Pages site URL pattern: `https://tanaykumarbera.github.io/compose-electric-pop/`. Used as image base URL in step 4's codegen.
- One step per session is the target cadence. Do NOT bundle steps to "save sessions" — the sequence is dependency-ordered.
