package com.example.healthtech.view

import android.R
import android.graphics.Paint
import androidx.compose.material3.ListItem
import android.icu.number.Scale
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.annotation.experimental.Experimental
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.healthtech.navigation.Routes
import com.example.healthtech.viewmodel.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = viewModel()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.fetchUserData()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (viewModel.userName.isEmpty()) "Menú" else "Hola, ${viewModel.userName}",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Ajustes") },
                    selected = false,
                    onClick = { /*Ajustes generales de la app*/ }
                )
                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            viewModel.logout {
                                navController.navigate(Routes.Login) { popUpTo(0) }
                            }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CustomHealthTechTopBar(
                    title = "HealthTech",
                    showMenuButton = true,
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            },
            bottomBar = {
                CustomHealthTechBottomBar(navController)
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate(Routes.AddDocScreen) },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("") },
                    containerColor = MaterialTheme.colorScheme.primary
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item { Spacer(modifier = Modifier.height(80.dp)) }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Bienvenido, ${viewModel.userName}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Tu resumen de salud de hoy",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }

                item {
                    CardIA(onClick = { /*TODO: Navegacion al chat con la IA*/ })
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }

                item {
                    Text(
                        text = "Centros",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    /*Hardcodeo de simulación*/
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        HealthCard("Osakidetza", Color(0xFF00568F), Modifier.weight(1f))
                        HealthCard("IMQ", Color(0xFF0096D6), Modifier.weight(1f))
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }

                item {
                    Text(
                        text = "Actividad reciente",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (viewModel.recentActivityList.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillParentMaxHeight(0.7f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inbox,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "No hay documentos subidos aún",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    items(viewModel.recentActivityList) { record ->
                        RegisterRecentItem(
                            titulo = record.title,
                            fecha = record.date,
                            origen = record.source
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardIA(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Asistente Inteligente", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Analiza tus informes de IA", color = Color.White.copy(alpha = 0.8f))
            }
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
        }
    }
}

@Composable
fun HealthCard(nombre: String, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier.height(80.dp),
        colors = CardDefaults.cardColors(containerColor =  color.copy(alpha = 0.1f))
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(nombre, color = color, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RegisterRecentItem(titulo: String, fecha: String, origen: String) {
    ListItem(
        modifier = Modifier.fillMaxWidth(),
        headlineContent = { Text(titulo, style = MaterialTheme.typography.bodyLarge) },
        supportingContent = { Text("$origen • $fecha", style = MaterialTheme.typography.bodyLarge) },
        leadingContent = {
            Icon(Icons.Default.Description, contentDescription = null)
        },
        trailingContent = {
            Icon(Icons.Default.ChevronRight, contentDescription = null)
        }
    )
}