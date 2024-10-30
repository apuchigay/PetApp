package com.example.cuidadoanimal.DAO

import androidx.room.*
import com.example.cuidadoanimal.Model.Cliente

@Dao
interface ClienteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCliente(cliente: Cliente)

    @Update
    suspend fun updateCliente(cliente: Cliente)

    @Delete
    suspend fun deleteCliente(cliente: Cliente)

    @Query("SELECT * FROM clientes")
    suspend fun getAllClientes(): List<Cliente>

    @Query("SELECT * FROM clientes WHERE clienteId = :id")
    suspend fun getClienteById(id: Int): Cliente?
}