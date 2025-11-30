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
import androidx.compose.material.icons.filled.Groups
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
import com.example.jamtogethermovil.data.local.entities.Banda
import com.example.jamtogethermovil.data.viewmodel.BandaViewModel
import com.example.jamtogethermovil.ui.components.BandaCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandasScreen(
    bandaViewModel: BandaViewModel, // Inyectamos el ViewModel
    onNavigateUp: () -> Unit
) {

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFFF9800), Color(0xFFFF5722), Color(0xFF000000))
    )

    // DATOS DESDE EL VIEWMODEL
    // Como usamos mutableStateListOf en el ViewModel, Compose detectará los cambios automáticamente
    val bandas = bandaViewModel.bandas

    // Estados del formulario
    var showDialog by remember { mutableStateOf(false) }
    var nuevoNombre by remember { mutableStateOf("") }
    var nuevaCiudad by remember { mutableStateOf("") }
    var nuevoTelefono by remember { mutableStateOf("") }
    var nuevoEstilo by remember { mutableStateOf("") }
    var nuevoBuscamos by remember { mutableStateOf("") }
    var nuevaImagenUri by remember { mutableStateOf<Uri?>(null) }

    // Validaciones
    var errorTelefono by remember { mutableStateOf(false) }
    var errorCamposVacios by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    val opcionesBuscamos = listOf("Cantante", "Guitarrista", "Bajista", "Baterista", "Tecladista", "Corista", "Otros")

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> nuevaImagenUri = uri }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bandas", color = Color.White) },
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
                Icon(Icons.Default.Add, contentDescription = "Crear Banda")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).background(brush = backgroundBrush)) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(bandas) { banda ->
                    BandaCard(banda, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Registrar mi Banda") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
                                    Icon(Icons.Default.Groups, null, modifier = Modifier.size(40.dp))
                                    Text("Subir foto grupal (Opcional)", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }

                        OutlinedTextField(value = nuevoNombre, onValueChange = { nuevoNombre = it }, label = { Text("Nombre de la Banda *") }, singleLine = true, isError = errorCamposVacios && nuevoNombre.isBlank(), modifier = Modifier.fillMaxWidth())

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = nuevaCiudad, onValueChange = { nuevaCiudad = it }, label = { Text("Ciudad *") }, singleLine = true, isError = errorCamposVacios && nuevaCiudad.isBlank(), modifier = Modifier.weight(1f))
                            OutlinedTextField(value = nuevoEstilo, onValueChange = { nuevoEstilo = it }, label = { Text("Estilo *") }, singleLine = true, isError = errorCamposVacios && nuevoEstilo.isBlank(), modifier = Modifier.weight(1f))
                        }

                        OutlinedTextField(
                            value = nuevoTelefono,
                            onValueChange = { input -> if (input.all { it.isDigit() } && input.length <= 8) { nuevoTelefono = input; errorTelefono = false } },
                            label = { Text("Teléfono *") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), prefix = { Text("+56 9 ", style = MaterialTheme.typography.bodyLarge) }, isError = errorTelefono || (errorCamposVacios && nuevoTelefono.isBlank()),
                            supportingText = { if (errorTelefono) Text("Debe ingresar 8 dígitos", color = MaterialTheme.colorScheme.error) },
                            trailingIcon = { if (errorTelefono) Icon(Icons.Default.Error, "Error", tint = MaterialTheme.colorScheme.error) else Icon(Icons.Default.Phone, null) }, modifier = Modifier.fillMaxWidth()
                        )

                        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(value = nuevoBuscamos, onValueChange = {}, readOnly = true, label = { Text("Buscamos *") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(), isError = errorCamposVacios && nuevoBuscamos.isBlank(), modifier = Modifier.menuAnchor().fillMaxWidth())
                            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                opcionesBuscamos.forEach { opcion -> DropdownMenuItem(text = { Text(opcion) }, onClick = { nuevoBuscamos = opcion; expanded = false }) }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val isTelefonoValid = nuevoTelefono.length == 8
                        val areAllFieldsFilled = nuevoNombre.isNotBlank() && nuevaCiudad.isNotBlank() && nuevoEstilo.isNotBlank() && nuevoBuscamos.isNotBlank() && nuevoTelefono.isNotBlank()

                        if (!isTelefonoValid) errorTelefono = true
                        errorCamposVacios = !areAllFieldsFilled

                        if (areAllFieldsFilled && isTelefonoValid) {
                            // LLAMAMOS AL VIEWMODEL
                            bandaViewModel.agregarBanda(
                                nombre = nuevoNombre,
                                genero = nuevoEstilo,
                                ubicacion = nuevaCiudad,
                                descripcion = "Buscamos: $nuevoBuscamos\nContacto: +56 9 $nuevoTelefono",
                                imagenUrl = nuevaImagenUri?.toString() ?: "https://picsum.photos/200/300?random=${System.currentTimeMillis()}"
                            )
                            // Reset
                            nuevoNombre = ""; nuevaCiudad = ""; nuevoEstilo = ""; nuevoTelefono = ""; nuevoBuscamos = ""; nuevaImagenUri = null
                            errorTelefono = false; errorCamposVacios = false; showDialog = false
                        }
                    }) { Text("Publicar Banda") }
                },
                dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Cancelar") } }
            )
        }
    }
}