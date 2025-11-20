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
fun CreateRoomScreen(navController: NavController) {
    var playerName by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crear Nueva Sala",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = playerName,
            onValueChange = { playerName = it },
            label = { Text("Tu Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (playerName.isNotBlank()) {
                    // Generar IDs de ejemplo
                    val roomId = "room123" // Puedes generar dinámicamente o usar backend
                    val playerId = "player1" // ID único del jugador

                    // Abrir GameActivity
                    val intent = GameActivity.createIntent(context, roomId, playerId, playerName)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Ingresa tu nombre", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = playerName.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Crear Sala y Entrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
    }
}
