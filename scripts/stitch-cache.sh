#!/usr/bin/env bash
# Project-local Stitch asset cache. Cache lives at ./tmp/stitch-cache/ (gitignored).
#
#   stitch-cache.sh path <component> <theme> <ext>
#     prints cache path; exit 0 if cached (non-empty, not HTML), 1 if missing
#   stitch-cache.sh save <component> <theme> <ext> <url>
#     downloads url into cache; retries with "=s0" suffix for Google Photos redirects;
#     prints cache path on success, exit 1 on failure
#
# MCP tools are session-bound and cannot be invoked from shell. The caller (agent)
# is responsible for calling mcp__stitch__* to obtain the URL on cache miss.

set -euo pipefail

root="$(git rev-parse --show-toplevel 2>/dev/null || { cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd; })"
dir="$root/tmp/stitch-cache"

usage() {
  echo "usage: $0 {path|save} <component> <theme> <ext> [url]" >&2
  exit 2
}

[ "$#" -lt 4 ] && usage
cmd="$1"; comp="$2"; theme="$3"; ext="$4"
file="$dir/${comp}_${theme}.${ext}"

is_html() { file -b "$1" 2>/dev/null | grep -qi 'html'; }
is_valid() {
  [ -s "$1" ] || return 1
  # For HTML assets, being HTML is valid. For others (png), it's a Google Photos redirect page.
  [ "$ext" = "html" ] && return 0
  ! is_html "$1"
}

case "$cmd" in
  path)
    printf '%s\n' "$file"
    is_valid "$file" && exit 0
    exit 1
    ;;
  save)
    [ "$#" -lt 5 ] && usage
    url="$5"
    mkdir -p "$dir"
    # Google Photos URLs need =s0 for full resolution; without it we get a thumbnail.
    # For non-Google URLs =s0 is a no-op the server ignores.
    if [[ "$url" == *"googleusercontent.com"* ]] && [[ "$url" != *"=s0"* ]]; then
      try_url="${url}=s0"
    else
      try_url="$url"
    fi
    curl -fsSL "$try_url" -o "$file" || curl -fsSL "$url" -o "$file" || true
    # PNG asset that downloaded as HTML → Google Photos redirect; retry with =s0.
    if [ "$ext" != "html" ] && is_html "$file"; then
      curl -fsSL "${url}=s0" -o "$file" || true
    fi
    is_valid "$file" || { echo "download failed: $url" >&2; exit 1; }
    printf '%s\n' "$file"
    ;;
  *)
    usage
    ;;
esac
