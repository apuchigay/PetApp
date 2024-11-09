package com.example.cuidadoanimal.Screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cuidadoanimal.Database.CuidadoAnimalDatabase
import com.example.cuidadoanimal.Repository.AutenticacionRepository
import com.example.cuidadoanimal.Repository.HistorialMedicoRepository
import com.example.cuidadoanimal.Repository.MascotaRepository
import com.example.cuidadoanimal.Repository.TrabajadorRepository

@Composable
fun Navigation(navController: NavHostController, db: CuidadoAnimalDatabase) {
    val autenticacionRepository = AutenticacionRepository(db.autenticacionDao(), db.personaDao())
    val mascotaRepository = MascotaRepository(db.mascotaDao())
    val historialMedicoRepository = HistorialMedicoRepository(db.historialMedicoDao())
    val trabajadorRepository = TrabajadorRepository(db.trabajadorDao())

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
        // Ruta para la nueva pantalla de veterinaria con mascotaId como argumento
        composable("veterinaria_screen/{mascotaId}/{userId}") { backStackEntry ->
            val mascotaId = backStackEntry.arguments?.getString("mascotaId")?.toIntOrNull() ?: 1
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 1
            VeterinariaScreen(
                navController = navController,
                mascotaId = mascotaId,
                userId = userId,
                historialMedicoRepository = historialMedicoRepository,
                trabajadorRepository = trabajadorRepository
            )
        }
        // Ruta para la nueva pantalla de registro
        composable("registro_screen") {
            RegistroScreen(navController, db)
        }
    }
}
