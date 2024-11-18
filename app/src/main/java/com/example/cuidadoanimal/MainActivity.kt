package com.example.cuidadoanimal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.cuidadoanimal.Screen.Navigation
import com.example.cuidadoanimal.Database.CuidadoAnimalDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Nombre de la base de datos
        val databaseName = "cuidado_animal_database"

        // Obtener una nueva instancia de la base de datos
        val database = CuidadoAnimalDatabase.getDatabase(applicationContext)

        setContent {
            val navController = rememberNavController()

            Navigation(
                navController = navController,
                db = database // Elimina autenticacionRepository aquí
            )
        }
    }
}