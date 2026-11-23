# App-owned Splash and Google Login

The four Gradle modules are unchanged. Discover and Manage retain feature-owned business layers. Authentication is app-owned because it serves both experiences: app/auth/domain (backend-independent session contract and use cases), data (Firebase and Credential Manager), login (shared auth state/ViewModel and official Google button), app/profile (auth-to-Profile state projection), feature:discover/profile (previewable account UI), and di (app-composed definitions).

MainActivity installs AndroidX's native SplashScreen before super.onCreate. Both device modes use the same fixed #004C43 native starting theme and transparent white/yellow pin/cutlery vector traced from the supplied logo, then normal Theme.Tuklas. See branding/README.md for source and mask sizing. The splash stays only while the first local auth-state observation resolves; there is no timer, animation or Compose splash destination.

Both guests and existing Firebase sessions start at Discover. Discover owns the compact Profile tab and receives only its own UI model plus callbacks, mapped by app from the existing Activity-scoped auth state. Profile Sign In opens app-owned Login above the main customer shell; Login has a Back action and no customer tabs or Continue as Guest button. Successful authentication pops Login once and reveals the selected authenticated Profile; cancellation/failure remains on Login. Sign-out updates Profile in place through the existing app callback. No additional session observer, email/password, role checkbox or fake identity path is implemented.

The Google SDK SignInButton supplies branding; Credential Manager's GetSignInWithGoogleOption requests an ID token for the generated web client ID. Only app/auth/data exchanges it with Firebase. An AuthStateListener observes the persistent Firebase session and is detached on collection cancellation. Duplicate operations are guarded, picker dismissal is not an error, and raw SDK exceptions/tokens are never rendered or logged.

## Manual Firebase setup

The repository does not bundle google-services.json. A configured build requires a matching Android application and generated default_web_client_id for its web OAuth client. If that resource is missing, credential retrieval reports a configuration error. Guest startup remains available. Firebase provider enablement and registered signing certificates require manual console verification.

1. In your own Firebase project, register Android application com.lakbay.tuklas, including the debug/release signing SHA fingerprints used for each installed build.
2. Enable the Google sign-in provider and configure its required support email/OAuth settings.
3. Download that Android application's genuine google-services.json into app/. Keep it ignored. It must include the web OAuth client so Google Services generates default_web_client_id. Do not use the Android OAuth client ID as the server client ID.
4. Rebuild. Presence of that file enables Firebase Auth/Google Services independently of the Discover data backend. Discover remains mock unless -Ptuklastayo.useFirebase=true is explicitly passed. This preserves the existing repository switch.
5. On a user-prepared Android device with Google Play services and an account, manually verify picker cancel/success/error, process restart/session persistence, sign-out, Guest, Back, recreation, light/dark splash, fonts and insets. No live sign-in was performed by the agent.

Setup references: https://firebase.google.com/docs/auth/android/google-signin and https://developer.android.com/identity/sign-in/credential-manager-siwg-implementation . Native splash: https://developer.android.com/develop/ui/views/launch/splash-screen . Brand button: https://developers.google.com/android/reference/com/google/android/gms/common/SignInButton .

## Authorization and future protected returns

Google sign-in proves identity only. There is still no trusted restaurant role/permission source or backend access enforcement. Manage remains absent from the runtime root graph for guests and signed-in users. No client-supplied role, owner preference or debug mode can grant access. Future protected-destination login must retain its destination in navigation state and recheck trusted authorization before entry; there is currently no protected destination to resume. Do not add a Manage route as part of authentication testing.

## Verification scope

Focused JVM tests use fake AuthRepository implementations only in test sources, covering guest/session restore, success, cancellation, errors/retry, duplicate requests, sign-out and guest continuation. Profile light/dark previews are offline; the SDK-rendered Google button is represented by a label in preview mode only. An instrumented navigation test covers Profile tab restoration and guest browsing without opening the Google picker. Instrumentation source is not evidence of device execution. Firebase credential exchange, persistent sessions and trusted role enforcement require separate configured-device verification.
## Debug-only authentication diagnostics

