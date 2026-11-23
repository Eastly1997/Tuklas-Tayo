---
name: tuklastayo-data
description: "Implement or modify TuklasTayo backend-independent data contracts, repositories, Firebase DTOs and mappings when data-layer work is the primary request. Exclude visual UI, documentation-only, test-only and review-only tasks."
---

# TuklasTayo data implementation

Read project-root `AGENTS.md` and applicable module guidance first. Its workspace, no-PC-control/no-GUI, security, and external-access restrictions apply. This skill does not authorize live Firebase/REST requests, credentials, schema deployment, or access-control changes.

## Inputs and scope

Use the requested repository behavior, existing domain contract and consumers, affected implementation, and any supplied schema/fixture examples. Establish null/not-found behavior, ordering/limits, errors, and cancellation relevant to the change. If contract changes could affect consumers, identify that impact before editing.

Activate for “Add a backend-independent place lookup contract” or “Fix Firestore category mapping.” Do not activate for “Change card spacing,” “Explain the README,” “Review repository changes,” or “Only add tests for this existing mapper.” A feature request uses this workflow only for its necessary data portion.

## Workflow

1. Trace the affected contract in feature Domain, implementation in feature Data, and binding in `app`. Read only relevant DTOs, mappings, callers, and tests.
2. Keep domain models and repository interfaces pure Kotlin and backend-independent, with ordinary unprefixed names (Place, PlaceRepository, use cases). Reserve the `Tuklas` prefix for branded application/design-system code as defined in AGENTS; preserve user-facing `TuklasTayo` text. Preserve contract semantics unless explicitly changed, keeping Firebase replaceable by REST.
3. Implement data access behind the domain interface with constructor-injected dependencies. Keep SDK-specific classes, transport DTOs, and conversion inside the data implementation. Map document IDs and malformed/unknown/optional fields deliberately; do not silently hide corrupt data or backend errors. Preserve cancellation.
4. Keep fictional fixtures in debug/test sources, separate from domain/use cases; Discover DI injects debug fixtures into the mock repository and release injects an empty list. Do not hardcode fixtures in production data sources. Maintain the mock implementation when a shared contract changes. Select the backend using the app-provided configuration and only when requested; mock remains default.
5. Add focused tests for changed mapping, boundary conditions, and repository semantics using fakes or existing test seams. Test offline behavior without contacting real services. Live integration tests require explicit service permission and real user-provided configuration; never create credentials or loosen rules to make tests pass.
6. Run relevant permitted CLI checks after checking workspace/cache and external-access limits. If live access or environment constraints block verification, report it and provide manual steps. Update backend/schema documentation only if its documented contract changes.

## Output and completion

Report contract/schema impact, files/modules changed, mapping/error decisions, mock compatibility, and exact checks with results. Completion requires data implementations satisfying the agreed domain contract, no SDK types leaking into feature/domain APIs, and targeted verification or clearly identified unverified integration behavior. Do not modify unrelated UI or migrate production data.



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