package com.example.jamtogethermovil.data.viewmodel

import com.example.jamtogethermovil.data.repository.BandaRepository
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BandaViewModelTest {

    private lateinit var viewModel: BandaViewModel
    private lateinit var repository: BandaRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = BandaViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `agregarBanda debe aumentar la lista y poner la nueva al inicio`() {
        // GIVEN
        val initialSize = viewModel.bandas.size

        // WHEN
        viewModel.agregarBanda("Nueva Banda", "Rock", "Stgo", "Desc", null)

        // THEN
        Assert.assertEquals(initialSize + 1, viewModel.bandas.size)
        Assert.assertEquals("Nueva Banda", viewModel.bandas[0].nombre)
    }
}