package com.example.cuidadoanimal.DAO

import androidx.room.*
import com.example.cuidadoanimal.Model.SolicitudServicio

@Dao
interface SolicitudServicioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSolicitud(solicitud: SolicitudServicio)

    @Update
    suspend fun updateSolicitud(solicitud: SolicitudServicio)

    @Delete
    suspend fun deleteSolicitud(solicitud: SolicitudServicio)

    @Query("SELECT * FROM solicitudes_servicio")
    suspend fun getAllSolicitudes(): List<SolicitudServicio>

    @Query("SELECT * FROM solicitudes_servicio WHERE solicitudId = :id")
    suspend fun getSolicitudById(id: Int): SolicitudServicio?
}