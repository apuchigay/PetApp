package com.example.cuidadoanimal.Repository

import com.example.cuidadoanimal.Database.CuidadoAnimalDatabase
import com.example.cuidadoanimal.Model.HistorialMedico

class HistorialMedicoRepository(private val db: CuidadoAnimalDatabase) {

    // Método para agregar un nuevo historial médico
    suspend fun addHistorialMedico(historialMedico: HistorialMedico) {
        db.historialMedicoDao().insert(historialMedico)
    }

    // Método para obtener el historial médico de una mascota específica
    suspend fun getHistorialByMascotaId(mascotaId: Int): List<HistorialMedico> {
        return db.historialMedicoDao().getHistorialByMascotaId(mascotaId)
    }
}
