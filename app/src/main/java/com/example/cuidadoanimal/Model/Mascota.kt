package com.example.cuidadoanimal.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "mascotas",
    foreignKeys = [ForeignKey(
        entity = Cliente::class,
        parentColumns = ["clienteId"],
        childColumns = ["cliente_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Mascota(
    @PrimaryKey(autoGenerate = true) val mascotaId: Int = 0,
    val nombre: String,
    val tipo: String,
    val edad: Int,
    val raza: String,
    val peso: Double,
    @ColumnInfo(name = "cliente_id") val clienteId: Int  // Foreign key referencing Cliente
)
