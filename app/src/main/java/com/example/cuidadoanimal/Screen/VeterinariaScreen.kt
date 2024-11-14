package com.example.cuidadoanimal.Screen

import android.app.DatePickerDialog
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
import androidx.compose.ui.draw.clip
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
    var expanded by remember { mutableStateOf(false) }
    var descripcion by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) } // Estado para el AlertDialog
    val calendar = Calendar.getInstance()

    // Obtener lista de trabajadores al cargar la pantalla
    LaunchedEffect(Unit) {
        trabajadores = trabajadorRepository.getAllTrabajadoresWithNombre()
    }

    // Función para convertir el valor de la fecha
    val currentDate = calendar.time.toString()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
            .padding(16.dp)
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
                    .clickable { expanded = !expanded },
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color.Gray,
                    cursorColor = Color.Black
                ),
                shape = RoundedCornerShape(25.dp)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.White, shape = RoundedCornerShape(25.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(25.dp))
                    .heightIn(max = 120.dp)
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

        // Selección de la fecha utilizando DatePickerDialog
        Text("Fecha de la visita", fontSize = 16.sp, color = Color.Black)
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    DatePickerDialog(
                        navController.context,
                        { _, year, month, dayOfMonth ->
                            selectedDate = "$dayOfMonth/${month + 1}/$year"
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).apply {
                        datePicker.minDate = calendar.timeInMillis // Restringe a fechas futuras
                    }.show()
                }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color.Gray
            ),
            shape = RoundedCornerShape(25.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción de la visita
        Text("Descripción", fontSize = 16.sp, color = Color.Black)
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(25.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(25.dp),
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
                        showDialog = true // Mostrar diálogo al guardar
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE26563)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Guardar", color = Color.White)
        }
    }

    // Diálogo de confirmación
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        selectedTrabajador = null // Limpiar selección del trabajador
                        selectedDate = ""         // Limpiar fecha seleccionada
                        descripcion = ""          // Limpiar descripción
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE26563)),
                    modifier = Modifier.fillMaxWidth() // Para centrar el botón en toda la fila
                ) {
                    Text("Aceptar", color = Color.White)
                }
            },
            title = {
                Text(
                    text = "Registro Exitoso",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = "La visita veterinaria ha sido registrada correctamente.",
                    color = Color.Black
                )
            },
            containerColor = Color.White, // Fondo blanco
            shape = RoundedCornerShape(25.dp) // Esquinas redondeadas
        )
    }
}
