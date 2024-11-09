package com.example.cuidadoanimal.Screen

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
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

        Text("Seleccionar Trabajador", fontSize = 16.sp, color = Color.Black)

        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = selectedTrabajador?.nombre ?: "Seleccione un trabajador",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
            )
        }

        // "LargeDropMenu" estilo diálogo centrado debajo de "Seleccionar Trabajador"
        if (expanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                    .shadow(8.dp, shape = RoundedCornerShape(12.dp))
                    .padding(8.dp)
                    .heightIn(max = 250.dp) // Limitar el tamaño máximo
                    .offset(y = 8.dp) // Separar un poco del TextField
            ) {
                LazyColumn {
                    items(trabajadores) { trabajador ->
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedTrabajador = trabajador
                                expanded = false
                            }
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                        ) {
                            Text(
                                text = trabajador.nombre,
                                fontSize = 16.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Divider(color = Color.Gray, thickness = 0.5.dp)
                        }
                    }
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
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
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
                            fecha_visita = fechaVisita,
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