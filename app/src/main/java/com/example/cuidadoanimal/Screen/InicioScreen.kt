package com.example.cuidadoanimal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
    var tipoUsuario by remember { mutableStateOf<Int?>(null) } // Identifica el rol

    // Obtener datos del usuario y el rol
    LaunchedEffect(userId) {
        val (autenticacion, persona) = withContext(Dispatchers.IO) {
            autenticacionRepository.getUserWithDetailsById(userId)
        }

        if (autenticacion != null && persona != null) {
            welcomeMessage = "Bienvenid@, ${persona.nombre}"
            tipoUsuario = autenticacion.tipo_usuario
        }
    }

    // Estructura de la interfaz
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFFFFF), Color(0xFFE76A68))
                )
            )
            .padding(16.dp),
    ) {
        // Mensaje de bienvenida
        Text(
            text = welcomeMessage,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Contenedor para los botones de opciones (20% de la pantalla)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((0.2f * LocalConfiguration.current.screenHeightDp).dp) // Corrección aquí
                .background(Color(0xFFF7F9F8), RoundedCornerShape(16.dp))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botón de Registrar Mascota (habilitado)
                FloatingActionButton(
                    onClick = {
                        navController.navigate("mascota_screen/$userId")
                    },
                    containerColor = Color(0xFFFF9F02)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Pets,
                        contentDescription = "Registrar Mascota",
                        tint = Color.White
                    )
                }

                // Botón de Agendar Veterinaria (deshabilitado por ahora)
                FloatingActionButton(
                    onClick = { /* Agendar Veterinaria - No habilitado */ },
                    //enabled = false,
                    containerColor = Color.Gray
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MedicalServices,
                        contentDescription = "Agendar Veterinaria",
                        tint = Color.White.copy(alpha = 0.5f) // Ícono deshabilitado
                    )
                }

                // Botón de Pasear (deshabilitado por ahora)
                FloatingActionButton(
                    onClick = { /* Pasear - No habilitado */ },
                    //enabled = false,
                    containerColor = Color.Gray
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DirectionsWalk,
                        contentDescription = "Pasear",
                        tint = Color.White.copy(alpha = 0.5f)
                    )
                }

                // Botón de Spa (deshabilitado por ahora)
                FloatingActionButton(
                    onClick = { /* Spa - No habilitado */ },
                    //enabled = false,
                    containerColor = Color.Gray
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Spa,
                        contentDescription = "Spa",
                        tint = Color.White.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
