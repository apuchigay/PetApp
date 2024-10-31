package com.example.cuidadoanimal.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "autenticacion",
    foreignKeys = [
        ForeignKey(entity = Cliente::class, parentColumns = ["clienteId"], childColumns = ["cliente_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Trabajador::class, parentColumns = ["trabajadorId"], childColumns = ["trabajador_id"], onDelete = ForeignKey.CASCADE)
    ]
)
data class Autenticacion(
    @PrimaryKey(autoGenerate = true) val autenticacionId: Int = 0,
    val email: String,  // Puede usarse como nombre de usuario
    val password: String,
    val cliente_id: Int? = null,  // Relación opcional con Cliente
    val trabajador_id: Int? = null  // Relación opcional con Trabajador
)