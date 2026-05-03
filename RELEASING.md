# Releasing Electric Pop

This is the maintainer runbook for cutting a Maven Central release. Contributors don't need this — releases are gated to maintainers via the `production` GitHub Environment.

The release pipeline is **tag-driven**. Pushing `vX.Y.Z` (or `vX.Y.Z-rc.N`) to the `compose-electric-pop` repo runs the full release through GitHub Actions. There is no version string in `build.gradle.kts` to bump — the tag is the source of truth.

## TL;DR

Once one-time setup is in place:

```bash
git checkout main
git pull
git tag -a v0.0.1 -m "Release v0.0.1"
git push origin v0.0.1
```

Then:

1. **Approve** the `publish-to-central` job in the GitHub Actions UI (it pauses for manual review).
2. **Inspect** the staged deployment at https://central.sonatype.com → Deployments. If it looks correct, click **Publish**. If not, click **Drop** and re-tag.
3. **Verify** at https://repo1.maven.org/maven2/co/tanay/compose-electric-pop/ after ~10 minutes (Maven Central index lag).

That's the whole flow. The rest of this document explains each step in detail.

---

## One-time setup

### 1. GPG signing key

Generate locally:

```bash
gpg --full-generate-key
# RSA and RSA → 4096 → no expiry → name + tanaykumarbera@gmail.com → strong passphrase

gpg --list-secret-keys --keyid-format=long
# Note the long key ID (16 hex after rsa4096/) and its last 8 hex chars

gpg --keyserver keys.openpgp.org    --send-keys <LONG_KEY_ID>
gpg --keyserver keyserver.ubuntu.com --send-keys <LONG_KEY_ID>
# keys.openpgp.org sends a verification email — click the link or the key
# is published without a UID and Sonatype rejects it

gpg --armor --export-secret-keys <LONG_KEY_ID> > /tmp/electric-pop-signing.asc
```

The full ASCII-armored block (BEGIN through END inclusive) becomes the `GPG_SIGNING_KEY` secret. Delete `/tmp/electric-pop-signing.asc` after pasting.

### 2. Sonatype Central user token

At https://central.sonatype.com → Account → **Generate User Token**. Save the username + password pair this returns — these are *not* your portal login. Stays valid until rotated.

### 3. GitHub secrets (environment level, not repo level)

Settings → Environments → `production` → Add environment secret. Each of:

| Secret name | Value | Source |
|---|---|---|
| `MAVEN_CENTRAL_USERNAME` | username half of the Sonatype user token | step 2 |
| `MAVEN_CENTRAL_PASSWORD` | password half of the Sonatype user token | step 2 |
| `GPG_SIGNING_KEY` | full ASCII-armored secret key block | step 1 |
| `GPG_KEY_ID` | last 8 hex chars of the long key ID (NOT the 40-char fingerprint) | step 1 |
| `GPG_KEY_PASSWORD` | passphrase typed during keygen | step 1 |

These live at **environment level** so they're only readable by jobs that opt into `environment: production` and pass its protection rules. A workflow that doesn't use the environment can't read them.

### 4. `production` environment configuration

Settings → Environments → `production`:

- **Required reviewers**: yourself (the maintainer).
- **Deployment branches and tags**: Selected → tag pattern `v*`. Optionally also restrict to commits from `main`.
- **Wait timer**: 0 (manual approval is the gate, not time).

### 5. Branch protection on `main`

Settings → Branches → `main` should be protected: require PR review, require status checks (CI green), require linear history, no force pushes.

Branch protection on `main` does **not** restrict tag creation — tags are independent refs and can point to any commit. The pipeline enforces "stable tags must come from main" itself, in the precheck job:

| Tag | Tagged commit on main? | Outcome |
|---|---|---|
| `v0.0.1` | yes | passes precheck → continues to build/publish |
| `v0.0.1` | no (feature branch, fork, etc.) | **rejected at precheck** |
| `v0.0.1-rc.1` | yes | passes |
| `v0.0.1-rc.1` | no | passes — RCs are intentionally allowed off-main so early testers can install pre-release builds from feature branches |

