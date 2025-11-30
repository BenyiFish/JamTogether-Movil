package com.example.jamtogethermovil.data.local.entities

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "artistas")
data class Artista (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val instrumento: String,
    val ciudad: String,
    val influencias: String,
    val descripcion: String = "",
    val imagenUrl: String? = null
)
