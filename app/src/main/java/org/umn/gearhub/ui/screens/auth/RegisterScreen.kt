package org.umn.gearhub.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
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
fun RegisterScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var nameTouched by rememberSaveable { mutableStateOf(false) }
    var emailTouched by rememberSaveable { mutableStateOf(false) }
    var passwordTouched by rememberSaveable { mutableStateOf(false) }
    var submitted by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val nameError = AuthValidator.validateName(name)
    val emailError = AuthValidator.validateEmail(email)
        ?: if (DummyUsers.emailExists(email)) "Email sudah terdaftar" else null
    val passwordError = AuthValidator.validatePassword(password)
    val isValid = nameError == null && emailError == null && passwordError == null

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(1200.milliseconds)
            DummyUsers.register(User(name = name, email = email, password = password))
            onRegisterSuccess()
        }
    }

    val submit = {
        submitted = true
        if (isValid && !isLoading) isLoading = true
    }

    AuthScaffold(
        title = "Daftar",
        onBack = onBack,
        modifier = modifier,
        fields = {
            AuthTextField(
                label = "Nama lengkap",
                value = name,
                onValueChange = {
                    name = it
                    nameTouched = true
                },
                placeholder = "Sesuai KTM",
                errorMessage = if (nameTouched || submitted) nameError else null,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            )
            AuthTextField(
                label = "Email kampus",
                value = email,
                onValueChange = {
                    email = it
                    emailTouched = true
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
                },
                placeholder = "Minimal 8 karakter",
                errorMessage = if (passwordTouched || submitted) passwordError else null,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                isPassword = true,
                onDone = submit
            )
        },
        actions = {
            AuthButton(text = "Daftar", isLoading = isLoading, onClick = submit)
            AuthSwitchPrompt(
                prompt = "Sudah punya akun?",
                action = "Login",
                onClick = onLoginClick
            )
        }
    )
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun RegisterScreenPreview() {
    GearHubTheme {
        RegisterScreen(onBack = {}, onRegisterSuccess = {}, onLoginClick = {})
    }
}