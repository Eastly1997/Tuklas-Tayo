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
6. Default to Light validation: one affected-scope compilation check, with focused tests only for changed logic/tested behavior or a concrete regression risk. Avoid full builds/suites, lint, instrumentation or release builds unless explicitly requested or necessary for a specific issue. Do not repeat successful checks; investigate relevant errors before retrying. Examples, when permitted: `./gradlew.bat :app:compileDebugKotlin --offline` or `./gradlew.bat :app:testDebugUnitTest --tests '*ExploreViewModelTest' --offline`. Derive task names from the actual module; these examples grant no execution permission.
7. Read exit status and current reports. Distinguish test failures from environment/setup failures. Do not weaken assertions, suppress failures, or change unrelated production code to get green results. If a production fix is outside the request, report the finding and needed follow-up.

## Output and completion

Report briefly: affected scope/files, exact command and result, focused counts when relevant, and blockers/unrun checks. Include detailed diagnostics only for failures that need investigation. Completion requires a supported verification result or diagnosis with honest limits; explicitly identify written-but-unexecuted tests and never substitute old reports for a current run.

## Consolidated architecture

Only :app and :core:designsystem are active modules. Organize features by Kotlin packages in app; domain/model, domain/repository and domain/usecase remain backend-independent, while data/dto and data/repository own Firebase/mock implementations. Presentation -> Domain <- Data remains mandatory. ViewModels call use cases; domain models can be used directly in UiState. Retain UI models only for meaningful presentation transformations. Keep one Activity, app-level navigation/Koin, Route -> Screen -> Components -> Preview, MVI, debug-only Gallery and the production design-system single source of truth. No PC control or Git mutations without authorization.

Feature package convention: :app owns feature packages with no presentation/ level. Keep Route, Screen, ViewModel, UiState and Intent together; optional Effect, component/, preview/, model/ or mapper/ organization must be justified. Domain and data stay application-level. Only code used by multiple screen flows belongs in feature/discover/shared. Preserve previewability, use-case boundaries, Koin composition, centralized navigation and design-system reuse. Validate once with :app:assembleDebug; target tests to changed business logic, avoiding broad suites, lint, instrumentation and release builds unless explicitly requested.
