package com.example.cuidadoanimal.Repository

import com.example.cuidadoanimal.Model.PaseoHistorial
import com.example.cuidadoanimal.DAO.*
import com.example.cuidadoanimal.Database.PaseoHistorialDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PaseoRepository(private val paseoHistorialDao: PaseoHistorialDao) {

    // Función para agregar un nuevo paseo a la base de datos
    suspend fun addPaseo(paseoHistorial: PaseoHistorial) {
        withContext(Dispatchers.IO) {
            paseoHistorialDao.insertPaseo(paseoHistorial)
        }
    }

    // Función para obtener todos los paseos de una mascota específica
    suspend fun getPaseosByMascotaId(mascotaId: Int): List<PaseoHistorial> {
        return withContext(Dispatchers.IO) {
            paseoHistorialDao.getPaseosByMascotaId(mascotaId)
        }
    }
}
