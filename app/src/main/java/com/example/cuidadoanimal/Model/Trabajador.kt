package com.example.cuidadoanimal.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "trabajadores",
    foreignKeys = [ForeignKey(
        entity = Persona::class,
        parentColumns = ["personaId"],
        childColumns = ["persona_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Trabajador(
    @PrimaryKey(autoGenerate = true) val trabajadorId: Int = 0,
    val persona_id: Int,  // Foreign key referencing Persona
    val especialidades: String,
    val calificacion: Float
)