package com.example.cuidadoanimal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
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
                    Text("Confirmar", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showConfirmDelete = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A6A6B))
                ) {
                    Text("Cancelar", color = Color.White)
                }
            },
            title = {
                Text(
                    text = "Confirmación",
                    color = Color.White,
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas eliminar esta mascota?",
                    color = Color.White
                )
            },
            containerColor = Color(0xFFC4C5C5), // Color de fondo del AlertDialog
            titleContentColor = Color.White, // Color del título
            textContentColor = Color.White // Color del texto
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

@OptIn(ExperimentalMaterial3Api::class)
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE26563)),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Guardar", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A6A6B)),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Cancelar", color = Color.White)
            }
        },
        title = {
            Text(
                text = if (mascota == null) "Agregar Mascota" else "Editar Mascota",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
                )
                TextField(
                    value = tipo,
                    onValueChange = { tipo = it },
                    label = { Text("Tipo") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
                )
                TextField(
                    value = edad,
                    onValueChange = { edad = it },
                    label = { Text("Edad") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
                )
                TextField(
                    value = raza,
                    onValueChange = { raza = it },
                    label = { Text("Raza") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
                )
                TextField(
                    value = peso,
                    onValueChange = { peso = it },
                    label = { Text("Peso") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
                )
            }
        },
        containerColor = Color(0xFFC4C5C5) // Cambiado color de fondo del AlertDialog
    )
}
