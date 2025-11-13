# Ejemplos de Uso del Backend - Para Persona 2 y 3

## 🎯 Guía Rápida de Integración

### 1. Inicialización en Activity/Fragment

```kotlin
class GameActivity : AppCompatActivity() {
    private val repository = GameRepository.getInstance()
    private lateinit var roomCode: String
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        roomCode = intent.getStringExtra(Constants.EXTRA_ROOM_CODE) ?: return
        
        // Observar cambios del juego
        lifecycleScope.launch {
            repository.observeGame(roomCode).collect { game ->
                game?.let { updateUI(it) }
            }
        }
    }
}
```

### 2. Crear Sala (Pantalla de Lobby - Persona 3)

```kotlin
class LobbyActivity : AppCompatActivity() {
    private val repository = GameRepository.getInstance()
    
    private fun createRoom() {
        lifecycleScope.launch {
            try {
                // Autenticar usuario
                val userId = repository.authenticateUser()
                
                // Crear sala
                val playerName = editTextName.text.toString()
                val roomCode = repository.createRoom(playerName)
                
                // Mostrar código de sala
                textViewRoomCode.text = roomCode
                
                // Observar jugadores que se unen
                observeGame(roomCode)
                
            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun observeGame(roomCode: String) {
        lifecycleScope.launch {
            repository.observeGame(roomCode).collect { game ->
                game?.let {
                    // Actualizar lista de jugadores
                    updatePlayerList(it.players.values.toList())
                    
                    // Si el juego inició, ir a GameActivity
                    if (it.getGameState() == GameState.STARTING) {
                        startGameActivity(roomCode)
                    }
                }
            }
        }
    }
}
```

### 3. Unirse a Sala (Pantalla de Join - Persona 3)

```kotlin
private fun joinRoom() {
    lifecycleScope.launch {
        try {
            // Autenticar usuario
            val userId = repository.authenticateUser()
            
            // Unirse a sala
            val roomCode = editTextRoomCode.text.toString()
            val playerName = editTextName.text.toString()
            
            val success = repository.joinRoom(roomCode, playerName)
            
            if (success) {
                // Ir al lobby
                val intent = Intent(this, LobbyActivity::class.java)
                intent.putExtra(Constants.EXTRA_ROOM_CODE, roomCode)
                intent.putExtra(Constants.EXTRA_IS_HOST, false)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Sala no encontrada", Toast.LENGTH_SHORT).show()
            }
            
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
```

### 4. Iniciar Juego (Solo Host - Persona 3)

```kotlin
private fun startGame() {
    lifecycleScope.launch {
        try {
            // Asignar emojis a los jugadores
            repository.assignEmojis(currentGame)
            
            // Iniciar el juego
            repository.startGame(roomCode)
            
        } catch (e: Exception) {
            Toast.makeText(this, "Error al iniciar: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
```

### 5. Mostrar Lista de Jugadores (Persona 2)

```kotlin
class PlayerAdapter(private val currentUserId: String) : 
    RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {
    
    private var players = listOf<Player>()
    
    fun updatePlayers(newPlayers: List<Player>) {
        players = newPlayers
        notifyDataSetChanged()
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = players[position]
        
        holder.textName.text = player.name
        
        // Ocultar emoji del jugador actual
        if (player.id == currentUserId) {
            holder.textEmoji.text = "❓" // Emoji oculto
            holder.textEmoji.setBackgroundResource(R.drawable.bg_my_emoji)
        } else {
            holder.textEmoji.text = player.emoji
        }
        
        // Mostrar si está vivo o eliminado
        holder.itemView.alpha = if (player.isAlive) 1.0f else 0.5f
        
        // Indicador de turno actual
        holder.indicatorTurn.visibility = 
            if (isCurrentTurn(player.id)) View.VISIBLE else View.GONE
    }
}
```

### 6. Implementar Temporizador (Persona 2)

```kotlin
class GameActivity : AppCompatActivity() {
    private var timerJob: Job? = null
    
    private fun startTimer(game: Game) {
        timerJob?.cancel()
        
        timerJob = lifecycleScope.launch {
            while (true) {
                val remaining = repository.getRemainingTime(game)
                
                // Actualizar UI
                textViewTimer.text = remaining.toString()
                progressBarTimer.progress = remaining
                
                // Si el tiempo se acabó
                if (remaining <= 0) {
                    onTimeExpired(game)
                    break
                }
                
                delay(1000) // Actualizar cada segundo
            }
        }
    }
    
    private suspend fun onTimeExpired(game: Game) {
        val currentUserId = repository.getCurrentUserId()
        
        // Si es mi turno y no respondí, me eliminan
        if (game.currentTurnPlayerId == currentUserId) {
            repository.eliminateByTimeout(roomCode, currentUserId!!)
        }
    }
}
```

### 7. Selector de Emoji (Persona 3)

```kotlin
class EmojiSelectorDialog : DialogFragment() {
    
    private lateinit var onEmojiSelected: (String) -> Unit
    
    override fun onCreateView(inflater: LayoutInflater, ...): View {
        val view = inflater.inflate(R.layout.dialog_emoji_selector, container, false)
        
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerEmojis)
        
        // Obtener opciones de emojis (incluye el correcto + otros aleatorios)
        val emojis = EmojiManager.getAllEmojis().shuffled().take(12)
        
        val adapter = EmojiAdapter { selectedEmoji ->
            onEmojiSelected(selectedEmoji)
            dismiss()
        }
        
        adapter.updateEmojis(emojis)
        recyclerView.adapter = adapter
        
        return view
    }
    
    companion object {
        fun show(fragmentManager: FragmentManager, onSelected: (String) -> Unit) {
            val dialog = EmojiSelectorDialog()
            dialog.onEmojiSelected = onSelected
            dialog.show(fragmentManager, "EmojiSelector")
        }
    }
}
```

