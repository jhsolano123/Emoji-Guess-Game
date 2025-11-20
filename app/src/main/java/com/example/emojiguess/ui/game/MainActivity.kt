package com.example.emojiguess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.emojiguess.ui.theme.EmojiGuessTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmojiGuessTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EmojiGuessApp()
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object CreateRoom : Screen("create_room")
    object JoinRoom : Screen("join_room")
    object Lobby : Screen("lobby")
    object Game : Screen("game")
    object Victory : Screen("victory")
    object Defeat : Screen("defeat")
}

@Composable
fun EmojiGuessApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(Screen.CreateRoom.route) {
            CreateRoomScreen(navController)
        }
        composable(Screen.JoinRoom.route) {
            JoinRoomScreen(navController)
        }
        composable(Screen.Lobby.route) {
            // Pasa los datos necesarios a LobbyScreen
            val roomId = "ABCD"        // Aquí puedes reemplazar con la lógica real
            val playerName = "Jugador1" // Aquí también
            val isHost = true           // Simulación de host
            LobbyScreen(
                navController = navController,
                roomId = roomId,
                playerName = playerName,
                isHost = isHost
            )
        }
        composable(Screen.Victory.route) {
            VictoryScreen(navController)
        }
        composable(Screen.Defeat.route) {
            DefeatScreen(navController)
        }
    }
}
