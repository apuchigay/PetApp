package com.example.cuidadoanimal.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cuidadoanimal.Model.PaseoHistorial

@Dao
interface PaseoHistorialDao {

    @Insert
    suspend fun insertPaseo(paseoHistorial: PaseoHistorial)

    @Query("SELECT * FROM PaseoHistorial WHERE mascota_id = :mascotaId")
    suspend fun getPaseosByMascotaId(mascotaId: Int): List<PaseoHistorial>
}
