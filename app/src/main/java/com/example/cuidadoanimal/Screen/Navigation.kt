package com.example.cuidadoanimal.Screen

import ClienteScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cuidadoanimal.Database.CuidadoAnimalDatabase
import com.example.cuidadoanimal.Repository.AutenticacionRepository
import com.example.cuidadoanimal.Repository.MascotaRepository // Asegúrate de importar el repositorio de mascotas

@Composable
fun Navigation(navController: NavHostController, db: CuidadoAnimalDatabase) {
    val autenticacionRepository = AutenticacionRepository(db.autenticacionDao(), db.personaDao())
    val mascotaRepository = MascotaRepository(db.mascotaDao())

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
        // Define la ruta de inicio_screen con un argumento userId
        composable("inicio_screen/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 1
            InicioScreen(navController, userId, autenticacionRepository)
        }
        // Definir la ruta de mascota_screen con clienteId como argumento
        composable("mascota_screen/{clienteId}") { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getString("clienteId")?.toIntOrNull() ?: 1
            MascotaScreen(navController, clienteId, mascotaRepository)
        }
    }
}

