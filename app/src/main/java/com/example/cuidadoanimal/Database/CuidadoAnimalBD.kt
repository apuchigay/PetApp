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
        Autenticacion::class,
        PaseoHistorial::class,    // Entidad para el historial de paseos
        ServicioSpaHistorial::class // Nueva entidad para el historial de servicios de spa
    ],
    version = 5,  // Incrementar la versión debido a la nueva entidad
    exportSchema = false
)
@TypeConverters(CuidadoAnimalDatabase.Converters::class)  // Registrar el TypeConverter para List<String>
abstract class CuidadoAnimalDatabase : RoomDatabase() {

    // Definición de los DAOs
    abstract fun personaDao(): PersonaDao
    abstract fun clienteDao(): ClienteDao
    abstract fun trabajadorDao(): TrabajadorDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun servicioDao(): ServicioDao
    abstract fun solicitudServicioDao(): SolicitudServicioDao
    abstract fun historialMedicoDao(): HistorialMedicoDao
    abstract fun autenticacionDao(): AutenticacionDao
    abstract fun paseoHistorialDao(): PaseoHistorialDao   // DAO para la entidad PaseoHistorial
    abstract fun spaDao(): SpaDao                // Nuevo DAO para la entidad ServicioSpaHistorial

    companion object {
        @Volatile
        private var INSTANCE: CuidadoAnimalDatabase? = null

        fun getDatabase(context: Context): CuidadoAnimalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CuidadoAnimalDatabase::class.java,
                    "cuidado_animal_database"
                )
                    //.fallbackToDestructiveMigration()  // Descomentar si deseas reconstruir la BD en cambios destructivos
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // TypeConverters para manejar conversiones de listas a JSON y viceversa
    class Converters {
        private val gson = Gson()

        @TypeConverter
        fun fromString(value: String): List<String> {
            val listType = object : TypeToken<List<String>>() {}.type
            return gson.fromJson(value, listType)
        }

        @TypeConverter
        fun fromList(list: List<String>): String {
            return gson.toJson(list)
        }
    }
}
