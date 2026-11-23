# Canonical persisted discovery schema

The existing Firestore collection is `places`, keyed by document ID (mapped to Place.id). This refactor changes no fields, indexes, queries or remote data. Discover owns the current DTO and read mapping; future Manage persistence must remain compatible with these fields until a separately approved schema change.

| Field | Existing meaning / mapping |
| --- | --- |
| name | String; blank is rejected when mapping a document. |
| category | RESTAURANT, CAFE or LOCAL_BUSINESS. Unknown values map to LOCAL_BUSINESS; DTO default is LOCAL_BUSINESS. |
| address | String full address; retained without parsing into city. DTO default is empty. |
| description | String descriptive text used as an optional UI highlight; not independently verified specialty metadata. DTO default is empty. |
| imageUrl | Optional string image reference; absent defaults to null. |
| city | Optional structured city string; trimmed, blank maps to null. |
| featured | Boolean, DTO default false. The featured query requires true. |

Featured retrieval filters featured == true, orders by document ID and applies the requested limit. Lookup reads places/{id}; absent documents return null. Empty names/IDs or decoding errors fail rather than silently discarding data. Firebase failures propagate; ViewModels preserve coroutine cancellation.

There is currently no published-status, owner, draft, approval or authorization contract. Do not treat featured as publication/authorization. The existing app reads featured places only (including Search over that loaded list); do not add filtering fields or production migrations during this ownership refactor. Manage has no data implementation and no runtime entry until real authentication and verified restaurant authorization are implemented. Canonical management publication rules require a future explicit decision.

Mock implements the same limit/lookup interface using injected in-memory fixtures, with no simulated Firebase ownership/approval policy. Debug fixtures are fictional; release defaults to an empty list. No live backend access is needed for unit tests.