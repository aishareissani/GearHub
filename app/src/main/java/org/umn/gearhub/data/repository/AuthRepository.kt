package org.umn.gearhub.data.repository

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthErrorCode
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import org.umn.gearhub.data.model.User
import org.umn.gearhub.data.remote.supabase

object AuthRepository {
    private const val NAME_KEY = "name"

    suspend fun restoreSession(): User? {
        supabase.auth.awaitInitialization()
        return supabase.auth.currentUserOrNull()?.toUser()
    }

    /** Returns the signed-in user, or an Indonesian error message for the login form. */
    suspend fun signIn(email: String, password: String): Result<User> = runCatching {
        supabase.auth.signInWith(Email) {
            this.email = email.trim()
            this.password = password
        }
        requireNotNull(supabase.auth.currentUserOrNull()).toUser()
    }.recoverAuthError {
        when (it) {
            AuthErrorCode.EmailNotConfirmed -> "Cek email kamu dan konfirmasi akun dulu"
            AuthErrorCode.InvalidCredentials -> "Email atau password salah"
            else -> null
        }
    }

    suspend fun signUp(name: String, email: String, password: String): Result<Unit> = runCatching {
        supabase.auth.signUpWith(Email) {
            this.email = email.trim().lowercase()
            this.password = password
            data = buildJsonObject { put(NAME_KEY, name.trim()) }
        }
        Unit
    }.recoverAuthError {
        when (it) {
            AuthErrorCode.UserAlreadyExists, AuthErrorCode.EmailExists -> "Email sudah terdaftar"
            AuthErrorCode.WeakPassword -> "Password terlalu lemah"
            else -> null
        }
    }

    suspend fun signOut() {
        supabase.auth.signOut()
    }

    private fun UserInfo.toUser() = User(
        name = userMetadata?.get(NAME_KEY)?.jsonPrimitive?.content ?: email.orEmpty().substringBefore("@"),
        email = email.orEmpty()
    )

    private fun <T> Result<T>.recoverAuthError(message: (AuthErrorCode?) -> String?): Result<T> =
        recoverCatching { error ->
            val known = (error as? AuthRestException)?.let { message(it.errorCode) }
            throw AuthException(known ?: "Gagal terhubung ke server. Coba lagi.", error)
        }
}

class AuthException(message: String, cause: Throwable) : Exception(message, cause)
