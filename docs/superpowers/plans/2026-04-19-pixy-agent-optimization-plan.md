# Pixy Agent Optimization Plan — Tailored to Electric Pop

**Date:** 2026-04-19
**Status:** Awaiting review

This plan replaces the generic prompt at `docs/superpowers/plans/agent-team-optimization-prompt.md`. Its assumptions (file names, file-based `PLAN.md`/`IMPL_LOG.md` handoff, 1024-token cache threshold, 300-token plan budget) don't match this repo. Below is what the research actually says and what to change based on it.

---

## 1. Research findings

All sourced from Anthropic official docs fetched 2026-04-19.

### Q1. Cache activation threshold

**Source:** https://platform.claude.com/docs/en/docs/build-with-claude/prompt-caching

Per-model minimum to activate caching:

| Model | Minimum tokens |
|---|---|
| Opus 4.7 / 4.6 | **4096** |
| Sonnet 4.6 | **2048** |
| Haiku 4.5 | 4096 |
| Sonnet 4.5 / earlier | 1024 |

> "Shorter prompts cannot be cached, even if marked with `cache_control`. Any requests to cache fewer than this number of tokens will be processed without caching, and no error is returned."

**Implication:** The generic prompt's "1024 threshold" is outdated. **Our planner runs on Opus 4.7 and needs 4096 static tokens to cache — currently it's ~3050.** Pixy orchestrator is also below the Opus threshold. Implementor and reviewer (Sonnet) clear 2048 and do cache.

### Q2. Cache TTL and cost

Default TTL is 5 minutes. 1-hour TTL available at 2× base input price.
Cache read: 0.1× base input (90% discount).

For our flow (planner → implementor → reviewer, each with Stitch fetches and a gradle build in between), a full cycle likely exceeds 5 minutes per agent type, so **inter-run caching is rare in practice**. The caching that matters is within a single dispatch's turns (system prompt reused on every tool-use round-trip).

### Q3. What Claude Code auto-injects into subagents

**Source:** https://code.claude.com/docs/en/sub-agents and https://code.claude.com/docs/en/memory

- Subagent system prompt = **only** the agent's markdown body + frontmatter + basic environment details. **It does NOT inherit the parent's system prompt.**
- `CLAUDE.md` (project + user + ancestor) **IS loaded into every subagent** as a user message after the system prompt. Confirmed by the memory doc: "Both [CLAUDE.md and auto memory] are loaded at the start of every conversation."
- Skills listed in `skills:` frontmatter are injected in full at startup.
- MCP servers listed in `mcpServers:` frontmatter are connected for the subagent's lifetime.
- Parent conversation history is **not** passed to subagents.

**Implication — and this is the biggest single optimization:** our pixy orchestrator currently tells subagents *"Pass in the 7 design rules (copy them from CLAUDE.md)"* and forwards theme API details. That content is already auto-loaded via CLAUDE.md. We're paying for it twice in every subagent dispatch.

### Q4. Subagent return cost

**Source:** sub-agents doc, warning box: *"When subagents complete, their results return to your main conversation. Running many subagents that each return detailed results can consume significant context."*

Subagent's final message lands in the orchestrator's context verbatim. Current pixy-planner returns an entire PLAN.md-style document (~2–3k tokens of structured markdown with Visual Spec, HTML mappings, test strategy, demo spec). That accumulates in the Opus orchestrator's context, and the orchestrator then **re-embeds the same plan** into its subsequent Agent tool calls to the implementor and reviewer. That's the 3× context tax you correctly called out.

### Q5. Supported frontmatter fields

**Source:** sub-agents doc, "Supported frontmatter fields"

Full list: `name`, `description`, `tools`, `disallowedTools`, `model`, `permissionMode`, `maxTurns`, `skills`, `mcpServers`, `hooks`, `memory`, `background`, `effort`, `isolation`, `color`, `initialPrompt`.

