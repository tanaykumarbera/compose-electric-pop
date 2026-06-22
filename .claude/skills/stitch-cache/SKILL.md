---
name: stitch-cache
description: Cache Stitch screenshot PNGs and HTML source files in a project-local gitignored folder so repeat runs (planner reading, then reviewer re-reading) don't redownload. Use whenever you need a Stitch asset for a component.
allowed-tools: Bash, Read
argument-hint: "[component name] [theme: light|dark] [ext: png|html]"
metadata:
  internal: true
---

# Stitch Asset Cache

Cache location: `./tmp/stitch-cache/{Component}_{theme}.{ext}` (gitignored).

The cache survives across sessions — if the planner already downloaded `PopButton_light.png`, the reviewer reuses it.

## Why a script (and not inline curl)

- MCP Stitch tools (`mcp__stitch__*`) are session-bound — they cannot be invoked from shell.
- curl with the Google Photos `=s0` fallback and HTML-redirect detection is annoying to write inline, easy to get wrong.
- A 40-line shell script handles the filesystem + download plumbing. The agent only does what requires the session: calling the MCP tool to obtain the URL.

## Usage

### Step 1 — check the cache

```bash
path=$(./scripts/stitch-cache.sh path {Component} {theme} {ext})
if [ $? -eq 0 ]; then
  # HIT — $path exists and is valid (non-empty, not an HTML redirect page)
  echo "cached at $path"
else
  # MISS — $path is where we will save once we have the URL
  echo "need to fetch; target: $path"
fi
```

`{Component}` is the PascalCase component name (e.g. `PopButton`). `{theme}` is `light` or `dark`. `{ext}` is `png` or `html`.

### Step 2 — on cache miss, get the URL via MCP

Only when the `path` command exits non-zero, call the appropriate MCP tool from this session (the script can't do this):

- **PNG URL** — `mcp__stitch__get_screen` → read `screenshotDownloadUrl` (for the requested theme)
- **HTML URL** — `mcp__stitch__get_screen` → read `htmlCode.downloadUrl`

If you haven't already located the right screen, call `mcp__stitch__list_screens` with `projectId: "7983075619754946215"` and find the screen containing the component (search broadly — a component may appear inside a composite/doc screen).

### Step 3 — save

```bash
./scripts/stitch-cache.sh save {Component} {theme} {ext} "{url-from-MCP}"
```

The script:
- tries the URL, then `${url}=s0` if the first fails or returns HTML (Google Photos redirect)
- writes into `./tmp/stitch-cache/` (creates the dir on demand)
- prints the saved path on success, exits 1 with a failure message on failure

### Step 4 — read the file

`Read` the printed path. For PNGs, you'll see the image. For HTML, the Tailwind classes tell you exact spacing/shape specs.

## Full flow (copy-paste)

```bash
# PNG — light theme
comp=PopButton
theme=light

if path=$(./scripts/stitch-cache.sh path "$comp" "$theme" png); then
  echo "HIT: $path"
else
  # MISS — call mcp__stitch__get_screen in the session, grab screenshotDownloadUrl for light
  # Then:
  ./scripts/stitch-cache.sh save "$comp" "$theme" png "$URL_FROM_MCP"
fi
```

## When to bust the cache

- Stitch design changed — delete the specific file: `rm ./tmp/stitch-cache/{Component}_{theme}.{ext}`
- Nuke all: `rm -rf ./tmp/stitch-cache/`

## Notes

- One component can have multiple files: `_light.png`, `_dark.png`, `_light.html`, `_dark.html`. Check each independently.
- For very tall images (>4000px), cache the full PNG and crop at read time — don't cache the crop. The full file is what persists across sessions.
- Do **not** commit the cache. `/tmp/` is gitignored at repo root.
