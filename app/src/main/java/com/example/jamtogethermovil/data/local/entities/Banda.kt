package com.example.jamtogethermovil.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bandas")
data class Banda (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val genero: String,
    val ubicacion: String,
    val integrantes: Int,
    val descripcion: String = "",
    val imagenUrl: String? = null
)
