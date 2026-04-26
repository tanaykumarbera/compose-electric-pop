# Repo Cleanup & CI Hardening Plan

**Status:** draft, awaiting user sign-off
**Owner:** Tanay (with Claude Code assist)
**Created:** 2026-04-25
**Target:** v0.1.0 → v0.2.0 release readiness

---

## 1. Context

`compose-electric-pop` is a Compose Multiplatform library targeting Android, iOS, and Desktop. The library is **feature-complete** against its design spec (26 components — 20 foundation + 6 composite + 1 PopChart with 3 styles). PR #34 finishes the doc cleanup. The next milestone is making the repo "professional Android lib" grade so we can publish v1.0 to Maven Central with confidence.

The user's ask, paraphrased:

1. CI runs build, tests, unit tests, coverage, "all the shiny things"
2. README badges for everything important
3. Tag-on-main triggers a job that builds + publishes a binary to Maven Central, creates a GitHub release page, and produces a changelog
4. Answer: can GitHub do all of this with hosted runners, or do we need self-hosted?

This plan inventories what already exists, identifies the gaps, and lays out an implementation sequence in small reviewable PRs.

## 2. Answer: GitHub-hosted runners are sufficient

**Short answer: yes, GitHub Actions hosted runners can do all of this. No self-hosted runner needed.**

Why:

