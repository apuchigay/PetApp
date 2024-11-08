package com.example.cuidadoanimal.DAO

import androidx.room.*
import com.example.cuidadoanimal.Model.HistorialMedico

@Dao
interface HistorialMedicoDao {

    @Insert
    suspend fun insert(historialMedico: HistorialMedico)

    @Query("SELECT * FROM historial_medico WHERE mascota_id = :mascotaId")
    suspend fun getHistorialByMascotaId(mascotaId: Int): List<HistorialMedico>

    @Update
    suspend fun updateHistorial(historial: HistorialMedico)

    @Delete
    suspend fun deleteHistorial(historial: HistorialMedico)

    @Query("SELECT * FROM historial_medico")
    suspend fun getAllHistoriales(): List<HistorialMedico>

}