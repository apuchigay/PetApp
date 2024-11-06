package com.example.cuidadoanimal.Repository

import com.example.cuidadoanimal.DAO.AutenticacionDao
import com.example.cuidadoanimal.DAO.PersonaDao
import com.example.cuidadoanimal.Model.Autenticacion
import com.example.cuidadoanimal.Model.Persona
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class AutenticacionRepository(
    private val autenticacionDao: AutenticacionDao,
    private val personaDao: PersonaDao
) {

    // Convertir una cadena a su hash MD5
    private fun hashMD5(password: String): String {
        val md = MessageDigest.getInstance("MD5")
        val hashBytes = md.digest(password.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) } // Convertir bytes a hexadecimal
    }

    // Insertar autenticación con hash de contraseña
    suspend fun insertAutenticacion(autenticacion: Autenticacion) {
        withContext(Dispatchers.IO) {
            val hashedPassword = hashMD5(autenticacion.password)
            val autenticacionConHash = autenticacion.copy(password = hashedPassword)
            autenticacionDao.insertAutenticacion(autenticacionConHash)
        }
    }

    // Actualizar autenticación
    suspend fun updateAutenticacion(autenticacion: Autenticacion) {
        withContext(Dispatchers.IO) {
            autenticacionDao.updateAutenticacion(autenticacion)
        }
    }

    // Eliminar autenticación
    suspend fun deleteAutenticacion(autenticacion: Autenticacion) {
        withContext(Dispatchers.IO) {
            autenticacionDao.deleteAutenticacion(autenticacion)
        }
    }

    // Obtener autenticación por email
    suspend fun getAutenticacionByEmail(email: String): Autenticacion? {
        return withContext(Dispatchers.IO) {
            autenticacionDao.getAutenticacionByEmail(email)
        }
    }

    // Verificar contraseña
    suspend fun verifyPassword(email: String, password: String): Boolean {
        val autenticacion = getAutenticacionByEmail(email)
        return if (autenticacion != null) {
            val hashedPassword = hashMD5(password)
            hashedPassword == autenticacion.password
        } else {
            false
        }
    }

    // Obtener autenticación por persona_id
    suspend fun getAutenticacionByPersonaId(personaId: Int): Autenticacion? {
        return withContext(Dispatchers.IO) {
            autenticacionDao.getAutenticacionByPersonaId(personaId)
        }
    }

    // Insertar autenticación con tipo de usuario (1 para trabajador, 2 para cliente)
    suspend fun insertAutenticacionWithUserType(
        personaId: Int,
        email: String,
        password: String,
        tipoUsuario: Int
    ) {
        withContext(Dispatchers.IO) {
            val hashedPassword = hashMD5(password)
            val autenticacion = Autenticacion(
                persona_id = personaId,
                email = email,
                password = hashedPassword,
                tipo_usuario = tipoUsuario
            )
            autenticacionDao.insertAutenticacion(autenticacion)
        }
    }

    // Nueva función para autenticar usuario con email y password
    suspend fun autenticarUsuario(email: String, password: String): Autenticacion? {
        return withContext(Dispatchers.IO) {
            val hashedPassword = hashMD5(password)
            autenticacionDao.getAutenticacionByEmailAndPassword(email, hashedPassword)
        }
    }

    // Obtener datos de Autenticacion y Persona por persona_id
    suspend fun getUserWithDetailsById(personaId: Int): Pair<Autenticacion?, Persona?> {
        return withContext(Dispatchers.IO) {
            val autenticacion = autenticacionDao.getAutenticacionByPersonaId(personaId)
            // Obtener datos de la persona solo si se encontró autenticación
            val persona = autenticacion?.let { personaDao.getPersonaById(it.persona_id) }
            Pair(autenticacion, persona)
        }
    }
}
