package com.example.emojiguess.models

/**
 * Modelo de datos para un jugador en el juego
 * @param id Identificador único del jugador
 * @param name Nombre del jugador
 * @param emoji Emoji asignado al jugador en la ronda actual
 * @param isAlive Estado del jugador (true = sigue en juego, false = eliminado)
 * @param isHost Indica si el jugador es el host de la sala
 */
data class Player(
    val id: String = "",
    val name: String = "",
    val emoji: String = "",
    val isAlive: Boolean = true,
    val isHost: Boolean = false
) {
    /**
     * Convierte el objeto Player a un Map para Firebase
     */
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "name" to name,
            "emoji" to emoji,
            "isAlive" to isAlive,
            "isHost" to isHost
        )
    }
    
    companion object {
        /**
         * Crea un objeto Player desde un Map de Firebase
         */
        fun fromMap(map: Map<String, Any>): Player {
            return Player(
                id = map["id"] as? String ?: "",
                name = map["name"] as? String ?: "",
                emoji = map["emoji"] as? String ?: "",
                isAlive = map["isAlive"] as? Boolean ?: true,
                isHost = map["isHost"] as? Boolean ?: false
            )
        }
    }
}
