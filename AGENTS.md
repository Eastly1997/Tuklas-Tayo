# TuklasTayo project rules

## Operating restrictions

- Never control the user's PC, activate Computer Use, use mouse/keyboard automation, or operate Android Studio or other desktop applications through a GUI.
- Work only inside the authorized TuklasTayo project workspace. Project-level commands are allowed when authorized and necessary. Do not modify files outside the project, change environment variables, change system settings, install software, or perform system-wide modifications. Check command side effects, including cache/toolchain writes, against this boundary.
- Do not initialize Git, stage files, create commits, configure remotes, push, deploy, or access external services without explicit permission. A skill's activation is not permission for those actions.
- Never expose secrets or credentials, bypass access controls, or run destructive commands without approval.
- If part of a task requires PC control or work outside these limits, stop that portion, explain the blocker, and give clear manual instructions. Continue only independent, permitted work.
- Never claim completion without verification; state exactly what was observed and what remains unverified.

## Architecture and organization

- Only :app and :core:designsystem are active Gradle modules. Features are Kotlin packages inside app; do not add feature modules, per-feature data/domain layers or generic MVI frameworks by default.
- Root package/application ID: com.lakbay.tuklas. App namespace: com.lakbay.tuklas.app. Match directories to packages in every source set. Branded identifiers use Tuklas with Kotlin casing; user-facing branding remains TuklasTayo. Ordinary feature/domain types need no branded prefix.
- Application-level domain/model, domain/repository and domain/usecase remain pure Kotlin without Android, Compose, Firebase, backend clients, Koin or data imports. Application-level data/dto and data/repository own backend models, mappings and mock/Firebase implementations. Preserve Presentation -> Domain <- Data.
- ViewModels call constructor-injected domain use cases, never repositories directly. Use cases depend on repository interfaces. No SDK types or DTOs in UI contracts. Use domain models directly when appropriate; optional UI models/mappers need meaningful display transformations.
- Features have no presentation/ level. Group Route, Screen, ViewModel, UiState, Intent and optional Effect in the screen package, with one primary responsibility per Kotlin file. Do not mandate contract/, model/ or mapper/ for small screens. Add component/, debug preview/ and other subpackages only when justified. Avoid empty packages, placeholder layers and file-per-helper fragmentation.
- Discover owns feature.discover.explore, feature.discover.search and genuinely reused code in feature.discover.shared. Saved and Place Detail use feature.saved and feature.place_detail. Discover remains the bottom tab opening ExploreRoute; never restore the removed legacy Explore feature.
- App-level di/AppModules.kt owns Koin composition and backend selection. Bind repositories to domain interfaces. Mock remains default, with fixtures injected only from debug/test sources and an empty release list. Preserve optional Firebase setup, DTO mapping and build flags; do not fabricate credentials or access live services without permission.
- Keep Kotlin, Compose-only Material 3, minimum SDK 26 and existing dependency versions. Use the version catalog and installed JDK 21; AGP 9 supplies Android Kotlin. Do not install toolchains.

## Compose, navigation and design system

- Follow MainActivity -> TuklasApp -> TuklasNavHost -> FeatureRoute -> FeatureScreen with one Activity and NavController. TuklasApp owns Scaffold, toolbar, tabs and insets; TuklasNavHost owns destinations and feature navigation callbacks; TuklasDestination centralizes production routes. Debug routes remain debug-only. No navigation managers or per-feature NavHosts.
- Routes resolve ViewModels through Koin, collect StateFlow with lifecycle awareness and wire intents/navigation. Screens accept immutable UiState and callbacks without ViewModel lookup, flow collection, backend work, or navigation/DI initialization. Local UI-only state is allowed. Keep navigation out of ViewModels and business logic out of composables.
- MVI uses immutable UiState, sealed Intent, read-only StateFlow and unidirectional updates. Add one-time effects only when needed; preserve structured concurrency and CancellationException. Stateless placeholders and local-state Gallery need no artificial runtime layers.
- Previews render actual production Screens in TuklasTheme with offline state. Include Light/Dark and supported loading/content/empty/error states for important screens. Keep previews, fixtures, photos and tooling debug-only; no Firebase, Koin, NavController, application initialization or live downloads. Never duplicate preview screen implementations.
- Every application-wide reusable component has one production implementation in :core:designsystem. Feature-specific components stay in features. Screens, previews and Gallery consume the same implementations and styling. Use Material 3 semantic tokens, accessible typography, shapes, spacing, touch targets and semantics. New shared components include suitable previews and Gallery demonstrations.
- Debug tabs: Discover/Saved/Debug. Release: Discover/Saved, with no Gallery implementation or destination. Preserve source-set hooks, tab restoration, Search keyboard/back handling, insets and development notice. Gallery Light/Dark/System and snackbar state stays local. Never add a separate Gallery launcher or Activity.

## Scope and lightweight validation

- Inspect ownership and indirect references before deleting. Preserve user changes, active functionality, intentional placeholders, useful tests, previews and operating rules. No unrelated redesign, formatting or speculative abstractions. Update relevant architecture documentation when conventions change.
- Default: implement -> one relevant debug compilation (:app:assembleDebug for app work) -> concise report -> stop. Target tests to changed business logic. No full suites, lint, instrumentation or release builds unless explicitly requested or necessary for a concrete issue. Investigate failures before justified retries; never repeat successful checks.
- Keep command caches, toolchain/output writes and external access within authorized limits. Prefer cached/offline execution; provide manual steps for blocked checks. Never claim CLI compilation proves IDE sync, preview rendering or device behavior.
- Never commit local.properties, google-services.json, credentials or signing keys. Saved and Place Detail remain placeholders; unrelated features need a separate request. Historical reports are not current verification evidence.
- Report summarized files/structure, exact validation command/result and blockers/manual checks concisely.

## Project skills

Use existing .agents/skills by primary task: tuklastayo-feature for feature work, tuklastayo-data for data work, tuklastayo-code-review for requested review, tuklastayo-testing for test-focused work. Ordinary implementation does not automatically activate review/testing skills. Update relevant existing skills without duplicates. Skills inherit operating restrictions and authorize no extra milestone, external access or Git action.