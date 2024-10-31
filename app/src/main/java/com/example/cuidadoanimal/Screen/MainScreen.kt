package com.example.cuidadoanimal.Screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cuidadoanimal.Model.Autenticacion
import com.example.cuidadoanimal.Repository.AutenticacionRepository
import kotlinx.coroutines.launch

class AuthViewModelFactory(private val authRepository: AutenticacionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// ViewModel para manejo de autenticación
class AuthViewModel(private val repository: AutenticacionRepository) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isRegister by mutableStateOf(false)

    var errorEmail by mutableStateOf("")
    var errorPassword by mutableStateOf("")
    var successMessage by mutableStateOf("")

    private fun validateFields(): Boolean {
        errorEmail = if (email.isBlank()) "El correo es obligatorio" else ""
        errorPassword = if (password.isBlank()) "La contraseña es obligatoria" else ""
        return errorEmail.isEmpty() && errorPassword.isEmpty()
    }

    fun authenticate() {
        if (validateFields()) {
            viewModelScope.launch {
                if (isRegister) {
                    registerUser()
                } else {
                    loginUser()
                }
            }
        }
    }

    private suspend fun registerUser() {
        val newUser = Autenticacion(email = email, password = password, cliente_id = null, trabajador_id = null)
        repository.insertAutenticacion(newUser)
        successMessage = "Registro exitoso"
    }

    private suspend fun loginUser() {
        val user = repository.getAutenticacionByEmail(email)
        successMessage = if (user?.password == password) "Inicio de sesión exitoso" else "Credenciales incorrectas"
    }
}

// Función Composable para la interfaz
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(autenticacionRepository: AutenticacionRepository) {
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(autenticacionRepository))
    val context = LocalContext.current
    val gradient = Brush.horizontalGradient(colors = listOf(Color(0xFF0b76d6), Color(0xFF0eb3aa)))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (authViewModel.isRegister) "Registro" else "Inicio de Sesión",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo de Email
        TextField(
            value = authViewModel.email,
            onValueChange = { authViewModel.email = it },
            label = { Text("Correo electrónico") },
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
            isError = authViewModel.errorEmail.isNotEmpty(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        if (authViewModel.errorEmail.isNotEmpty()) {
            Text(text = authViewModel.errorEmail, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Contraseña
        TextField(
            value = authViewModel.password,
            onValueChange = { authViewModel.password = it },
            label = { Text("Contraseña") },
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
            isError = authViewModel.errorPassword.isNotEmpty(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )
        if (authViewModel.errorPassword.isNotEmpty()) {
            Text(text = authViewModel.errorPassword, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para autenticar (iniciar sesión o registrar)
        Button(
            onClick = {
                authViewModel.authenticate()
                Toast.makeText(context, authViewModel.successMessage, Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0b76d6))
        ) {
            Text(if (authViewModel.isRegister) "Registrar" else "Iniciar Sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para cambiar entre inicio de sesión y registro
        TextButton(onClick = { authViewModel.isRegister = !authViewModel.isRegister }) {
            Text(
                text = if (authViewModel.isRegister) "¿Ya tienes cuenta? Inicia sesión" else "¿No tienes cuenta? Regístrate",
                color = Color.White
            )
        }
    }
}