# TuklasTayo project rules

## Operating restrictions

- Never control the user's PC, activate Computer Use, use mouse/keyboard automation, or operate Android Studio or other desktop applications through a GUI.
- Work only inside the authorized TuklasTayo project workspace. Project-level commands are allowed when authorized and necessary. Do not modify files outside the project, change environment variables, change system settings, install software, or perform system-wide modifications. Check command side effects, including cache/toolchain writes, against this boundary.
- Do not initialize Git, stage files, create commits, configure remotes, push, deploy, or access external services without explicit permission. A skill's activation is not permission for those actions.
- Never expose secrets or credentials, bypass access controls, or run destructive commands without approval.
- If part of a task requires PC control or work outside these limits, stop that portion, explain the blocker, and give clear manual instructions. Continue only independent, permitted work.
- Never claim completion without verification; state exactly what was observed and what remains unverified.

## Architecture and development

- Use `com.lakbay.tuklas` as the root package and application ID; the app namespace is `com.lakbay.tuklas.app`. Preserve core/feature suffixes and match Kotlin/Java directories to packages in every source set. Do not conflate the application ID with the code namespace.
- Use `Tuklas` for branded types, composables and reusable components (`TuklasApplication`, `TuklasApp`, `TuklasTheme`, `TuklasButton`). Types/composables use PascalCase; functions/properties use camelCase (`tuklasTypography`, `tuklasShapes`, color-scheme values). Preserve `TuklasTayo` in user-facing product labels, marketing and project display names. Do not brand-prefix ordinary feature/domain classes. Keep Gradle module names and existing skill invocation names unchanged.
- Use Kotlin, Compose-only UI, Material 3, one MainActivity in all builds, and minimum SDK 26. Debug builds use Discover/Saved/Debug bottom tabs; release uses Discover/Saved. The Gallery is an in-app debug-only destination, never a separate Activity or launcher. Prefer readable, maintainable code over premature abstraction.
- Clean Architecture separates presentation, domain, and data: screen -> MVI ViewModel -> concrete use case -> repository interface -> data implementation.
- `core:domain` is pure Kotlin/JVM: no Android, UI/Compose, Firebase, Retrofit/network clients, or Koin/DI dependencies. Use cases depend on domain repository interfaces.
- `core:data-mock` and `core:data-firebase` implement domain contracts. Keep DTO/backend mapping in data modules; inject fictional fixtures only from debug/test source sets; never expose SDK types through domain or feature APIs. Firebase must remain replaceable by a REST implementation without changing use cases or ViewModels.
- Features depend on domain and `core:designsystem`, never concrete data modules or backend clients. Respect Gradle module boundaries; no circular dependencies.
- `app` owns navigation, Koin module assembly, and backend selection. Feature-owned DI declarations live in that feature's `di` package; app assembles them with domain/data bindings. Use constructor injection and bind repositories to domain interfaces. `core:designsystem` owns shared Compose themes/components and Coil image UI.
- MVI uses immutable UiState, sealed Intent, read-only StateFlow, lifecycle-aware collection, and unidirectional updates. Add one-time effects only when needed. Stateless placeholders do not need empty ViewModels.
- Keep business logic out of composables and navigation callbacks in routes/app; never put NavController in ViewModels. Use suspend functions, Coroutines and Flow; preserve CancellationException.
- Avoid unnecessary base classes, generic MVI frameworks, and unrelated abstractions. Use stable compatible dependencies through `gradle/libs.versions.toml`; AGP 9 supplies Kotlin, so do not apply `org.jetbrains.kotlin.android`. Use the installed JDK 21; do not install a toolchain.

## Kotlin files and feature packages

- Give each Kotlin source file one primary responsibility. For MVI features, separate `FeatureScreen.kt` (rendering), `FeatureViewModel.kt` (state orchestration/intent handling), `FeatureUiState.kt` (immutable state), `FeatureIntent.kt` (user actions), optional `FeatureEffect.kt` (one-time effects), and `FeatureModule.kt` (DI declarations). Never combine Screen, ViewModel, UiState, Intent, and Effect responsibilities in one file. Route runtime integration belongs in its own file for lifecycle collection, ViewModel resolution and navigation callbacks; omit unnecessary runtime layers for stateless placeholders.
- Use feature-first packages following the existing module/package prefix. Discover is the reference: `feature/discover/.../discover/presentation/` holds screen/route/ViewModel, `presentation/contract/` holds UiState/Intent and any necessary Effect, and `di/` holds `DiscoverModule.kt`.
- Future features use presentation/ for Route, Screen and ViewModel; presentation/contract/ for UiState, Intent and optional Effect; presentation/model/ for presentation-only UI models; presentation/mapper/ for domain-to-presentation transformations; presentation/component/ for feature-specific composables; presentation/preview/ for larger fixtures and dedicated previews when useful; and di/ for dependency injection declarations. Match directories to package declarations in each source set. Keep preview code/fixtures debug-only.
- Keep small private helpers and tightly related private implementation details with their owner. Do not split every small function or introduce deeper nesting, unused Effect files, placeholder classes or modules for symmetry. Apply these conventions to affected work; do not reorganize unrelated features without a scoped request.
- ViewModels orchestrate immutable StateFlow state through domain use cases, not concrete data implementations. Keep rendering in composables, DTO/backend mapping in data, domain-to-presentation mapping in presentation/mapper, and framework/UI/navigation dependencies out of domain and presentation contracts. Preserve structured concurrency and cancellation when moving code.

