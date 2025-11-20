package com.example.emojiguess

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.emojiguess.ui.game.GameActivity

data class Player(val id: Int, val name: String, val isHost: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(
    navController: NavController,
    roomId: String,
    playerName: String,
    isHost: Boolean
) {
    val players = remember {
        mutableStateListOf(
            Player(1, "$playerName (Host)", isHost),
            Player(2, "Jugador 2"),
            Player(3, "Jugador 3"),
            Player(4, "Jugador 4")
        )
    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Sala: $roomId") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Esperando jugadores...",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Lista de jugadores
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(players) { player ->
                        ListItem(
                            leadingContent = {
                                Icon(Icons.Filled.Person, contentDescription = "Jugador")
                            },
                            headlineContent = { Text(player.name) },
                            trailingContent = {
                                if (player.isHost) {
                                    Text("HOST", color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón para iniciar partida (solo visible para el host)
            if (isHost) {
                Button(
                    onClick = {
                        val playerId = "player1" // O tu lógica para asignar ID
                        val intent = GameActivity.createIntent(context, roomId, playerId, playerName)
                        context.startActivity(intent)
                    }
                ) {
                    Text("Iniciar Partida (${players.size}/4)")
                }
            } else {
                Text(
                    text = "Esperando a que el Host inicie la partida...",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para salir del juego (con diálogo de confirmación)
            var showExitDialog by remember { mutableStateOf(false) }

            OutlinedButton(
                onClick = { showExitDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Salir del Juego")
            }

            if (showExitDialog) {
                AlertDialog(
                    onDismissRequest = { showExitDialog = false },
                    title = { Text("Confirmar Salida") },
                    text = { Text("¿Estás seguro de que quieres salir de la sala? Perderás tu progreso.") },
                    confirmButton = {
                        Button(
                            onClick = {
                                showExitDialog = false
                                navController.popBackStack()
                            }
                        ) {
                            Text("Sí, Salir")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showExitDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}
