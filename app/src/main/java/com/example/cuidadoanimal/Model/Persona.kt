package com.example.cuidadoanimal.Model

import androidx.room.*

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personas")
data class Persona(
    @PrimaryKey(autoGenerate = true) val personaId: Int = 0,
    val nombre: String,
    val email: String,
    val telefono: String,
    val direccion: String
)