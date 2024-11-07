package com.example.cuidadoanimal.Screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cuidadoanimal.Database.CuidadoAnimalDatabase
import com.example.cuidadoanimal.Repository.AutenticacionRepository
import com.example.cuidadoanimal.Repository.MascotaRepository

@Composable
fun Navigation(navController: NavHostController, db: CuidadoAnimalDatabase) {
    val autenticacionRepository = AutenticacionRepository(db.autenticacionDao(), db.personaDao())
    val mascotaRepository = MascotaRepository(db.mascotaDao())

    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(navController)
        }
        composable("login_screen") {
            LoginScreen(navController, db)
        }
        // Ruta para la pantalla de inicio con userId como argumento
        composable("inicio_screen/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 1
            InicioScreen(navController, userId, autenticacionRepository)
        }
        // Ruta para la pantalla de mascota con clienteId como argumento
        composable("mascota_screen/{clienteId}") { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getString("clienteId")?.toIntOrNull() ?: 1
            MascotaScreen(navController, clienteId, mascotaRepository)
        }
        // Ruta para la nueva pantalla de registro
        composable("registro_screen") {
            RegistroScreen(navController, db) // Ahora se pasa db en lugar de autenticacionRepository
        }
    }
}