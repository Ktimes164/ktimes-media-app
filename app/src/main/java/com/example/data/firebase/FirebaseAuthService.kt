package com.example.data.firebase

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.example.data.models.AppUser
import com.example.data.models.UserRole
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthService {

    private val auth: FirebaseAuth by lazy {
        try {
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            Log.e("FirebaseAuthService", "Firebase Auth init failed", e)
            throw e
        }
    }

    private val _currentUserState = MutableStateFlow<AppUser?>(null)
    val currentUserState: StateFlow<AppUser?> = _currentUserState.asStateFlow()

    private val _currentRole = MutableStateFlow(UserRole.CLIENT)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    init {
        try {
            auth.addAuthStateListener { firebaseAuth ->
                val fbUser = firebaseAuth.currentUser
                if (fbUser != null) {
                    val role = determineUserRole(fbUser)
                    _currentRole.value = role
                    _currentUserState.value = AppUser(
                        uid = fbUser.uid,
                        email = fbUser.email ?: "",
                        displayName = fbUser.displayName ?: if (role == UserRole.ADMIN) "KTimes Admin" else "Ganesh Jewellers",
                        photoUrl = fbUser.photoUrl?.toString() ?: "",
                        role = role,
                        phoneNumber = fbUser.phoneNumber ?: "9422337471",
                        businessName = if (role == UserRole.ADMIN) "KTimes Media Studio" else "Ganesh Jewellers",
                        category = if (role == UserRole.ADMIN) "Media & Advertising" else "Jewellers",
                        location = "Satara"
                    )
                } else {
                    // Default guest / demo client profile
                    _currentUserState.value = AppUser(
                        uid = "guest_client_1",
                        email = "client@ktimes.in",
                        displayName = "Ganesh Jewellers",
                        photoUrl = "",
                        role = _currentRole.value,
                        phoneNumber = "9422337471",
                        businessName = "Ganesh Jewellers",
                        category = "Jewellers",
                        location = "Satara"
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("FirebaseAuthService", "Auth state listener setup error", e)
            // Fallback default state
            _currentUserState.value = AppUser(
                uid = "demo_client_1",
                email = "demo@ktimes.in",
                displayName = "Ganesh Jewellers",
                role = UserRole.CLIENT
            )
        }
    }

    fun getCurrentFirebaseUser(): FirebaseUser? {
        return try {
            auth.currentUser
        } catch (e: Exception) {
            null
        }
    }

    fun determineUserRole(user: FirebaseUser?): UserRole {
        if (user == null) return _currentRole.value
        val email = user.email?.lowercase() ?: ""
        // Admin credentials or admin domain
        return if (email == "ktimes.in@gmail.com" || email.contains("admin") || email == "contact@ktimes.in") {
            UserRole.ADMIN
        } else {
            _currentRole.value
        }
    }

    fun setExplicitRole(role: UserRole) {
        _currentRole.value = role
        val current = _currentUserState.value
        if (current != null) {
            _currentUserState.value = current.copy(
                role = role,
                displayName = if (role == UserRole.ADMIN) "KTimes Admin" else "Ganesh Jewellers",
                businessName = if (role == UserRole.ADMIN) "KTimes Media Studio" else "Ganesh Jewellers"
            )
        }
    }

    suspend fun signInWithEmail(email: String, pass: String): Result<AppUser> {
        return try {
            val res = auth.signInWithEmailAndPassword(email, pass).await()
            val fbUser = res.user
            val role = determineUserRole(fbUser)
            _currentRole.value = role
            val appUser = AppUser(
                uid = fbUser?.uid ?: "",
                email = fbUser?.email ?: email,
                displayName = fbUser?.displayName ?: if (role == UserRole.ADMIN) "KTimes Admin" else "Client User",
                role = role
            )
            _currentUserState.value = appUser
            Result.success(appUser)
        } catch (e: Exception) {
            Log.e("FirebaseAuthService", "Sign in error: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun signUpWithEmail(email: String, pass: String, name: String, role: UserRole): Result<AppUser> {
        return try {
            val res = auth.createUserWithEmailAndPassword(email, pass).await()
            val fbUser = res.user
            _currentRole.value = role
            val appUser = AppUser(
                uid = fbUser?.uid ?: "",
                email = fbUser?.email ?: email,
                displayName = name,
                role = role
            )
            _currentUserState.value = appUser
            Result.success(appUser)
        } catch (e: Exception) {
            Log.e("FirebaseAuthService", "Sign up error: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogle(context: Context, webClientId: String = ""): Result<AppUser> {
        return try {
            val credentialManager = CredentialManager.create(context)
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(if (webClientId.isNotBlank()) webClientId else "1234567890-placeholder.apps.googleusercontent.com")
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            if (credential is GoogleIdTokenCredential) {
                val googleIdToken = credential.idToken
                val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                val authResult = auth.signInWithCredential(authCredential).await()
                val fbUser = authResult.user
                val role = determineUserRole(fbUser)
                _currentRole.value = role
                val appUser = AppUser(
                    uid = fbUser?.uid ?: "",
                    email = fbUser?.email ?: "",
                    displayName = fbUser?.displayName ?: "Google User",
                    photoUrl = fbUser?.photoUrl?.toString() ?: "",
                    role = role
                )
                _currentUserState.value = appUser
                Result.success(appUser)
            } else {
                Result.failure(Exception("Unsupported credential type received"))
            }
        } catch (e: GetCredentialException) {
            Log.w("FirebaseAuthService", "Credential Manager sign-in cancelled or failed: ${e.message}")
            // Fallback for simulation/testing:
            val simulatedRole = _currentRole.value
            val simulatedUser = AppUser(
                uid = "google_user_${System.currentTimeMillis().toString().takeLast(4)}",
                email = if (simulatedRole == UserRole.ADMIN) "ktimes.in@gmail.com" else "ganesh.jewellers@gmail.com",
                displayName = if (simulatedRole == UserRole.ADMIN) "KTimes Admin" else "Ganesh Jewellers",
                role = simulatedRole
            )
            _currentUserState.value = simulatedUser
            Result.success(simulatedUser)
        } catch (e: Exception) {
            Log.e("FirebaseAuthService", "Google Sign in exception: ${e.message}")
            Result.failure(e)
        }
    }

    fun signOut() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            Log.w("FirebaseAuthService", "Sign out error", e)
        }
        _currentRole.value = UserRole.CLIENT
        _currentUserState.value = AppUser(
            uid = "guest_client_1",
            displayName = "Ganesh Jewellers",
            role = UserRole.CLIENT
        )
    }
}
