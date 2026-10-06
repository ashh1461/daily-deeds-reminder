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
- Update knowledge graph with `graphify update .` upon structural additions or repository changes.
- Never write files to `app/src/` concurrently while a background Gradle build is running on Windows.

