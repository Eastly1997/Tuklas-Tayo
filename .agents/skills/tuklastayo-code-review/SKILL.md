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
3. Trace screen -> ViewModel -> use case -> repository interface -> implementation. Check ViewModels use domain use cases rather than direct repository access, domain has no Android/Firebase/DI dependency, and SDK types/DTO mapping stay out of presentation contracts; domain-to-UI mapping belongs in screen or justified shared mapping package and display models in screen or justified shared model package. Check Screen independence from Koin/ViewModel lookup, flow collection and backend calls; previews must consume production Screens with offline state. Distinguish legitimate state orchestration from misplaced UI rendering/navigation logic. Check lifecycle, coroutine cancellation/concurrency, state transitions, shared-component reuse and unnecessary abstractions.
4. Look for demonstrated regressions, duplicated design-system components/styles, exposed credentials, bypassed access controls and unsafe backend assumptions. Never reproduce secret values. Separate evidence-backed defects from optional style preferences.
5. Default to read-only inspection and Light verification. Run a relevant compilation task or focused test only when authorized and useful to resolve a specific finding. No full suites, lint, instrumentation or release builds without a concrete reason/request; no GUI or unauthorized external-service access. Never claim unexecuted checks passed.
6. Report only actionable findings, ordered P0 critical, P1 high, P2 medium, P3 low. Give a short title, exact file/line, trigger/impact, evidence and a practical correction. Explain uncertainty. If none are found, say so with verification limitations.
7. If fixes were explicitly requested, make only scoped confirmed corrections and report resolved/open findings with actual validation. Never perform destructive fixes or create commits without required approval.

## Output and completion

Return concise prioritized findings with file locations, then necessary assumptions and actual verification results. Completion means evidence-backed assessment of the requested scope, not proof of zero defects. For authorized fixes, also summarize changed files. Stop after the review/fix scope.

## Consolidated architecture

Only :app and :core:designsystem are active modules. Organize features by Kotlin packages in app; domain/model, domain/repository and domain/usecase remain backend-independent, while data/dto and data/repository own Firebase/mock implementations. Presentation -> Domain <- Data remains mandatory. ViewModels call use cases; domain models can be used directly in UiState. Retain UI models only for meaningful presentation transformations. Keep one Activity, app-level navigation/Koin, Route -> Screen -> Components -> Preview, MVI, debug-only Gallery and the production design-system single source of truth. No PC control or Git mutations without authorization.

Feature package convention: :app owns feature packages with no presentation/ level. Keep Route, Screen, ViewModel, UiState and Intent together; optional Effect, component/, preview/, model/ or mapper/ organization must be justified. Domain and data stay application-level. Only code used by multiple screen flows belongs in feature/discover/shared. Preserve previewability, use-case boundaries, Koin composition, centralized navigation and design-system reuse. Validate once with :app:assembleDebug; target tests to changed business logic, avoiding broad suites, lint, instrumentation and release builds unless explicitly requested.

Review incorrect package placement, needless abstractions/UI models, direct ViewModel-to-repository access, DTO leaks, Android/Firebase imports in domain, duplicated design-system styling, navigation/DI coupling, stale imports and unused files. Check only the requested scope; do not enforce optional subpackages as mandatory.
