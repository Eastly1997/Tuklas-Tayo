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



## Feature-owned Clean Architecture

- Active modules: :app, :feature:discover, :feature:manage, :core:designsystem. Each feature owns its Domain, Data and Presentation responsibilities, use cases, repository interfaces, implementations, models, mappings and tests. No shared business core modules or cross-feature dependencies without an actual cross-feature contract requirement.
- Discover uses com.lakbay.tuklas.feature.discover.domain (model/repository/usecase), data (dto/repository), and its existing screen packages (explore/search/saved/place_detail/shared/navigation). Existing screen packages are its Presentation layer; do not add redundant presentation nesting or empty layers merely for symmetry.
- Manage owns its independent dashboard/navigation Presentation and all future management Domain/Data logic. It currently needs no repositories, use cases or business models: do not invent editing, publication, queue, permissions, statistics or empty package scaffolding.
- Within each feature, Presentation -> Domain and Data -> Domain. Domain stays pure Kotlin without Android, Compose, Firebase, DI, Data or UI imports. ViewModels call feature-owned use cases, never repositories or SDKs directly. DTO/backend mapping stays in feature Data. Use domain models directly where sufficient; optional UI projections require meaningful formatting/display behavior.
- App owns Application/Activity startup, root navigation, build configuration, app/auth session and Google sign-in integration and Koin initialization/composition. Features provide their own DI definitions; app selects the backend flag and composes modules. Discover's discoverModules(useFirebase) owns its repositories, use cases and ViewModels. Mock remains default; debug fixtures and photos remain debug-only, release mock data is empty.
- Both features depend on :core:designsystem for generic reusable UI only. Discover and Manage never depend on one another or app; app imports feature integration APIs rather than internal business implementations. No generic base ViewModels/MVI/navigation framework, extra Activities or per-screen modules.
- App root NavHost owns experience integration; Discover and Manage own internal NavHosts/Scaffolds with controllers scoped to their graphs. Preserve one Activity, approved Discover UI, Search keyboard/back behavior, tab restoration, carousel semantics and lifecycle, Gallery source-set hooks and previewability. No legacy Explore feature.
- App-owned Google authentication is implemented (requires real Firebase setup). Discover remains guest-accessible. Restaurant authorization is not implemented; keep Manage unregistered until verified restaurant authorization and backend enforcement exist. Owner preference/debug mode never grants authorization. Manage previews/tests may instantiate UI directly without granting access.
- Route -> Screen -> Components -> Preview and screen-level immutable MVI remain mandatory. Keep Route/Screen/ViewModel/UiState/Intent together, with optional justified subpackages/effects. Koin initializes only in app. Previews use offline production Screens with TuklasTheme and require no backend/runtime initialization.
- :core:designsystem stays shared because it provides generic UI infrastructure, never feature business contracts. Gallery and all feature consumers reuse its production components; no duplicated styling or business-model dependencies.
- Preserve the documented canonical places schema, existing Firebase/mock selection, cancellation/error semantics and repository contracts. Do not introduce published/approval fields, migrate production data or invent management persistence rules. See docs/DATA_SCHEMA.md before implementing future management persistence.
- Tests belong to the owning feature. Use one :app:assembleDebug plus directly relevant targeted unit tests; no broad suites, lint, instrumentation or release builds by default. Existing no-PC-control, workspace, external-access, Git safety and dependency-version restrictions remain unchanged.