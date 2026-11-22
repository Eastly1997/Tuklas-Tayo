---
name: tuklastayo-code-review
description: "Review requested TuklasTayo feature architecture or code quality for actionable correctness, organization, dependency, security and regression issues. Default to read-only; exclude ordinary implementation and test-creation requests."
---

# TuklasTayo code review

Read project-root `AGENTS.md` and applicable module guidance first. Its workspace, no-PC-control/no-GUI, security, and external-access restrictions apply. Review is read-only unless the user explicitly asks for fixes; a finding is not authorization to edit or commit.

## Inputs and scope

Use the requested diff/commit range or named files, intended behavior, and acceptance criteria. Identify the actual review target from available local evidence. If no Git metadata or baseline is available, state that limitation and review the supplied files; do not invent a diff, fetch a remote, or initialize Git.

Activate for “Review the Discover implementation for regressions” or “Review these repository changes and fix confirmed bugs.” Only the second authorizes fixes. Do not activate for “Implement Explore,” “Add tests,” or “Update AGENTS.md.”

## Workflow

1. Inspect the target change and enough callers/tests/configuration to assess its effects. Keep the review within the requested scope; do not audit the entire repository by default.
2. Check one primary responsibility per Kotlin file and feature-first package ownership. Check branded code uses `Tuklas`, ordinary feature/domain classes remain unprefixed, Kotlin casing is respected, and product labels stay `TuklasTayo`. Distinguish application ID from namespace using AGENTS. Flag mixed responsibilities/misplaced contracts or DI only for the reviewed scope; allow small private helpers, with no file-per-function, empty effects or unrelated migrations.
3. Trace screen -> ViewModel -> use case -> repository interface -> implementation. Check ViewModels use domain use cases rather than concrete repositories, domain has no Android/Firebase/DI dependency, and SDK types/DTO mapping stay out of presentation contracts; domain-to-UI mapping belongs in presentation/mapper and display models in presentation/model. Check Screen independence from Koin/ViewModel lookup, flow collection and backend calls; previews must consume production Screens with offline state. Distinguish legitimate state orchestration from misplaced UI rendering/navigation logic. Check lifecycle, coroutine cancellation/concurrency, state transitions, shared-component reuse and unnecessary abstractions.
4. Look for demonstrated regressions, duplicated design-system components/styles, exposed credentials, bypassed access controls and unsafe backend assumptions. Never reproduce secret values. Separate evidence-backed defects from optional style preferences.
5. Default to read-only inspection and Light verification. Run a relevant compilation task or focused test only when authorized and useful to resolve a specific finding. No full suites, lint, instrumentation or release builds without a concrete reason/request; no GUI or unauthorized external-service access. Never claim unexecuted checks passed.
6. Report only actionable findings, ordered P0 critical, P1 high, P2 medium, P3 low. Give a short title, exact file/line, trigger/impact, evidence and a practical correction. Explain uncertainty. If none are found, say so with verification limitations.
7. If fixes were explicitly requested, make only scoped confirmed corrections and report resolved/open findings with actual validation. Never perform destructive fixes or create commits without required approval.

## Output and completion

Return concise prioritized findings with file locations, then necessary assumptions and actual verification results. Completion means evidence-backed assessment of the requested scope, not proof of zero defects. For authorized fixes, also summarize changed files. Stop after the review/fix scope.
