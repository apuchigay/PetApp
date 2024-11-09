package com.example.cuidadoanimal.Repository

import com.example.cuidadoanimal.DAO.HistorialMedicoDao
import com.example.cuidadoanimal.Model.HistorialMedico

class HistorialMedicoRepository(private val historialMedicoDao: HistorialMedicoDao) {

    // Metodo para agregar un nuevo historial médico
    suspend fun addHistorialMedico(historialMedico: HistorialMedico) {
        historialMedicoDao.insert(historialMedico)
    }

    // Metodo para obtener el historial médico de una mascota específica
    suspend fun getHistorialByMascotaId(mascotaId: Int): List<HistorialMedico> {
        return historialMedicoDao.getHistorialByMascotaId(mascotaId)
    }
}
