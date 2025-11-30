package com.example.jamtogethermovil.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.jamtogethermovil.data.local.entities.Artista
import com.example.jamtogethermovil.data.viewmodel.ArtistaViewModel
import com.example.jamtogethermovil.ui.components.ArtistCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistasScreen(
    artistaViewModel: ArtistaViewModel,
    onNavigateUp: () -> Unit
) {

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFFF9800), Color(0xFFFF5722), Color(0xFF000000))
    )

    //  DATOS DESDE EL VIEWMODEL
    val artistas = artistaViewModel.artistas

    // Estados para el Formulario
    var showDialog by remember { mutableStateOf(false) }
    var nuevoNombre by remember { mutableStateOf("") }
    var nuevaCiudad by remember { mutableStateOf("") }
    var nuevaEdad by remember { mutableStateOf("") }
    var nuevoInstrumento by remember { mutableStateOf("") }
    var nuevoTelefono by remember { mutableStateOf("") }
    var nuevaImagenUri by remember { mutableStateOf<Uri?>(null) }

    // Estados de validacion
    var errorTelefono by remember { mutableStateOf(false) }
    var errorCamposVacios by remember { mutableStateOf(false) }

    // Estados para el Dropdown
    var expanded by remember { mutableStateOf(false) }
    val opcionesInstrumento = listOf("Cantante", "Batería", "Guitarra", "Bajo", "Teclado", "Otros")

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> nuevaImagenUri = uri }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Artistas", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFF9800))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color.White,
                contentColor = Color(0xFFFF5722)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar mi Card")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(brush = backgroundBrush)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(artistas) { artista ->
                    ArtistCard(artista, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Crear mi Carta de Artista") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // FOTO
                        Box(
                            modifier = Modifier
                                .fillMaxWidth().height(150.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Gray.copy(alpha = 0.2f))
                                .clickable { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (nuevaImagenUri != null) {
                                AsyncImage(model = nuevaImagenUri, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Person, null, modifier = Modifier.size(40.dp))
                                    Text("Subir foto (Opcional)", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }

                        // NOMBRE
                        OutlinedTextField(value = nuevoNombre, onValueChange = { nuevoNombre = it }, label = { Text("Nombre *") }, singleLine = true, isError = errorCamposVacios && nuevoNombre.isBlank(), modifier = Modifier.fillMaxWidth())

                        // CIUDAD
                        OutlinedTextField(value = nuevaCiudad, onValueChange = { nuevaCiudad = it }, label = { Text("Ciudad *") }, singleLine = true, isError = errorCamposVacios && nuevaCiudad.isBlank(), modifier = Modifier.fillMaxWidth())

                        // EDAD
                        OutlinedTextField(value = nuevaEdad, onValueChange = { if (it.all { char -> char.isDigit() }) nuevaEdad = it }, label = { Text("Edad *") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), isError = errorCamposVacios && nuevaEdad.isBlank(), modifier = Modifier.fillMaxWidth())

                        // TELÉFONO
                        OutlinedTextField(
                            value = nuevoTelefono,
                            onValueChange = { input -> if (input.all { it.isDigit() } && input.length <= 8) { nuevoTelefono = input; errorTelefono = false } },
                            label = { Text("Teléfono *") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), prefix = { Text("+56 9 ", style = MaterialTheme.typography.bodyLarge) }, isError = errorTelefono || (errorCamposVacios && nuevoTelefono.isBlank()),
                            supportingText = { if (errorTelefono) Text("Debe ingresar 8 dígitos", color = MaterialTheme.colorScheme.error) },
                            trailingIcon = { if (errorTelefono) Icon(Icons.Default.Error, "Error", tint = MaterialTheme.colorScheme.error) else Icon(Icons.Default.Phone, null) }, modifier = Modifier.fillMaxWidth()
                        )

                        // INSTRUMENTO (DROPDOWN)
                        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(value = nuevoInstrumento, onValueChange = {}, readOnly = true, label = { Text("Instrumento *") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(), isError = errorCamposVacios && nuevoInstrumento.isBlank(), modifier = Modifier.menuAnchor().fillMaxWidth())
                            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                opcionesInstrumento.forEach { opcion -> DropdownMenuItem(text = { Text(opcion) }, onClick = { nuevoInstrumento = opcion; expanded = false }) }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val isTelefonoValid = nuevoTelefono.length == 8
                        val areAllFieldsFilled = nuevoNombre.isNotBlank() && nuevaCiudad.isNotBlank() && nuevaEdad.isNotBlank() && nuevoInstrumento.isNotBlank() && nuevoTelefono.isNotBlank()

                        if (!isTelefonoValid) errorTelefono = true
                        errorCamposVacios = !areAllFieldsFilled

                        if (areAllFieldsFilled && isTelefonoValid) {
                            //  Llamamos al ViewModel para agregar
                            artistaViewModel.agregarArtista(
                                nombre = nuevoNombre,
                                edad = nuevaEdad,
                                instrumento = nuevoInstrumento,
                                ciudad = nuevaCiudad,
                                telefono = nuevoTelefono,
                                imagenUrl = nuevaImagenUri?.toString() ?: "https://picsum.photos/200/300?random=${System.currentTimeMillis()}"
                            )
                            // Reset
                            nuevoNombre = ""; nuevaCiudad = ""; nuevaEdad = ""; nuevoInstrumento = ""; nuevoTelefono = ""; nuevaImagenUri = null
                            errorTelefono = false; errorCamposVacios = false; showDialog = false
                        }
                    }) { Text("Publicar") }
                },
                dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Cancelar") } }
            )
        }
    }
}