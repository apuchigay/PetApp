package com.example.cuidadoanimal.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "clientes",
    foreignKeys = [ForeignKey(
        entity = Persona::class,
        parentColumns = ["personaId"],
        childColumns = ["persona_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Cliente(
    @PrimaryKey(autoGenerate = true) val clienteId: Int = 0,
    val persona_id: Int  // Foreign key referencing Persona
)