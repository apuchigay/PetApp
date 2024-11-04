package com.example.cuidadoanimal.Screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cuidadoanimal.Repository.AutenticacionRepository
import com.example.cuidadoanimal.Database.CuidadoAnimalDatabase

@Composable
fun Navigation(navController: NavHostController, autenticacionRepository: AutenticacionRepository, db: CuidadoAnimalDatabase) {
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(navController)
        }
        composable("cliente_screen") {
            ClienteScreen(navController, db)
        }
        composable("trabajador_screen") {
            TrabajadorScreen(navController, db) // Se pasa `db` aquí
        }
    }
}