Fields we're not using that are relevant:
- **`effort`**: overrides session effort for this subagent. On Opus 4.7, default is `xhigh` (deeper reasoning, more output tokens). For structured plan generation we likely don't need xhigh.
- **`disallowedTools`**: denylist alternative to `tools`. Cleaner when we want most tools minus a few.
- **`memory`**: gives a subagent a persistent `~/.claude/agent-memory/<name>/` directory. Content auto-injected (first 200 lines / 25KB of its `MEMORY.md`).

### Q6. `opusplan` validity in subagent `model`

**Source:** model-config doc

`opusplan` is a valid alias, **but** it only has an effect when the session enters plan mode (shift+tab). A subagent that's always planning doesn't benefit — it needs plain `model: opus` (which we already use). Keep as-is.

### Q7. What invalidates the cache

Tool definition changes invalidate the **entire** cache. So changing an agent's `tools:` frontmatter invalidates its cache for every user. Do it once, rarely.

### Q8. Tool/tool-result caching

Tool definitions and tool results are both cacheable. Good — the large MCP tool list (`mcp__stitch__*`, `mcp__plugin_github_github__*`) contributes to the cacheable prefix.

### Q9. Comment stripping in CLAUDE.md

**Source:** memory doc: *"Block-level HTML comments (`<!-- maintainer notes -->`) in CLAUDE.md files are stripped before the content is injected into Claude's context."*

Useful for adding maintainer notes without paying tokens. Doesn't apply to agent markdown (not stripped there — tested behavior).

### Unverified

- Whether CLAUDE.md injection happens before or after the subagent system prompt token-wise (affects whether it counts toward the cacheable prefix). Treated as "probably after, not cached with system prompt" — conservative assumption.
- Whether the Agent tool's invocation prompt from parent-to-subagent is cached. Treat as not cached (per-run dynamic content).

---

## 2. Current state audit

### Token budget per agent

| File | Bytes | Approx tokens | Model | Cache threshold | Caches? |
|---|---|---|---|---|---|
| `pixy.md` | 8974 | ~2560 | opus | 4096 | **No** |
| `pixy-planner.md` | 10675 | ~3050 | opus | 4096 | **No** |
| `pixy-implementor.md` | 8320 | ~2380 | sonnet | 2048 | Yes |
| `pixy-reviewer.md` | 12376 | ~3540 | sonnet | 2048 | Yes |
| `CLAUDE.md` | 10041 | ~2870 | (auto-loaded) | — | — |

**Total payload per full build cycle (approx):**
- Orchestrator: 2560 (system) + 2870 (CLAUDE.md) + 3000 (plan text after planner returns) + dispatch prompts ≈ **12k tokens** on Opus baseline, growing with each subagent return.
- Each subagent dispatch: its own system prompt + CLAUDE.md + Agent-tool prompt (currently includes the full plan text for implementor/reviewer).

### Duplication detected

- **7 design rules mentioned 26 times across the 4 agent files.** Canonical source is `CLAUDE.md` §"7 Design Rules (Non-Negotiable)".
- **Theme token rules (`MaterialTheme.colorScheme`, `ElectricPopTheme.spacing`, `PopShapeFull`, `RoundedCornerShape`) mentioned 27 times across 3 agent files.** Canonical source: `CLAUDE.md` §"Component Creation SOP / Coding Rules".
- **SOP steps (branch naming, commit format, test paths) repeated in `pixy.md`, `pixy-implementor.md`, `pixy-reviewer.md`, and `CLAUDE.md`.**

### Specific anti-patterns

1. **pixy.md Step 0.2**: *"Read `CLAUDE.md` for project rules"* — CLAUDE.md is auto-loaded. This is a wasted `Read`.
2. **pixy.md dispatch instructions**: each dispatch paragraph says *"Pass in the prompt: ... The 7 design rules (copy them from CLAUDE.md) ... Theme API details (actual code snippets from theme files) ..."* — asks the orchestrator to duplicate content the subagent already has.
3. **Plan-text forwarding**: pixy.md Step 2 says *"Pass in the prompt: The complete plan from Step 1"* and Step 3 similarly *"Pass in the prompt: The plan from Step 1"*. The plan traverses the orchestrator's context three times: planner return → implementor dispatch → reviewer dispatch.
4. **pixy-planner.md §2**: ~50 lines of shape-mapping table and Stitch download shell snippets. This is stable reference material, not a per-run instruction — could live in a `.claude/rules/stitch-mapping.md` path-scoped rule that only loads when the planner reads stitch HTML.
5. **pixy-implementor.md §7 screenshot test template**: a ~50-line kotlin template pasted inline. Useful for new agents but it's reference, not instruction. Could be a file the agent reads on demand (or kept — it's arguably fine inline since it pins the pattern).

