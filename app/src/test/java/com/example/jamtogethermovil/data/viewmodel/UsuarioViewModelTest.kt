package com.example.jamtogethermovil.data.viewmodel

import com.example.jamtogethermovil.data.local.entities.Usuario
import com.example.jamtogethermovil.data.repository.UsuarioRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioViewModelTest {

    private lateinit var viewModel: UsuarioViewModel
    private lateinit var repository: UsuarioRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = UsuarioViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `register guarda usuario y muestra exito si no existe`() = runTest {
        // GIVEN
        val email = "nuevo@test.com"
        coEvery { repository.getUsuarioPorEmail(email) } returns null

        // WHEN
        viewModel.register("Nombre", "25", "Bajo", email, "pass", null)

        // THEN
        coVerify { repository.insert(any()) }
        Assert.assertEquals("Registro exitoso", viewModel.snackbarMessage.value)
    }

    @Test
    fun `register muestra error si usuario existe`() = runTest {
        // GIVEN
        val email = "existe@test.com"
        coEvery { repository.getUsuarioPorEmail(email) } returns Usuario(
            email = email,
            contrasena = "123",
            nombre = "X",
            edad = 10,
            instrumento = "X"
        )

        // WHEN
        viewModel.register("Otro", "22", "Voz", email, "pass", null)

        // THEN
        coVerify(exactly = 0) { repository.insert(any()) }
        Assert.assertEquals("El usuario ya existe", viewModel.snackbarMessage.value)
    }
}