package org.umn.gearhub.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import org.umn.gearhub.data.dummy.DummyUsers
import org.umn.gearhub.data.model.User
import org.umn.gearhub.ui.components.AuthButton
import org.umn.gearhub.ui.components.AuthScaffold
import org.umn.gearhub.ui.components.AuthSwitchPrompt
import org.umn.gearhub.ui.components.AuthTextField
import org.umn.gearhub.ui.theme.GearHubTheme
import org.umn.gearhub.util.AuthValidator
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun LoginScreen(
    onLoginSuccess: (User) -> Unit,
    onRegisterClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var emailTouched by rememberSaveable { mutableStateOf(false) }
    var passwordTouched by rememberSaveable { mutableStateOf(false) }
    var submitted by rememberSaveable { mutableStateOf(false) }
    var loginError by rememberSaveable { mutableStateOf<String?>(null) }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val emailError = AuthValidator.validateEmail(email)
    val passwordError = AuthValidator.validatePassword(password)
    val isValid = emailError == null && passwordError == null

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(1200.milliseconds)
            val user = DummyUsers.authenticate(email, password)
            if (user != null) {
                onLoginSuccess(user)
            } else {
                loginError = "Email atau password salah"
                isLoading = false
            }
        }
    }

    val submit = {
        submitted = true
        if (isValid && !isLoading) {
            loginError = null
            isLoading = true
        }
    }

    AuthScaffold(
        title = "Login",
        modifier = modifier,
        fields = {
            AuthTextField(
                label = "Email kampus",
                value = email,
                onValueChange = {
                    email = it
                    emailTouched = true
                    loginError = null
                },
                placeholder = "nama@kampus.ac.id",
                errorMessage = if (emailTouched || submitted) emailError else null,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
            AuthTextField(
                label = "Password",
                value = password,
                onValueChange = {
                    password = it
                    passwordTouched = true
                    loginError = null
                },
                placeholder = "Minimal 8 karakter",
                errorMessage = (if (passwordTouched || submitted) passwordError else null) ?: loginError,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                isPassword = true,
                onDone = submit
            )
        },
        actions = {
            AuthButton(text = "Login", isLoading = isLoading, onClick = submit)
            AuthSwitchPrompt(
                prompt = "Belum punya akun?",
                action = "Daftar",
                onClick = { onRegisterClick?.invoke() },
                enabled = onRegisterClick != null
            )
        }
    )
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun LoginScreenPreview() {
    GearHubTheme {
        LoginScreen(onLoginSuccess = {})
    }
}