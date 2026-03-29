package com.example.buyerappdemo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.buyerappdemo.models.UserModel
import com.example.buyerappdemo.supabase.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest

data class AuthUiState(
    val isAuthenticated: Boolean? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val session = supabase.auth.currentSessionOrNull()
                _uiState.value = _uiState.value.copy(
                    isAuthenticated = session != null,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to check auth status"
                )
            }
        }
    }

    fun signUp(
        email: String,
        password: String,
        name: String,
        area: String,
        phone: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")
            try {
                val exists = checkUserExists(_uiState.value.emailInput)
                if (_uiState.value.isSignUp) {
                    // Sign up
                    if(!exists){
                        signUp(_uiState.value.emailInput, _uiState.value.passwordInput)
                    } else {
                        updateError("User already exists")
                    }
                } else {
                    // Sign in
                    supabase.auth.signInWith(Email) {
                        email = _uiState.value.emailInput
                        password = _uiState.value.passwordInput
                    }
                }
                val currentSession = supabase.auth.currentSessionOrNull()
                updateSession(currentSession)
                updateAuthenticated(currentSession != null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Sign out failed"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }
}
