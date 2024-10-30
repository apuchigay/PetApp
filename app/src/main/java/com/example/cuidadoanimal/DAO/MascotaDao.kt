package com.example.cuidadoanimal.DAO

import androidx.room.*
import com.example.cuidadoanimal.Model.Mascota

@Dao
interface MascotaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMascota(mascota: Mascota)

    @Update
    suspend fun updateMascota(mascota: Mascota)

    @Delete
    suspend fun deleteMascota(mascota: Mascota)

    @Query("SELECT * FROM mascotas")
    suspend fun getAllMascotas(): List<Mascota>

    @Query("SELECT * FROM mascotas WHERE mascotaId = :id")
    suspend fun getMascotaById(id: Int): Mascota?
}