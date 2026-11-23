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

1. Trace the affected contract in `app/domain`, implementation in `app/data`, and binding in `app`. Read only relevant DTOs, mappings, callers, and tests.
2. Keep domain models and repository interfaces pure Kotlin and backend-independent, with ordinary unprefixed names (Place, PlaceRepository, use cases). Reserve the `Tuklas` prefix for branded application/design-system code as defined in AGENTS; preserve user-facing `TuklasTayo` text. Preserve contract semantics unless explicitly changed, keeping Firebase replaceable by REST.
3. Implement data access behind the domain interface with constructor-injected dependencies. Keep SDK-specific classes, transport DTOs, and conversion inside the data implementation. Map document IDs and malformed/unknown/optional fields deliberately; do not silently hide corrupt data or backend errors. Preserve cancellation.
4. Keep fictional fixtures in debug/test sources, separate from domain/use cases; app debug injects them into the mock repository and release injects an empty list. Do not hardcode fixtures in production data sources. Maintain the mock implementation when a shared contract changes. Change backend selection only in the app composition root and only when requested; mock remains default.
5. Add focused tests for changed mapping, boundary conditions, and repository semantics using fakes or existing test seams. Test offline behavior without contacting real services. Live integration tests require explicit service permission and real user-provided configuration; never create credentials or loosen rules to make tests pass.
6. Run relevant permitted CLI checks after checking workspace/cache and external-access limits. If live access or environment constraints block verification, report it and provide manual steps. Update backend/schema documentation only if its documented contract changes.

## Output and completion

Report contract/schema impact, files/modules changed, mapping/error decisions, mock compatibility, and exact checks with results. Completion requires data implementations satisfying the agreed domain contract, no SDK types leaking into feature/domain APIs, and targeted verification or clearly identified unverified integration behavior. Do not modify unrelated UI or migrate production data.

## Consolidated architecture

Only :app and :core:designsystem are active modules. Organize features by Kotlin packages in app; domain/model, domain/repository and domain/usecase remain backend-independent, while data/dto and data/repository own Firebase/mock implementations. Presentation -> Domain <- Data remains mandatory. ViewModels call use cases; domain models can be used directly in UiState. Retain UI models only for meaningful presentation transformations. Keep one Activity, app-level navigation/Koin, Route -> Screen -> Components -> Preview, MVI, debug-only Gallery and the production design-system single source of truth. No PC control or Git mutations without authorization.
