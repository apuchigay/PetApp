package com.example.cuidadoanimal.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cuidadoanimal.Model.*
import com.example.cuidadoanimal.DAO.*

@Database(
    entities = [
        Persona::class,
        Cliente::class,
        Trabajador::class,
        Mascota::class,
        Servicio::class,
        SolicitudServicio::class,
        HistorialMedico::class,
        Autenticacion::class // Asegúrate de incluir la entidad Autenticacion
    ],
    version = 1,
    exportSchema = false
)
abstract class CuidadoAnimalDatabase : RoomDatabase() {

    abstract fun personaDao(): PersonaDao
    abstract fun clienteDao(): ClienteDao
    abstract fun trabajadorDao(): TrabajadorDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun servicioDao(): ServicioDao
    abstract fun solicitudServicioDao(): SolicitudServicioDao
    abstract fun historialMedicoDao(): HistorialMedicoDao


    abstract fun autenticacionDao(): AutenticacionDao

    companion object {
        // Singleton to prevent multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: CuidadoAnimalDatabase? = null

        fun getDatabase(context: Context): CuidadoAnimalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CuidadoAnimalDatabase::class.java,
                    "cuidado_animal_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}