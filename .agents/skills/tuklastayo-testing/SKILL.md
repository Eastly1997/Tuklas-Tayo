---
name: tuklastayo-testing
description: "Perform requested targeted TuklasTayo verification or create, improve and troubleshoot focused Kotlin/Android tests. Exclude ordinary feature implementation with routine checks, review-only and documentation-only maintenance."
---

# TuklasTayo targeted testing

Read project-root `AGENTS.md` and applicable module guidance first. Its workspace, no-PC-control/no-GUI, security, and external-access restrictions apply. Use permitted command-line checks only; never automate Android Studio or start/configure an emulator. Skill activation does not authorize downloads or external-service access.

## Inputs and scope

Use the requested behavior or failing test, reproduction details, affected code/tests, and available project toolchain. Determine whether the task concerns domain, data, presentation, or an integration boundary. Ordinary feature/data work already includes its own focused tests and need not load this skill.

Activate for “Test GetPlaceByIdUseCase edge cases” or “Diagnose this Discover coroutine test failure.” Do not activate for “Implement a new Explore feature,” “Review this diff,” or “Update Firebase setup documentation.”

## Workflow

1. Inspect the affected behavior/files and nearby tests before selecting the smallest useful check. Prefer compilation of the affected module/consumer for structural changes. Use Discover's screen-level Route/Screen/ViewModel/UiState/Intent files and optional component/debug preview packages as the organization reference; mirror affected packages in tests and update imports after moves. Keep test changes in scope and assert observable behavior rather than implementation details.
2. Test pure use cases with fake repository interfaces. Cover relevant delegation, validation, not-found/empty results, and failure propagation. Test DTO mapping and repository contract semantics with fakes or established test seams; no live backend by default.
3. For MVI, control the main dispatcher/test scheduler and restore it after tests. Observe meaningful StateFlow transitions, retries, duplicate-request behavior, and cancellation where affected. Avoid arbitrary sleeps and dependence on incidental emission timing.
4. Add integration/navigation coverage only when the requested behavior crosses that boundary. Device tests require explicit authorization and an already user-prepared device; otherwise provide manual setup/run instructions. Do not turn a unit-test task into broad instrumentation work.
5. Inspect available tasks/toolchain locally. Before executing Gradle, ensure its wrapper, dependency resolution, caches, toolchain provisioning, and outputs can respect workspace and external-access restrictions. Prefer offline execution when dependencies are available; `--offline` alone does not guarantee all writes stay in the workspace. If constraints cannot be met, report the blocker without downloading/installing software or writing global caches.
6. Default to Light validation: one affected-scope compilation check, with focused tests only for changed logic/tested behavior or a concrete regression risk. Avoid full builds/suites, lint, instrumentation or release builds unless explicitly requested or necessary for a specific issue. Do not repeat successful checks; investigate relevant errors before retrying. Examples, when permitted: `./gradlew.bat :app:compileDebugKotlin --offline` or `./gradlew.bat :feature:discover:testDebugUnitTest --tests '*ExploreViewModelTest' --offline`. Derive task names from the actual module; these examples grant no execution permission.
7. Read exit status and current reports. Distinguish test failures from environment/setup failures. Do not weaken assertions, suppress failures, or change unrelated production code to get green results. If a production fix is outside the request, report the finding and needed follow-up.

## Output and completion

Report briefly: affected scope/files, exact command and result, focused counts when relevant, and blockers/unrun checks. Include detailed diagnostics only for failures that need investigation. Completion requires a supported verification result or diagnosis with honest limits; explicitly identify written-but-unexecuted tests and never substitute old reports for a current run.



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