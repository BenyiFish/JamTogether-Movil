package com.example.jamtogethermovil.data.repository

import com.example.jamtogethermovil.data.local.dao.ArtistaDao
import com.example.jamtogethermovil.data.local.entities.Artista
import kotlinx.coroutines.flow.Flow


class ArtistaRepository(private val artistaDao: ArtistaDao) {

    val artistas: Flow<List<Artista>> = artistaDao.getAll()

    suspend fun insert(artista: Artista) {
        artistaDao.insert(artista)
    }

    suspend fun delete(artista: Artista) {
        artistaDao.delete(artista)
    }
}
