package com.example.cuidadoanimal.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "autenticacion",
    foreignKeys = [
        ForeignKey(
            entity = Persona::class,
            parentColumns = ["personaId"],
            childColumns = ["persona_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Autenticacion(
    @PrimaryKey(autoGenerate = true) val autenticacionId: Int = 0,
    val email: String,         // Puede usarse como nombre de usuario
    val password: String,
    val persona_id: Int,       // Relación con Persona
    val tipo_usuario: Int      // 1 para Trabajador, 2 para Cliente
)