---

## 3. Proposed optimizations (ranked by impact × confidence)

### ★★★★★  File-based plan handoff (the insight you raised)

**Change:** planner writes the full plan to `.pixy/plans/{ComponentName}.md` and returns to the orchestrator only a short summary (~150 tokens): file path, variant count, any flags, one-line shape/color note. Implementor and reviewer receive only the file path in their dispatch prompt and `Read` the plan.

**Why:** stops the plan from occupying the Opus orchestrator's context and from being re-embedded in two downstream Agent dispatches. Rough savings: **~6k tokens of Opus context per build cycle** (plan is ~3k; embedded twice downstream = 2× more in dispatch prompts the orchestrator has to issue).

**Secondary wins:**
- Plan persists on disk — survives orchestrator restarts, can be re-read by the reviewer after a fix loop without the orchestrator buffering it.
- Plan becomes a reviewable artifact — checkable into git in `.pixy/plans/` or gitignored per preference (leaning gitignored, they're ephemeral).
- Reviewer can reread the plan on each fix iteration without orchestrator re-forwarding.

**Concrete edits:**
- `pixy-planner.md`: replace "Return your plan in this exact structure" section with "Write your plan to `.pixy/plans/{ComponentName}.md` in this structure. Return only: (1) path, (2) 3-line summary, (3) any warnings." Add `Write` to its `tools:` allowlist.
- `pixy.md` Step 1: change "Wait for the plan. Review it for completeness" to "Planner writes plan to file. Read `.pixy/plans/{ComponentName}.md` only if summary flags concerns. Otherwise proceed."
- `pixy.md` Steps 2 & 3: replace "Pass in the prompt: The complete plan from Step 1" with "Pass plan path `.pixy/plans/{ComponentName}.md`; the subagent will `Read` it."
- `pixy-implementor.md` §1: change "Read the plan carefully" to "Read `.pixy/plans/{ComponentName}.md`. That is your plan."
- `pixy-reviewer.md` §Input: change "The implementation plan (what was supposed to be built)" to "Plan path at `.pixy/plans/{ComponentName}.md` — read it."
- Add `.pixy/plans/` to `.gitignore` unless we want them checked in.

**Risk:** planner must be reliable about writing. Mitigation: planner's return message contains the path and explicit "WROTE" or "FAILED" status. Orchestrator asserts the file exists before dispatching implementor.

### ★★★★☆  Deduplicate content already in CLAUDE.md

**Change:** remove from each agent file the parts that repeat CLAUDE.md. Agents already receive CLAUDE.md auto-loaded — they don't need us to paste it again.

Specific deletions:
- `pixy.md` Step 0.2: delete "Read CLAUDE.md for project rules".
- `pixy.md` Steps 1–3 dispatch bullets: delete "The 7 design rules (copy them from CLAUDE.md)" and "Theme API details".
- `pixy-planner.md`, `pixy-implementor.md`, `pixy-reviewer.md`: replace repeated lists of color/typography/spacing/shape rules with one line — *"Follow the 7 design rules and Coding Rules in CLAUDE.md."*
- Keep `pixy-implementor.md` §Coding Standards (CORRECT vs WRONG code blocks) — these are concrete and reinforce the rules for the implementor in particular. Candidate for trimming but not deletion.

**Savings:** ~400–800 tokens per agent × 3 agents = **~1.5–2.5k tokens reclaimed per dispatch**. Also fixes the content-drift risk: today if we update CLAUDE.md's 7 rules, the agent files fall out of sync.

### ★★★★☆  Push planner prefix over Opus's 4096 cache threshold (or switch to Sonnet)

**Current:** ~3050 tokens → no caching → full input price every run.

**Option A (recommended):** consolidate and raise the planner prefix by **moving the "shape mapping cheat sheet" + "Stitch download playbook" into the planner's system prompt** (they're currently there, but also partly duplicated in the reviewer — consolidate into planner only, with reviewer referencing a `/tmp/stitch_*` convention). Trim instruction prose elsewhere. Target 4200–4500 tokens for a comfortable margin.

