package com.example.jamtogethermovil.data.repository

import com.example.jamtogethermovil.data.local.dao.BandaDao
import com.example.jamtogethermovil.data.local.entities.Banda
import kotlinx.coroutines.flow.Flow

class BandaRepository(private val dao: BandaDao) {
    val bandas: Flow<List<Banda>> = dao.getAll()

    suspend fun insert(banda: Banda) {
        dao.insert(banda)
    }

    suspend fun delete(banda: Banda) {
        dao.delete(banda)
    }
}
