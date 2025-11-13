package com.example.emojiguess.models

/**
 * Modelo de datos para un mensaje del chat
 * @param id Identificador único del mensaje
 * @param playerId ID del jugador que envió el mensaje
 * @param playerName Nombre del jugador que envió el mensaje
 * @param text Contenido del mensaje
 * @param timestamp Timestamp del mensaje
 */
data class Message(
    val id: String = "",
    val playerId: String = "",
    val playerName: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Convierte el objeto Message a un Map para Firebase
     */
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "playerId" to playerId,
            "playerName" to playerName,
            "text" to text,
            "timestamp" to timestamp
        )
    }
    
    companion object {
        /**
         * Crea un objeto Message desde un Map de Firebase
         */
        fun fromMap(map: Map<String, Any>): Message {
            return Message(
                id = map["id"] as? String ?: "",
                playerId = map["playerId"] as? String ?: "",
                playerName = map["playerName"] as? String ?: "",
                text = map["text"] as? String ?: "",
                timestamp = map["timestamp"] as? Long ?: System.currentTimeMillis()
            )
        }
    }
}
