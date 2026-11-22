---
name: tuklastayo-feature
description: "Implement or modify TuklasTayo application features with separated presentation, MVI contracts, domain and DI responsibilities. Exclude documentation-only, data-only, review-only and test-only requests."
---

# TuklasTayo feature implementation

Read the project-root `AGENTS.md` and applicable module guidance first. Its workspace, no-PC-control/no-GUI, security, and external-access restrictions apply throughout. This skill does not authorize backend access, tool installation, or another milestone.

## Inputs and scope

Use the requested behavior, acceptance criteria, UX specifications, and existing affected code. Identify missing decisions that materially affect behavior; inspect available project evidence before asking. Confirm the feature is within the user's requested milestone scope. Do not invent product behavior or polish placeholder UI without requirements.

Activate for “Add an approved category filter to Discover using the finalized UX.” Do not activate for “Correct the README,” “Change only Firestore DTO mapping,” “Review this diff,” or “Add tests for existing Discover behavior.”

## Workflow

1. Inspect the feature's existing packages, route, rendering, contracts, ViewModel, use cases and DI wiring. Identify the owner of each responsibility before editing. Keep the existing feature/module prefix; use presentation/ for Route/Screen/ViewModel, contract/ for UiState/Intent/optional Effect, model/ for UI models, mapper/ for domain-to-presentation transformations, component/ for feature composables, debug preview/ for fixtures/previews, and di/ for DI without empty nesting. Do not reorganize unrelated features.
2. Give Screen, ViewModel, UiState, Intent and DI declarations separate appropriately named Kotlin files. Follow Route -> Screen -> Components -> Preview. Routes own existing Koin ViewModel resolution, lifecycle-aware StateFlow collection, intent dispatch and navigation/application callbacks. Screens accept immutable UiState and callbacks and must render independently without ViewModel lookup, flow collection, DI/Firebase/navigation initialization, repositories or use cases. Local UI-only state is allowed. Use immutable UiState and sealed Intent; add a separate Effect file only for a needed one-time event. Keep small private helpers with their owner; never generate empty contracts/classes or split every function for symmetry.
3. Implement or reuse concrete domain use cases. ViewModels call use cases through constructor injection and expose read-only StateFlow. Handle request concurrency deliberately and propagate coroutine cancellation.
4. Keep Compose focused on rendering state and dispatching intents. General-purpose shared components belong in core:designsystem; feature-specific components belong in the feature. Both the Gallery and screens consume the same production implementations. Collect state with lifecycle awareness; keep navigation callbacks in the route/app layer. Reuse designsystem components. Follow root naming: branded code uses `Tuklas` (types/composables PascalCase, functions/properties camelCase), user-facing product text stays `TuklasTayo`, and ordinary feature/domain names stay unprefixed. Keep application ID and code namespace distinct as specified in AGENTS.
5. Put feature-owned Koin declarations in `di/FeatureModule.kt`; let `app` assemble that module with domain/data bindings and own navigation/backend selection. If data changes are needed, keep contracts backend-independent and backend implementations/DTO mapping in data modules; domain-to-presentation mapping stays in presentation/mapper. Never inject a concrete repository into a ViewModel or expose SDK types in presentation contracts.
6. Add debug-only previews under presentation/preview using the actual production Screen and offline sample UiState, wrapped in TuklasTheme (and Surface when a background is needed). Include Light/Dark for important screens and loading/content/empty/error when supported; add a sample provider only when useful. Keep fictional fixtures and tooling dependencies out of release. Never create preview-only screen copies or new business states for examples. Keep contracts in presentation/contract and feature components in presentation/component when appropriate. Stateless placeholders and the local-state Gallery need no ViewModel or Route just for symmetry.
7. Default to Light validation: compile only the affected scope once. Run focused tests when changed logic, moved test imports, or a concrete regression risk warrants them. No full suites, lint, instrumentation or release build unless requested or specifically justified. Respect workspace/cache/external-access limits; provide manual steps for blocked checks. Update only affected documentation.

## Output and completion

Give a concise summary of affected files, behavior delivered, responsibility/package separation and actual validation results/blockers. Completion requires the requested scope, preserved existing behavior, correct module boundaries and honest verification evidence. Do not repeat successful checks or start another feature.