- `compose-electric-pop` is a **public repository** → unlimited free minutes for `ubuntu-latest` runners and a generous tier for `macos-latest` (currently 2,000 free minutes/month even for public on the consumer plan; effectively unmetered for typical OSS workloads).
- Total per-run cost estimates (multipliers per [GitHub billing docs](https://docs.github.com/en/billing/managing-billing-for-your-products/managing-billing-for-github-actions/about-billing-for-github-actions#minute-multipliers)):
  - `ubuntu-latest`: 1× minute, **free for public repos**
  - `macos-latest`: 10× minute multiplier (only matters on private; public repos still consume from the OSS tier)
- We split the matrix to keep macOS time minimal — only iOS framework builds and the release job need macOS.
- All required tooling — JDK, Android SDK, Gradle, GPG, signing keys, Node — runs cleanly on `ubuntu-latest` for everything except iOS Kotlin/Native.

Self-hosted runners would only be worth it if we needed (a) pre-warmed Gradle caches beyond what `actions/cache` gives us, (b) ARM Linux for some niche test, or (c) hardware-accelerated emulator builds. None apply.

## 3. Current state inventory

| Area | Already exists | File | Status |
|------|----------------|------|--------|
| CI workflow | `.github/workflows/ci.yml` | runs `:library:build`, `:demo:build`, `:library:allTests` on `macos-latest` for push/PR to `main` | works, but pays macOS multiplier for everything; no coverage, no screenshot verify, no lint |
| Release workflow | `.github/workflows/release.yml` | tag-triggered (`v*`), publishes to Maven Central via `vanniktech/gradle-maven-publish-plugin`, creates GitHub release with auto notes | works in principle; secrets stubbed; needs Sonatype Central Portal target, signing verification, changelog generator |
| Pages workflow | `.github/workflows/pages.yml` | deploys `docs/` to GitHub Pages | works but currently publishes the spec; we'll repurpose for Dokka HTML later |
| Maven publish plugin | `vanniktech/gradle-maven-publish-plugin` 0.28 wired in `library/build.gradle.kts` | coordinates `co.tanay:compose-electric-pop:0.1.0`, POM has license/dev/SCM | needs explicit `publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)` call |
| Screenshot tests | Roborazzi 1.59 with goldens for all 26 components | `library/src/desktopTest/snapshots/` | not run in CI today |
| Tests | `kotlin("test")` in commonTest, Compose UI tests in desktopTest | passes locally | runs in CI |
| Coverage | none | — | gap |
| Lint / static analysis | none | — | gap |
| Changelog | none | — | gap |
| LICENSE file | **missing** despite README and POM both claiming Apache 2.0 | — | gap (blocks Maven Central reproducibility check) |
| README badges | none | — | gap |
| Dependabot / wrapper validation | none | — | gap |
| Maven Central account | **TBD** — needs user confirmation | — | external prerequisite |

## 4. Gaps to close

Grouped by intent:

### 4.1 CI hardening (every push / PR)

1. **Move desktop work to `ubuntu-latest`** — keep `macos-latest` only for iOS jobs. Cost reduction + faster matrix.
2. **Run screenshot verify** — `./gradlew :library:verifyRoborazziDesktop`. On failure, upload diff PNGs as artifacts so reviewers can see what regressed.
3. **Coverage** — add Kover, generate XML report, upload to Codecov (free for OSS), enforce a floor (e.g., "do not regress below current %") via `koverVerify`.
4. **Lint / format** — Detekt + Spotless. Both as advisory at first (warn-only) then promote to required.
5. **Gradle wrapper validation** — `gradle/actions/wrapper-validation@v3` step on every PR (supply-chain).
6. **Build all multiplatform targets** — currently CI only does `:library:build` (which does desktop + android + iOS on macos-latest). Split: `compileKotlin{Android,Desktop}` on ubuntu, `linkDebugFrameworkIos{Arm64,SimulatorArm64}` on macos.
7. **Cache Gradle / Konan** — `actions/cache` for `~/.gradle/caches`, `~/.konan` (iOS native deps are slow to download).
8. **Concurrency** — already in `ci.yml`, keep it.
9. **PR title lint** (optional) — enforce Conventional Commits via `amannn/action-semantic-pull-request`. Lays groundwork for release-please.

### 4.2 Release pipeline (tag-triggered)

1. **Source of truth for version** — drop the hardcoded `0.1.0` in `library/build.gradle.kts` and read from the git tag (strip `v` prefix). Keeps tag and artifact in lockstep.
2. **Sonatype Central Portal target** — explicitly call `publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)` in the `mavenPublishing` block. Sonatype OSSRH (the old endpoint) is end-of-life.
3. **Auto-publish dropoff** — vanniktech plugin can call `automaticRelease = true` so we don't have to log into Central Portal to "release" the staging repo manually.
4. **Signing** — verify the in-memory GPG key approach is wired. The current `release.yml` env vars are correct; we need to confirm the key is exported as ASCII-armored without passphrase prompt (`gpg --export-secret-keys --armor`).
5. **Changelog + version bump** — recommend `googleapis/release-please-action`. It opens a PR that bumps the version, regenerates `CHANGELOG.md` from Conventional Commit messages, and on merge tags the release. The existing tag-triggered `release.yml` then fires.
6. **GitHub Release page** — `softprops/action-gh-release` already in `release.yml`. Replace `generate_release_notes: true` with the changelog body produced by release-please for cleaner notes.
7. **Artifact attachments to release** — attach the `.module`, `.jar`, `.aar`, `.klib` files for visibility (optional).
8. **Snapshot publishing on `main` push** (optional) — separate job that publishes `0.x.0-SNAPSHOT` to the Central Portal snapshot repo on every merge to main. Lets consumers test pre-release.

### 4.3 README badges

After the CI/release infra exists, add a badge row at the top of the README. Concrete badges:

```markdown
[![Maven Central](https://img.shields.io/maven-central/v/co.tanay/compose-electric-pop?label=Maven%20Central)](https://central.sonatype.com/artifact/co.tanay/compose-electric-pop)
[![CI](https://github.com/tanaykumarbera/compose-electric-pop/actions/workflows/ci.yml/badge.svg)](https://github.com/tanaykumarbera/compose-electric-pop/actions/workflows/ci.yml)
[![Coverage](https://codecov.io/gh/tanaykumarbera/compose-electric-pop/branch/main/graph/badge.svg)](https://codecov.io/gh/tanaykumarbera/compose-electric-pop)
[![Kotlin](https://img.shields.io/badge/kotlin-2.3.20-7f52ff?logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/compose--mp-1.10.3-4285F4)](https://github.com/JetBrains/compose-multiplatform)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Platforms](https://img.shields.io/badge/platforms-android%20%7C%20ios%20%7C%20desktop-lightgrey)](#platforms)
```

### 4.4 Other professional-repo essentials

1. **`LICENSE` file** at repo root — Apache 2.0 plain text; required for Maven Central, also ensures the badge isn't lying.
2. **`CHANGELOG.md`** seeded with the 0.1.0 release notes; release-please will append.
3. **Issue + PR templates** under `.github/` — bug report, feature request, default PR checklist.
4. **`CODE_OF_CONDUCT.md`** + **`CONTRIBUTING.md`** — standard OSS files; use templates.
5. **Dependabot config** — `.github/dependabot.yml` for `gradle` + `github-actions` ecosystems.
6. **Branch protection** on `main` — require CI green, require linear history, require PR reviews. Set via GitHub UI; document the chosen rules in CONTRIBUTING.

## 5. Implementation plan — sequenced PRs

Each PR is small, reviewable, mergeable independently. PR ordering matters because later PRs assume earlier infrastructure exists.

### PR 1 — Add `LICENSE`, seed `CHANGELOG.md`, add basic GitHub templates (~30 min)

- `LICENSE` (Apache 2.0)
- `CHANGELOG.md` with `## 0.1.0` initial entry
- `.github/PULL_REQUEST_TEMPLATE.md`
- `.github/ISSUE_TEMPLATE/bug_report.md`, `feature_request.md`
- `.github/dependabot.yml` (gradle + github-actions, weekly)
- `CODE_OF_CONDUCT.md` (Contributor Covenant)
- `CONTRIBUTING.md` (build commands, commit conventions, golden recording flow)

**Verification:** repo loads in GitHub UI showing the License banner, "New issue" picks up a template, dependabot tab populated.

### PR 2 — CI matrix split + caching + screenshot verify + wrapper validation (~1h)

Replace `.github/workflows/ci.yml` with a matrix:

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
  desktop-android:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: gradle/wrapper-validation-action@v3
      - uses: actions/setup-java@v4
        with:
          distribution: zulu
          java-version: 17
      - uses: android-actions/setup-android@v3
      - uses: gradle/actions/setup-gradle@v4
      - uses: actions/cache@v4
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: gradle-${{ runner.os }}-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties', 'gradle/libs.versions.toml') }}
      - name: Compile desktop + android
        run: ./gradlew :library:compileKotlinDesktop :library:assembleRelease :demo:compileKotlinDesktop
      - name: Unit tests
        run: ./gradlew :library:desktopTest :library:testReleaseUnitTest
      - name: Verify screenshots
        run: ./gradlew :library:verifyRoborazziDesktop
      - name: Upload screenshot diffs on failure
        if: failure()
        uses: actions/upload-artifact@v4
        with:
          name: screenshot-diffs
          path: library/build/outputs/roborazzi/

  ios:
    runs-on: macos-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: zulu
          java-version: 17
      - uses: gradle/actions/setup-gradle@v4
      - uses: actions/cache@v4
        with:
          path: |
            ~/.gradle/caches
            ~/.konan
          key: konan-${{ runner.os }}-${{ hashFiles('gradle/libs.versions.toml') }}
      - name: Build iOS frameworks
        run: ./gradlew :library:linkDebugFrameworkIosArm64 :library:linkDebugFrameworkIosSimulatorArm64
```

**Verification:** trigger a PR, see two parallel jobs, watch desktop job finish in ~2-3 min and iOS in ~8-10 min. On a deliberately-broken golden, check the artifact upload happens.

### PR 3 — Coverage with Kover + Codecov (~45 min)

In `gradle/libs.versions.toml`:

```toml
[versions]
kover = "0.8.3"

[plugins]
kover = { id = "org.jetbrains.kotlinx.kover", version.ref = "kover" }
```

In `library/build.gradle.kts`:

```kotlin
plugins {
    // ... existing
    alias(libs.plugins.kover)
}

kover {
    reports {
        verify {
            rule {
                minBound(60) // start lenient, raise over time
            }
        }
    }
}
```

In CI, after unit tests on the desktop-android job:

```yaml
- name: Coverage report
  run: ./gradlew :library:koverXmlReport :library:koverVerify
- uses: codecov/codecov-action@v4
  with:
    files: library/build/reports/kover/report.xml
    token: ${{ secrets.CODECOV_TOKEN }}
    fail_ci_if_error: false
```

User action: sign up at codecov.io with the GitHub repo, copy the upload token into `CODECOV_TOKEN` secret.

**Verification:** PR shows Codecov bot comment with delta; coverage badge will read once a run completes on `main`.

### PR 4 — Detekt + Spotless (advisory) (~30 min)

Plugins in version catalog:

```toml
detekt = "1.23.6"
spotless = "6.25.0"
```

Root `build.gradle.kts`:

```kotlin
plugins {
    // ...
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.spotless) apply false
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "com.diffplug.spotless")

    detekt {
        config.setFrom("$rootDir/config/detekt.yml")
        buildUponDefaultConfig = true
    }
    spotless {
        kotlin {
            target("**/*.kt")
            ktlint("1.3.1")
        }
    }
}
```

CI job (advisory — `continue-on-error: true` initially):

```yaml
- name: Lint
  continue-on-error: true
  run: ./gradlew detekt spotlessCheck
