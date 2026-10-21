package org.umn.gearhub.ui.screens.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.umn.gearhub.data.dummy.DummyUsers
import org.umn.gearhub.data.model.User

class AuthViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _currentUser = MutableStateFlow(
        savedStateHandle.get<String>(KEY_EMAIL)?.let { DummyUsers.findByEmail(it) }
    )
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun login(user: User) {
        savedStateHandle[KEY_EMAIL] = user.email
        _currentUser.value = user
    }

    fun logout() {
        savedStateHandle.remove<String>(KEY_EMAIL)
        _currentUser.value = null
    }

    private companion object {
        const val KEY_EMAIL = "current_user_email"
    }
}