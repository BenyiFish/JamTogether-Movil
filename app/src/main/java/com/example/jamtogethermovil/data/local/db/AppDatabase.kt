package com.example.jamtogethermovil.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.jamtogethermovil.data.local.dao.ArtistaDao
import com.example.jamtogethermovil.data.local.dao.BandaDao
import com.example.jamtogethermovil.data.local.dao.UsuarioDao
import com.example.jamtogethermovil.data.local.entities.Artista
import com.example.jamtogethermovil.data.local.entities.Banda
import com.example.jamtogethermovil.data.local.entities.Usuario

@Database(entities = [Artista::class, Banda::class, Usuario::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun artistaDao(): ArtistaDao
    abstract fun bandaDao(): BandaDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jam_together_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
