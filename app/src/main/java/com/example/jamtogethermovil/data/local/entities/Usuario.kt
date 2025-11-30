package com.example.jamtogethermovil.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val edad: Int,
    val instrumento: String,
    val email: String,
    val contrasena: String,
    val imagenUrl: String? = null
)