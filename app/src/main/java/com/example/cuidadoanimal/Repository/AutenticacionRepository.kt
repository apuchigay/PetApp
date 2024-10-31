package com.example.cuidadoanimal.Repository

import com.example.cuidadoanimal.DAO.AutenticacionDao
import com.example.cuidadoanimal.Model.Autenticacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AutenticacionRepository(private val autenticacionDao: AutenticacionDao) {

    suspend fun insertAutenticacion(autenticacion: Autenticacion) {
        withContext(Dispatchers.IO) {
            autenticacionDao.insertAutenticacion(autenticacion)
        }
    }

    suspend fun updateAutenticacion(autenticacion: Autenticacion) {
        withContext(Dispatchers.IO) {
            autenticacionDao.updateAutenticacion(autenticacion)
        }
    }

    suspend fun deleteAutenticacion(autenticacion: Autenticacion) {
        withContext(Dispatchers.IO) {
            autenticacionDao.deleteAutenticacion(autenticacion)
        }
    }

    suspend fun getAutenticacionByEmail(email: String): Autenticacion? {
        return withContext(Dispatchers.IO) {
            autenticacionDao.getAutenticacionByEmail(email)
        }
    }
}