package com.example.cuidadoanimal.Screen

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cuidadoanimal.Model.HistorialMedico
import com.example.cuidadoanimal.Model.Trabajador
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
    historialMedicoRepository: HistorialMedicoRepository,
    trabajadorRepository: TrabajadorRepository
) {
    val coroutineScope = rememberCoroutineScope()
    var trabajadores by remember { mutableStateOf<List<TrabajadorConNombre>>(emptyList()) }
    var selectedTrabajador by remember { mutableStateOf<TrabajadorConNombre?>(null) }
    var fechaVisita by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // Obtener lista de trabajadores al cargar la pantalla
    LaunchedEffect(Unit) {
        trabajadores = trabajadorRepository.getAllTrabajadoresWithNombre()
    }

    // Configuración del DatePicker
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        navController.context,
        { _, year, month, day ->
            fechaVisita = "$day/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF3F4F6))
    ) {
        Text(
            "Agendar Visita Veterinaria",
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Selección del trabajador
        Text("Seleccionar Trabajador", fontSize = 16.sp, color = Color.Black)
        ExposedDropdownMenuBox(
            expanded = selectedTrabajador == null,
            onExpandedChange = { },
        ) {
            TextField(
                value = selectedTrabajador?.nombre ?: "Seleccione un trabajador",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = selectedTrabajador == null)
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White
                )
            )
            ExposedDropdownMenu(
                expanded = selectedTrabajador == null,
                onDismissRequest = { /* Opcional: Ocultar menú al seleccionar */ }
            ) {
                trabajadores.forEach { trabajador ->
                    DropdownMenuItem(
                        text = { Text(trabajador.nombre) },
                        onClick = { selectedTrabajador = trabajador }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selección de fecha
        Text("Fecha de la visita", fontSize = 16.sp, color = Color.Black)
        TextField(
            value = fechaVisita,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { datePickerDialog.show() },
            readOnly = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción de la visita
        Text("Descripción", fontSize = 16.sp, color = Color.Black)
        TextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White
            ),
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
                            fecha_visita = fechaVisita,
                            descripcion = descripcion
                        )
                        historialMedicoRepository.addHistorialMedico(historialMedico)
                        navController.popBackStack() // Regresa a la pantalla anterior
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
