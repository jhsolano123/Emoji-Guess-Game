package com.example.emojiguess.models

/**
 * Representa un mensaje enviado dentro del chat global de la sala.
 * Se incluyen valores por defecto para permitir la deserialización automática de Firebase.
 */
data class Message(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val content: String = "",
    val timestamp: Long = 0L
)
