package com.example.cuidadoanimal.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cuidadoanimal.Database.CuidadoAnimalDatabase
import com.example.cuidadoanimal.R
import com.example.cuidadoanimal.Repository.AutenticacionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController, db: CuidadoAnimalDatabase) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }  // Estado para el icono de ojo
    var emailError by remember { mutableStateOf(false) }  // Estado para la validación de correo

    // Crear una instancia del repositorio de autenticación con ambos DAOs
    val autenticacionRepository = AutenticacionRepository(
        db.autenticacionDao(),
        db.personaDao() // Añadimos personaDao
    )

    // Mover la creación del coroutineScope fuera del botón
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFE76A68), Color(0xFFCF4E49))
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenido de vuelta",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen de logo
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "Logo de Cuidado Animal",
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de correo electrónico con validación
        TextField(
            value = email,
            onValueChange = {
                email = it
                emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            },
            label = { Text("Correo electrónico") },
            isError = emailError,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(25.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(25.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        if (emailError) {
            Text(
                text = "Por favor ingrese un correo electrónico válido",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña con icono de ojo para mostrar/ocultar
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            placeholder = { Text("Ingrese su contraseña") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(25.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(25.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Lanzar la coroutine
                coroutineScope.launch {
                    val isValidUser = withContext(Dispatchers.IO) {
                        autenticacionRepository.verifyPassword(email, password)
                    }
                    if (isValidUser) {
                        val autenticacion = withContext(Dispatchers.IO) {
                            autenticacionRepository.getAutenticacionByEmail(email)
                        }
                        if (autenticacion != null) {
                            navController.navigate("inicio_screen/${autenticacion.persona_id}")
                        } else {
                            errorMessage = "Usuario no encontrado"
                        }
                    } else {
                        errorMessage = "Correo o contraseña incorrectos"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text("Ingresar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A6A6B))
        ) {
            Text("Volver", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotBlank()) {
            Text(text = errorMessage, color = Color.Red)
        }
    }
}