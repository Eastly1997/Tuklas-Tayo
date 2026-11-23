package com.lakbay.tuklas.auth.data

import com.lakbay.tuklas.auth.AUTH_LOG_TAG

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.*
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import android.util.Log
import com.lakbay.tuklas.app.BuildConfig
import com.lakbay.tuklas.auth.domain.*
import kotlinx.coroutines.CancellationException



/** Lazy extraction prevents unsupported provider responses from reaching Google's parser. */
internal data class GoogleCredentialResponse(val isGoogleIdToken: Boolean, val extractToken: () -> String)

internal suspend fun retrieveGoogleIdToken(
    request: suspend () -> GoogleCredentialResponse,
): String {
    if (BuildConfig.DEBUG) Log.d(AUTH_LOG_TAG, "Credential Manager request started")
    val response = try { request() }
    catch (cancelled: CancellationException) { throw cancelled }
    catch (failure: Exception) {
        if (failure is GetCredentialCancellationException) {
            throw AuthCancelledException()
        }
        if (BuildConfig.DEBUG) Log.w(AUTH_LOG_TAG, "Credential retrieval failed: ${sanitizedAuthFailure(failure.javaClass.simpleName, null)}")
        when (failure) {
            is NoCredentialException -> throw AuthFailure(AuthFailureKind.NO_CREDENTIALS)
            is GetCredentialProviderConfigurationException -> throw AuthFailure(AuthFailureKind.CONFIGURATION)
            is GetCredentialUnsupportedException -> throw AuthFailure(AuthFailureKind.UNSUPPORTED_CREDENTIAL)
            else -> throw AuthFailure(AuthFailureKind.UNEXPECTED)
        }
    }
    if (!response.isGoogleIdToken) {
        throw AuthFailure(AuthFailureKind.UNSUPPORTED_CREDENTIAL)
    }
    val token = try { response.extractToken() }
    catch (failure: Exception) {
        if (failure is CancellationException) throw failure
        if (BuildConfig.DEBUG) Log.w(AUTH_LOG_TAG, "Credential parsing failed: ${sanitizedAuthFailure(failure.javaClass.simpleName, null)}")
        throw AuthFailure(AuthFailureKind.INVALID_TOKEN)
    }
    if (token.isBlank()) throw AuthFailure(AuthFailureKind.INVALID_TOKEN)
    if (BuildConfig.DEBUG) Log.d(AUTH_LOG_TAG, "Credential retrieval succeeded")
    return token
}

/** Activity context is scoped to this one request and never retained by a singleton. */
internal suspend fun requestGoogleIdToken(context: Context): String {
    var current = context
    while (current !is Activity && current is ContextWrapper && current.baseContext !== current) current = current.baseContext
    val activity = current as? Activity
    if (activity == null || activity.isFinishing || activity.isDestroyed) {
        if (BuildConfig.DEBUG) Log.w(AUTH_LOG_TAG, "Credential request requires an active Activity")
        throw AuthFailure(AuthFailureKind.CONFIGURATION)
    }
    val resource = activity.resources.getIdentifier("default_web_client_id", "string", activity.packageName)
    val clientId = if (resource == 0) "" else activity.getString(resource)
    if (!clientId.matches(Regex("[0-9]+-[A-Za-z0-9_-]+\\.apps\\.googleusercontent\\.com"))) {
        if (BuildConfig.DEBUG) Log.w(AUTH_LOG_TAG, "Missing or invalid web OAuth configuration")
        throw AuthFailure(AuthFailureKind.CONFIGURATION)
    }
    return retrieveGoogleIdToken(request = {
        val manager = CredentialManager.create(activity)
        val option = GetSignInWithGoogleOption.Builder(clientId).build()
        val result = manager.getCredential(activity, GetCredentialRequest.Builder().addCredentialOption(option).build())
        val credential = result.credential
        GoogleCredentialResponse(credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            GoogleIdTokenCredential.createFrom((credential as CustomCredential).data).idToken
        }
    })
}
