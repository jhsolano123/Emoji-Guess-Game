package com.example.emojiguess.models

/**
 * Estados posibles del juego
 */
enum class GameState {
    WAITING,      // Esperando jugadores en el lobby
    STARTING,     // Iniciando el juego
    IN_PROGRESS,  // Juego en curso
    ROUND_END,    // Fin de ronda
    FINISHED      // Juego terminado
}
