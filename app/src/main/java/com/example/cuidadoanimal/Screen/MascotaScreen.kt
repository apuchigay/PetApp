package com.example.cuidadoanimal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
    clienteId: Int,
    mascotaRepository: MascotaRepository
) {
    val coroutineScope = rememberCoroutineScope()
    var mascotas by remember { mutableStateOf<List<Mascota>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var showConfirmDelete by remember { mutableStateOf(false) }
    var selectedMascota by remember { mutableStateOf<Mascota?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    // Obtener la lista de mascotas
    LaunchedEffect(clienteId) {
        mascotas = withContext(Dispatchers.IO) {
            mascotaRepository.getMascotasByClienteId(clienteId)
        }
    }

    // Función para agregar/editar mascota
    fun saveMascota(mascota: Mascota) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                if (isEditing) {
                    mascotaRepository.updateMascota(mascota)
                } else {
                    mascotaRepository.addMascota(mascota)
                }
            }
            mascotas = mascotaRepository.getMascotasByClienteId(clienteId)
            isEditing = false
        }
    }

    // Función para eliminar mascota
    fun deleteMascota() {
        coroutineScope.launch {
            selectedMascota?.let {
                withContext(Dispatchers.IO) {
                    mascotaRepository.deleteMascota(it)
                }
                mascotas = mascotaRepository.getMascotasByClienteId(clienteId)
            }
            showConfirmDelete = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
            .padding(16.dp)
    ) {
        IconButton(
            onClick = {
                navController.navigate("inicio_screen/$clienteId")
            },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.Black)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Gestión de Mascotas", fontSize = 24.sp, color = Color.Black, modifier = Modifier.padding(bottom = 16.dp))

        Button(
            onClick = {
                showDialog = true
                isEditing = false
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE26563))
        ) {
            Text("Añadir Nueva Mascota")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(mascotas) { mascota ->
                MascotaItem(
                    mascota = mascota,
                    onEdit = {
                        selectedMascota = mascota
                        showDialog = true
                        isEditing = true
                    },
                    onDelete = {
                        selectedMascota = mascota
                        showConfirmDelete = true
                    }
                )
            }
        }
    }

    if (showDialog) {
        MascotaDialog(
            onDismiss = { showDialog = false },
            onSave = { newMascota ->
                saveMascota(newMascota)
                showDialog = false
            },
            clienteId = clienteId,
            mascota = if (isEditing) selectedMascota else null
        )
    }

    if (showConfirmDelete) {
        AlertDialog(
            onDismissRequest = { showConfirmDelete = false },
            confirmButton = {
                Button(
                    onClick = { deleteMascota() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE26563))
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(onClick = { showConfirmDelete = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Confirmación") },
            text = { Text("¿Estás seguro de que deseas eliminar esta mascota?") }
        )
    }
}

@Composable
fun MascotaItem(
    mascota: Mascota,
    onEdit: () -> Unit,
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
            Row {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = Color.Gray)
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun MascotaDialog(
    onDismiss: () -> Unit,
    onSave: (Mascota) -> Unit,
    clienteId: Int,
    mascota: Mascota? = null
) {
    var nombre by remember { mutableStateOf(mascota?.nombre ?: "") }
    var tipo by remember { mutableStateOf(mascota?.tipo ?: "") }
    var edad by remember { mutableStateOf(mascota?.edad?.toString() ?: "") }
    var raza by remember { mutableStateOf(mascota?.raza ?: "") }
    var peso by remember { mutableStateOf(mascota?.peso?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val newMascota = Mascota(
                        mascotaId = mascota?.mascotaId ?: 0,
                        nombre = nombre,
                        tipo = tipo,
                        edad = edad.toIntOrNull() ?: 0,
                        raza = raza,
                        peso = peso.toDoubleOrNull() ?: 0.0,
                        clienteId = clienteId
                    )
                    onSave(newMascota)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE26563))
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text(if (mascota == null) "Agregar Mascota" else "Editar Mascota") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.background(Color(0xFF9D9D9E))
            ) {
                TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                TextField(value = tipo, onValueChange = { tipo = it }, label = { Text("Tipo") })
                TextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") })
                TextField(value = raza, onValueChange = { raza = it }, label = { Text("Raza") })
                TextField(value = peso, onValueChange = { peso = it }, label = { Text("Peso") })
            }
        }
    )
}