Authentication Data implementations and LoginViewModel call Android Log directly, guarded by app BuildConfig.DEBUG, using tag `TuklasTayoAuth`. There are no logging interfaces, injected sinks or variant logger objects. Messages contain fixed checkpoints, safe error categories, sanitized exception types and allowlisted Firebase Auth codes. No error.message, causes, stack traces, identifiers, emails, tokens, web client IDs or raw credential payloads are logged. Cancellation is separate from failure. Session state is logged on initial restoration or signed-in/signed-out transitions, not repeated identical emissions. Release logging is disabled by the same build flag. Local JVM tests use default Android stub return values for Log while continuing to assert authentication behavior with fake repositories.

### Manual configuration checks

- Select the Firebase project corresponding to the existing local Android config; confirm Google is enabled under Authentication > Sign-in method and its support/OAuth settings are complete.
- Confirm the registered Android app is com.lakbay.tuklas. Add SHA-1 and SHA-256 for the signing certificate of the actual installed debug/release build. You can run `./gradlew :app:signingReport` manually; fingerprints are public certificate information, but do not share keystores, signing passwords or unrelated console output.
- Download a fresh google-services.json for that Android app after enabling Google, and replace app/google-services.json. Do not paste an Android client ID into default_web_client_id or fabricate OAuth values. The generated resource must represent an OAuth client of type Web application.
- Rebuild the debug app and install/run it manually on your already prepared device. Google Play services and a Google account must be available. No emulator/device is controlled by Codex.
- There are no google-services.json overrides under app/src. Current dependencies are Credential Manager + Play Services provider 1.5.0 and googleid 1.1.1. The explicit GetSignInWithGoogleOption permits first-time account selection; it does not filter to previously authorized accounts. Activity context is now explicitly unwrapped/validated before invoking Credential Manager.

### Logcat workflow

1. Manually select the running debug app/device in Android Studio Logcat.
2. Enter `tag:TuklasTayoAuth` in the Logcat query field.
3. Open Profile, tap Sign In, then tap the Google button on Login once, and complete or dismiss the account picker.
4. Follow the checkpoint groups below. Auth listener callbacks can interleave with task completion; the order is not a strict global sequence.

Expected successful milestones:

```text
Google sign-in requested
Credential Manager request started
Credential retrieval succeeded
Firebase authentication started
Firebase authentication succeeded
Signed-in session observed
```

Failure checkpoints and next checks:

| Checkpoint | Meaning / next check |
| --- | --- |
| Missing or invalid web OAuth configuration | Local/generated config is incomplete. Replace the downloaded config after checking Google provider/OAuth setup. No credential request has been made yet. |
| Credential retrieval failed: NoCredentialException | Provider returned no usable credential; check Google account, Play services and provider/device setup. This is not user cancellation. |
| Google sign-in cancelled | Normal picker dismissal, without an error banner or automatic retry. |
| GetCredentialProviderConfigurationException | Check provider dependencies/device Play services setup. |
| GetCredentialUnsupportedException / UNSUPPORTED_CREDENTIAL | Device/provider or returned credential type is unsupported. The Google parser is not called for another provider's credential. |
| Credential parsing failed / INVALID_TOKEN | Provider response could not produce a token. Logs show sanitized metadata, never the payload. |
| Firebase authentication failed | Firebase received an attempted exchange. Use its sanitized error code to narrow the issue: ERROR_OPERATION_NOT_ALLOWED suggests provider settings; ERROR_INVALID_CREDENTIAL suggests credential/project configuration; ERROR_NETWORK_REQUEST_FAILED suggests connectivity. These are clues, not remote configuration verification. |
| Login return skipped: no previous entry | The root graph could not pop Login. Normal successful returns are not logged. |
| Credential state clearing failed / timed out | Firebase sign-out has already occurred. Profile shows the signed-out session and a safe cleanup failure message. |

Login consumes its return event once before popping back to the main shell. Profile follows the single observed auth projection; sign-out restores Guest content in place. Manage remains absent from the root graph for both signed-in users and guests.

For further debugging, share only the lines with this tag from one attempt, from sign-in requested through success or failure. Include app version/build type, Android version and whether Play services/accounts are available. Review/redact any unexpected personal data or values before sharing. Do not share the entire Logcat, JSON file, tokens, emails, account IDs, OAuth client IDs or signing keys. Report which UI message appeared. No end-to-end sign-in success is claimed without a real-device test.