```

After two clean runs, flip to required.

**Verification:** PR comments show detekt findings inline (via `reviewdog` action, optional).

### PR 5 — Maven Central wiring + signing readiness (~1h)

In `library/build.gradle.kts`:

```kotlin
import com.vanniktech.maven.publish.SonatypeHost

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)
    signAllPublications()

    coordinates(
        groupId = "co.tanay.electricpop",
        artifactId = "compose-electric-pop",
        version = providers.gradleProperty("VERSION_NAME").orElse("0.1.0-SNAPSHOT").get(),
    )
    // ... existing pom
}
```

In `gradle.properties`:

```
VERSION_NAME=0.1.0-SNAPSHOT
```

Release job (driven by tag) overrides via `-PVERSION_NAME=${tag#v}`.

**User actions before this PR can be merged + tagged:**

1. **Claim namespace** on Sonatype Central Portal (https://central.sonatype.com/) — verify ownership of `co.tanay.electricpop` (or switch to a verified group like `io.github.tanaykumarbera`).
2. **Generate a User Token** in Central Portal account settings; store as `MAVEN_CENTRAL_USERNAME` + `MAVEN_CENTRAL_PASSWORD` secrets. (release.yml already references these names.)
3. **Generate a GPG key** dedicated to release signing:
   ```bash
   gpg --full-generate-key       # RSA 4096, no expiry, no passphrase
   gpg --list-secret-keys --keyid-format=long
   gpg --export-secret-keys --armor <KEY_ID> > signing-key.asc
   gpg --keyserver keys.openpgp.org --send-keys <KEY_ID>
   ```
   Store as `GPG_SIGNING_KEY` (entire armored file content), `GPG_KEY_ID` (long-form ID), `GPG_KEY_PASSWORD` (empty if no passphrase). release.yml already references these names.

**Group decision:** the current group `co.tanay.electricpop` requires DNS verification of `electricpop.com`. If you don't own that domain, switch to `io.github.tanaykumarbera` which Sonatype verifies via your GitHub account — much simpler. **This is the single largest decision in the plan; please pick one before PR 5.**

**Verification:** dry-run `./gradlew :library:publishToMavenLocal` produces signed artifacts in `~/.m2/repository/`.

### PR 6 — release-please + tag-driven release (~45 min)

Add `.github/workflows/release-please.yml`:

```yaml
name: release-please

on:
  push:
    branches: [main]

permissions:
  contents: write
  pull-requests: write

jobs:
  release-please:
    runs-on: ubuntu-latest
    steps:
      - uses: googleapis/release-please-action@v4
        with:
          release-type: simple
          package-name: compose-electric-pop
```

Configure via `release-please-config.json`:

```json
{
  "packages": {
    ".": {
      "release-type": "simple",
      "extra-files": [
        { "type": "generic", "path": "gradle.properties" }
      ]
    }
  }
}
```

`.release-please-manifest.json`:

```json
{ ".": "0.1.0" }
```

Update `release.yml` so the tag publishes the version baked into the merge commit:

```yaml
- name: Publish
  run: ./gradlew :library:publishAllPublicationsToMavenCentral -PVERSION_NAME=${GITHUB_REF_NAME#v} --no-configuration-cache
  env:
    ORG_GRADLE_PROJECT_mavenCentralUsername: ${{ secrets.MAVEN_CENTRAL_USERNAME }}
    ORG_GRADLE_PROJECT_mavenCentralPassword: ${{ secrets.MAVEN_CENTRAL_PASSWORD }}
    ORG_GRADLE_PROJECT_signingInMemoryKey: ${{ secrets.GPG_SIGNING_KEY }}
    ORG_GRADLE_PROJECT_signingInMemoryKeyId: ${{ secrets.GPG_KEY_ID }}
    ORG_GRADLE_PROJECT_signingInMemoryKeyPassword: ${{ secrets.GPG_KEY_PASSWORD }}

- uses: softprops/action-gh-release@v2
  with:
    body_path: CHANGELOG.md   # or extract just the new section via release-please's output
```

**Verification:** merge a Conventional-Commit PR (`feat: ...`); release-please opens a PR titled `chore(main): release 0.2.0` with version + CHANGELOG bump; merging that triggers the `v0.2.0` tag, which fires release.yml; artifact appears on Central Portal within ~30 min, GitHub release page appears immediately.

### PR 7 — Add badges to README (~10 min)

Insert the badge block from §4.3 below the title. Done after a successful release so the version + coverage badges resolve.

### PR 8 — Optional polish (deferred)

- Snapshot publishing on every `main` push to Sonatype snapshots repo
- Dokka HTML site published via `pages.yml` (replace the current static `docs/` deploy)
- Bundle SBOM via `cyclonedx-gradle-plugin`
- Code scanning via GitHub CodeQL (Kotlin support is preview)

## 6. Secrets needed in repo settings

By the time PR 6 merges:

| Secret | Source | Used by |
|--------|--------|---------|
| `MAVEN_CENTRAL_USERNAME` | Sonatype Central Portal user token (username half) | `release.yml` |
| `MAVEN_CENTRAL_PASSWORD` | Sonatype Central Portal user token (password half) | `release.yml` |
| `GPG_SIGNING_KEY` | armored secret key file content | `release.yml` |
| `GPG_KEY_ID` | long-form key id (last 16 hex chars) | `release.yml` |
| `GPG_KEY_PASSWORD` | passphrase or empty | `release.yml` |
| `CODECOV_TOKEN` | Codecov repo upload token | `ci.yml` |

`GITHUB_TOKEN` is auto-injected; release-please uses it without configuration.

## 7. Open questions for the user

Decisions needed before PR 1 starts:

1. **Group ID.** Stick with `co.tanay.electricpop` (needs DNS verification of `electricpop.com`) or switch to `io.github.tanaykumarbera` (verified via GitHub username, zero domain setup)? Switching is a one-line change in `library/build.gradle.kts` plus updating README install snippet. **Recommendation: switch to `io.github.tanaykumarbera` unless you actively want the `co.tanay.electricpop` brand on Maven Central.**
2. **Conventional Commits enforcement.** Should we require `feat:`/`fix:`/`chore:` prefixes via PR-title lint? Required for release-please to bump versions correctly. **Recommendation: yes, advisory at first then required.**
3. **First public version.** Tag the existing `0.1.0` as a real release once PR 6 lands, or jump to `0.2.0` to mark the docs-cleaned milestone? **Recommendation: tag the existing as `0.1.0` to preserve commit history, then let release-please drive `0.2.0` from the next feature commit.**
4. **Snapshot publishing.** Worth setting up `0.x-SNAPSHOT` builds on every `main` push? Useful if external testers want bleeding-edge before tags. **Recommendation: defer to PR 8 unless someone asks for it.**
5. **Coverage threshold.** Start at 60% and only raise after we know what's reachable, or measure first then set? **Recommendation: measure first via koverHtmlReport on a clean run, then set the floor 5pp below current.**
6. **Detekt/Spotless required vs advisory.** **Recommendation: advisory for two PRs, then promote to required once we've fixed the first wave of findings.**
7. **iOS test execution.** Currently we only build the iOS framework — no iOS unit tests run. Worth setting up Kotlin/Native test execution on macOS? **Recommendation: defer; the desktop tests cover all common-source logic.**

## 8. Verification end-to-end

After all PRs merged, the green-flag checklist:

- [ ] PR opened against main → CI shows two jobs (desktop-android on ubuntu, ios on macos), screenshot diffs uploaded on golden mismatch
- [ ] Codecov bot comments coverage delta on each PR
- [ ] Detekt + Spotless run advisory and clean
- [ ] Merging a `feat:` PR opens a release-please PR within 30s
- [ ] Merging that release PR cuts a `vX.Y.Z` tag, which triggers release.yml
- [ ] Within ~30 min the artifact appears at https://central.sonatype.com/artifact/{group}/compose-electric-pop
- [ ] GitHub release page shows changelog entries grouped by type
- [ ] README badges render: build status green, coverage %, latest version, license, platforms
- [ ] Dependabot opens its first weekly PR within a week

## 9. Critical files to modify (paths only)

| Path | PR | Action |
|------|----|--------|
| `LICENSE` | 1 | create |
| `CHANGELOG.md` | 1 | create, seed |
| `.github/PULL_REQUEST_TEMPLATE.md` | 1 | create |
| `.github/ISSUE_TEMPLATE/*.md` | 1 | create |
| `.github/dependabot.yml` | 1 | create |
| `CODE_OF_CONDUCT.md` | 1 | create |
| `CONTRIBUTING.md` | 1 | create |
| `.github/workflows/ci.yml` | 2 | rewrite |
| `gradle/libs.versions.toml` | 3, 4 | add kover, detekt, spotless |
| `library/build.gradle.kts` | 3, 5 | add kover plugin block, switch to Central Portal, version from gradle property |
| `gradle.properties` | 5 | add `VERSION_NAME=0.1.0-SNAPSHOT` |
| `config/detekt.yml` | 4 | create with starter ruleset |
| `.github/workflows/release-please.yml` | 6 | create |
| `release-please-config.json`, `.release-please-manifest.json` | 6 | create |
| `.github/workflows/release.yml` | 6 | tweak to read version from tag |
| `README.md` | 7 | add badge block |

## 10. Estimated effort

- PR 1: 30 min (mostly drafting templates)
- PR 2: 1 h (CI rewrite + first run debugging)
- PR 3: 45 min (Kover + Codecov account)
- PR 4: 30 min (Detekt config; expect findings)
- PR 5: 1 h (Sonatype + GPG; mostly waiting for verification email)
- PR 6: 45 min (release-please config)
- PR 7: 10 min
- PR 8: deferred

**Total active work: ~5 hours, plus ~24 h Sonatype verification wait once email is sent.**
