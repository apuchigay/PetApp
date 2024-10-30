package com.example.cuidadoanimal.DAO

import androidx.room.*
import com.example.cuidadoanimal.Model.HistorialMedico

@Dao
interface HistorialMedicoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistorial(historial: HistorialMedico)

    @Update
    suspend fun updateHistorial(historial: HistorialMedico)

    @Delete
    suspend fun deleteHistorial(historial: HistorialMedico)

    @Query("SELECT * FROM historial_medico")
    suspend fun getAllHistoriales(): List<HistorialMedico>

    @Query("SELECT * FROM historial_medico WHERE historialId = :id")
    suspend fun getHistorialById(id: Int): HistorialMedico?
}