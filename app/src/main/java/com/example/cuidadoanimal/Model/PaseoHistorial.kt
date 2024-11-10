package com.example.cuidadoanimal.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PaseoHistorial")
data class PaseoHistorial(
    @PrimaryKey(autoGenerate = true) val paseoId: Int = 0,
    val mascota_id: Int,
    val trabajador_id: Int,
    val fecha_paseo: String,
    val duracion: Int,
    val instrucciones: String
)
