package com.dam2jms.apppuntosvioletas.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dam2jms.apppuntosvioletas.R
import com.dam2jms.apppuntosvioletas.models.ViewModelEncuestaScreen
import com.dam2jms.apppuntosvioletas.navigation.AppScreens
import com.dam2jms.apppuntosvioletas.models.ViewModelFirstScreen
import com.dam2jms.apppuntosvioletas.states.UiState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstScreen(navController: NavHostController, mvvm: ViewModelFirstScreen) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            DrawerContent(navController = navController)
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("App Puntos Violetas") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    GoogleMapView(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        viewModel = mvvm
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(route = AppScreens.SOS_Screen.route) },
                    containerColor = Color(0xFFAC53F7),
                    elevation = FloatingActionButtonDefaults.elevation(),
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = "SOS")
                }
            }
        )
    }
}

@Composable
fun DrawerContent(navController: NavHostController) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(200.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.logo_puntos_violetas),
                contentDescription = "logoPuntos"
            )
            Divider()
            NavigationDrawerItem(
                label = { Text(text = "Videos explicativos") },
                selected = false,
                onClick = { navController.navigate(route = AppScreens.OpcionesScreen.route)}
            )
            Divider()
            NavigationDrawerItem(
                label = { Text(text = "Encuesta") },
                selected = false,
                onClick = { navController.navigate(route = AppScreens.EncuestaScreen.route) }
            )
            Divider()
        }
    }
}

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    viewModel: ViewModelFirstScreen
) {
    val uiState by viewModel.uiState.collectAsState()

    GoogleMap(
        modifier = modifier,
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(uiState.coordenadas, 11f)
        }
    ) {
        MarkerOptions().position(uiState.coordenadas)
            .title("IES San Alberto")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
            .draggable(true)
            .flat(true)

        MarkerOptions().position(uiState.puntoVioleta1)
            .title("Ayuntamiento")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
            .draggable(true)
            .flat(true)
    }
}
