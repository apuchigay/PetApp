package com.example.cuidadoanimal.Repository

import com.example.cuidadoanimal.DAO.ClienteDao
import com.example.cuidadoanimal.Model.Cliente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ClienteRepository(private val clienteDao: ClienteDao) {

    suspend fun insertCliente(cliente: Cliente) {
        withContext(Dispatchers.IO) {
            clienteDao.insertCliente(cliente)
        }
    }

    suspend fun updateCliente(cliente: Cliente) {
        withContext(Dispatchers.IO) {
            clienteDao.updateCliente(cliente)
        }
    }

    suspend fun deleteCliente(cliente: Cliente) {
        withContext(Dispatchers.IO) {
            clienteDao.deleteCliente(cliente)
        }
    }

    suspend fun getAllClientes(): List<Cliente> {
        return withContext(Dispatchers.IO) {
            clienteDao.getAllClientes()
        }
    }

    suspend fun getClienteById(id: Int): Cliente? {
        return withContext(Dispatchers.IO) {
            clienteDao.getClienteById(id)
        }
    }
}