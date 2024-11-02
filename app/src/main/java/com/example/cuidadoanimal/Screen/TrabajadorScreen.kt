package com.example.cuidadoanimal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cuidadoanimal.Model.Trabajador

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrabajadorScreen(navController: NavHostController) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var especialidades by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B303F))  // Fondo de pantalla
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro de Trabajador",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo de Nombre
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Email
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Teléfono
        TextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Dirección
        TextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Especialidades
        TextField(
            value = especialidades,
            onValueChange = { especialidades = it },
            label = { Text("Especialidades (separadas por comas)") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para confirmar registro
        Button(
            onClick = {
                if (nombre.isNotBlank() && email.isNotBlank() && telefono.isNotBlank() && direccion.isNotBlank() && especialidades.isNotBlank()) {
                    val nuevoTrabajador = Trabajador(
                        persona_id = 0,
                        especialidades = especialidades.split(",").map { it.trim() },
                        calificacion = 0.0f
                    )
                    successMessage = "Registro exitoso para $nombre"
                } else {
                    successMessage = "Por favor, completa todos los campos"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9F02))
        ) {
            Text("Confirmar Registro")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (successMessage.isNotBlank()) {
            Text(text = successMessage, color = Color.White)
        }
    }
}