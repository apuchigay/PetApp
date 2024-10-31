package com.example.cuidadoanimal.Repository

import com.example.cuidadoanimal.DAO.PersonaDao
import com.example.cuidadoanimal.Model.Persona
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersonaRepository(private val personaDao: PersonaDao) {

    suspend fun insertPersona(persona: Persona) {
        withContext(Dispatchers.IO) {
            personaDao.insertPersona(persona)
        }
    }

    suspend fun updatePersona(persona: Persona) {
        withContext(Dispatchers.IO) {
            personaDao.updatePersona(persona)
        }
    }

    suspend fun deletePersona(persona: Persona) {
        withContext(Dispatchers.IO) {
            personaDao.deletePersona(persona)
        }
    }

    suspend fun getAllPersonas(): List<Persona> {
        return withContext(Dispatchers.IO) {
            personaDao.getAllPersonas()
        }
    }

    suspend fun getPersonaById(id: Int): Persona? {
        return withContext(Dispatchers.IO) {
            personaDao.getPersonaById(id)
        }
    }
}