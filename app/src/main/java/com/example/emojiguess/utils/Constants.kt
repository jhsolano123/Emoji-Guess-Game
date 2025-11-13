package com.example.emojiguess.utils

/**
 * Constantes globales del juego
 */
object Constants {
    
    // Configuración del juego
    const val DEFAULT_ROUND_DURATION = 30 // segundos
    const val MIN_PLAYERS = 2
    const val MAX_PLAYERS = 8
    const val ROOM_CODE_LENGTH = 6
    
    // Tiempos de espera
    const val GAME_START_DELAY = 1000L // ms
    const val ROUND_END_DELAY = 2000L // ms
    const val ELIMINATION_ANIMATION_DELAY = 1000L // ms
    
    // Claves de Intent/Bundle
    const val EXTRA_ROOM_CODE = "room_code"
    const val EXTRA_PLAYER_NAME = "player_name"
    const val EXTRA_IS_HOST = "is_host"
    const val EXTRA_WINNER_NAME = "winner_name"
    
    // Estados del juego (para referencia)
    const val STATE_WAITING = "WAITING"
    const val STATE_STARTING = "STARTING"
    const val STATE_IN_PROGRESS = "IN_PROGRESS"
    const val STATE_ROUND_END = "ROUND_END"
    const val STATE_FINISHED = "FINISHED"
    
    // Mensajes del sistema
    const val SYSTEM_MESSAGE_PLAYER_JOINED = "%s se unió al juego"
    const val SYSTEM_MESSAGE_PLAYER_LEFT = "%s salió del juego"
    const val SYSTEM_MESSAGE_PLAYER_ELIMINATED = "%s fue eliminado"
    const val SYSTEM_MESSAGE_GAME_STARTED = "¡El juego ha comenzado!"
    const val SYSTEM_MESSAGE_NEW_ROUND = "¡Nueva ronda! Ronda %d"
    const val SYSTEM_MESSAGE_GAME_OVER = "¡%s ha ganado el juego!"
    
    // Configuración de emojis
    const val EMOJI_SELECTOR_OPTIONS = 12 // Número de opciones en el selector
    
    // Validación
    const val MIN_PLAYER_NAME_LENGTH = 2
    const val MAX_PLAYER_NAME_LENGTH = 20
    
    // Firebase paths (para referencia)
    const val FIREBASE_GAMES_PATH = "games"
    const val FIREBASE_MESSAGES_PATH = "messages"
    const val FIREBASE_PLAYERS_PATH = "players"
}
