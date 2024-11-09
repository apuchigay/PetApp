package com.example.cuidadoanimal.DAO

import androidx.room.*
import com.example.cuidadoanimal.Model.Trabajador
import com.example.cuidadoanimal.Model.TrabajadorConNombre
import com.example.cuidadoanimal.Model.Persona

@Dao
interface TrabajadorDao {

    @Query("SELECT * FROM trabajadores")
    suspend fun getAllTrabajadores(): List<Trabajador>

    @Query("""
        SELECT 
            t.trabajadorId, 
            t.persona_id AS personaId,  -- Alias para que coincida con TrabajadorConNombre
            t.especialidades, 
            t.calificacion, 
            p.nombre
        FROM trabajadores t
        INNER JOIN personas p ON t.persona_id = p.personaId
    """)
    suspend fun getAllTrabajadoresWithNombre(): List<TrabajadorConNombre>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrabajador(trabajador: Trabajador)

    @Update
    suspend fun updateTrabajador(trabajador: Trabajador)

    @Delete
    suspend fun deleteTrabajador(trabajador: Trabajador)

    @Query("SELECT * FROM trabajadores WHERE trabajadorId = :id")
    suspend fun getTrabajadorById(id: Int): Trabajador?
}