## Preview-friendly screen architecture

- Compose features must follow Route -> Screen -> Reusable Components, with previews consuming the actual production Screen. Routes own runtime integration: resolve existing ViewModels through Koin, collect StateFlow with collectAsStateWithLifecycle, wire intents and navigation callbacks, and keep rendering minimal.
- Screens accept immutable UiState and callbacks. They must render independently without obtaining ViewModels, collecting ViewModel flows, initializing Koin/Firebase/navigation, or invoking repositories/use cases. Local UI-only state is appropriate.
- Keep rendering, runtime integration, state/intent/effect contracts and preview responsibilities in separate files. Prefer presentation/FeatureRoute.kt, FeatureScreen.kt, FeatureViewModel.kt; contract/ for immutable contracts; component/ for feature-specific components; preview/ for preview data and declarations. Preserve established public route entry points when moving them would add unnecessary churn.
- Add TuklasTheme-wrapped Light/Dark previews for important screens, plus loading/content/empty/error states when the existing contract supports them. Use offline sample UiState; providers are optional. Never duplicate screen implementations for previews or add business states solely for previews.
- Keep preview declarations and fictional fixtures in debug source sets with debug-only tooling dependencies. Previews must require no application initialization, backend, DI, NavController or live image downloads. A themed Surface may supply the screen background; application Scaffold chrome remains app-owned.
- Application-wide reusable components stay in core:designsystem; feature-specific components stay in their feature. Screens, previews and Gallery consume the same production implementations and styling.
- Stateless placeholders and the local-state debug Gallery need no ViewModel, effect, repository, or extra Route solely for symmetry. Preserve existing MVI transitions and navigation behavior during refactors.

## Scope, quality, and configuration

- Inspect affected files and applicable guidance before editing. Preserve user changes and existing functionality; do not modify unrelated files. Update README only when the task changes documented architecture, build steps, or backend setup.
- Add focused tests for affected behavior: fake repositories for domain, mapping/contract tests for data, coroutine tests for MVI transitions. Documentation-only work needs documentation validation, not application changes or new app tests.
- Default to implement -> compile affected scope -> report concisely -> stop. Prefer `:app:assembleDebug` for debug UI. Check release compilation only when variant-specific changes require it. Run targeted tests only for changed business logic or tested behavior; no full suites, lint, instrumentation or multiple variants unless requested or necessary for a specific issue. Do not repeat successful checks; investigate a relevant failure before retrying. Device tests require an authorized, already user-prepared device; never start/configure emulators or automate a GUI. Do not claim IDE sync from a CLI build.
- Verify commands can respect workspace and external-access restrictions before running them; otherwise report the limitation and provide manual commands. Report exact commands/results, distinguishing passed, failed, and not run checks. Do not treat old results as a current run.
- Mock is the default backend. Never fabricate Firebase configuration or commit `google-services.json`, credentials, signing keys, or `local.properties`. Real backend access requires explicit permission.
- Discover, Featured Shops and the design-system Gallery are implemented; Saved, Explore and Place Detail remain placeholders. Storage, authentication, security rules, uploads, maps, location permissions and synchronization require a separate scoped request. Preserve the implemented UI during cleanup.

## Reusable component rule

- Every reusable general-purpose UI component has one production implementation in `core:designsystem`. Feature screens, component previews, and the debug Component Gallery consume that same implementation. The Gallery is a consumer, never a second design system.
- Shared styling must use Material 3 semantic tokens and accessible typography, shapes, spacing, touch targets and interaction semantics. Changes propagate to consumers through the shared implementation and normal Compose dependencies/recomposition; never copy tokens or styles to synchronize modules.
- When creating a shared component: implement it in `core:designsystem` using Material 3 and TuklasTayo theme tokens; add relevant Compose previews; demonstrate its variants/states in the debug Gallery; use it in features wherever it replaces an established equivalent. Do not wrap unrelated Material controls without a shared customization need.
- When modifying a component: update its single implementation, preserve its public API where practical, update previews/Gallery when supported variants or APIs change, and verify affected consumers compile. Never duplicate styling to synchronize consumers.
- Keep Gallery implementations/resources in `app/src/debug` and component previews in designsystem debug sources. Integrate navigation using matching debug/release source-set hooks: debug registers its tab/destination; release registers neither and contains no concrete Gallery references. Shared navigation must not import debug-only classes. The Gallery uses local Compose state, not business-layer dependencies/MVI. Its Light/Dark/System choice applies to Gallery content, while app navigation/system chrome follows the app theme.

- Shop-specific presentation components and models may live in their feature module. The debug Gallery and previews must consume those same production composables; generic image loading remains in core:designsystem. Development shop fixtures/photos must stay in debug/test source sets, never production data sources.

- Keep completion reports concise: summarized changed files, main behavior/structure, actual validation command/result and blockers/manual actions. Keep historical reports distinct from current implementation and checks.

## Project skills

Use `.agents/skills/` according to the primary request: `tuklastayo-feature` for feature implementation, `tuklastayo-data` for data-layer work, `tuklastayo-code-review` for requested review, and `tuklastayo-testing` for test-focused work. Ordinary feature/data changes include their focused tests without automatically invoking the review/testing skills. A multi-part request may use more than one skill only for the relevant portions. Skills inherit these restrictions and never authorize a new milestone or unrelated edits.
