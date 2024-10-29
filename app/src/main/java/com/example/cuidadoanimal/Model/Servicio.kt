package com.example.cuidadoanimal.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "servicios")
data class Servicio(
    @PrimaryKey(autoGenerate = true) val servicioId: Int = 0,
    val tipo_servicio: String,
    val descripcion: String,
    val precio: Double,
    val duracion_estimada: Int
)