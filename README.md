# TuklasTayo

TuklasTayo is an Android local-business discovery application, initially focused
on Malolos, Bulacan. The current development baseline implements Discover and a
reusable Material 3 design system. Debug data is fictional and clearly labeled;
no verified business database is bundled.

## Current status

Implemented:
- Single Activity with Navigation Compose. Debug tabs: Discover, Saved, Profile, Debug.
  Release tabs: Discover, Saved and Profile. Search and Place Detail are nested routes; Search hides bottom navigation.
- Discover loading/error/retry, a dedicated Search route over the loaded featured-place list,
  centered Featured Shops photo carousel, and existing Explore Shops rows.
- Photo cards with bottom gradients, category/city metadata, optional highlights,
  and ID callbacks to the existing detail placeholder.
- Shared Tuklas theme, buttons, cards, chips, text fields, image loading/fallbacks,
  debug Component Gallery and production-screen previews.
- Optional Firestore repository and pure Kotlin mock repository behind one contract.

Saved and Place Detail are placeholders. Maps, device
location, uploads, storage and production synchronization are not implemented.
Search is not a complete business-catalog search. Discover uses a non-editable search trigger; the Search route owns query/results in a SavedStateHandle-backed ViewModel. Its bottom input consumes remaining IME insets inside the app Scaffold, with no custom Back interception. The initial focus request is saved per navigation entry to avoid reopening the keyboard after Details or intentional dismissal. Firebase live connectivity has
not been verified.

Debug fixtures use numeric drawable resource IDs in local image URIs. Gallery previews also supply resource IDs directly. Device rendering and accessibility still require manual verification.

## Technology

- Kotlin 2.2.10; Android Gradle Plugin 9.0.1 with built-in Android Kotlin
- Gradle wrapper 9.2.1; JDK 21
- Compile/target SDK 36, minimum SDK 26
- Compose BOM 2025.08.01, Material 3, Navigation Compose 2.9.3
- Lifecycle 2.9.3, Koin 4.1.0, Coroutines 1.10.2
- Coil Compose and OkHttp transport 3.3.0
- Firebase BOM 34.1.0 / Firestore (optional backend)
- JUnit 4, coroutine tests and Compose UI instrumentation test sources

Pinned versions are in gradle/libs.versions.toml; no claim of latest versions.
The application ID/root package is com.lakbay.tuklas. App generated-code namespace is
com.lakbay.tuklas.app. Branded code uses Tuklas; product branding stays TuklasTayo.

## Modules and architecture

| Module | Responsibility |
| --- | --- |
| :app | Activity/Application, root navigation, app-owned Google authentication and Koin/backend composition |
| :feature:discover | Customer Domain/Data/Presentation, use cases, repositories, DI, navigation, Gallery and fixtures |
| :feature:manage | Independent management experience; currently dashboard/navigation Presentation only |
| :core:designsystem | Shared production components and theme |
Flow: Route -> Screen -> ViewModel intents -> UseCase -> Repository interface ->
data implementation. Screens render state; routes connect runtime dependencies
and callbacks. App assembles Koin modules. Feature presentation never imports concrete data implementations; domain never depends on Android, Firebase, Compose or Koin.

See docs/ARCHITECTURE.md for boundaries and docs/DEVELOPMENT.md for conventions.

## Local setup, build and run

Use an existing JDK 21, Android SDK 36 and Build Tools 36.0.0. Configure the SDK
location locally in ignored local.properties (or through your own Android Studio
setup). Do not commit machine paths. The wrapper and daemon criteria pin Gradle
and JDK versions; absent toolchains/dependencies may trigger downloads during a
normal developer build.

From the project root on Windows:

```powershell
.\gradlew.bat :app:assembleDebug
```

On macOS/Linux use ./gradlew with the same task. The debug APK is
app/build/outputs/apk/debug/app-debug.apk. To run manually, open the project in
Android Studio, sync, select the app run configuration and an already configured
device, then Run. IDE/device interaction is a manual action, not agent automation.

Agent runs must respect AGENTS.md: installed tools only, authorized downloads
only, project-local caches/temp/output, and no environment-variable changes.
An existing ignored local Gradle validation helper may be used on this machine;
it is not part of the portable setup and is not required by contributors.

## Mock and optional Firebase

Mock is the default. App composition injects Discover debug development places;
release injects an empty list. No fictional business fixtures or photos are
embedded in production data sources. Repository lookup/limit semantics and
cancellation are preserved.

To configure Firebase manually later:
1. Register an Android application with ID com.lakbay.tuklas in your own Firebase
   project. Place its genuine google-services.json in app/; it remains ignored.
