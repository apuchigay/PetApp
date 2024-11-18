package com.example.cuidadoanimal.Screen

import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cuidadoanimal.Model.Mascota
import com.example.cuidadoanimal.Model.ServicioSpaHistorial
import com.example.cuidadoanimal.Model.TrabajadorConNombre
import com.example.cuidadoanimal.Repository.MascotaRepository
import com.example.cuidadoanimal.Repository.SpaRepository
import com.example.cuidadoanimal.Repository.TrabajadorRepository
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpaScreen(
    navController: NavController,
    userId: Int,
    mascotaId: Int,
    mascotaRepository: MascotaRepository,
    trabajadorRepository: TrabajadorRepository,
    spaRepository: SpaRepository
) {
    val coroutineScope = rememberCoroutineScope()
    var trabajadores by remember { mutableStateOf<List<TrabajadorConNombre>>(emptyList()) }
    var mascotas by remember { mutableStateOf<List<Mascota>>(emptyList()) }
    var selectedTrabajador by remember { mutableStateOf<TrabajadorConNombre?>(null) }
    var selectedMascota by remember { mutableStateOf<Mascota?>(null) }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var servicioSpa by remember { mutableStateOf("Baño Básico") }
    var instrucciones by remember { mutableStateOf("") }
    var trabajadorExpanded by remember { mutableStateOf(false) }
    var mascotaExpanded by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    LaunchedEffect(Unit) {
        trabajadores = trabajadorRepository.getAllTrabajadoresWithNombre()
        mascotas = mascotaRepository.getMascotasByClienteId(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF3F4F6))
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
        }

        Text(
            "Agendar Servicio de Spa para Mascota",
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text("Seleccionar Mascota", fontSize = 16.sp, color = Color.Black)
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedMascota?.nombre ?: "Seleccione una mascota",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { mascotaExpanded = !mascotaExpanded },
                trailingIcon = {
                    IconButton(onClick = { mascotaExpanded = !mascotaExpanded }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp), // Bordes redondeados

            )

            DropdownMenu(
                expanded = mascotaExpanded,
                onDismissRequest = { mascotaExpanded = false }
            ) {
                mascotas.forEach { mascota ->
                    DropdownMenuItem(
                        onClick = {
                            selectedMascota = mascota
                            mascotaExpanded = false
                        },
                        text = { Text(mascota.nombre) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Seleccionar Trabajador", fontSize = 16.sp, color = Color.Black)
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedTrabajador?.nombre ?: "Seleccione un trabajador",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { trabajadorExpanded = !trabajadorExpanded },
                trailingIcon = {
                    IconButton(onClick = { trabajadorExpanded = !trabajadorExpanded }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp), // Bordes redondeados
            )

            DropdownMenu(
                expanded = trabajadorExpanded,
                onDismissRequest = { trabajadorExpanded = false }
            ) {
                trabajadores.forEach { trabajador ->
                    DropdownMenuItem(
                        onClick = {
                            selectedTrabajador = trabajador
                            trabajadorExpanded = false
                        },
                        text = { Text(trabajador.nombre) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Fecha del Servicio", fontSize = 16.sp, color = Color.Black)
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    android.app.DatePickerDialog(
                        navController.context,
                        { _, year, month, dayOfMonth ->
                            calendar.set(year, month, dayOfMonth)
                            selectedDate = dateFormat.format(calendar.time)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).apply {
                        datePicker.minDate = System.currentTimeMillis() // Restringir fechas pasadas
                    }.show()
                }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                }
            },
            modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp) // Bordes redondeados
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Hora del Servicio", fontSize = 16.sp, color = Color.Black)
        OutlinedTextField(
            value = selectedTime,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    TimePickerDialog(
                        navController.context,
                        { _, hourOfDay, minute ->
                            selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Seleccionar hora")
                }
            },
            modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp) // Bordes redondeados
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Servicio de Spa", fontSize = 16.sp, color = Color.Black)
        OutlinedTextField(
            value = servicioSpa,
            onValueChange = { servicioSpa = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp), // Bordes redondeados

        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Instrucciones Especiales", fontSize = 16.sp, color = Color.Black)
        TextField(
            value = instrucciones,
            onValueChange = { instrucciones = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp), // Bordes redondeados
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    selectedTrabajador?.let { trabajador ->
                        val servicioSpaHistorial = ServicioSpaHistorial(
                            mascota_id = selectedMascota?.mascotaId ?: 0,
                            trabajador_id = trabajador.trabajadorId,
                            fecha_servicio = selectedDate,
                            hora_servicio = selectedTime,
                            servicio = servicioSpa,
                            instrucciones = instrucciones
                        )
                        spaRepository.addServicioSpa(servicioSpaHistorial)
                        showMessage = true
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE26563)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Guardar Servicio de Spa", color = Color.White)
        }

        if (showMessage) {
            AlertDialog(
                onDismissRequest = { showMessage = false },
                confirmButton = {
                    TextButton(onClick = { showMessage = false }) {
                        Text("Aceptar", color = Color.Black)
                    }
                },
                title = {
                    Text("Éxito", color = Color.Black)
                },
                text = {
                    Text(
                        "Servicio de Spa guardado exitosamente.",
                        color = Color.Black
                    )
                },
                containerColor = Color(0xFFFFEBEE), // Fondo del cuadro
                shape = RoundedCornerShape(16.dp) // Bordes redondeados
            )
        }
    }
}