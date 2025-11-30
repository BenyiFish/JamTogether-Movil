package com.example.jamtogethermovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.jamtogethermovil.data.local.db.AppDatabase
import com.example.jamtogethermovil.data.repository.ArtistaRepository
import com.example.jamtogethermovil.data.repository.BandaRepository
import com.example.jamtogethermovil.data.repository.UsuarioRepository
import com.example.jamtogethermovil.data.viewmodel.ArtistaViewModel
import com.example.jamtogethermovil.data.viewmodel.BandaViewModel
import com.example.jamtogethermovil.data.viewmodel.UsuarioViewModel
import com.example.jamtogethermovil.ui.screens.*
import com.example.jamtogethermovil.ui.theme.JamTogetherMovilTheme

class MainActivity : ComponentActivity() {

    private val viewModelFactory by lazy {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "jam_together_db"
                ).fallbackToDestructiveMigration().build()

                val artistaRepository = ArtistaRepository(db.artistaDao())
                val bandaRepository = BandaRepository(db.bandaDao())
                val usuarioRepository = UsuarioRepository(db.usuarioDao())

                return when {
                    modelClass.isAssignableFrom(ArtistaViewModel::class.java) ->
                        ArtistaViewModel(artistaRepository) as T
                    modelClass.isAssignableFrom(BandaViewModel::class.java) ->
                        BandaViewModel(bandaRepository) as T
                    modelClass.isAssignableFrom(UsuarioViewModel::class.java) ->
                        UsuarioViewModel(usuarioRepository) as T
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }

    private val artistaViewModel: ArtistaViewModel by viewModels { viewModelFactory }
    private val bandaViewModel: BandaViewModel by viewModels { viewModelFactory }
    private val usuarioViewModel: UsuarioViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JamTogetherMovilTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
                            usuarioViewModel = usuarioViewModel,
                            onLoginSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                            onRegisterClick = { navController.navigate("register") }
                        )
                    }
                    composable("register") {
                        RegisterScreen(
                            usuarioViewModel = usuarioViewModel,
                            onRegisterSuccess = { navController.navigate("login") { popUpTo("login") { inclusive = true } } }
                        )
                    }
                    composable("home") {
                        HomeScreen(
                            usuarioViewModel = usuarioViewModel,
                            onNavigateToArtistas = { navController.navigate("artistas") },
                            onNavigateToBandas = { navController.navigate("bandas") },
                            onProfileClick = { navController.navigate("profile") }
                        )
                    }
                    composable("artistas") {

                        ArtistasScreen(
                            artistaViewModel = artistaViewModel,
                            onNavigateUp = { navController.navigateUp() }
                        )
                    }
                    composable("bandas") {
                        BandasScreen(
                            bandaViewModel = bandaViewModel,
                            onNavigateUp = { navController.navigateUp() }
                        )
                    }
                    composable("profile") {
                        ProfileScreen(
                            usuarioViewModel = usuarioViewModel,
                            onBackClick = { navController.popBackStack() },
                            onLogout = {
                                usuarioViewModel.cerrarSesion()
                                navController.navigate("login") {
                                    popUpTo(0)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}