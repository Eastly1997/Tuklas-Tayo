# Architecture

## Boundaries and dependency direction

App is the composition root: TuklasApplication starts Koin; MainActivity applies
TuklasTheme and renders TuklasApp with one NavController. App owns navigation and
selects mock or optional Firestore bindings.

- core:domain: pure Kotlin Place/PlaceCategory, PlaceRepository and concrete use
  cases. No Android, Compose, backend SDK, network client or DI dependencies.
- core:data-mock and core:data-firebase: implement PlaceRepository and depend on
  domain. DTO/backend conversion stays in the data implementation. The mock
  repository accepts a fixture list; app debug sources supply fictional places,
  release supplies none. Core mock test fixtures are test-only.
- core:designsystem: reusable general-purpose Compose components, Material 3
  theme/tokens and Coil image handling. No shop/business models.
- feature modules: depend on domain and designsystem, never concrete data modules.
  Discover owns shop-specific UI models, mapping and card/section components.
- app: assembles domain/data and feature-owned DI modules; contains Saved until an
  actual feature justifies a module. Existing module names remain unchanged.

A future REST implementation replaces the data binding behind PlaceRepository;
use cases/ViewModels remain backend-independent. SDK types never enter domain
or presentation contracts.

## Runtime and rendering

Route -> Screen -> Components is the UI boundary.

DiscoverRoute resolves DiscoverViewModel through Koin, collects StateFlow using
collectAsStateWithLifecycle and passes intents/navigation callbacks.
DiscoverScreen renders immutable UiState and callbacks without runtime lookup,
repository work or flow collection. Local UI-only state, such as scroll state,
is allowed. Navigation callbacks reach app; NavController never enters a ViewModel.

DiscoverViewModel orchestrates GetFeaturedPlacesUseCase and exposes a read-only
StateFlow with Loading, Content (including empty) and Error. Retry ignores active
duplicate loads; errors are retryable and CancellationException propagates.
Search filters loaded data locally and suppresses featured cards until cleared.
No one-time effect or generic MVI base is needed.

Domain-to-presentation transformations live in presentation/mapper; display-only
models live in presentation/model. UiState/Intent live in presentation/contract.
Optional structured city is mapped without address parsing; full address remains
in domain data. Gallery/previews consume the actual production screen/components.

Explore, Place Detail and Saved currently render placeholders from explicit
display state. They have no artificial ViewModel/use-case/repository layers.
Their existing public route entry points are preserved.

## Design system and development tooling

Shared Tuklas components are the single source of styling for all consumers.
Material 3 semantic roles and Tuklas spacing/shapes/typography supply tokens.
Feature-specific cards stay in Discover rather than introducing business
dependencies into designsystem.

Debug bottom navigation adds the in-app Gallery through a source-set hook.
Release supplies a hook with no tab or route and has no Gallery class references.
The Gallery uses local Light/Dark/System and snackbar state, not a ViewModel.
Previews and fictional fixtures/resources live in debug source sets; preview
functions wrap production Screens in TuklasTheme and need no app initialization.
Photo credits remain alongside debug resources.

Known limitations and setup live in README.md; this document describes ownership,
not a claim that planned screens or live services have been implemented.