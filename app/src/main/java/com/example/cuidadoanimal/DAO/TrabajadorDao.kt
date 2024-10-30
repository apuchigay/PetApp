package com.example.cuidadoanimal.DAO

import androidx.room.*
import com.example.cuidadoanimal.Model.Trabajador

@Dao
interface TrabajadorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrabajador(trabajador: Trabajador)

    @Update
    suspend fun updateTrabajador(trabajador: Trabajador)

    @Delete
    suspend fun deleteTrabajador(trabajador: Trabajador)

    @Query("SELECT * FROM trabajadores")
    suspend fun getAllTrabajadores(): List<Trabajador>

    @Query("SELECT * FROM trabajadores WHERE trabajadorId = :id")
    suspend fun getTrabajadorById(id: Int): Trabajador?
}