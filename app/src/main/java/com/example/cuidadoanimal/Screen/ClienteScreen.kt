import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cuidadoanimal.Model.Cliente
import com.example.cuidadoanimal.Model.Persona
import com.example.cuidadoanimal.Repository.ClienteRepository
import com.example.cuidadoanimal.Repository.PersonaRepository
import com.example.cuidadoanimal.Database.CuidadoAnimalDatabase
import com.example.cuidadoanimal.Model.Autenticacion
import com.example.cuidadoanimal.Repository.AutenticacionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteScreen(navController: NavHostController, db: CuidadoAnimalDatabase) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") } // Mensaje de error para el correo

    // Crear instancias de los repositorios
    val clienteRepository = ClienteRepository(db.clienteDao())
    val personaRepository = PersonaRepository(db.personaDao())
    val autenticacionRepository = AutenticacionRepository(
        db.autenticacionDao(),
        db.personaDao()
    )

    // Expresión regular para validar el formato del correo electrónico
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B303F))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro de Cliente",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campos de entrada
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFD4E4FF)),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = if (emailRegex.matches(it)) "" else "Correo no válido"
            },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage.isNotEmpty(),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFD4E4FF)),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFD4E4FF)),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFD4E4FF)),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            placeholder = { Text("Ingrese su contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFD4E4FF)),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para confirmar registro
        Button(
            onClick = {
                if (nombre.isNotBlank() && emailRegex.matches(email) && telefono.isNotBlank() && direccion.isNotBlank() && password.isNotBlank()) {
                    val nuevaPersona = Persona(
                        nombre = nombre,
                        email = email,
                        telefono = telefono,
                        direccion = direccion
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        val personaId = personaRepository.insertPersona(nuevaPersona).toInt()
                        val nuevoCliente = Cliente(
                            persona_id = personaId
                        )

                        // Insertar cliente
                        clienteRepository.insertCliente(nuevoCliente)

                        // Insertar autenticación con tipo de usuario 2 para cliente
                        autenticacionRepository.insertAutenticacionWithUserType(
                            personaId = personaId,
                            email = email,
                            password = password,
                            tipoUsuario = 2
                        )

                        successMessage = "Registro exitoso para $nombre"

                        // Limpiar los campos después del registro exitoso
                        nombre = ""
                        email = ""
                        telefono = ""
                        direccion = ""
                        password = ""
                        errorMessage = ""
                    }
                } else {
                    successMessage = "Por favor, completa todos los campos"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9F02))
        ) {
            Text("Confirmar Registro")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de volver
        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A7AF2))
        ) {
            Text("Volver", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (successMessage.isNotBlank()) {
            Text(text = successMessage, color = Color.White)
        }
    }
}
