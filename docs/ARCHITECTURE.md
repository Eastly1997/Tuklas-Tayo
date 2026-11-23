# Architecture

Only :app and :core:designsystem are active Gradle modules. Application functionality is organized by Kotlin packages under com.lakbay.tuklas; module boundaries are not required for each feature.

## Ownership and dependency direction

- domain/model, domain/repository and domain/usecase contain business models, repository interfaces and concrete use cases. They use pure Kotlin without Android, Compose, Firebase, Koin or data imports.
- data/dto and data/repository own backend models, conversion and Firebase/mock repository implementations. Firebase remains replaceable behind PlaceRepository. Debug/test sources inject fictional fixtures; release supplies an empty mock list.
- feature/discover/explore owns the Discover landing screen (ExploreRoute/Screen/ViewModel), contracts and screen-specific components. Search has its own feature/discover/search flow. Genuinely reused cards, filtering and display mapping live in feature/discover/shared.
- feature/saved and feature/place_detail retain placeholders.
- di/AppModules.kt registers repositories, use cases and ViewModels and selects the optional Firebase backend. navigation/TuklasApp.kt owns the single NavController and shared Scaffold; TuklasNavHost.kt owns destinations and feature navigation callbacks. MainActivity and TuklasApplication live at the root package. Generated R/BuildConfig remain in the unchanged com.lakbay.tuklas.app namespace; application ID remains com.lakbay.tuklas.
- core:designsystem independently owns reusable Material 3 theme/tokens, components and Coil image UI. It has no business-model dependency.

Preserve Presentation -> Domain <- Data. ViewModels use constructor-injected use cases, never concrete repositories or SDK clients. Domain models may be used directly in presentation. FeaturedShopUiModel and its mapper remain because they format category labels, normalize optional fields and provide preview image-resource overrides; UI models are optional, not required duplicates of domain data.

## Rendering and state

Route -> Screen -> Components -> Preview remains mandatory. Routes resolve Koin ViewModels, collect StateFlow with lifecycle awareness and wire navigation/intent callbacks. Screens accept immutable state and callbacks without runtime lookup, repositories or flow collection. MVI contracts, ViewModels, rendering and runtime integration have separate responsibilities.

Discover opens ExploreRoute while retaining its Discover tab label. The Explore screen keeps category filtering, announcements and carousel state. Dedicated Search uses the same local matching and use case, preserves query through SavedStateHandle, and renders results above a bottom IME-aware input. Existing loading, error, retry and cancellation behavior is unchanged. No new algorithms or features were introduced.

## Variants, previews and reuse

Debug navigation registers Discover, Saved and Debug; release hooks register only Discover and Saved and no Gallery destination. Gallery, previews, sample photos and development fixtures remain in debug sources. Light/Dark/System Gallery state is local Compose state. Gallery and previews consume production screens/components and the shared Tuklas theme, without requiring Koin, Firebase or application initialization.

One Activity and application-level navigation remain mandatory. Shared styling has one production implementation in core:designsystem. Feature-specific business UI stays in app feature packages. No PC/GUI control, external-service access, staging, commits or pushes are authorized by this architecture.

Navigation follows MainActivity -> TuklasApp -> TuklasNavHost -> FeatureRoute -> FeatureScreen. TuklasApp owns the shared Scaffold, toolbar, bottom navigation and insets, using one NavController. TuklasNavHost owns destination declarations and feature navigation callbacks; TuklasDestination centralizes production routes (the debug route stays debug-only). Discover displays feature.discover.explore.ExploreRoute. No separate legacy Explore feature or destination remains. Preserve Route/Screen separation, tab state restoration, Search keyboard/back handling, debug exclusion from release, and shared production components in :core:designsystem. Do not introduce feature Gradle modules or generic navigation managers.

Feature package convention: :app owns feature packages with no presentation/ level. Keep Route, Screen, ViewModel, UiState and Intent together; optional Effect, component/, preview/, model/ or mapper/ organization must be justified. Domain and data stay application-level. Only code used by multiple screen flows belongs in feature/discover/shared. Preserve previewability, use-case boundaries, Koin composition, centralized navigation and design-system reuse. Validate once with :app:assembleDebug; target tests to changed business logic, avoiding broad suites, lint, instrumentation and release builds unless explicitly requested.
