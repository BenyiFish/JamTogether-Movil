package com.example.jamtogethermovil.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.jamtogethermovil.data.local.entities.Banda
import kotlinx.coroutines.flow.Flow

@Dao
interface BandaDao {

    @Query("SELECT * FROM bandas")
    fun getAllBandasFlow(): Flow<List<Banda>>
    @Insert
    suspend fun insert(banda: Banda)

    @Delete
    suspend fun delete(banda: Banda)

    @Query("SELECT * FROM bandas")
    fun getAll(): Flow<List<Banda>>
}
