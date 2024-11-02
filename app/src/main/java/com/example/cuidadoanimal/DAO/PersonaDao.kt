package com.example.cuidadoanimal.DAO

import androidx.room.*
import com.example.cuidadoanimal.Model.Persona

@Dao
interface PersonaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersona(persona: Persona): Long  // Cambiado a Long para retornar el ID

    @Update
    suspend fun updatePersona(persona: Persona)

    @Delete
    suspend fun deletePersona(persona: Persona)

    @Query("SELECT * FROM personas")
    suspend fun getAllPersonas(): List<Persona>

    @Query("SELECT * FROM personas WHERE personaId = :id")
    suspend fun getPersonaById(id: Int): Persona?
}