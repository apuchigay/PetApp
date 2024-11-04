package com.example.cuidadoanimal.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController, db: CuidadoAnimalDatabase) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

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
            .background(Color(0xFF2B303F))
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
            painter = painterResource(id = R.drawable.login), // Reemplaza con el nombre correcto de tu imagen
            contentDescription = "Logo de Cuidado Animal",
            modifier = Modifier.size(220.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFD4E4FF)),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            placeholder = { Text("Ingrese su contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFD4E4FF)),
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
                        // Redirigir a InicioScreen
                        navController.navigate("inicio_screen")
                    } else {
                        errorMessage = "Correo o contraseña incorrectos"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9F02))
        ) {
            Text("Ingresar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A7AF2))
        ) {
            Text("Volver", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotBlank()) {
            Text(text = errorMessage, color = Color.Red)
        }
    }
}