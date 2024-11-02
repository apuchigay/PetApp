package com.example.cuidadoanimal.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.cuidadoanimal.Model.*
import com.example.cuidadoanimal.DAO.*
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

@Database(
    entities = [
        Persona::class,
        Cliente::class,
        Trabajador::class,
        Mascota::class,
        Servicio::class,
        SolicitudServicio::class,
        HistorialMedico::class,
        Autenticacion::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(CuidadoAnimalDatabase.Converters::class) // Registrar el TypeConverter
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

    // Clase para manejar conversiones de listas a cadenas y viceversa
    class Converters {

        @TypeConverter
        fun fromString(value: String): List<String> {
            val listType = object : TypeToken<List<String>>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromList(list: List<String>): String {
            return Gson().toJson(list)
        }
    }
}
