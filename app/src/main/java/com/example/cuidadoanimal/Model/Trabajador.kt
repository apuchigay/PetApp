package com.example.cuidadoanimal.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cuidadoanimal.Database.CuidadoAnimalDatabase.Converters

@Entity(
    tableName = "trabajadores",
    foreignKeys = [ForeignKey(
        entity = Persona::class,
        parentColumns = ["personaId"],
        childColumns = ["persona_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
@TypeConverters(Converters::class) // Para manejar la lista de especialidades
data class Trabajador(
    @PrimaryKey(autoGenerate = true) val trabajadorId: Int = 0,
    val persona_id: Int,  // Foreign key referencing Persona
    val especialidades: List<String>, // Ahora es una lista
    val calificacion: Float
)