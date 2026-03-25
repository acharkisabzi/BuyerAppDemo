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
                // Sign up
                val result = supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }

                // Save buyer profile
                supabase.postgrest["buyers"].insert(
                    UserModel(
                        id = result?.id,
                        name = name,
                        area = area,
                        email = email,
                        phone = phone
                    )
                )

                _uiState.value = _uiState.value.copy(
                    isAuthenticated = true,
                    isLoading = false
                )
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Sign up failed"
                )
            }
        }
    }

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")
            try {
                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                _uiState.value = _uiState.value.copy(
                    isAuthenticated = true,
                    isLoading = false
                )
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Sign in failed"
                )
            }
        }
    }

    fun signOut(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                supabase.auth.signOut()
                _uiState.value = _uiState.value.copy(isAuthenticated = false)
                onSuccess()
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
