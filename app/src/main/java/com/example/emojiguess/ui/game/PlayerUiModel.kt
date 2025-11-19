package com.example.emojiguess.ui.game

/**
 * Modelo ligero utilizado por la capa de UI para representar el estado visual de cada jugador.
 */
data class PlayerUiModel(
    val id: String,
    val displayName: String,
    val visibleEmoji: String,
    val isCurrentTurn: Boolean,
    val isSelf: Boolean,
    val isEliminated: Boolean
)