The check is `git merge-base --is-ancestor "$GITHUB_SHA" origin/main`, gated by `if: steps.parse.outputs.is_prerelease == 'false'`. Stable releases must be cut from a commit reachable from main; RCs may be cut from anywhere.

---

## Versioning

Tags must match `v<MAJOR>.<MINOR>.<PATCH>` or `v<MAJOR>.<MINOR>.<PATCH>-rc.<N>`:

| Tag | Result |
|---|---|
| `v0.0.1` | stable release |
| `v1.2.3` | stable release |
| `v0.0.1-rc.1` | pre-release (renders with prerelease badge on GH; can be staged on Sonatype indefinitely without publishing) |
| `v0.1` | rejected — needs three segments |
| `v0.0.1-rc` | rejected — `-rc` requires a number |
| `v0.0.1-RC.1` | rejected — lowercase only |
| `v0.0.1-snapshot` | rejected — only `-rc.N` is allowed |
| `0.0.1` | rejected — needs `v` prefix |

The format choice (`-rc.N` lowercase, dot-separated) is SemVer 2.0 compliant and sorts correctly on Maven Central. Avoid `.rc-001` or other variants — they sort wrong.

---

## Cutting a release

Step by step:

```bash
# 1. Make sure your local main is the commit you want to release.
git checkout main
git pull origin main

# 2. Tag it. Annotated tags (-a) are required so GH renders the message.
git tag -a v0.0.1 -m "Release v0.0.1"

# 3. Push the tag. This triggers .github/workflows/release.yml.
git push origin v0.0.1
```

Don't bump anything in `build.gradle.kts` — the version is read from the tag at build time via the `VERSION_NAME` gradle property.

If you tag the wrong commit, delete the tag locally and remotely:

```bash
git tag -d v0.0.1
git push origin :refs/tags/v0.0.1
# Re-tag the correct commit and re-push.
```

---

## Pipeline stages

`.github/workflows/release.yml` runs four jobs in series. Each is independently re-runnable from the Actions UI if it fails.

### precheck (ubuntu, ~30s)

- Validates the tag matches the semver regex above.
- For **stable** tags (no `-rc.N` suffix): asserts the tagged commit is reachable from `origin/main`. Fails if you tagged a feature branch. RCs skip this check.
- HEAD-checks `https://repo1.maven.org/maven2/co/tanay/compose-electric-pop/<version>/`. If it returns 200, fails immediately — that version is already on Maven Central.
- Outputs `version` (no `v` prefix) and `is_prerelease` for downstream jobs.

Failure here means a bad tag (wrong format, off-main stable tag, or duplicate version). Delete the tag, re-tag with a corrected name or commit.

### build-and-test (macos)

Runs `:library:build :library:allTests :library:verifyRoborazziDesktop detekt spotlessCheck`. Same gate as a PR — but rerun on the exact tagged commit so we don't ship a release that wouldn't pass CI.

Failure here means the tagged commit is broken. Either the tag was cut from a non-main commit, or main itself has a regression. Fix on a PR, merge to main, delete the tag, re-tag.

### publish-to-central (macos, environment: production — **manual approval gate**)

Once `build-and-test` passes, GitHub pauses this job and notifies the required reviewer (you). You'll see a "Review pending deployments" prompt in the Actions UI.

After approval:

- Injects `ORG_GRADLE_PROJECT_VERSION_NAME` from precheck output, so the version flows tag → precheck → gradle → POM.
- Reads the five `MAVEN_CENTRAL_*` / `GPG_*` secrets from the `production` environment.
- Runs `./gradlew :library:publishAllPublicationsToMavenCentral --no-configuration-cache`.

Because `mavenPublishing { publishToMavenCentral(automaticRelease = false) }` is set in `library/build.gradle.kts`, the upload **stages** on Sonatype Central rather than auto-promoting. You then inspect and publish manually (next section).

