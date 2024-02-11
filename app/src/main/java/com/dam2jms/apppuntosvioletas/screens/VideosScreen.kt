package com.dam2jms.apppuntosvioletas.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import com.dam2jms.apppuntosvioletas.navigation.AppScreens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpcionesScreen(navController: NavHostController) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController = navController)
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("App Puntos Violetas") },
                    navigationIcon = {
                        //icono del menu para controlar la apertura y cierre del menu lateral
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
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(route = AppScreens.SOS_Screen.route) },
                    containerColor = Color(0xFFAC53F7),
                    elevation = FloatingActionButtonDefaults.elevation()
                ) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = "SOS")
                }
            }
        ) { paddingValues ->
            videosScreenBodyContent(modifier = Modifier.padding(paddingValues), navController)
        }
    }
}

@Composable
fun videosScreenBodyContent(modifier: Modifier, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExoPlayerView()
    }
}

@Composable
fun ExoPlayerView() {
    val video = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Que_es_punto_violeta_-_Ciudadanos.mp4"
    val context = LocalContext.current

    val exoPlayer = ExoPlayer.Builder(context).build()

    val mediaSource = remember(video) {
        MediaItem.fromUri(video)
    }

    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.prepare()
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

/*
* @Composable
fun TresEnRaya(viewModel: GameViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "JUGADORES BALONCESTO")
                }
            )
        }
    ) {
        BodyTresEnRaya(viewModel)
    }
}

enum class EstadoJuego {
    EN_CURSO,
    GANADO,
    EMPATADO
}

@Composable
fun BodyTresEnRaya(viewModel: GameViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Tres en Raya", style = MaterialTheme.typography.h5)
        Text(
            text = "Turno del jugador: ${uiState.jugador?.name ?: ""}",
            style = MaterialTheme.typography.subtitle1
        )

        Spacer(modifier = Modifier.height(16.dp))

        Grid(rows = 3, cols = 3) { row, col ->
            Button(
                onClick = { viewModel.realizarJugada(row, col) },
                modifier = Modifier.size(80.dp)
            ) {
                val jugador = uiState.tablero[row][col]
                Text(text = jugador?.symbol ?: "", style = MaterialTheme.typography.h4)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState.estadoJuego) {
            EstadoJuego.GANADO -> {
                Text(
                    text = "¡Ha ganado el jugador ${uiState.ganador?.name}!",
                    style = MaterialTheme.typography.subtitle1
                )
            }
            EstadoJuego.EMPATADO -> {
                Text(text = "¡Empate!", style = MaterialTheme.typography.subtitle1)
            }
            else -> Unit
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.reiniciarJuego() },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Reiniciar Juego")
        }
    }
}

@Composable
fun Grid(rows: Int, cols: Int, content: @Composable (row: Int, col: Int) -> Unit) {
    Column {
        repeat(rows) { row ->
            Row {
                repeat(cols) { col ->
                    content(row, col)
                }
            }
        }
    }
}
*
*
* adorActual, tablero)) {
                _uiState.value = state.copy(estadoJuego = EstadoJuego.GANADO, ganador = jugadorActual)
            } else if (hayEmpate(tablero)) {
                _uiState.value = state.copy(estadoJuego = EstadoJuego.EMPATADO)
            } else {
                val siguienteJugador = if (jugadorActual == state.jugador1) state.jugador2 else state.jugador1
                _uiState.value = state.copy(jugador = siguienteJugador)
            }
        }
    }

    private fun hayGanador(jugador: Jugador, tablero: Array<Array<Jugador?>>): Boolean {
        for (i in 0..2) {
            if (tablero[i][0] == jugador && tablero[i][1] == jugador && tablero[i][2] == jugador) {
                return true
            }
            if (tablero[0][i] == jugador && tablero[1][i] == jugador && tablero[2][i] == jugador) {
                return true
            }
        }
        if (tablero[0][0] == jugador && tablero[1][1] == jugador && tablero[2][2] == jugador) {
            return true
        }
        if (tablero[2][0] == jugador && tablero[1][1] == jugador && tablero[0][2] == jugador) {
            return true
        }
        return false
    }

    private fun hayEmpate(tablero: Array<Array<Jugador?>>): Boolean {
        for (i in 0..2) {
            for (j in 0..2) {
                if (tablero[i][j] == null) {
                    return false
                }
            }
        }
        return true
    }

    fun reiniciarJuego() {
        _uiState.value = GameUiState()
    }
}

*
*
*
*
*data class GameUiState(
    val jugador1: Jugador = Jugador("Jugador 1", "X"),
    val jugador2: Jugador = Jugador("Jugador 2", "O"),
    val jugador: Jugador? = null,
    val tablero: Array<Array<Jugador?>> = Array(3) { arrayOfNulls<Jugador?>(3) },
    val estadoJuego: EstadoJuego = EstadoJuego.EN_CURSO,
    val ganador: Jugador? = null
)

*
*
*
* */