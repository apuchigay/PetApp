package com.example.cuidadoanimal.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cuidadoanimal.Model.ServicioSpaHistorial

@Dao
interface SpaDao {

    @Insert
    suspend fun insertServicioSpa(servicioSpaHistorial: ServicioSpaHistorial)

    @Query("SELECT * FROM servicio_spa_historial")
    suspend fun getAllServiciosSpa(): List<ServicioSpaHistorial>

    @Query("SELECT * FROM servicio_spa_historial WHERE mascota_id = :mascotaId")
    suspend fun getServiciosSpaByMascotaId(mascotaId: Int): List<ServicioSpaHistorial>
}
