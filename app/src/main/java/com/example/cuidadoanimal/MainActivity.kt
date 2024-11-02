package com.example.cuidadoanimal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.cuidadoanimal.Screen.Navigation
import com.example.cuidadoanimal.Database.CuidadoAnimalDatabase
import com.example.cuidadoanimal.Repository.AutenticacionRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener la instancia de la base de datos
        val database = CuidadoAnimalDatabase.getDatabase(applicationContext)

        // Crear una instancia del AutenticacionRepository con el DAO correspondiente
        val autenticacionRepository = AutenticacionRepository(database.autenticacionDao())

        setContent {
            // Crear el NavHostController
            val navController = rememberNavController()

            // Pasar el navController y autenticacionRepository a la función de Navigation
            Navigation(navController = navController, autenticacionRepository = autenticacionRepository)
        }
    }
}