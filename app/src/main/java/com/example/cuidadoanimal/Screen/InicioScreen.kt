package com.example.cuidadoanimal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cuidadoanimal.Repository.AutenticacionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    navController: NavHostController,
    userId: Int,
    mascotaId: Int,
    autenticacionRepository: AutenticacionRepository
) {
    var welcomeMessage by remember { mutableStateOf("Cargando...") }
    var tipoUsuario by remember { mutableStateOf<Int?>(null) }

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

    // Fondo sólido blanco
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mensaje de bienvenida
            Text(
                text = welcomeMessage,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .padding(top = 48.dp, bottom = 24.dp) // Separación adicional en la parte superior
                    .align(Alignment.CenterHorizontally)
            )

            // Contenedor para los botones de opciones
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7F9F8), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Primera fila de botones
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OptionButton(
                            icon = Icons.Outlined.Pets,
                            label = "Registrar Mascota",
                            description = "Añade una nueva mascota",
                            onClick = { navController.navigate("mascota_screen/$userId") },
                            isEnabled = true
                        )

                        OptionButton(
                            icon = Icons.Outlined.MedicalServices,
                            label = "Agendar Veterinaria",
                            description = "Programa una cita",
                            onClick = { navController.navigate("veterinaria_screen/$userId/$userId") },  // Aquí pasamos ambos parámetros
                            isEnabled = true
                        )
                    }

                    // Segunda fila de botones
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OptionButton(
                            icon = Icons.Outlined.DirectionsWalk,
                            label = "Pasear",
                            description = "Programa un paseo",
                            onClick = {
                                navController.navigate("paseo_screen/$mascotaId/$userId") // Navegación a PaseoScreen con parámetros
                            },
                            isEnabled = true // Habilitamos el botón
                        )

                        OptionButton(
                            icon = Icons.Outlined.Spa,
                            label = "Spa",
                            description = "Reserva un spa",
                            onClick = { /* Acción futura */ },
                            isEnabled = false
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de cierre de sesión como FloatingActionButton
            FloatingActionButton(
                onClick = { navController.navigate("main_screen") },
                containerColor = Color(0xFFE26563),
                contentColor = Color.White,
                modifier = Modifier
                    .size(56.dp)
                    .padding(top = 16.dp) // Espacio adicional arriba del botón de cerrar sesión
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Cerrar sesión"
                )
            }
        }
    }
}

@Composable
fun OptionButton(
    icon: ImageVector,
    label: String,
    description: String,
    onClick: () -> Unit,
    isEnabled: Boolean
) {
    var isPressed by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        FloatingActionButton(
            onClick = {
                if (isEnabled) {
                    isPressed = true
                    onClick()
                    coroutineScope.launch {
                        kotlinx.coroutines.delay(200)
                        isPressed = false
                    }
                }
            },
            containerColor = if (isEnabled) {
                if (isPressed) Color(0xFFE26563) else Color.White
            } else {
                Color.Gray
            },
            contentColor = if (isEnabled) {
                if (isPressed) Color.White else Color.Black
            } else {
                Color.White.copy(alpha = 0.5f)
            },
            modifier = Modifier
                .size(56.dp)
                .shadow(4.dp, RoundedCornerShape(28.dp)), // Añadir sombra
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isEnabled) {
                    if (isPressed) Color.White else Color.Black
                } else {
                    Color.White.copy(alpha = 0.5f)
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isEnabled) Color.Black else Color.Gray,
            textAlign = TextAlign.Center
        )
        if (isEnabled) {
            Text(
                text = description,
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}
