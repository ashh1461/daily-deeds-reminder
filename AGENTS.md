# Agent Directives & Operational Rules

## Core Mandatory Workflow
For every task, milestone, or significant change in this repository, all AI agents MUST adhere strictly to the following three pillars:

1. **GitHub Synchronization**:
   - Every feature, fix, or release must be committed with meaningful, descriptive commit messages.
   - Pushes to GitHub must be verified and protected against secret leakage (API keys must never be hardcoded into tracked files; use environment variables or gitignored files like `.linear_token`).
   - Releases must be tagged and published on GitHub Releases along with distribution APKs, SHA-256 digests, and detailed release documentation in `docs/releases/`.

2. **Linear Issue Tracking**:
   - Every major task or milestone must be represented and tracked in Linear.
   - Always record the Linear Issue ID and Identifier (e.g., `ALI-20`).
   - Use `scripts/linear_tool.py` to post status updates, progress comments, and state transitions (e.g., Todo -> In Progress -> Done).
   - Ensure Linear comments document verification evidence (test counts, APK sizes, release links).

3. **Progress Documentation (`progress.md`)**:
   - `progress.md` must be updated after EVERY step and milestone.
   - Keep `## Completed`, `## Remaining`, and `## Ongoing log` accurate and up to date with dates, commits, and verification metrics.
   - Never skip updating `progress.md`.

## Quality & Verification Standards
- Zero empty or corrupted Kotlin files in `app/src/`.
- Verify with clean Gradle builds and unit tests before committing (`:app:testDebugUnitTest`).
- Never write files to `app/src/` concurrently while a background Gradle build is running on Windows.

## Knowledge graph (graphify): look up first, read second
`graphify-out/` holds a code graph of this repository (classes, functions, imports, calls, docs, each with `file:line`). Use it to find code instead of opening many files; it saves most of the tokens spent on exploring.

**Before exploring code** (where something lives, who calls it, what a change touches):
1. Ask the graph first. Each command answers in one call and lists exact `file:line` locations. Run them with `PYTHONIOENCODING=utf-8` from the repository root:
   - `graphify explain "PrefsStorage"`: a node, its file, and everything that imports or calls it.
   - `graphify affected "BackupCodec"`: what depends on it. Run this before changing a shared class or function.
   - `graphify path "PrayerAlarmReceiver" "AdhanPlanner"`: how two parts connect.
   - `graphify query "how are prayer alarms rescheduled after a reboot"`: a broader question.
   - `graphify god-nodes`: the main hubs, for orientation on a new task. The top of `graphify-out/GRAPH_REPORT.md` gives the same overview.
2. Then open only the files and lines the graph names. Use Grep for literal text the graph does not model: Arabic strings, resource names, manifest attributes.
3. Do not rely on the graph alone:
   - **Before editing:** read the file itself, since the graph is built from the last update and can lag behind uncommitted work.
   - **Exit code 3:** the name is ambiguous; use the full name the command prints.
   - **Skip it for small, known edits:** a typo, a version bump, or the release steps in a file you already have open cost more tokens to look up than to read.

**Keeping it current:**
- Run `PYTHONIOENCODING=utf-8 graphify update .` after each change set and before each commit, but only when `git status` shows changes in source or docs since the last graph update; skip it otherwise.
- Commit `graphify-out/` together with the change.
- Never run it while a Gradle build is running.
- Warnings about files with syntax errors or no symbols (`VoicePackDownloader.kt`, `ReligiousAlarms.kt`, `Type.kt`, the Gradle scripts) are limits of the extractor, not build problems.

