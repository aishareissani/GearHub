package org.umn.gearhub.util

private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

object AuthValidator {
    const val MIN_PASSWORD_LENGTH = 8

    fun validateName(value: String): String? {
        val name = value.trim()
        return when {
            name.isEmpty() -> "Nama lengkap wajib diisi"
            name.length < 3 -> "Nama minimal 3 karakter"
            !name.all { it.isLetter() || it == ' ' || it == '.' || it == '\'' || it == '-' } ->
                "Nama hanya boleh berisi huruf"
            else -> null
        }
    }

    fun validateEmail(value: String): String? {
        val email = value.trim()
        return when {
            email.isEmpty() -> "Email kampus wajib diisi"
            !emailRegex.matches(email) -> "Format email tidak valid"
            !email.endsWith(".ac.id", ignoreCase = true) -> "Gunakan email kampus berakhiran .ac.id"
            else -> null
        }
    }

    fun validatePassword(value: String): String? = when {
        value.isEmpty() -> "Password wajib diisi"
        value.length < MIN_PASSWORD_LENGTH -> "Password minimal $MIN_PASSWORD_LENGTH karakter"
        else -> null
    }
}