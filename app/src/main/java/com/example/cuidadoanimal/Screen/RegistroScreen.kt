package com.example.cuidadoanimal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cuidadoanimal.Model.Cliente
import com.example.cuidadoanimal.Model.Persona
import com.example.cuidadoanimal.Model.Trabajador
import com.example.cuidadoanimal.Repository.ClienteRepository
import com.example.cuidadoanimal.Repository.PersonaRepository
import com.example.cuidadoanimal.Repository.TrabajadorRepository
import com.example.cuidadoanimal.Repository.AutenticacionRepository
import com.example.cuidadoanimal.Database.CuidadoAnimalDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavHostController, db: CuidadoAnimalDatabase) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var especialidades by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Cliente") }

    val clienteRepository = ClienteRepository(db.clienteDao())
    val trabajadorRepository = TrabajadorRepository(db.trabajadorDao())
    val personaRepository = PersonaRepository(db.personaDao())
    val autenticacionRepository = AutenticacionRepository(db.autenticacionDao(), db.personaDao())

    fun isValidEmail(email: String): Boolean {
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.matches(emailPattern)
    }

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
            text = "Registro",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(25.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(25.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(25.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(25.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedRole == "Cliente",
                    onClick = { selectedRole = "Cliente" }
                )
                Text("Cliente", color = Color.White)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedRole == "Trabajador",
                    onClick = { selectedRole = "Trabajador" }
                )
                Text("Trabajador", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedRole == "Trabajador") {
            TextField(
                value = especialidades,
                onValueChange = { especialidades = it },
                label = { Text("Especialidades (separadas por comas)") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(25.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            placeholder = { Text("Ingrese su contraseña") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(25.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(25.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (password == confirmPassword && nombre.isNotBlank() && email.isNotBlank() && telefono.isNotBlank() && direccion.isNotBlank() && password.isNotBlank()) {
                    if (isValidEmail(email)) {
                        val nuevaPersona = Persona(
                            nombre = nombre,
                            email = email,
                            telefono = telefono,
                            direccion = direccion
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            val personaId = personaRepository.insertPersona(nuevaPersona).toInt()
                            // Registro según el rol
                            if (selectedRole == "Cliente") {
                                val nuevoCliente = Cliente(persona_id = personaId)
                                clienteRepository.insertCliente(nuevoCliente)
                            } else {
                                val nuevoTrabajador = Trabajador(
                                    persona_id = personaId,
                                    especialidades = especialidades.split(",").map { it.trim() },
                                    calificacion = 0.0f
                                )
                                trabajadorRepository.insertTrabajador(nuevoTrabajador)
                            }
                            // Autenticación
                            autenticacionRepository.insertAutenticacionWithUserType(
                                personaId = personaId,
                                email = email,
                                password = password,
                                tipoUsuario = if (selectedRole == "Cliente") 2 else 1
                            )
                            successMessage = "Registro exitoso para $nombre"
                            errorMessage = ""
                            // Limpiar campos
                            nombre = ""
                            email = ""
                            telefono = ""
                            direccion = ""
                            especialidades = ""
                            password = ""
                            confirmPassword = ""
                        }
                    } else {
                        errorMessage = "Por favor ingrese un correo electrónico válido."
                    }
                } else {
                    errorMessage = "Las contraseñas no coinciden o faltan campos."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("Registrar", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (successMessage.isNotEmpty()) {
            Text(text = successMessage, color = Color.Green)
        }
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
        }

        Button(
            onClick = { navController.navigate("main_screen") },
            modifier = Modifier.padding(top = 12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A6A6B))
        ) {
            Text("Volver al Menú Principal", color = Color.White)
        }
    }
}
