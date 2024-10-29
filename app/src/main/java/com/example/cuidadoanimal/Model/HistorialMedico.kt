package com.example.cuidadoanimal.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "historial_medico",
    foreignKeys = [ForeignKey(
        entity = Mascota::class,
        parentColumns = ["mascotaId"],
        childColumns = ["mascota_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class HistorialMedico(
    @PrimaryKey(autoGenerate = true) val historialId: Int = 0,
    val mascota_id: Int,  // Foreign key referencing Mascota
    val fecha_visita: String,
    val descripcion: String
)