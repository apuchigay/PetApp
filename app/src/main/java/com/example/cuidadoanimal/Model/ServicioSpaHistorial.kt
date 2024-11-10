package com.example.cuidadoanimal.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "servicio_spa_historial")
data class ServicioSpaHistorial(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mascota_id: Int,
    val trabajador_id: Int,
    val fecha_servicio: String,
    val hora_servicio: String,
    val servicio: String,
    val instrucciones: String
)
