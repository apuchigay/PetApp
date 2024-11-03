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
        keyGenerator.init(256) // Puedes elegir 128, 192 o 256 bits
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

    suspend fun insertAutenticacion(autenticacion: Autenticacion) {
        withContext(Dispatchers.IO) {
            // Cifrar la contraseña antes de insertar
            val encryptedPassword = encrypt(autenticacion.password)
            autenticacionDao.insertAutenticacion(autenticacion.copy(password = encryptedPassword))
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

    // Metodo para verificar la contraseña
    suspend fun verifyPassword(email: String, password: String): Boolean {
        val autenticacion = getAutenticacionByEmail(email)
        return if (autenticacion != null) {
            val decryptedPassword = decrypt(autenticacion.password)
            decryptedPassword == password
        } else {
            false
        }
    }
}
