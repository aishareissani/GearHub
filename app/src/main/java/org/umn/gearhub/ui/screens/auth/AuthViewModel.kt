package org.umn.gearhub.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.umn.gearhub.data.model.User
import org.umn.gearhub.data.repository.AuthRepository
import org.umn.gearhub.data.repository.RentalRepository

class AuthViewModel : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun login(user: User) {
        _currentUser.value = user
    }

    fun logout() {
        _currentUser.value = null
        RentalRepository.clear()
        viewModelScope.launch { runCatching { AuthRepository.signOut() } }
    }
}
