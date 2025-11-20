package com.example.emojiguess

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.emojiguess.ui.game.GameActivity

@Composable
fun JoinRoomScreen(navController: NavController) {
    var playerName by remember { mutableStateOf("") }
    var roomId by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Unirse a una Sala",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo para nombre del jugador
        OutlinedTextField(
            value = playerName,
            onValueChange = { playerName = it },
            label = { Text("Tu Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para ID de la sala
        OutlinedTextField(
            value = roomId,
            onValueChange = { roomId = it },
            label = { Text("ID de la Sala") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón para unirse y abrir GameActivity
        Button(
            onClick = {
                if (playerName.isNotBlank() && roomId.isNotBlank()) {
                    val playerId = "player1" // Puedes generar dinámicamente si quieres
                    val intent = GameActivity.createIntent(context, roomId, playerId, playerName)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(
                        context,
                        "Ingresa tu nombre y el ID de la sala",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            enabled = playerName.isNotBlank() && roomId.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Unirse a Sala")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de volver
        TextButton(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
    }
}
