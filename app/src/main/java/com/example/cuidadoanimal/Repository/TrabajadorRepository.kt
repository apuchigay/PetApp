package com.example.cuidadoanimal.Repository

import com.example.cuidadoanimal.DAO.TrabajadorDao
import com.example.cuidadoanimal.Model.Trabajador
import com.example.cuidadoanimal.Model.TrabajadorConNombre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrabajadorRepository(private val trabajadorDao: TrabajadorDao) {

    suspend fun insertTrabajador(trabajador: Trabajador) {
        withContext(Dispatchers.IO) {
            trabajadorDao.insertTrabajador(trabajador)
        }
    }

    suspend fun updateTrabajador(trabajador: Trabajador) {
        withContext(Dispatchers.IO) {
            trabajadorDao.updateTrabajador(trabajador)
        }
    }

    suspend fun deleteTrabajador(trabajador: Trabajador) {
        withContext(Dispatchers.IO) {
            trabajadorDao.deleteTrabajador(trabajador)
        }
    }

    suspend fun getAllTrabajadores(): List<Trabajador> {
        return withContext(Dispatchers.IO) {
            trabajadorDao.getAllTrabajadores()
        }
    }

    suspend fun getTrabajadorById(id: Int): Trabajador? {
        return withContext(Dispatchers.IO) {
            trabajadorDao.getTrabajadorById(id)
        }
    }

    // Nueva función para obtener trabajadores con nombres
    suspend fun getAllTrabajadoresWithNombre(): List<TrabajadorConNombre> {
        return withContext(Dispatchers.IO) {
            trabajadorDao.getAllTrabajadoresWithNombre()
        }
    }
}
