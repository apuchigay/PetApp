package com.example.cuidadoanimal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cuidadoanimal.Repository.AutenticacionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun InicioScreen(
    userId: Int,
    autenticacionRepository: AutenticacionRepository
) {
    val coroutineScope = rememberCoroutineScope()
    var welcomeMessage by remember { mutableStateOf("Cargando...") }
    var roleMessage by remember { mutableStateOf("") }
    var testMessage by remember { mutableStateOf("") }
    val commonMessage = "Este mensaje es para los dos"

    // Obtener datos del usuario y el rol
    coroutineScope.launch {
        val (autenticacion, persona) = withContext(Dispatchers.IO) {
            autenticacionRepository.getUserWithDetailsById(userId)
        }

        if (autenticacion != null && persona != null) {
            val nombre = persona.nombre
            val tipoUsuario = autenticacion.tipo_usuario

            welcomeMessage = "Bienvenido/a, $nombre"

            when (tipoUsuario) {
                1 -> {
                    roleMessage = "Rol: Trabajador"
                    testMessage = "haciendo su trabajo"
                }
                2 -> {
                    roleMessage = "Rol: Cliente"
                    testMessage = "Dueño de un peludo"
                }
                else -> {
                    roleMessage = "Rol desconocido"
                    testMessage = ""
                }
            }
        }
    }


    // Estructura de la interfaz
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B303F))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = welcomeMessage,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = roleMessage,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = testMessage,
            fontSize = 16.sp,
            color = Color.LightGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Mensaje común para ambos roles
        Text(
            text = commonMessage,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}
