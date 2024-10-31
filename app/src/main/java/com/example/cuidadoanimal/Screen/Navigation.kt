package com.example.cuidadoanimal.Screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cuidadoanimal.DAO.AutenticacionDao
import com.example.cuidadoanimal.Repository.AutenticacionRepository

@Composable
fun Navigation(autenticacionDao: AutenticacionDao) {
    // Crear la instancia del repositorio de autenticación
    val autenticacionRepository = AutenticacionRepository(autenticacionDao)

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "auth_screen") {
        // Pantalla de Autenticación
        composable("auth_screen") { AuthScreen(autenticacionRepository) } // Pasar autenticacionRepository

        // Futuras pantallas a continuación
    }
}