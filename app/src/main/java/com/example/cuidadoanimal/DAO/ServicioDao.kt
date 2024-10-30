package com.example.cuidadoanimal.DAO

import androidx.room.*
import com.example.cuidadoanimal.Model.Servicio

@Dao
interface ServicioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServicio(servicio: Servicio)

    @Update
    suspend fun updateServicio(servicio: Servicio)

    @Delete
    suspend fun deleteServicio(servicio: Servicio)

    @Query("SELECT * FROM servicios")
    suspend fun getAllServicios(): List<Servicio>

    @Query("SELECT * FROM servicios WHERE servicioId = :id")
    suspend fun getServicioById(id: Int): Servicio?
}