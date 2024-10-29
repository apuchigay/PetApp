package com.example.cuidadoanimal.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "solicitudes_servicio",
    foreignKeys = [
        ForeignKey(entity = Cliente::class, parentColumns = ["clienteId"], childColumns = ["cliente_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Trabajador::class, parentColumns = ["trabajadorId"], childColumns = ["trabajador_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Mascota::class, parentColumns = ["mascotaId"], childColumns = ["mascota_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Servicio::class, parentColumns = ["servicioId"], childColumns = ["servicio_id"], onDelete = ForeignKey.CASCADE)
    ]
)
data class SolicitudServicio(
    @PrimaryKey(autoGenerate = true) val solicitudId: Int = 0,
    val cliente_id: Int,  // Foreign key referencing Cliente
    val trabajador_id: Int,  // Foreign key referencing Trabajador
    val mascota_id: Int,  // Foreign key referencing Mascota
    val servicio_id: Int,  // Foreign key referencing Servicio
    val fecha_solicitud: String,
    val fecha_programada: String,
    val estado: String,
    val valoracion: Float?
)