Net: planner system prompt becomes slightly longer but **now caches**, saving ~70% on subsequent plan runs within a 5-min TTL (rare) AND 70% on every tool-use roundtrip within a single plan run (common, since planner makes multiple Stitch/Read calls per run).

**Option B:** switch planner to `model: sonnet` + `effort: high`. Sonnet's threshold is 2048 so caching already works. Tradeoff: plan quality on complex composites might drop. We'd want to A/B this — keep two planner variants and pick after 2–3 components.

**Recommendation:** Option A. Keep planning on Opus for quality; earn the cache by growing static content that's actually useful.

### ★★★☆☆  Set `effort: high` on pixy-planner

**Current:** Opus 4.7 defaults to `effort: xhigh` (max of 5 levels on 4.7: low/medium/high/xhigh/max). xhigh adds adaptive reasoning tokens.

**Change:** add `effort: high` to `pixy-planner.md` frontmatter. `high` is still "intelligence-sensitive work" per the model-config doc; xhigh is "most coding and agentic tasks" but our planner is structured output, not open-ended agentic.

**Tradeoff:** if plans get worse, revert. Lowest-risk way to test: A/B on 2 components. Opus still runs its full reasoning; only the adaptive-reasoning scheduler gets a tighter budget.

### ★★★☆☆  Tighten output discipline in all 3 agents

**Change:** add a short block to each agent's system prompt:
```
## Output discipline
- Skip preamble ("I'll now...", "Let me...")
- Skip post-action summary if you've already reported a verdict
- Tool calls before prose; prose only when the tool-call alone doesn't convey intent
```

Sonnet and Opus both over-narrate by default. Saves ~100–300 output tokens per dispatch on Sonnet (cheap), bigger save on Opus orchestrator (expensive — output priced 5× input).

### ★★☆☆☆  Scope static reference material to `.claude/rules/`

**Change:** move non-instruction reference material (Stitch→Compose CSS class map, squircle shape mapping table, screenshot-test template) into `.claude/rules/*.md` files with `paths:` frontmatter so they load only when relevant files are touched. Agent files then reference them by name.

**Benefit:** agent files shrink, reference stays discoverable, CLAUDE.md doesn't grow.

**Risk:** path-scoped rules trigger on file reads, not on agent dispatch. The planner may not touch any matching path, so the rule wouldn't auto-load. Workaround: agents `Read` the relevant rules file explicitly in their checklist.

**Verdict:** medium impact. Worth doing for the Stitch mapping since it's the largest non-instruction chunk. Defer the screenshot template — moving it risks breaking the implementor's pattern-matching.

### ★★☆☆☆  Replace `tools:` long allowlists with `disallowedTools:` where sensible

**Change:** pixy-reviewer's tools list is long (many MCP entries). Consider:
```yaml
disallowedTools: Write, Edit, NotebookEdit
```
Gives it Read/Grep/Glob/Bash/all-MCP while banning writes. Cleaner, less brittle when a new MCP tool is added.

**Risk:** denylist semantics — any new tool becomes available by default. For a reviewer, read-only is the core contract, so `disallowedTools` is safer to state positively. Prefer keeping allowlist but audit for accuracy.

**Verdict:** low impact, low confidence. Skip unless we're already editing frontmatter.

### ★★☆☆☆  Enable `memory: project` on pixy-reviewer

**Change:** give the reviewer persistent memory so it accumulates recurring issues ("last 3 components had shape radii one tier too large").

**Cost:** reviewer's context grows by MEMORY.md content (capped at 25KB/200 lines).
**Benefit:** catches drift without us re-explaining. Aligns with repeated user feedback about review quality.

