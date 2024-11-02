package com.example.cuidadoanimal.Screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cuidadoanimal.Repository.AutenticacionRepository

@Composable
fun Navigation(navController: NavHostController, autenticacionRepository: AutenticacionRepository) {
    NavHost(navController = navController, startDestination = "main_screen") {
        // Ruta para MainScreen
        composable("main_screen") {
            MainScreen(navController)
        }
        // Ruta para ClienteScreen
        composable("cliente_screen") {
            ClienteScreen(navController)
        }
        // Ruta para TrabajadorScreen
        composable("trabajador_screen") {
            TrabajadorScreen(navController)
        }
    }
}