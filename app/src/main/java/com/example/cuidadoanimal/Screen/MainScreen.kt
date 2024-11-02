package com.example.cuidadoanimal.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cuidadoanimal.R

@Composable
fun MainScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B303F))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen de logo
        Image(
            painter = painterResource(id = R.drawable.logo), // Reemplaza con el nombre correcto de tu imagen
            contentDescription = "Logo de Cuidado Animal",
            modifier = Modifier.size(260.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de Iniciar Sesión
        Button(
            onClick = { navController.navigate("login_screen") },
            modifier = Modifier
                .width(300.dp) // Ancho reducido
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9F02))
        ) {
            Text(text = "Iniciar Sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de Registro Cliente
        Button(
            onClick = { navController.navigate("cliente_screen") },
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A7AF2))
        ) {
            Text(text = "Registrarse como Cliente")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de Registro Trabajador
        Button(
            onClick = { navController.navigate("trabajador_screen") },
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A7AF2))
        ) {
            Text(text = "Registrarse como Trabajador")
        }
    }
}