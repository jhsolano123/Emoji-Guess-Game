package com.example.emojiguess.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val _roomId = MutableLiveData<String>()
    val roomId: LiveData<String> = _roomId

    private val _players = MutableLiveData<List<PlayerUiModel>>(emptyList())
    val players: LiveData<List<PlayerUiModel>> = _players

    private val _availableEmojis = MutableLiveData<List<String>>(emptyList())
    val availableEmojis: LiveData<List<String>> = _availableEmojis

    private val _selectedEmoji = MutableLiveData<String?>(null)
    val selectedEmoji: LiveData<String?> = _selectedEmoji

    private val _hiddenEmoji = MutableLiveData("?")
    val hiddenEmoji: LiveData<String> = _hiddenEmoji

    private val _statusMessage = MutableLiveData<String>("")
    val statusMessage: LiveData<String> = _statusMessage

    private val _timerSeconds = MutableLiveData(30)
    val timerSeconds: LiveData<Int> = _timerSeconds

    private val _isPlayerTurn = MutableLiveData(false)
    val isPlayerTurn: LiveData<Boolean> = _isPlayerTurn

    private val _isConfirmEnabled = MediatorLiveData<Boolean>().apply {
        value = false
        addSource(_selectedEmoji) { evaluateConfirmState() }
        addSource(_isPlayerTurn) { evaluateConfirmState() }
    }
    val isConfirmEnabled: LiveData<Boolean> = _isConfirmEnabled

    private var timerJob: Job? = null
    private var selfPlayerId: String = ""
    private var selfPlayerName: String = ""

    fun initialize(roomId: String, playerId: String, playerName: String) {
        _roomId.value = roomId
        selfPlayerId = playerId
        selfPlayerName = playerName
        if (_players.value.isNullOrEmpty()) {
            bootstrapPlayers()
        }
    }

    private fun bootstrapPlayers() {
        val fakePlayers = listOf(
            PlayerUiModel(id = selfPlayerId, displayName = selfPlayerName, visibleEmoji = "?", isCurrentTurn = true, isSelf = true, isEliminated = false),
            PlayerUiModel(id = "p2", displayName = "Luna", visibleEmoji = "😎", isCurrentTurn = false, isSelf = false, isEliminated = false),
            PlayerUiModel(id = "p3", displayName = "Max", visibleEmoji = "🤠", isCurrentTurn = false, isSelf = false, isEliminated = false),
            PlayerUiModel(id = "p4", displayName = "Kai", visibleEmoji = "🤖", isCurrentTurn = false, isSelf = false, isEliminated = false)
        )
        _players.value = fakePlayers
        _availableEmojis.value = EMOJI_POOL.shuffled().take(12)
        _isPlayerTurn.value = true
        _statusMessage.value = STATUS_SELECT
        startTimer()
    }

    fun onEmojiSelected(emoji: String) {
        _selectedEmoji.value = emoji
        refreshStatus()
    }

    fun confirmSelection() {
        if (_isPlayerTurn.value != true || _selectedEmoji.value.isNullOrBlank()) return
        val guess = _selectedEmoji.value ?: return
        _statusMessage.value = "Intentaste con $guess. Espera resultados..."
        _selectedEmoji.value = null
        advanceTurn()
    }

    private fun advanceTurn() {
        val currentPlayers = _players.value ?: return
        val currentIndex = currentPlayers.indexOfFirst { it.isCurrentTurn }
        val nextIndex = if (currentIndex == -1) 0 else (currentIndex + 1) % currentPlayers.size
        val updatedPlayers = currentPlayers.mapIndexed { index, player ->
            player.copy(isCurrentTurn = index == nextIndex)
        }
        _players.value = updatedPlayers
        _isPlayerTurn.value = updatedPlayers.getOrNull(nextIndex)?.id == selfPlayerId
        refreshStatus()
        startTimer()
    }

    private fun refreshStatus() {
        _statusMessage.value = when {
            _isPlayerTurn.value != true -> STATUS_WAITING
            _selectedEmoji.value.isNullOrBlank() -> STATUS_SELECT
            else -> "Listo para confirmar ${_selectedEmoji.value}"
        }
    }

    private fun startTimer(duration: Int = Random.nextInt(20, 35)) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (second in duration downTo 0) {
                _timerSeconds.postValue(second)
                delay(1_000)
            }
            if (_isPlayerTurn.value == true) {
                _statusMessage.postValue("Se acabó el tiempo de tu turno")
            }
        }
    }

    private fun MediatorLiveData<Boolean>.evaluateConfirmState() {
        value = _isPlayerTurn.value == true && !_selectedEmoji.value.isNullOrBlank()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    companion object {
        private val EMOJI_POOL = listOf("😀", "😎", "🤠", "🤖", "👽", "🥳", "😺", "🦊", "🐼", "🦄", "🐧", "🐸", "🐙", "🍀", "🌈", "⚡️")
        private const val STATUS_SELECT = "Selecciona un emoji para adivinar"
        private const val STATUS_WAITING = "Esperando tu turno"
    }
}