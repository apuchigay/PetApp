package com.example.cuidadoanimal.DAO

import androidx.room.*
import com.example.cuidadoanimal.Model.Autenticacion

@Dao
interface AutenticacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAutenticacion(autenticacion: Autenticacion)

    @Update
    suspend fun updateAutenticacion(autenticacion: Autenticacion)

    @Delete
    suspend fun deleteAutenticacion(autenticacion: Autenticacion)

    @Query("SELECT * FROM autenticacion WHERE email = :email LIMIT 1")
    suspend fun getAutenticacionByEmail(email: String): Autenticacion?
}