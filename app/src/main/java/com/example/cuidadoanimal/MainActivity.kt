package com.example.cuidadoanimal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cuidadoanimal.Screen.Navigation
import com.example.cuidadoanimal.Database.CuidadoAnimalDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener la instancia de la base de datos
        val database = CuidadoAnimalDatabase.getDatabase(applicationContext)
        val autenticacionDao = database.autenticacionDao() // Linea del error

        setContent {
            // Pasar el autenticacionDao a la función de Navigation
            Navigation(autenticacionDao = autenticacionDao)
        }
    }
}