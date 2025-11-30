// ArtistaDao.kt
package com.example.jamtogethermovil.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.jamtogethermovil.data.local.entities.Artista
import com.example.jamtogethermovil.data.local.entities.Banda
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistaDao {
    @Query("SELECT * FROM artistas")
    fun getAll(): Flow<List<Artista>>

    @Delete
    suspend fun delete(artista: Artista)

    @Insert
    suspend fun insert(artista: Artista)
}
