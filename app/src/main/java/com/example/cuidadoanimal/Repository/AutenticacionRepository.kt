package com.example.cuidadoanimal.Repository

import com.example.cuidadoanimal.DAO.AutenticacionDao
import com.example.cuidadoanimal.Model.Autenticacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

class AutenticacionRepository(private val autenticacionDao: AutenticacionDao) {

    // Generar una clave de cifrado
    private val secretKey: SecretKey = generateKey()

    // Metodo para generar una clave AES
    private fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        return keyGenerator.generateKey()
    }

    // Metodo para cifrar contraseñas
    private fun encrypt(password: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(password.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    // Metodo para descifrar contraseñas
    private fun decrypt(encryptedPassword: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decodedBytes = Base64.decode(encryptedPassword, Base64.DEFAULT)
        val originalBytes = cipher.doFinal(decodedBytes)
        return String(originalBytes)
    }

    // Insertar autenticación con cifrado de contraseña
    suspend fun insertAutenticacion(autenticacion: Autenticacion) {
        withContext(Dispatchers.IO) {
            val encryptedPassword = encrypt(autenticacion.password)
            val autenticacionConCifrado = autenticacion.copy(password = encryptedPassword)
            autenticacionDao.insertAutenticacion(autenticacionConCifrado)
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
            val decryptedPassword = decrypt(autenticacion.password)
            decryptedPassword == password
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
            val encryptedPassword = encrypt(password)
            val autenticacion = Autenticacion(
                persona_id = personaId,
                email = email,
                password = encryptedPassword,
                tipo_usuario = tipoUsuario
            )
            autenticacionDao.insertAutenticacion(autenticacion)
        }
    }
}
