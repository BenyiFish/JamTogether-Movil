package com.example.jamtogethermovil.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu

@Composable
fun HomeBottomBar(
    onArtistasClick: () -> Unit = {},
    onBandasClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = onArtistasClick,
            icon = { Icon(Icons.Default.Search, contentDescription = "Buscar Artistas") },
            label = { Text("Artistas") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onBandasClick,
            icon = { Icon(Icons.Default.List, contentDescription = "Buscar Bandas") },
            label = { Text("Bandas") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onMenuClick,
            icon = { Icon(Icons.Default.Menu, contentDescription = "Menú") },
            label = { Text("Menú") }
        )
    }
}
