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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cuidadoanimal.Model.Trabajador
import com.example.cuidadoanimal.Model.Persona
import com.example.cuidadoanimal.Repository.TrabajadorRepository
import com.example.cuidadoanimal.Repository.PersonaRepository
import com.example.cuidadoanimal.Database.CuidadoAnimalDatabase
import com.example.cuidadoanimal.Model.Autenticacion
import com.example.cuidadoanimal.Repository.AutenticacionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrabajadorScreen(navController: NavHostController, db: CuidadoAnimalDatabase) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var especialidades by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    // Crear instancias de los repositorios
    val trabajadorRepository = TrabajadorRepository(db.trabajadorDao())
    val personaRepository = PersonaRepository(db.personaDao())
    val autenticacionRepository = AutenticacionRepository(
        db.autenticacionDao(),
        db.personaDao() // Agrega personaDao aquí
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B303F))
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

        // Campos de entrada
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFD4E4FF)),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
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
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFD4E4FF)),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFD4E4FF)),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = especialidades,
            onValueChange = { especialidades = it },
            label = { Text("Especialidades (separadas por comas)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFD4E4FF)),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            placeholder = { Text("Ingrese su contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFD4E4FF)),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para confirmar registro
        Button(
            onClick = {
                if (nombre.isNotBlank() && email.isNotBlank() && telefono.isNotBlank() && direccion.isNotBlank() && especialidades.isNotBlank() && password.isNotBlank()) {
                    val nuevaPersona = Persona(
                        nombre = nombre,
                        email = email,
                        telefono = telefono,
                        direccion = direccion
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        val personaId = personaRepository.insertPersona(nuevaPersona).toInt()
                        val nuevoTrabajador = Trabajador(
                            persona_id = personaId,
                            especialidades = especialidades.split(",").map { it.trim() },
                            calificacion = 0.0f
                        )

                        // Insertar trabajador
                        trabajadorRepository.insertTrabajador(nuevoTrabajador)

                        // Insertar autenticación con tipo de usuario 1 para trabajador
                        val nuevaAutenticacion = Autenticacion(
                            persona_id = personaId,
                            email = email,
                            password = password,
                            tipo_usuario = 1
                        )
                        autenticacionRepository.insertAutenticacion(nuevaAutenticacion)

                        successMessage = "Registro exitoso para $nombre"
                    }
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

        // Botón de volver
        Button(
            onClick = {
                navController.popBackStack() // Vuelve a la pantalla anterior
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A7AF2))
        ) {
            Text("Volver", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (successMessage.isNotBlank()) {
            Text(text = successMessage, color = Color.White)
        }
    }
}