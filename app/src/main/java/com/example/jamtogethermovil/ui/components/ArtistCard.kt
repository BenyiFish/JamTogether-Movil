package com.example.jamtogethermovil.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jamtogethermovil.data.local.entities.Artista

@Composable
fun ArtistCard(
    artista: Artista,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // Estado para controlar el Pop-up
    var showDetail by remember { mutableStateOf(false) }

    // TARJETA RESUMIDA
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDetail = true }, // Clic para abrir detalle
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // FOTO PEQUEÑA
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(artista.imagenUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                placeholder = rememberVectorPainter(image = Icons.Default.AccountCircle),
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // INFO BASICA
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = artista.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MusicNote, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Text(
                        text = " ${artista.instrumento}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Text(
                        text = " ${artista.ciudad}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }

    //  Tarjeta se hace mas grande al momento de darle click para ver toda la informacion y asi poder contactar via Whatsapp
    if (showDetail) {
        Dialog(onDismissRequest = { showDetail = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 650.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    // FOTO GRANDE
                    Box {
                        AsyncImage(
                            model = artista.imagenUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp),
                            contentScale = ContentScale.Crop
                        )
                        // Boton Cerrar (X)
                        IconButton(
                            onClick = { showDetail = false },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                        }
                    }

                    Column(modifier = Modifier.padding(24.dp)) {
                        // TITULO
                        Text(
                            text = artista.nombre,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // DATOS CLAVE
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.primary)
                            Text(text = " Instrumento: ${artista.instrumento}", style = MaterialTheme.typography.bodyLarge)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, tint = Color.Gray)
                            Text(text = " Ubicación: ${artista.ciudad}", style = MaterialTheme.typography.bodyLarge)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(16.dp))

                        // ESTILO MUSICAL
                        if (artista.influencias.isNotBlank()) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Column {
                                    Text(text = "Influencias:", fontWeight = FontWeight.Bold)
                                    Text(text = artista.influencias, color = Color.DarkGray)
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // DESCRIPCION
                        Text(text = "Sobre mí:", fontWeight = FontWeight.Bold)
                        Text(
                            text = artista.descripcion,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // BOTÓN WHATSAPP
                        val numeroContacto = extractPhoneNumberArtist(artista.descripcion)
                        //Manejo de errores en numero de contacto
                        if (numeroContacto != null) {
                            Button(
                                onClick = {
                                    try {
                                        val cleanNumber = numeroContacto.filter { it.isDigit() }
                                        val url = "https://api.whatsapp.com/send?phone=$cleanNumber"
                                        val intent = Intent(Intent.ACTION_VIEW)
                                        intent.data = Uri.parse(url)
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        // Error handling
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Contactar por WhatsApp", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Función auxiliar para extraer teléfono (nombre distinto para evitar conflicto con BandaCard)
fun extractPhoneNumberArtist(text: String): String? {
    // Busca el patrón "+56 9" o "Contacto:"
    if (text.contains("Contacto:")) {
        val partes = text.split("Contacto:")
        if (partes.size > 1) {
            return partes[1].trim()
        }
    }
    return null
}