### 8. Enviar Respuesta (Persona 2)

```kotlin
private fun submitAnswer(selectedEmoji: String) {
    lifecycleScope.launch {
        try {
            // Deshabilitar botones mientras se procesa
            setButtonsEnabled(false)
            
            val isCorrect = repository.submitAnswer(currentGame, selectedEmoji)
            
            if (isCorrect) {
                showFeedback("¡Correcto! ✅", true)
                // Continúas en el juego
            } else {
                showFeedback("Incorrecto ❌", false)
                // Fuiste eliminado
                showEliminationScreen()
            }
            
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
```

### 9. Chat en Tiempo Real (Persona 2)

```kotlin
class ChatFragment : Fragment() {
    private val repository = GameRepository.getInstance()
    private lateinit var roomCode: String
    private lateinit var adapter: ChatAdapter
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        roomCode = arguments?.getString(Constants.EXTRA_ROOM_CODE) ?: return
        
        setupRecyclerView()
        observeMessages()
        setupSendButton()
    }
    
    private fun observeMessages() {
        lifecycleScope.launch {
            repository.observeMessages(roomCode).collect { messages ->
                adapter.updateMessages(messages)
                recyclerView.scrollToPosition(messages.size - 1)
            }
        }
    }
    
    private fun setupSendButton() {
        buttonSend.setOnClickListener {
            val text = editTextMessage.text.toString()
            
            if (text.isNotBlank()) {
                lifecycleScope.launch {
                    repository.sendMessage(roomCode, text)
                    editTextMessage.text.clear()
                }
            }
        }
    }
}
```

### 10. Detectar Victoria (Persona 2 y 3)

```kotlin
private fun observeGame(roomCode: String) {
    lifecycleScope.launch {
        repository.observeGame(roomCode).collect { game ->
            game?.let {
                when (it.getGameState()) {
                    GameState.WAITING -> showLobby()
                    GameState.STARTING -> showStartingAnimation()
                    GameState.IN_PROGRESS -> showGameScreen(it)
                    GameState.ROUND_END -> showRoundEndScreen(it)
                    GameState.FINISHED -> showVictoryScreen(it)
                }
                
                // Verificar si hay ganador
                it.winnerId?.let { winnerId ->
                    val winner = it.players[winnerId]
                    showWinnerDialog(winner?.name ?: "Desconocido")
                }
            }
        }
    }
}
```

### 11. Salir del Juego (Persona 3)

```kotlin
private fun exitGame() {
    AlertDialog.Builder(this)
        .setTitle("Salir del juego")
        .setMessage("¿Estás seguro de que quieres salir?")
        .setPositiveButton("Sí") { _, _ ->
            lifecycleScope.launch {
                repository.leaveRoom(roomCode)
                finish()
            }
        }
        .setNegativeButton("No", null)
        .show()
}

override fun onBackPressed() {
    exitGame()
}
```

### 12. ViewModel Completo (Persona 2)

```kotlin
class GameViewModel : ViewModel() {
    private val repository = GameRepository.getInstance()
    
    private val _game = MutableLiveData<Game?>()
    val game: LiveData<Game?> = _game
    
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages
    
    private val _remainingTime = MutableLiveData<Int>()
    val remainingTime: LiveData<Int> = _remainingTime
    
    fun observeGame(roomCode: String) {
        viewModelScope.launch {
            repository.observeGame(roomCode).collect {
                _game.postValue(it)
            }
        }
    }
    
    fun observeMessages(roomCode: String) {
        viewModelScope.launch {
            repository.observeMessages(roomCode).collect {
                _messages.postValue(it)
            }
        }
    }
    
    fun startTimer(game: Game) {
        viewModelScope.launch {
            while (true) {
                val remaining = repository.getRemainingTime(game)
                _remainingTime.postValue(remaining)
                
                if (remaining <= 0) break
                delay(1000)
            }
        }
    }
    
    fun submitAnswer(selectedEmoji: String) {
        viewModelScope.launch {
            game.value?.let {
                repository.submitAnswer(it, selectedEmoji)
            }
        }
    }
    
    fun sendMessage(roomCode: String, text: String) {
        viewModelScope.launch {
            repository.sendMessage(roomCode, text)
        }
    }
}
```

## 🎨 Datos para la UI

### Obtener mi jugador:
```kotlin
val myId = repository.getCurrentUserId()
val myPlayer = game.players[myId]
```

### Verificar si es mi turno:
```kotlin
val isMyTurn = game.currentTurnPlayerId == repository.getCurrentUserId()
```

### Obtener jugadores vivos:
```kotlin
val alivePlayers = game.getAlivePlayers()
```

### Verificar si soy el host:
```kotlin
val isHost = game.players[repository.getCurrentUserId()]?.isHost == true
```

## 🔄 Flujo Completo del Juego

1. **WelcomeActivity** → Crear o Unirse
2. **LobbyActivity** → Esperar jugadores
3. Host presiona "Iniciar" → `repository.startGame()`
4. **GameActivity** → Juego en progreso
5. Cada jugador en su turno selecciona emoji
6. Si acierta → continúa, si falla → eliminado
7. Cuando queda 1 jugador → **ResultActivity**

---

**¡Todo está listo para integrar! 🚀**
