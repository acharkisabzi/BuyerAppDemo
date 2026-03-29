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

data class AuthState(
    val nameInput: String = "",
    val emailInput: String = "",
    val phoneInput: String = "",
    val passwordInput: String = "",
    val usernameInput: String = "",
    val areaInput: String = "",
    val isSignUp: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(nameInput = name) }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(emailInput = email) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(passwordInput = password) }
    }

    fun updateArea(area: String) {
        _uiState.update { it.copy(areaInput = area) }
    }

    fun updateUserName(userName: String) {
        _uiState.update { it.copy(usernameInput = userName) }
    }

    fun updateLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun updateSignUp(isSignUp: Boolean) {
        _uiState.update { it.copy(isSignUp = isSignUp) }
    }

    fun updateAuthenticated(isAuthenticated: Boolean) {
        _uiState.update { it.copy(isAuthenticated = isAuthenticated) }
    }

    fun updateSession(session: UserSession?) {
        _uiState.update { it.copy(session = session) }
    }

    fun updateError(error: String) {
        _uiState.update { it.copy(errorMessage = error) }
    }

    fun signOut() {
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
                Log.d("AuthViewModel", e.toString())
                updateError("Error signing in")
            } finally {
                updateLoading(false)
            }
        }
    }

    private suspend fun checkUserExists(email: String): Boolean {
        return supabase.postgrest.rpc(
            function = "check_user_exists",
            parameters = mapOf("email_input" to email)
        ).decodeAs<Boolean>()
    }

    private suspend fun signIn(emailIn: String, passwordIn: String) {
        supabase.auth.signInWith(Email) {
            email = emailIn
            password = passwordIn
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }
}
