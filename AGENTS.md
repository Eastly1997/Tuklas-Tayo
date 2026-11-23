# TuklasTayo project rules

## Operating restrictions

- Never control the user's PC, activate Computer Use, use mouse/keyboard automation, or operate Android Studio or other desktop applications through a GUI.
- Work only inside the authorized TuklasTayo project workspace. Project-level commands are allowed when authorized and necessary. Do not modify files outside the project, change environment variables, change system settings, install software, or perform system-wide modifications. Check command side effects, including cache/toolchain writes, against this boundary.
- Do not initialize Git, stage files, create commits, configure remotes, push, deploy, or access external services without explicit permission. A skill's activation is not permission for those actions.
- Never expose secrets or credentials, bypass access controls, or run destructive commands without approval.
- If part of a task requires PC control or work outside these limits, stop that portion, explain the blocker, and give clear manual instructions. Continue only independent, permitted work.
- Never claim completion without verification; state exactly what was observed and what remains unverified.

## Architecture and organization

- Root package/application ID: com.lakbay.tuklas. App namespace: com.lakbay.tuklas.app. Match directories to packages in every source set. Branded identifiers use Tuklas with Kotlin casing; user-facing branding remains TuklasTayo. Ordinary feature/domain types need no branded prefix.
- ViewModels call constructor-injected domain use cases, never repositories directly. Use cases depend on repository interfaces. No SDK types or DTOs in UI contracts. Use domain models directly when appropriate; optional UI models/mappers need meaningful display transformations.
- Features have no presentation/ level. Group Route, Screen, ViewModel, UiState, Intent and optional Effect in the screen package, with one primary responsibility per Kotlin file. Do not mandate contract/, model/ or mapper/ for small screens. Add component/, debug preview/ and other subpackages only when justified. Avoid empty packages, placeholder layers and file-per-helper fragmentation.
- App-level di/AppModules.kt composes feature DI; each feature owns repository/use-case/ViewModel definitions. App supplies backend configuration and initializes Koin. Mock remains default; preserve debug-only fixtures and Firebase configuration without fabricating credentials or live access.
- Keep Kotlin, Compose-only Material 3, minimum SDK 26 and existing dependency versions. Use the version catalog and installed JDK 21; AGP 9 supplies Android Kotlin. Do not install toolchains.

## Compose, navigation and design system

- Routes resolve ViewModels through Koin, collect StateFlow with lifecycle awareness and wire intents/navigation. Screens accept immutable UiState and callbacks without ViewModel lookup, flow collection, backend work, or navigation/DI initialization. Local UI-only state is allowed. Keep navigation out of ViewModels and business logic out of composables.
- MVI uses immutable UiState, sealed Intent, read-only StateFlow and unidirectional updates. Add one-time effects only when needed; preserve structured concurrency and CancellationException. Stateless placeholders and local-state Gallery need no artificial runtime layers.
- Previews render actual production Screens in TuklasTheme with offline state. Include Light/Dark and supported loading/content/empty/error states for important screens. Keep previews, fixtures, photos and tooling debug-only; no Firebase, Koin, NavController, application initialization or live downloads. Never duplicate preview screen implementations.
- Every application-wide reusable component has one production implementation in :core:designsystem. Feature-specific components stay in features. Screens, previews and Gallery consume the same implementations and styling. Use Material 3 semantic tokens, accessible typography, shapes, spacing, touch targets and semantics. New shared components include suitable previews and Gallery demonstrations.
- Debug tabs: Discover/Saved/Profile/Debug. Release: Discover/Saved/Profile, with no Gallery implementation or destination. Preserve source-set hooks, tab restoration, Search keyboard/back handling, insets and development notice. Gallery Light/Dark/System and snackbar state stays local. Never add a separate Gallery launcher or Activity.

## Scope and lightweight validation

- Inspect ownership and indirect references before deleting. Preserve user changes, active functionality, intentional placeholders, useful tests, previews and operating rules. No unrelated redesign, formatting or speculative abstractions. Update relevant architecture documentation when conventions change.
- Default: implement -> one relevant debug compilation (:app:assembleDebug for app work) -> concise report -> stop. Target tests to changed business logic. No full suites, lint, instrumentation or release builds unless explicitly requested or necessary for a concrete issue. Investigate failures before justified retries; never repeat successful checks.
- Keep command caches, toolchain/output writes and external access within authorized limits. Prefer cached/offline execution; provide manual steps for blocked checks. Never claim CLI compilation proves IDE sync, preview rendering or device behavior.
- Never commit local.properties, google-services.json, credentials or signing keys. Saved and Place Detail remain placeholders; unrelated features need a separate request. Historical reports are not current verification evidence.
- Report summarized files/structure, exact validation command/result and blockers/manual checks concisely.

## Project skills

Use existing .agents/skills by primary task: tuklastayo-feature for feature work, tuklastayo-data for data work, tuklastayo-code-review for requested review, tuklastayo-testing for test-focused work. Ordinary implementation does not automatically activate review/testing skills. Update relevant existing skills without duplicates. Skills inherit operating restrictions and authorize no extra milestone, external access or Git action.

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
- Profile UI/state/avatar/previews belong to :feature:discover; app maps its single observed auth state into ProfileUiState and supplies sign-in/sign-out callbacks. No app auth type, Firebase or Credential Manager dependency may enter Profile. Login UI/auth implementation and root navigation belong to :app. Profile Sign In opens root Login without customer tabs; Back/success restore the selected Profile, and sign-out updates Profile in place. No composable Profile content slot or parallel auth state store. Keep Manage blocked without trusted authorization.
