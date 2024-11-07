package com.example.cuidadoanimal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cuidadoanimal.Model.Mascota
import com.example.cuidadoanimal.Repository.MascotaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MascotaScreen(
    navController: NavHostController,
    clienteId: Int, // ID del cliente que se pasa desde InicioScreen
    mascotaRepository: MascotaRepository
) {
    val coroutineScope = rememberCoroutineScope()
    var mascotas by remember { mutableStateOf<List<Mascota>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) } // Controlar diálogo para agregar/editar mascota

    // Obtener la lista de mascotas para el cliente
    LaunchedEffect(clienteId) {
        mascotas = withContext(Dispatchers.IO) {
            mascotaRepository.getMascotasByClienteId(clienteId)
        }
    }

    // Función para agregar una nueva mascota
    fun addMascota(mascota: Mascota) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                mascotaRepository.addMascota(mascota)
            }
            // Actualizar la lista de mascotas después de agregar
            mascotas = mascotaRepository.getMascotasByClienteId(clienteId)
        }
    }

    // Función para eliminar una mascota
    fun deleteMascota(mascota: Mascota) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                mascotaRepository.deleteMascota(mascota)
            }
            // Actualizar la lista después de eliminar
            mascotas = mascotaRepository.getMascotasByClienteId(clienteId)
        }
    }

    // Estructura de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
            .padding(16.dp)
    ) {
        // Botón de regreso al menú principal
        IconButton(
            onClick = { navController.navigate("InicioScreen") }, // Navega a InicioScreen
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.Black)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Título de la pantalla
        Text(
            text = "Gestión de Mascotas",
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Botón para agregar nueva mascota
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Añadir Nueva Mascota")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de mascotas
        LazyColumn {
            items(mascotas) { mascota ->
                MascotaItem(
                    mascota = mascota,
                    onDelete = { deleteMascota(mascota) }
                )
            }
        }
    }

    // Mostrar diálogo para agregar/editar mascota
    if (showDialog) {
        MascotaDialog(
            onDismiss = { showDialog = false },
            onSave = { newMascota ->
                addMascota(newMascota)
                showDialog = false
            },
            clienteId = clienteId
        )
    }
}

@Composable
fun MascotaItem(
    mascota: Mascota,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Nombre: ${mascota.nombre}", fontSize = 16.sp, color = Color.Black)
                Text(text = "Tipo: ${mascota.tipo}", fontSize = 14.sp, color = Color.Gray)
                Text(text = "Edad: ${mascota.edad}", fontSize = 14.sp, color = Color.Gray)
            }
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

@Composable
fun MascotaDialog(
    onDismiss: () -> Unit,
    onSave: (Mascota) -> Unit,
    clienteId: Int
) {
    var nombre by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val mascota = Mascota(
                        nombre = nombre,
                        tipo = tipo,
                        edad = edad.toIntOrNull() ?: 0,
                        raza = raza,
                        peso = peso.toDoubleOrNull() ?: 0.0,
                        clienteId = clienteId
                    )
                    onSave(mascota)
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Agregar Mascota") },
        text = {
            Column {
                TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                TextField(value = tipo, onValueChange = { tipo = it }, label = { Text("Tipo") })
                TextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") })
                TextField(value = raza, onValueChange = { raza = it }, label = { Text("Raza") })
                TextField(value = peso, onValueChange = { peso = it }, label = { Text("Peso") })
            }
        }
    )
}
