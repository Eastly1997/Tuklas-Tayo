# Development conventions

## Naming and organization

Root package/application ID: com.lakbay.tuklas.
App namespace: com.lakbay.tuklas.app. App functionality uses domain, data and feature packages under com.lakbay.tuklas.
Use Tuklas for branded code (TuklasApplication, TuklasTheme, TuklasButton);
TuklasTayo remains the product name. Do not prefix ordinary domain/feature types.
Types/composables use PascalCase, functions/properties camelCase. Match directories

Feature layout, created only as needed:

```text
feature/discover/explore/
  ExploreRoute.kt
  ExploreScreen.kt
  ExploreViewModel.kt
  ExploreUiState.kt
  ExploreIntent.kt
  component/    # screen-specific composables
  preview/      # debug sources only
feature/discover/search/   # same screen-level organization
feature/discover/shared/   # genuinely shared UI, filtering and mapping
feature/saved/
feature/place_detail/
```

One primary responsibility per Kotlin file. Small private helpers can stay with
their owner. No empty effects/packages, generic base classes or boilerplate layers.
Discover is the structural reference. Preserve existing public route locations
when moving them is unnecessary for the task.

## Compose and reuse

Routes own Koin resolution, lifecycle-aware collection, intent dispatch and
navigation/application integration. Screens accept immutable UiState and callbacks
and render independently; no ViewModel lookup, flow collection, backend operations
or DI/navigation initialization. ViewModels call domain use cases and preserve
structured concurrency and cancellation.

Put general-purpose reusable UI in core:designsystem and feature-specific UI in
its feature. Consume the same production implementation in screens, previews and
Gallery; do not copy component styling. Use Material 3 semantic tokens and shared
spacing/shapes/typography, accessible click targets and nonduplicated announcements.

Previews use the production Screen in TuklasTheme with offline sample state.
Cover Light/Dark for important screens and existing loading/content/empty/error
states. Providers are optional. Keep previews/tooling and fictional fixtures in
debug sources; no Firebase/Koin/ViewModel or live photo download is needed.
Stateless placeholders and Gallery local state do not justify extra runtime layers.

## Validation and operating limits

Default: implement -> compile affected scope once -> report concisely -> stop.
Use :app:assembleDebug for cross-module/debug UI changes, or an affected module's
compileDebugKotlin for a focused structural edit. Add targeted tests for changed
logic/tested behavior. Do not run full suites, lint, instrumentation or release
builds unless explicitly requested or needed for a specific issue.

Use only permitted project-level CLI commands and installed tools. Keep agent
cache/temp/output writes within the project; do not change environment variables,
install software, modify system settings, automate desktop UI or start emulators.
External access needs authorization. For missing setup, provide manual steps.

Report exact checks/results and unverified device behavior. Do not turn historical
test reports into current passes. A concise completion report lists changed files,
main implementation, validation and blockers/manual actions.

## Git workflow after explicit approval

This project is prepared for an initial repository; preparation does not authorize
git init, staging, commits, remotes or pushes.

After approval, inspect candidate files and diffs, verify ignore coverage, and
stage only intended files. Keep commits focused and describe behavior plus actual
validation. Review before committing and request separate authorization for remote
configuration/push when not already granted. Never stage caches/build output,
local SDK/IDE state, Firebase JSON, environment files or signing/service credentials.

Retain wrapper scripts/JAR, version catalog, Gradle build configuration, source,
resources, tests, documentation, AGENTS.md and .agents/skills. Do not delete useful
local artifacts merely to clean the candidate file list. If a credential is found,
leave it intact locally, exclude it, and arrange rotation/removal from published
history if it has ever been shared; never print its value.

## Local skills

Use the existing tuklastayo-feature, tuklastayo-data, tuklastayo-code-review and
tuklastayo-testing workflows for their documented scopes. Names are preserved
invocation identifiers, not branded Kotlin code. AGENTS.md and user authorization
govern all skills; do not create duplicate workflows.




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