2. Build with -Ptuklastayo.useFirebase=true. This selects the lazy Firestore bindings. Google Services is also enabled by app/google-services.json for authentication. Missing JSON fails the build.
3. Firestore documents live in places/{id}. Fields: name, category (RESTAURANT,
   CAFE, LOCAL_BUSINESS), address, description; optional imageUrl and structured
   city; boolean featured. Document ID becomes Place.id. Unknown categories fall
   back to LOCAL_BUSINESS; blank names/IDs are rejected. Featured queries filter
   featured == true, order by document ID and honor the requested limit.
4. Configure access controls/indexes separately; no credentials or permissive
   security rules are supplied here. No remote setup is performed by this project.
5. Remove the opt-in or pass -Ptuklastayo.useFirebase=false to return to mock.
   Merely supplying JSON does not switch the backend.

The card shows category and optional city, never a parsed/full street address.
Place.address remains intact for future details. Repository calls stay behind
domain use cases; individual cards request images only, not business records.

## Debug Gallery and previews

The normal debug app's Debug tab opens the Component Gallery; there is no separate
launcher Activity. It demonstrates semantic colors, typography, shapes/spacing,
shared components, reference controls/progress, snackbar interactions, and the
production Featured Shop components. Light/Dark/System selection is local UI state.
Release hooks register neither the Debug tab nor destination; Gallery code,
fixtures, photos and preview tooling are debug-only.

FeaturedShopsSection / FeaturedShopsCarousel accept autoScrollEnabled (default true)
and autoScrollIntervalMillis (default 6000). Cards use viewport minus 48dp (600dp
maximum), centered snapping, and 12dp spacing. Forward advancement animates for
450ms; last-to-first resets directly. A heading accessibility action pauses/resumes without a visible button. Timers
stop on touch/drag/fling, non-resumed lifecycle, off-screen composition/visibility,
and disabled system motion. Countdown restarts on reactivation/settling.
Discover hoists saveable carousel position, selected shop ID and pause state.
Static Gallery examples disable autoplay; one interactive example enables it.

Screen previews use actual production Screens in TuklasTheme without Koin,
Firebase or ViewModels. Discover covers loading/content/empty/error/search.
Saved, Place Detail and Gallery have light/dark previews. Featured-card
examples include long/missing text, image fallback and increased font scale.
Select the debug variant and render *ScreenPreview.kt / card previews manually.
Stock photograph sources are recorded in feature/discover/src/debug/PHOTO_CREDITS.md.

## Targeted verification

Choose only checks relevant to the change; these are examples, not a full suite:

```powershell
.\gradlew.bat :app:compileDebugKotlin
.\gradlew.bat :feature:discover:testDebugUnitTest --tests '*ExploreViewModelTest'
.\gradlew.bat :feature:discover:testDebugUnitTest --tests '*FeaturedShopMapperTest'
.\gradlew.bat :feature:discover:testDebugUnitTest --tests '*PlaceDtoTest'
.\gradlew.bat :feature:discover:testDebugUnitTest --tests '*MockPlaceRepositoryTest'
.\gradlew.bat :feature:discover:testDebugUnitTest --tests '*PlaceUseCasesTest'
```

Unit tests cover use cases, DTO/presentation mapping, mock behavior and Discover
state transitions. Instrumentation sources cover components/navigation, but
compilation is not device execution. Run device tests only with explicit
authorization and an already user-prepared device. Never start an emulator or
automate desktop UI as part of agent validation.

Current cleanup validation is limited to :app:assembleDebug and static checks;
older milestone reports are historical evidence, not current test results.

## Contributions and repository preparation

Follow AGENTS.md, docs/DEVELOPMENT.md and the existing .agents/skills workflows.
Keep changes scoped, preserve the experience/core module boundaries, use semantic theme tokens and reuse
production components. No unrelated refactors, dependency upgrades or features.

.gitignore excludes caches/build output, IDE/local SDK state and common credential
files while retaining wrapper scripts/JAR, source/resources/tests, Gradle configs,
docs and project-local skills. Ignore rules are not a secret scanner: inspect the
candidate diff before any approved initial commit.

Git history is preserved. Do not initialize, stage,
commit, configure remotes or push without explicit approval. MILESTONE_02_REPORT.txt
is retained as a historical record; its obsolete paths/names are not active
instructions. Local absolute project paths in that report were sanitized.
Dedicated Search reuses the same local matching helper and GetFeaturedPlacesUseCase
as the existing Discover list. No full-catalog API or structured specialty/food
keyword fields currently exist; none are fabricated. The existing inline rows
were consolidated into one production ExploreShopCard, shared by Discover and
Search, showing the provided image/name/category/city/highlight.
The app shell owns Search Places / Back chrome; SearchScreen owns the results and
bottom input. Actual IME movement, system Back, TalkBack and focus restoration
require manual device verification. Previews remain offline/debug-only.



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
App-owned native Splash and Profile-based Google sign-in are implemented. Discover remains available to guests; Manage is still blocked pending trusted restaurant authorization. See docs/AUTHENTICATION.md for setup, behavior and manual verification. No real Firebase configuration is bundled.
