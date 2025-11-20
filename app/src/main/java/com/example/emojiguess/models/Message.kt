package com.example.emojiguess.models

/**
 * Modelo de datos para un mensaje del chat
 * Representa un mensaje enviado dentro del chat global de la sala.
 * Se incluyen valores por defecto para permitir la deserialización automática de Firebase.
 * 
 * @param id Identificador único del mensaje
 * @param playerId ID del jugador que envió el mensaje (alias: senderId)
 * @param playerName Nombre del jugador que envió el mensaje (alias: senderName)
 * @param text Contenido del mensaje (alias: content)
 * @param timestamp Timestamp del mensaje
 */
data class Message(
    val id: String = "",
    val playerId: String = "",
    val playerName: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    // Propiedades alias para compatibilidad con el código del equipo
    val senderId: String get() = playerId
    val senderName: String get() = playerName
    val content: String get() = text
    
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
         * Soporta ambos formatos de nombres de campos para compatibilidad
         */
        fun fromMap(map: Map<String, Any>): Message {
            return Message(
                id = map["id"] as? String ?: "",
                playerId = (map["playerId"] ?: map["senderId"]) as? String ?: "",
                playerName = (map["playerName"] ?: map["senderName"]) as? String ?: "",
                text = (map["text"] ?: map["content"]) as? String ?: "",
                timestamp = map["timestamp"] as? Long ?: System.currentTimeMillis()
            )
        }
    }
}
