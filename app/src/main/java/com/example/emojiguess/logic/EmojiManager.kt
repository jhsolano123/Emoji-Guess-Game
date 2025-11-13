package com.example.emojiguess.logic

/**
 * Gestor de emojis para el juego
 * Maneja la lista de emojis disponibles y su asignación aleatoria
 */
object EmojiManager {
    
    /**
     * Lista de emojis disponibles para el juego
     */
    private val availableEmojis = listOf(
        "😀", "😃", "😄", "😁", "😆", "😅", "🤣", "😂",
        "🙂", "🙃", "😉", "😊", "😇", "🥰", "😍", "🤩",
        "😘", "😗", "😚", "😙", "🥲", "😋", "😛", "😜",
        "🤪", "😝", "🤑", "🤗", "🤭", "🤫", "🤔", "🤐",
        "🤨", "😐", "😑", "😶", "😏", "😒", "🙄", "😬",
        "🤥", "😌", "😔", "😪", "🤤", "😴", "😷", "🤒",
        "🤕", "🤢", "🤮", "🤧", "🥵", "🥶", "🥴", "😵",
        "🤯", "🤠", "🥳", "🥸", "😎", "🤓", "🧐", "😕",
        "😟", "🙁", "☹️", "😮", "😯", "😲", "😳", "🥺",
        "😦", "😧", "😨", "😰", "😥", "😢", "😭", "😱",
        "😖", "😣", "😞", "😓", "😩", "😫", "🥱", "😤",
        "😡", "😠", "🤬", "😈", "👿", "💀", "☠️", "💩",
        "🤡", "👹", "👺", "👻", "👽", "👾", "🤖", "😺",
        "😸", "😹", "😻", "😼", "😽", "🙀", "😿", "😾"
    )
    
    /**
     * Obtiene un emoji aleatorio de la lista
     */
    fun getRandomEmoji(): String {
        return availableEmojis.random()
    }
    
    /**
     * Asigna emojis únicos a una lista de jugadores
     * @param playerCount Número de jugadores
     * @return Lista de emojis únicos (uno por jugador)
     */
    fun assignEmojis(playerCount: Int): List<String> {
        require(playerCount <= availableEmojis.size) {
            "No hay suficientes emojis para $playerCount jugadores"
        }
        return availableEmojis.shuffled().take(playerCount)
    }
    
    /**
     * Obtiene todos los emojis disponibles
     */
    fun getAllEmojis(): List<String> {
        return availableEmojis.toList()
    }
    
    /**
     * Obtiene un subconjunto aleatorio de emojis para mostrar como opciones
     * @param count Número de emojis a obtener
     * @param includeEmoji Emoji que debe estar incluido en la lista
     */
    fun getEmojiOptions(count: Int, includeEmoji: String): List<String> {
        val options = mutableListOf(includeEmoji)
        val remaining = availableEmojis.filter { it != includeEmoji }.shuffled()
        options.addAll(remaining.take(count - 1))
        return options.shuffled()
    }
}
