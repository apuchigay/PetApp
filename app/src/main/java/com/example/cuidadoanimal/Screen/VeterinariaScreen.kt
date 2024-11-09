package com.example.cuidadoanimal.Screen

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cuidadoanimal.Model.HistorialMedico
import com.example.cuidadoanimal.Model.TrabajadorConNombre
import com.example.cuidadoanimal.Repository.HistorialMedicoRepository
import com.example.cuidadoanimal.Repository.TrabajadorRepository
import kotlinx.coroutines.launch
import java.util.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Popup
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VeterinariaScreen(
    navController: NavController,
    mascotaId: Int,
    userId: Int,
    historialMedicoRepository: HistorialMedicoRepository,
    trabajadorRepository: TrabajadorRepository
) {
    val coroutineScope = rememberCoroutineScope()
    var trabajadores by remember { mutableStateOf<List<TrabajadorConNombre>>(emptyList()) }
    var selectedTrabajador by remember { mutableStateOf<TrabajadorConNombre?>(null) }
    var fechaVisita by remember { mutableStateOf("Programar fecha") }
    var descripcion by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Obtener lista de trabajadores al cargar la pantalla
    LaunchedEffect(Unit) {
        trabajadores = trabajadorRepository.getAllTrabajadoresWithNombre()
    }

    // Función para convertir el valor de la fecha
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    // Configuración de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF3F4F6))
    ) {
        // Botón de regreso
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
        }

        Text(
            "Agendar Visita Veterinaria",
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Selección del trabajador utilizando DropdownMenu
        Text("Seleccionar Trabajador", fontSize = 16.sp, color = Color.Black)
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedTrabajador?.nombre ?: "Seleccione un trabajador",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    ),
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                trabajadores.forEach { trabajador ->
                    DropdownMenuItem(
                        onClick = {
                            selectedTrabajador = trabajador
                            expanded = false
                        },
                        text = { Text(trabajador.nombre) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selección de fecha utilizando DatePicker
        Text("Fecha de la visita", fontSize = 16.sp, color = Color.Black)
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            label = { Text("Fecha de visita") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Seleccionar fecha"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        // Mostrar el DatePicker en un Popup
        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción de la visita
        Text("Descripción", fontSize = 16.sp, color = Color.Black)
        TextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                ),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para guardar la solicitud
        Button(
            onClick = {
                coroutineScope.launch {
                    selectedTrabajador?.let { trabajador ->
                        val historialMedico = HistorialMedico(
                            mascota_id = mascotaId,
                            trabajador_id = trabajador.trabajadorId,
                            fecha_visita = selectedDate,
                            descripcion = descripcion
                        )
                        historialMedicoRepository.addHistorialMedico(historialMedico)
                        navController.popBackStack()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE26563)),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Guardar", color = Color.White)
        }
    }
}

// Función de conversión de milisegundos a fecha
fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
