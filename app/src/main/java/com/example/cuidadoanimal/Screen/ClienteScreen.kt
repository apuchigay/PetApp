package com.example.cuidadoanimal.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cuidadoanimal.Model.Cliente

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteScreen(navController: NavHostController) {
    // Variables de estado para almacenar los datos ingresados
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro de Cliente",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo de Nombre
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Email
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
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
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para confirmar registro
        Button(
            onClick = {
                if (nombre.isNotBlank() && email.isNotBlank() && telefono.isNotBlank() && direccion.isNotBlank()) {
                    // Aquí puedes guardar la información en la base de datos
                    val nuevoCliente = Cliente(
                        persona_id = 0, // ID único que generará la BD
                    )
                    // Asumimos que la BD se maneja en un ViewModel o Repository
                    successMessage = "Registro exitoso para $nombre"
                } else {
                    successMessage = "Por favor, completa todos los campos"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Confirmar Registro")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar mensaje de éxito
        if (successMessage.isNotBlank()) {
            Text(text = successMessage, color = MaterialTheme.colorScheme.primary)
        }
    }
}