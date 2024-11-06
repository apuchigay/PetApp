package com.example.cuidadoanimal.Repository

import com.example.cuidadoanimal.DAO.MascotaDao
import com.example.cuidadoanimal.Model.Mascota

class MascotaRepository(private val mascotaDao: MascotaDao) {

    // Insertar una nueva mascota
    suspend fun addMascota(mascota: Mascota) {
        mascotaDao.insertMascota(mascota)
    }

    // Obtener todas las mascotas
    suspend fun getAllMascotas(): List<Mascota> {
        return mascotaDao.getAllMascotas()
    }

    // Obtener una mascota por su ID
    suspend fun getMascotaById(id: Int): Mascota? {
        return mascotaDao.getMascotaById(id)
    }

    // Actualizar la información de una mascota
    suspend fun updateMascota(mascota: Mascota) {
        mascotaDao.updateMascota(mascota)
    }

    // Eliminar una mascota
    suspend fun deleteMascota(mascota: Mascota) {
        mascotaDao.deleteMascota(mascota)
    }

    // Obtener todas las mascotas de un cliente específico
    suspend fun getMascotasByClienteId(clienteId: Int): List<Mascota> {
        return mascotaDao.getMascotasByClienteId(clienteId)
    }
}
