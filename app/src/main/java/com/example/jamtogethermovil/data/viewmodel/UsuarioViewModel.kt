package com.example.jamtogethermovil.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jamtogethermovil.data.local.entities.Usuario
import com.example.jamtogethermovil.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _loginExitoso = MutableStateFlow<Boolean?>(null)
    val loginExitoso: StateFlow<Boolean?> = _loginExitoso

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    // Flujo para mantener al usuario que ha iniciado sesión
    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser

    fun login(email: String, contrasena: String) {
        viewModelScope.launch {
            val usuario = repository.getUsuarioPorEmail(email)
            if (usuario != null && usuario.contrasena == contrasena) {
                _currentUser.value = usuario // Guardamos el usuario
                _loginExitoso.value = true   // Activamos la navegación
            } else {
                _loginExitoso.value = false
                _snackbarMessage.value = "Email o contraseña incorrectos"
            }
        }
    }

    fun register(nombre: String, edad: String, instrumento: String, email: String, contrasena: String, imagenUrl: String?) {
        viewModelScope.launch {
            if (nombre.isBlank() || edad.isBlank() || instrumento.isBlank() || email.isBlank() || contrasena.isBlank()) {
                _snackbarMessage.value = "Por favor completa todos los campos"
                return@launch
            }

            if (repository.getUsuarioPorEmail(email) != null) {
                _snackbarMessage.value = "El usuario ya existe"
            } else {
                val edadInt = edad.toIntOrNull() ?: 0
                val nuevoUsuario = Usuario(
                    nombre = nombre,
                    edad = edadInt,
                    instrumento = instrumento,
                    email = email,
                    contrasena = contrasena,
                    imagenUrl = imagenUrl
                )
                repository.insert(nuevoUsuario)
                _snackbarMessage.value = "Registro exitoso"
            }
        }
    }



    // funcion para resetear la navegacion
    fun limpiarEstadoLogin() {
        _loginExitoso.value = null
    }

    // Cerrar sesion
    fun cerrarSesion() {
        _loginExitoso.value = null
        _currentUser.value = null
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }
}