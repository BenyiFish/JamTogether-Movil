package com.example.jamtogethermovil.data.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jamtogethermovil.data.local.entities.Banda
import com.example.jamtogethermovil.data.repository.BandaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



class BandaViewModel(private val repository: BandaRepository) : ViewModel() {

    // List MUTABLE que almacena bandas pre-creadas
    private val _bandas = mutableStateListOf(
        Banda(nombre = "The Rockers", genero = "Rock Alternativo", ubicacion = "Sevilla", integrantes = 4, descripcion = "Banda de rock alternativo con repertorio propio.\nContacto: +56 9 1111 2222", imagenUrl = "https://picsum.photos/id/1/200/200"),
        Banda(nombre = "Pop Fusion", genero = "Pop", ubicacion = "Bilbao", integrantes = 3, descripcion = "Grupo de pop en busca de un batería.\nContacto: +56 9 3333 4444", imagenUrl = "https://picsum.photos/id/10/200/200"),
        Banda(nombre = "Metal Warriors", genero = "Heavy Metal", ubicacion = "Zaragoza", integrantes = 5, descripcion = "Banda de heavy metal consolidada.\nContacto: +56 9 5555 6666", imagenUrl = "https://picsum.photos/id/100/200/200")
    )
    val bandas: List<Banda> get() = _bandas

    fun agregarBanda(nombre: String, genero: String, ubicacion: String, descripcion: String, imagenUrl: String?) {
        val nuevaBanda = Banda(
            nombre = nombre,
            genero = genero,
            ubicacion = ubicacion,
            integrantes = 1,
            descripcion = descripcion,
            imagenUrl = imagenUrl
        )
        // Agregamos la banda nueva al inicio de la lista.
        _bandas.add(0, nuevaBanda)


    }
}