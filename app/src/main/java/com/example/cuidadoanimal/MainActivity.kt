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

        // Nombre de la base de datos
        val databaseName = "cuidado_animal_database"

        // Eliminar la base de datos al iniciar la aplicación
        applicationContext.deleteDatabase(databaseName)

        // Obtener una nueva instancia de la base de datos
        val database = CuidadoAnimalDatabase.getDatabase(applicationContext)
        val autenticacionRepository = AutenticacionRepository(database.autenticacionDao())

        setContent {
            val navController = rememberNavController()

            Navigation(
                navController = navController,
                autenticacionRepository = autenticacionRepository,
                db = database
            )
        }
    }
}
