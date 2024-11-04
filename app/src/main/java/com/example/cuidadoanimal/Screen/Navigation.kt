package com.example.cuidadoanimal.Screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cuidadoanimal.Database.CuidadoAnimalDatabase
import com.example.cuidadoanimal.Repository.AutenticacionRepository

@Composable
fun Navigation(navController: NavHostController, db: CuidadoAnimalDatabase) {
    // Crear la instancia de AutenticacionRepository
    val autenticacionRepository = AutenticacionRepository(db.autenticacionDao(), db.personaDao())

    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(navController)
        }
        composable("cliente_screen") {
            ClienteScreen(navController, db)
        }
        composable("trabajador_screen") {
            TrabajadorScreen(navController, db)
        }
        composable("login_screen") {
            LoginScreen(navController, db)
        }
        composable("inicio_screen") {
            val userId = 1 // Asegúrate de obtener este ID del contexto correcto
            InicioScreen(userId, autenticacionRepository) // Llamada actualizada
        }
    }
}
