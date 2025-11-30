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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Send
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
import com.example.jamtogethermovil.data.local.entities.Banda

@Composable
fun BandaCard(banda: Banda, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    // Estado para controlar si mostramos el detalle(Pop-Up)
    var showDetail by remember { mutableStateOf(false) }

    // TARJETA RESUMIDA
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDetail = true }, // Al hacer clic, abrimos el detalle
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // FOTO
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(banda.imagenUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen de la banda",
                placeholder = rememberVectorPainter(image = Icons.Default.Group),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp) // Imagen bien visible
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )

            // INFO BASICA (Solo Nombre y Estilo)
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = banda.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = banda.genero,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }

    // Card se hace mas grande para mostrar todos los datos y asi poder contactar via Whatsapp
    if (showDetail) {
        Dialog(onDismissRequest = { showDetail = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp), // Altura máxima para que no se salga de pantalla
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                // Hacemos scrollable por si la descripción es muy larga
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    // IMAGEN GRANDE
                    Box {
                        AsyncImage(
                            model = banda.imagenUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop
                        )
                        // Botón cerrar (X)
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
                        // TÍTULO
                        Text(
                            text = banda.nombre,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // ESTILO Y UBICACIÓN
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MusicNote, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Text(text = " ${banda.genero}", style = MaterialTheme.typography.bodyLarge)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                            Text(text = " ${banda.ubicacion}", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(16.dp))

                        // INTEGRANTES
                        Text(
                            text = "Integrantes: ${banda.integrantes}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // DESCRIPCIÓN COMPLETA
                        Text(
                            text = "Sobre nosotros:",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = banda.descripcion,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // BOTÓN WHATSAPP (Solo aparece aquí)
                        val numeroContacto = extractPhoneNumberBanda(banda.descripcion)
                        //Manejo de errores para numero de contacto
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
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)), // Verde WhatsApp
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

// Función auxiliar (asegúrate de que no esté duplicada si ya la tienes en otro lado)
fun extractPhoneNumberBanda(text: String): String? {
    if (text.contains("Contacto:")) {
        val partes = text.split("Contacto:")
        if (partes.size > 1) {
            return partes[1].trim()
        }
    }
    return null
}