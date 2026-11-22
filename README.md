# TuklasTayo

TuklasTayo is an Android local-business discovery application, initially focused
on Malolos, Bulacan. The current development baseline implements Discover and a
reusable Material 3 design system. Debug data is fictional and clearly labeled;
no verified business database is bundled.

## Current status

Implemented:
- Single Activity with Navigation Compose. Debug tabs: Discover, Saved, Debug.
  Release tabs: Discover and Saved. Explore and Place Detail are nested routes.
- Discover loading/error/retry, local search over the loaded featured-place list,
  Featured Shops photo carousel, and existing Explore Shops rows.
- Photo cards with bottom gradients, category/city metadata, optional highlights,
  and ID callbacks to the existing detail placeholder.
- Shared Tuklas theme, buttons, cards, chips, text fields, image loading/fallbacks,
  debug Component Gallery and production-screen previews.
- Optional Firestore repository and pure Kotlin mock repository behind one contract.

Saved, Explore and Place Detail are placeholders. Authentication, maps, device
location, uploads, storage and production synchronization are not implemented.
Search is not a complete business-catalog search. Firebase live connectivity has
not been verified.

Known follow-up: debug fixture photo URIs use drawable names, while Coil 3.3's
resource fetcher expects numeric resource IDs. Discover may show fallbacks for
those samples. Gallery previews supply resource IDs directly. This cleanup does
not change image loading or backend behavior. Device rendering and accessibility
still require manual verification.

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
The application ID/root package is com.lakbay.tuklas. App code namespace is
com.lakbay.tuklas.app. Branded code uses Tuklas; product branding stays TuklasTayo.

## Modules and architecture

| Module | Responsibility |
| --- | --- |
| :app | MainActivity, Koin startup, navigation, backend selection, Saved placeholder, debug Gallery and fixtures |
| :core:domain | Pure Kotlin Place model, repository interface and use cases |
| :core:data-mock | Pure Kotlin in-memory repository; empty unless fixtures are injected |
| :core:data-firebase | Firestore DTOs, mapping and repository implementation |
| :core:designsystem | Material 3 theme/tokens, shared components, Coil image UI and component previews |
| :feature:discover | MVI state, route/screen, UI models/mappers and shop-specific components |
| :feature:explore | Previewable placeholder screen and route |
| :feature:place-detail | Previewable placeholder screen and route receiving an ID |

Flow: Route -> Screen -> ViewModel intents -> UseCase -> Repository interface ->
data implementation. Screens render state; routes connect runtime dependencies
and callbacks. App assembles Koin modules. Features never depend on concrete data
modules; domain never depends on Android, Firebase, Compose or Koin.

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

Mock is the default. App debug sources inject fictional development places;
release injects an empty list. No fictional business fixtures or photos are
embedded in production data sources. Repository lookup/limit semantics and
cancellation are preserved.

To configure Firebase manually later:
1. Register an Android application with ID com.lakbay.tuklas in your own Firebase
   project. Place its genuine google-services.json in app/; it remains ignored.
2. Build with -Ptuklastayo.useFirebase=true. Only this opt-in applies Google
   Services and selects the lazy Firestore bindings. Missing JSON fails the build.
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

Screen previews use actual production Screens in TuklasTheme without Koin,
Firebase or ViewModels. Discover covers loading/content/empty/error/search.
Saved, Explore, Place Detail and Gallery have light/dark previews. Featured-card
examples include long/missing text, image fallback and increased font scale.
Select the debug variant and render *ScreenPreview.kt / card previews manually.
Stock photograph sources are recorded in app/src/debug/PHOTO_CREDITS.md.

## Targeted verification

Choose only checks relevant to the change; these are examples, not a full suite:

```powershell
.\gradlew.bat :feature:discover:compileDebugKotlin
.\gradlew.bat :feature:discover:testDebugUnitTest --tests '*DiscoverViewModelTest'
.\gradlew.bat :feature:discover:testDebugUnitTest --tests '*FeaturedShopMapperTest'
.\gradlew.bat :core:data-firebase:testDebugUnitTest --tests '*PlaceDtoTest'
.\gradlew.bat :core:data-mock:test --tests '*MockPlaceRepositoryTest'
.\gradlew.bat :core:domain:test --tests '*PlaceUseCasesTest'
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
Keep changes scoped, preserve module names, use semantic theme tokens and reuse
production components. No unrelated refactors, dependency upgrades or features.

.gitignore excludes caches/build output, IDE/local SDK state and common credential
files while retaining wrapper scripts/JAR, source/resources/tests, Gradle configs,
docs and project-local skills. Ignore rules are not a secret scanner: inspect the
candidate diff before any approved initial commit.

No Git repository was initialized by this cleanup. Do not initialize, stage,
commit, configure remotes or push without explicit approval. MILESTONE_02_REPORT.txt
is retained as a historical record; its obsolete paths/names are not active
instructions. Local absolute project paths in that report were sanitized.