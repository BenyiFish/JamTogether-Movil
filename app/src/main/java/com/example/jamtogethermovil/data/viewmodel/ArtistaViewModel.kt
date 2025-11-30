package com.example.jamtogethermovil.data.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.jamtogethermovil.data.local.entities.Artista
import com.example.jamtogethermovil.data.repository.ArtistaRepository

class ArtistaViewModel(private val repository: ArtistaRepository) : ViewModel() {

    // Lista mutable de artistas
    private val _artistas = mutableStateListOf(
        Artista(
            nombre = "Juan Pérez",
            instrumento = "Guitarra",
            ciudad = "Madrid",
            influencias = "Rock, Blues",
            descripcion = "Guitarrista con 10 años de experiencia.\nContacto: +56 9 1234 5678",
            imagenUrl = "https://picsum.photos/id/1018/200/200"
        ),
        Artista(
            nombre = "Ana López",
            instrumento = "Voz",
            ciudad = "Barcelona",
            influencias = "Pop, Soul",
            descripcion = "Cantante versátil, disponible para colaboraciones.\nContacto: +56 9 8765 4321",
            imagenUrl = "https://picsum.photos/id/1025/200/200"
        ),
        Artista(
            nombre = "Carlos García",
            instrumento = "Batería",
            ciudad = "Valencia",
            influencias = "Metal, Hard Rock",
            descripcion = "Baterista enérgico, con ganas de marcha.\nContacto: +56 9 1122 3344",
            imagenUrl = "https://picsum.photos/id/1027/200/200"
        )
    )
    // Exponemos la lista como solo lectura para la UI
    val artistas: List<Artista> get() = _artistas

    //  Función para agregar artista
    fun agregarArtista(
        nombre: String,
        edad: String,
        instrumento: String,
        ciudad: String,
        telefono: String,
        imagenUrl: String?
    ) {
        val nuevoArtista = Artista(
            nombre = nombre,
            instrumento = instrumento,
            ciudad = ciudad,
            influencias = "Varias",
            descripcion = "Edad: $edad años.\nContacto: +56 9 $telefono",
            imagenUrl = imagenUrl
        )
        // Agregamos al inicio de la lista
        _artistas.add(0, nuevoArtista)
    }
}