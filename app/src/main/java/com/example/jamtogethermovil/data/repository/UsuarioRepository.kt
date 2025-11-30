package com.example.jamtogethermovil.data.repository

import com.example.jamtogethermovil.data.local.dao.UsuarioDao
import com.example.jamtogethermovil.data.local.entities.Usuario

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    suspend fun getUsuarioPorEmail(email: String): Usuario? {
        return usuarioDao.getUsuarioPorEmail(email)
    }

    suspend fun insert(usuario: Usuario) {
        usuarioDao.insert(usuario)
    }
}
