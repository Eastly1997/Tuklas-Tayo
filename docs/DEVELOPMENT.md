# Development conventions

## Naming and organization

Root package/application ID: com.lakbay.tuklas.
App namespace: com.lakbay.tuklas.app. App functionality uses domain, data and feature packages under com.lakbay.tuklas.
Use Tuklas for branded code (TuklasApplication, TuklasTheme, TuklasButton);
TuklasTayo remains the product name. Do not prefix ordinary domain/feature types.
Types/composables use PascalCase, functions/properties camelCase. Match directories
to package declarations in every source set; keep only :app and :core:designsystem.

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
Feature packages own presentation; application-level domain and data packages preserve Presentation -> Domain <- Data. ViewModels call use cases. UI models are optional and justified by display transformations. The independent design system is the shared styling source; Gallery consumes production components.

Navigation follows MainActivity -> TuklasApp -> TuklasNavHost -> FeatureRoute -> FeatureScreen. TuklasApp owns the shared Scaffold, toolbar, bottom navigation and insets, using one NavController. TuklasNavHost owns destination declarations and feature navigation callbacks; TuklasDestination centralizes production routes (the debug route stays debug-only). Discover displays feature.discover.explore.ExploreRoute. No separate legacy Explore feature or destination remains. Preserve Route/Screen separation, tab state restoration, Search keyboard/back handling, debug exclusion from release, and shared production components in :core:designsystem. Do not introduce feature Gradle modules or generic navigation managers.

Feature package convention: :app owns feature packages with no presentation/ level. Keep Route, Screen, ViewModel, UiState and Intent together; optional Effect, component/, preview/, model/ or mapper/ organization must be justified. Domain and data stay application-level. Only code used by multiple screen flows belongs in feature/discover/shared. Preserve previewability, use-case boundaries, Koin composition, centralized navigation and design-system reuse. Validate once with :app:assembleDebug; target tests to changed business logic, avoiding broad suites, lint, instrumentation and release builds unless explicitly requested.
