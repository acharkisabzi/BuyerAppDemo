package com.example.buyerappdemo.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.buyerappdemo.R
import com.example.buyerappdemo.viewmodels.AuthViewModel

@Composable
fun LoginScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val authUiState by authViewModel.uiState.collectAsState()

    var emailInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("") }
    var areaInput by remember { mutableStateOf("") }
    var isSignUp by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.login_title),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.login_subtitle),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Extra fields for sign up
        if (isSignUp) {
            OutlinedTextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                label = { Text(stringResource(R.string.label_name)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !authUiState.isLoading
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = areaInput,
                onValueChange = { areaInput = it },
                label = { Text(stringResource(R.string.label_area)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !authUiState.isLoading
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        OutlinedTextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text(stringResource(R.string.label_email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            enabled = !authUiState.isLoading
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phoneInput,
            onValueChange = { phoneInput = it },
            label = { Text(stringResource(R.string.label_phone)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            enabled = !authUiState.isLoading
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text(stringResource(R.string.label_password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            enabled = !authUiState.isLoading
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Error message
        if (authUiState.errorMessage.isNotEmpty()) {
            Text(
                text = authUiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (isSignUp) {
                    authViewModel.signUp(
                        email = emailInput,
                        password = passwordInput,
                        name = nameInput,
                        area = areaInput,
                        phone = phoneInput,
                        onSuccess = {
                            navController.navigate("productFeed") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    )
                } else {
                    authViewModel.signIn(
                        email = emailInput,
                        password = passwordInput,
                        onSuccess = {
                            navController.navigate("productFeed") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !authUiState.isLoading
        ) {
            if (authUiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(if (isSignUp) stringResource(R.string.btn_create_account) else stringResource(R.string.btn_sign_in))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = { isSignUp = !isSignUp },
            enabled = !authUiState.isLoading
        ) {
            Text(if (isSignUp) stringResource(R.string.msg_already_have_account) else stringResource(R.string.msg_first_time))
        }
    }
}