Failure modes:

- Bad signing config → `BadPassphraseException` or "no signing key found". Check `GPG_KEY_ID` is the last 8 hex (not the fingerprint), `GPG_SIGNING_KEY` is the full ASCII-armored secret block, `GPG_KEY_PASSWORD` matches the keygen passphrase.
- Bad credentials → 401 from Sonatype. Regenerate the user token at https://central.sonatype.com/account.
- Validation failure on Sonatype → the portal shows specific reasons (missing javadoc, unsigned, etc.). Fix and re-tag (with a new version, since the old one is now associated with the failed deployment).

### github-release (ubuntu)

Creates the GH release page at https://github.com/tanaykumarbera/compose-electric-pop/releases/tag/v0.0.1 with auto-generated notes from PRs merged since the previous tag. Sets the prerelease badge based on the `-rc.N` suffix.

This runs only after `publish-to-central` succeeds, so a missing release page is the safest failure — artifacts are already on Sonatype, just create the release page manually if needed.

---

## Sonatype Portal: inspect → publish or drop

After `publish-to-central` succeeds, your artifacts are **staged**, not yet published. Open https://central.sonatype.com/publishing/deployments. You'll see a deployment with:

- **State**: `VALIDATED` (Sonatype's automated checks passed) or `FAILED` (with specific errors).
- **Components**: every artifact uploaded — `compose-electric-pop-<version>.aar`, `.jar`, `.module`, `.pom`, plus `.asc` signatures and `.md5`/`.sha1` checksums.

If state is `VALIDATED` and the components look right:

- Click **Publish**. Within minutes the artifacts move to `repo1.maven.org`. Index lag means Maven Central search may take longer (~hours), but Gradle dependency resolution sees them within ~10 minutes.

If state is `FAILED` or you spot something wrong:

- Click **Drop**. The deployment is discarded; nothing reaches Maven Central. You can re-tag (same version is fine since nothing was published) and re-run.

A staged deployment lives indefinitely until you Publish or Drop. Pre-release tags (`-rc.N`) can stay staged forever for internal review without ever publishing.

---

## Verification

After clicking Publish on the portal:

```bash
# 1. Direct Maven Central URL — populated within ~10 minutes.
curl -I https://repo1.maven.org/maven2/co/tanay/compose-electric-pop/0.0.1/

# 2. Search index — populated within ~hours.
open https://central.sonatype.com/artifact/co.tanay/compose-electric-pop

# 3. Try resolving from a fresh project.
echo 'implementation("co.tanay:compose-electric-pop:0.0.1")' >> demo-app/build.gradle.kts
./gradlew :demo-app:dependencies | grep electric-pop
```

The badge in `README.md` (`https://img.shields.io/maven-central/v/co.tanay/compose-electric-pop`) updates automatically once the search index catches up.

---

## Recovery: I tagged but want to abort

Before any approval:

- Cancel the `publish-to-central` job in the Actions UI (decline the deployment review).
- Delete the tag: `git tag -d v0.0.1 && git push origin :refs/tags/v0.0.1`.

After publish-to-central uploaded but before clicking Publish on the portal:

- Click **Drop** on the staged deployment in the portal.
- Delete the tag as above.

After clicking Publish on the portal:

- The version is permanent on Maven Central. Maven Central does not allow deletion or overwriting of a published version. Cut a new patch (`v0.0.2`) with the fix.

---

## Day-to-day reference

- Workflow: `.github/workflows/release.yml`
- Publish config: `library/build.gradle.kts` — `mavenPublishing { ... }` block
- Plugin: `com.vanniktech.maven.publish` (version pinned in `gradle/libs.versions.toml`)
- Coordinates: `co.tanay:compose-electric-pop`
- Group ownership: DNS-anchored on `tanay.co` (verified via TXT record on Sonatype Central Portal)
