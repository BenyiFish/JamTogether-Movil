package com.example.jamtogethermovil.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.jamtogethermovil.R
import com.example.jamtogethermovil.data.local.entities.Artista
import com.example.jamtogethermovil.data.local.entities.Banda
import com.example.jamtogethermovil.data.local.entities.Usuario
import com.example.jamtogethermovil.data.viewmodel.UsuarioViewModel
import com.example.jamtogethermovil.ui.components.ArtistCard
import com.example.jamtogethermovil.ui.components.BandaCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    usuarioViewModel: UsuarioViewModel,
    modifier: Modifier = Modifier,
    onNavigateToArtistas: () -> Unit = {},
    onNavigateToBandas: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val currentUser by usuarioViewModel.currentUser.collectAsState()


    val customFontFamily = FontFamily(
        Font(R.font.science_gothic_bold))


    // degradado para el fondo
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFF9800),
            Color(0xFFFF5722),
            Color(0xFF000000)
        )
    )

    // Datos simulados
    val featuredArtistas = listOf(
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

    val featuredBandas = listOf(
        Banda(
            nombre = "The Rockers",
            genero = "Rock Alternativo",
            ubicacion = "Sevilla",
            integrantes = 4,
            descripcion = "Banda de rock alternativo con repertorio propio.\nContacto: +56 9 1111 2222",
            imagenUrl = "https://picsum.photos/id/1/200/200"
        ),
        Banda(
            nombre = "Pop Fusion",
            genero = "Pop",
            ubicacion = "Bilbao",
            integrantes = 3,
            descripcion = "Grupo de pop en busca de un batería.\nContacto: +56 9 3333 4444",
            imagenUrl = "https://picsum.photos/id/10/200/200"
        ),
        Banda(
            nombre = "Metal Warriors",
            genero = "Heavy Metal",
            ubicacion = "Zaragoza",
            integrantes = 5,
            descripcion = "Banda de heavy metal consolidada.\nContacto: +56 9 5555 6666",
            imagenUrl = "https://picsum.photos/id/100/200/200"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Jam Together",

                        fontFamily = customFontFamily,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        currentUser?.imagenUrl?.let {
                            AsyncImage(
                                model = it,
                                contentDescription = "Perfil",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } ?: Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "Perfil",
                            modifier = Modifier.size(40.dp),
                            tint = Color.White //
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(

                    containerColor = Color(0xFFFF9800),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        // Contenedor principal con degradado
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(brush = backgroundBrush)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {



            currentUser?.let { UserProfileCard(it) }

            // Sección Artistas
            SectionHeader(title = "Artistas destacados", onNavigate = onNavigateToArtistas)

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(featuredArtistas) { artista ->
                    ArtistCard(artista, modifier = Modifier.width(280.dp))
                }
            }

            // Sección Bandas
            SectionHeader(title = "Bandas destacadas", onNavigate = onNavigateToBandas)

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(featuredBandas) { banda ->
                    BandaCard(banda, modifier = Modifier.width(280.dp))
                }
            }
        }
    }
}

// Componente auxiliar para no repetir codigo en los encabezados
@Composable
fun SectionHeader(title: String, onNavigate: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        TextButton(onClick = onNavigate) {
            Text("Ver todo", color = Color(0xFF000000))
        }
    }
}

@Composable
fun UserProfileCard(usuario: Usuario) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),

        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            usuario.imagenUrl?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Perfil",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } ?:  Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Perfil",
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(usuario.email, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}