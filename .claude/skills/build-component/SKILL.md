---
name: build-component
description: Build an Electric Pop component end-to-end. Orchestrates pixy-planner, pixy-implementor, and pixy-reviewer subagents in sequence. Handles branching, Telegram notifications, and PR creation. Use when building any component from the Electric Pop design system.
allowed-tools: Agent, Read, Grep, Glob, Bash, ToolSearch, "mcp__plugin_github_github__*", "mcp__stitch__*", "mcp__plugin_telegram_telegram__*"
argument-hint: "<ComponentName> [tier] [variant details]"
metadata:
  internal: true
---

# Build an Electric Pop Component

You are now the **component orchestrator**. You coordinate three subagents — `pixy-planner`, `pixy-implementor`, and `pixy-reviewer` — to build one component at a time.

You do NOT write component code yourself. You delegate, review results, and handle notifications.

## Telegram Notifications

**chat_id: 1402731017**

Send Telegram notifications at these milestones — the user is NOT watching the terminal:

| Event | What to send |
|-------|-------------|
| **Any PR created** | PR URL, branch, short summary of changes |
| Component APPROVED + PR raised | Component name, PR URL, variant list. Attach light golden screenshot. |
| BLOCKED (after 3 fix attempts) | Component name, what step failed, what error |
| Wave complete | Short summary: wave name, components built, PRs raised |
| Decision needed from user | Short question with context so user can reply from Telegram |

**How to send:**
- Load the Telegram tool via ToolSearch: `ToolSearch(query="select:mcp__plugin_telegram_telegram__reply", max_results=1)`
- Text: `mcp__plugin_telegram_telegram__reply` with `chat_id: "1402731017"`
- With screenshot: same tool with `files: ["/abs/path/to/snapshot.png"]`
- Keep messages concise

**Do NOT send** for: each sub-step, planner dispatches, reviewer passes, intermediate build results.

## Project Context

Key files (read these FIRST before dispatching any subagent):
- `CLAUDE.md` — Project rules, SOP, build commands
- `DESIGN.md` — 7 design rules, theme architecture, color/typography/shape/spacing tokens
- `AGENTS.md` — Agent dispatch rules, MCP tool usage, branch + commit conventions
- Pages reference: <https://tanaykumarbera.github.io/compose-electric-pop/api/> — per-component API + screenshots

Key paths:
- Component source: `library/src/commonMain/kotlin/co/tanay/electricpop/{tier}/`
- Tests: `library/src/commonTest/kotlin/co/tanay/electricpop/{tier}/`
- Screenshot tests: `library/src/desktopTest/kotlin/co/tanay/electricpop/{tier}/`
- Demo: `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/`
- Catalog: `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/CatalogScreen.kt`
- Golden snapshots: `library/src/desktopTest/snapshots/`

Stitch design project: `7983075619754946215`

## Workflow

### Step 0: Branch and Gather Context
1. Create a feature branch from the current HEAD:
   ```bash
   git checkout -b feat/pop-{component-name-kebab}
   ```
   Branch from wherever HEAD is (which may be a previous component's branch), NOT from `main`.
2. Read `CLAUDE.md` for project rules
3. Read the design spec section for the target component
4. Read `AGENTS.md` for component details (variants, dependencies, notes)
5. Read existing code in `library/src/commonMain/kotlin/co/tanay/electricpop/theme/` to understand the theme API
6. If this is a composite component, read its foundation dependencies first

### Step 1: Plan (pixy-planner)
Dispatch `pixy-planner` via the Agent tool:

```
Agent(
    subagent_type="pixy-planner",
    prompt="<full context: component name, tier, variants, 7 design rules, theme API details, Stitch project ID>"
)
```

Pass in:
- Component name, tier, and all variant details from the spec/inventory
- The 7 design rules (copy from CLAUDE.md)
- Theme API details (color scheme tokens, typography styles, spacing values, shape tokens)
- For composites: the actual code of foundation components it depends on
- Stitch project ID for visual reference

Review the plan for completeness:
- Does it define exact file paths?
- Does it list ALL variants and parameters?
- Does it include test strategy?
- Does it include demo page spec?

If incomplete, re-dispatch with clarifying context.

### Step 2: Implement (pixy-implementor)
Dispatch `pixy-implementor` via the Agent tool:

```
Agent(
    subagent_type="pixy-implementor",
    prompt="<complete plan from Step 1, 7 design rules, theme API reference, build/test commands>"
)
```

The implementor will write the component, tests, demo page, register in CatalogScreen, run builds, and commit.

**Handle status:**
- DONE — proceed to Step 3
- DONE_WITH_CONCERNS — read concerns, address if critical, proceed to Step 3
- NEEDS_CONTEXT — provide missing context, re-dispatch
- BLOCKED — send Telegram alert, STOP, report to human

### Step 3: Review (pixy-reviewer)
Dispatch `pixy-reviewer` via the Agent tool:

```
Agent(
    subagent_type="pixy-reviewer",
    prompt="<plan from Step 1, 7 design rules, file paths of all created/modified files>"
)
```

Returns: APPROVED | ISSUES_FOUND with specific fix list

### Step 4: Fix Loop (if ISSUES_FOUND)
- Dispatch `pixy-implementor` with the reviewer's specific fix list
- Re-dispatch `pixy-reviewer` after fixes
- Max 3 iterations. After 3 failures:
  - Send Telegram alert (component name, what keeps failing, last reviewer output)
  - STOP and report all unresolved issues to human

### Step 5: Verify Clean State
After APPROVED, run `git status`. If uncommitted changes remain:
- Component-related (missed files) — dispatch implementor to fix
- Artifacts/caches — add to `.gitignore` and commit

Working tree MUST be clean before proceeding.

### Step 6: Push and Create PR
Push the feature branch:
```bash
git push -u origin feat/pop-{component-name-kebab}
```

> **NEVER push to `main` directly.** Only push feature branches.

Create the PR using the `/git-pr` skill. PR metadata:
- **owner:** `tanaykumarbera`
- **repo:** `compose-electric-pop`
- **title:** `feat({tier}): add {ComponentName} with variants`
- **head:** `feat/pop-{component-name-kebab}`
- **base:** `main`

If GitHub MCP is unavailable, just push the branch and note it in the Telegram message.

### Step 7: Telegram Notification
Send a Telegram message after PR creation:
```
{ComponentName} done!

Tier: {foundation/composite/chart}
Variants: {list}
PR: {URL}
Branch: feat/pop-{name}
```
Attach `library/src/desktopTest/snapshots/{ComponentName}_allVariants_light.png` if available.

### Step 8: Summary
Report to terminal:
```
## Component: {Name}
- Tier: {foundation/composite/chart}
- Files created: {list}
- Files modified: {list}
- Tests: {count} passing
- Screenshots: {list of golden PNGs}
- Variants: {list}
- Review: APPROVED
- Branch: feat/pop-{name}
- PR: {PR URL}
- Telegram: notified
- Git: clean working tree
- Concerns: {any notes}
```

## Rules
- NEVER write component code yourself — always delegate to subagents
- NEVER skip the review step
- NEVER let the implementor proceed without a plan
- If the same error appears twice in a fix loop, STOP — don't loop blindly
- Always provide COMPLETE context to subagents — they have no memory of prior dispatches
- Each subagent dispatch must be self-contained with all needed information
- **NEVER invoke sub-agents via Bash or the `claude` CLI** — always use the Agent tool with `subagent_type`
- **NEVER push to `main`** — only push feature branches
- Always send Telegram notification at APPROVED+PR and BLOCKED milestones
