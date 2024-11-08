package com.example.cuidadoanimal.Model

data class TrabajadorConNombre(
    val trabajadorId: Int,
    val personaId: Int,  // Asegúrate de que el nombre coincida con el alias en el DAO
    val especialidades: List<String>,
    val calificacion: Float,
    val nombre: String
)
