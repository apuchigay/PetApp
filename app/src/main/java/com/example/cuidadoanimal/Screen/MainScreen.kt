package com.example.cuidadoanimal.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cuidadoanimal.R

@Composable
fun MainScreen(navController: NavHostController) {
    // Degradado de fondo
    val gradientColors = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE76A68),  // Color superior
            Color(0xFFCF4E49)   // Color inferior
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientColors)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Texto de bienvenida
        Text(
            text = "¡El mejor cuidado para tu mascota!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Nunca le faltará nada de nuevo",
            fontSize = 16.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Imagen de logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de Cuidado Animal",
            modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de Iniciar Sesión
        Button(
            onClick = { navController.navigate("login_screen") },
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "Iniciar Sesión",
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enlace de texto para registrarse
        Text(
            text = "¿No tienes cuenta? Regístrate ahora",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { navController.navigate("registro_screen") }
        )
    }
}
