package com.example.emojiguess

import com.example.emojiguess.logic.EmojiManager
import com.example.emojiguess.logic.GameEngine
import com.example.emojiguess.models.Game
import com.example.emojiguess.models.GameState
import com.example.emojiguess.models.Player
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para la lógica del juego
 */
class GameEngineTest {
    
    private lateinit var game: Game
    
    @Before
    fun setup() {
        // Crear un juego de prueba con 3 jugadores
        val players = mapOf(
            "player1" to Player("player1", "Alice", "😀", true, true),
            "player2" to Player("player2", "Bob", "😃", true, false),
            "player3" to Player("player3", "Charlie", "😄", true, false)
        )
        
        game = Game(
            roomCode = "TEST01",
            hostId = "player1",
            players = players,
            state = GameState.IN_PROGRESS.name,
            currentRound = 1,
            currentTurnPlayerId = "player1",
            roundStartTime = System.currentTimeMillis(),
            roundDuration = 30
        )
    }
    
    @Test
    fun testValidateCorrectAnswer() {
        val gameEngine = GameEngine(null!!)
        val result = gameEngine.validateAnswer(game, "player1", "😀")
        assertTrue("La respuesta correcta debe ser válida", result)
    }

    
    @Test
    fun testValidateIncorrectAnswer() {
        val gameEngine = GameEngine(null!!)
        val result = gameEngine.validateAnswer(game, "player1", "😃")
        assertFalse("La respuesta incorrecta debe ser inválida", result)
    }
    
    @Test
    fun testCheckGameOverWithOnePlayer() {
        val gameEngine = GameEngine(null!!)
        
        // Eliminar dos jugadores
        val updatedPlayers = game.players.toMutableMap()
        updatedPlayers["player2"] = updatedPlayers["player2"]!!.copy(isAlive = false)
        updatedPlayers["player3"] = updatedPlayers["player3"]!!.copy(isAlive = false)
        
        val gameWithOnePlayer = game.copy(players = updatedPlayers)
        
        val isGameOver = gameEngine.checkGameOver(gameWithOnePlayer)
        assertTrue("El juego debe terminar con solo 1 jugador vivo", isGameOver)
    }
    
    @Test
    fun testCheckGameOverWithMultiplePlayers() {
        val gameEngine = GameEngine(null!!)
        val isGameOver = gameEngine.checkGameOver(game)
        assertFalse("El juego no debe terminar con múltiples jugadores vivos", isGameOver)
    }
    
    @Test
    fun testGetNextPlayer() {
        val gameEngine = GameEngine(null!!)
        val nextPlayer = gameEngine.getNextPlayer(game, "player1")
        assertEquals("El siguiente jugador debe ser player2", "player2", nextPlayer)
    }
    
    @Test
    fun testGetNextPlayerWrapsAround() {
        val gameEngine = GameEngine(null!!)
        val nextPlayer = gameEngine.getNextPlayer(game, "player3")
        assertEquals("Después del último jugador debe volver al primero", "player1", nextPlayer)
    }
    
    @Test
    fun testEmojiAssignment() {
        val emojis = EmojiManager.assignEmojis(5)
        assertEquals("Debe asignar 5 emojis", 5, emojis.size)
        
        // Verificar que todos son únicos
        val uniqueEmojis = emojis.toSet()
        assertEquals("Todos los emojis deben ser únicos", emojis.size, uniqueEmojis.size)
    }
    
    @Test
    fun testGetAlivePlayers() {
        val alivePlayers = game.getAlivePlayers()
        assertEquals("Debe haber 3 jugadores vivos", 3, alivePlayers.size)
    }
    
    @Test
    fun testGetAlivePlayersAfterElimination() {
        val updatedPlayers = game.players.toMutableMap()
        updatedPlayers["player2"] = updatedPlayers["player2"]!!.copy(isAlive = false)
        
        val gameWithElimination = game.copy(players = updatedPlayers)
        val alivePlayers = gameWithElimination.getAlivePlayers()
        
        assertEquals("Debe haber 2 jugadores vivos", 2, alivePlayers.size)
    }
}