**Verdict:** worth trying after the higher-impact changes land. Not on the critical path.

### ★☆☆☆☆  Pre-flight validation (the generic prompt's "missing section" check)

**Relevance:** low in our architecture. The generic prompt wanted agents to check `PLAN.md` sections before proceeding. With the file-based handoff above, the implementor should at least assert the plan file exists and has a `## Files` section before proceeding — cheap, catches planner crashes.

**Change:** `pixy-implementor.md` §1 adds: *"If `.pixy/plans/{ComponentName}.md` is missing or lacks `## Files`, write `BLOCKED: plan file missing/malformed` and stop."*

### Not doing

- **Agent Teams** — experimental, not enabled, coordination overhead.
- **Renaming files to `planner.md`/`implementor.md`/`reviewer.md`** — the `pixy-*` prefix is intentional.
- **Session-persistent memory on all agents** — implementor doesn't benefit; its job is fresh code generation.
- **`bypassPermissions`** — security risk.
- **Hard `max_turns` reduction** — already sized per agent; premature.
- **Tool-definition reordering for cache optimization** — frontmatter order is fixed; MCP tool definitions aren't the volatile part.

---

## 4. Implementation order

Do in this order; each step is independently reversible.

1. **File-based plan handoff** (biggest single win) — edit pixy.md, pixy-planner.md, pixy-implementor.md, pixy-reviewer.md; add `.pixy/plans/` to `.gitignore`; add `Write` to planner tools.
2. **Deduplicate CLAUDE.md content** — delete the three "Pass in the 7 rules / theme API" blocks from pixy.md, trim repeated rule listings in the three subagent files.
3. **Output discipline block** — append the 3-line block to each of the 4 agent files.
4. **Planner cache threshold** — measure exact token count, consolidate Stitch playbook in, aim for 4200+. Verify with a test dispatch.
5. **Effort: high on planner** — one-line frontmatter add. Test on next 2 components.
6. **`.claude/rules/` extraction** — move Stitch mapping table out of pixy-planner.md into `.claude/rules/stitch-compose-mapping.md` with path scope.
7. **Reviewer memory** — add `memory: project` and a line in the reviewer prompt about consulting it.
8. **Pre-flight plan assertion** — implementor step 1 adds existence check.

---

## 5. Expected outcome

- Opus orchestrator per-cycle context: **~12k → ~6k tokens** (plan no longer triple-buffered).
- Planner: goes from uncached to cached on Opus 4.7 → ~70% cheaper tool-use roundtrips within a single plan run.
- Agent files: ~10% shorter on avg after dedup.
- No behavioral regressions (behavior comes from CLAUDE.md; we're removing duplicate instructions, not rules).

Success check: rebuild one already-built component (e.g., PopImageBannerCard) under the new system, compare transcript token usage and output quality against its original PR. If the new flow produces a materially different or worse plan, roll back step-by-step.

---

## 6. Open questions for you

1. **`.pixy/plans/` in git or gitignored?** My lean: gitignored (ephemeral; each PR already ships its implementation). If we want them archived for learning, keep a separate `docs/pixy-plans/` with only finalized plans.
2. **Opus vs Sonnet for planner?** Keep Opus (recommendation) or A/B test Sonnet? I'd keep Opus for now and revisit.
3. **`effort: high` vs leave at xhigh default?** Set `high` and test, or leave alone?
4. **Reviewer memory?** Adopt now or defer?

---

## 7. What we're NOT doing from the generic prompt

- "Static prefix ≥1024 tokens" — wrong threshold for Opus. Corrected above.
- "`PLAN.md` under 400 tokens" — unrealistic. Our plan format has meaningful structure and the file-based handoff makes its size less relevant (it's read once by implementor, once by reviewer — not buffered in orchestrator).
- "Add validation that `IMPL_LOG.md` ends with STATUS:" — we don't use IMPL_LOG.md; the implementor reports status in its return message.
- "Move model: to comment explaining Pro cost" — model-config confirms `opusplan` invalid for always-planning subagent; `opus` is correct; comment is noise.
