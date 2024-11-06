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
import androidx.navigation.NavHostController
import com.example.cuidadoanimal.Repository.AutenticacionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun InicioScreen(
    navController: NavHostController,
    userId: Int,
    autenticacionRepository: AutenticacionRepository
) {
    var welcomeMessage by remember { mutableStateOf("Cargando...") }
    var roleMessage by remember { mutableStateOf("") }
    var tipoUsuario by remember { mutableStateOf<Int?>(null) } // Identifica el rol

    // Obtener datos del usuario y el rol
    LaunchedEffect(userId) {
        val (autenticacion, persona) = withContext(Dispatchers.IO) {
            autenticacionRepository.getUserWithDetailsById(userId)
        }

        if (autenticacion != null && persona != null) {
            welcomeMessage = "Bienvenido/a, ${persona.nombre}"
            tipoUsuario = autenticacion.tipo_usuario

            roleMessage = when (tipoUsuario) {
                1 -> "Rol: Trabajador"
                2 -> "Rol: Cliente"
                else -> "Rol desconocido"
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
        // Mensaje de bienvenida
        Text(
            text = welcomeMessage,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Rol del usuario
        Text(
            text = roleMessage,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Opciones basadas en el rol
        if (tipoUsuario == 2) {
            // Opciones para el Cliente
            Button(
                onClick = {
                    navController.navigate("mascota_screen/$userId") // `userId` debe corresponder a `clienteId` del cliente
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Gestión de Mascotas")
            }

            // Aquí puedes agregar otros botones o funciones específicas para el cliente
            // Ejemplo: Historial de servicios, Solicitar servicio, etc.
        } else if (tipoUsuario == 1) {
            // Mensaje para el Trabajador
            Text(
                text = "Nuevas funciones próximamente",
                fontSize = 16.sp,
                color = Color.LightGray,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}
