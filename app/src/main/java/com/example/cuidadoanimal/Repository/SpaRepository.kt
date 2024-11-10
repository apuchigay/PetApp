package com.example.cuidadoanimal.Repository

import com.example.cuidadoanimal.DAO.SpaDao
import com.example.cuidadoanimal.Model.ServicioSpaHistorial

class SpaRepository(private val spaDao: SpaDao) {

    // Función para agregar un nuevo registro de servicio de spa
    suspend fun addServicioSpa(servicioSpaHistorial: ServicioSpaHistorial) {
        spaDao.insertServicioSpa(servicioSpaHistorial)
    }

    // Función para obtener todos los registros de servicios de spa
    suspend fun getServiciosSpa(): List<ServicioSpaHistorial> {
        return spaDao.getAllServiciosSpa()
    }

    // Función para obtener los servicios de spa de una mascota específica
    suspend fun getServiciosSpaByMascotaId(mascotaId: Int): List<ServicioSpaHistorial> {
        return spaDao.getServiciosSpaByMascotaId(mascotaId)
    }
}
