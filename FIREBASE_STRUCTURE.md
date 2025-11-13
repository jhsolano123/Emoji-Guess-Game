# Estructura de Firebase Realtime Database

## 📊 Organización de Datos

```
firebase-root/
├── games/
│   └── {roomCode}/              # Ej: "ABC123"
│       ├── roomCode: "ABC123"
│       ├── hostId: "user123"
│       ├── state: "IN_PROGRESS"
│       ├── currentRound: 1
│       ├── currentTurnPlayerId: "user123"
│       ├── roundStartTime: 1699999999999
│       ├── roundDuration: 30
│       ├── winnerId: ""
│       └── players/
│           ├── {playerId1}/     # Ej: "user123"
│           │   ├── id: "user123"
│           │   ├── name: "Alice"
│           │   ├── emoji: "😀"
│           │   ├── isAlive: true
│           │   └── isHost: true
│           ├── {playerId2}/
│           │   ├── id: "user456"
│           │   ├── name: "Bob"
│           │   ├── emoji: "😃"
│           │   ├── isAlive: true
│           │   └── isHost: false
│           └── {playerId3}/
│               ├── id: "user789"
│               ├── name: "Charlie"
│               ├── emoji: "😄"
│               ├── isAlive: false
│               └── isHost: false
│
└── messages/
    └── {roomCode}/              # Ej: "ABC123"
        ├── {messageId1}/        # Auto-generado por push()
        │   ├── id: "msg1"
        │   ├── playerId: "user123"
        │   ├── playerName: "Alice"
        │   ├── text: "Hola a todos!"
        │   └── timestamp: 1699999999999
        ├── {messageId2}/
        │   ├── id: "msg2"
        │   ├── playerId: "user456"
        │   ├── playerName: "Bob"
        │   ├── text: "Creo que tengo 😀"
        │   └── timestamp: 1699999999999
        └── {messageId3}/
            ├── id: "msg3"
            ├── playerId: "user789"
            ├── playerName: "Charlie"
            ├── text: "No estoy seguro..."
            └── timestamp: 1699999999999
```

## 🔑 Campos Importantes

### Game
- **roomCode**: Código único de 6 caracteres (A-Z, 0-9)
- **hostId**: ID del jugador que creó la sala
- **state**: Estado actual del juego (WAITING, STARTING, IN_PROGRESS, ROUND_END, FINISHED)
- **currentRound**: Número de ronda actual (empieza en 1)
- **currentTurnPlayerId**: ID del jugador que tiene el turno actual
- **roundStartTime**: Timestamp en milisegundos del inicio del turno
- **roundDuration**: Duración del turno en segundos (default: 30)
- **winnerId**: ID del ganador (vacío si no hay ganador aún)

### Player
- **id**: ID único del jugador (Firebase Auth UID)
- **name**: Nombre del jugador
- **emoji**: Emoji asignado en la ronda actual
- **isAlive**: true si sigue en juego, false si fue eliminado
- **isHost**: true si es el creador de la sala

### Message
- **id**: ID único del mensaje
- **playerId**: ID del jugador que envió el mensaje
- **playerName**: Nombre del jugador (para mostrar en UI)
- **text**: Contenido del mensaje
- **timestamp**: Timestamp en milisegundos

## 🔄 Flujo de Datos en Tiempo Real

### 1. Crear Sala
```
POST /games/{roomCode}
{
  roomCode: "ABC123",
  hostId: "user123",
  state: "WAITING",
  players: {
    "user123": { ... }
  }
}
```

### 2. Unirse a Sala
```
POST /games/{roomCode}/players/{playerId}
{
  id: "user456",
  name: "Bob",
  isAlive: true,
  isHost: false
}
```

### 3. Iniciar Juego
```
UPDATE /games/{roomCode}
{
  state: "STARTING"
}
```

### 4. Asignar Emojis
```
UPDATE /games/{roomCode}/players
{
  "user123": { emoji: "😀", ... },
  "user456": { emoji: "😃", ... }
}
```

### 5. Iniciar Turno
```
UPDATE /games/{roomCode}
{
  currentTurnPlayerId: "user123",
  roundStartTime: 1699999999999
}
```

### 6. Eliminar Jugador
```
UPDATE /games/{roomCode}/players/{playerId}
{
  isAlive: false
}
```

### 7. Enviar Mensaje
```
POST /messages/{roomCode}/{autoId}
{
  playerId: "user123",
  playerName: "Alice",
  text: "Hola!",
  timestamp: 1699999999999
}
```

## 👀 Observables (Listeners)

### Observar Juego Completo
```kotlin
firebaseManager.observeGame(roomCode).collect { game ->
    // Se emite cada vez que cambia cualquier dato del juego
}
```

### Observar Mensajes
```kotlin
firebaseManager.observeMessages(roomCode).collect { messages ->
    // Se emite cada vez que hay un nuevo mensaje
}
```

## 🔒 Reglas de Seguridad (Producción)

Para producción, usa estas reglas más seguras:

```json
{
  "rules": {
    "games": {
      "$roomCode": {
        ".read": true,
        ".write": "auth != null",
        "players": {
          "$playerId": {
            ".write": "$playerId === auth.uid || data.parent().child('hostId').val() === auth.uid"
          }
        }
      }
    },
    "messages": {
      "$roomCode": {
        ".read": true,
        ".write": "auth != null"
      }
    }
  }
}
```

## 💾 Limpieza de Datos

Las salas se eliminan cuando:
1. El host llama a `deleteRoom()`
2. Todos los jugadores salen de la sala
3. (Opcional) Implementar Cloud Functions para limpiar salas inactivas

## 📱 Ejemplo de Uso en UI

```kotlin
// En tu ViewModel
class GameViewModel : ViewModel() {
    private val repository = GameRepository.getInstance()
    
    val game = MutableLiveData<Game?>()
    val messages = MutableLiveData<List<Message>>()
    
    fun observeGame(roomCode: String) {
        viewModelScope.launch {
            repository.observeGame(roomCode).collect {
                game.postValue(it)
            }
        }
    }
    
    fun observeMessages(roomCode: String) {
        viewModelScope.launch {
            repository.observeMessages(roomCode).collect {
                messages.postValue(it)
            }
        }
    }
}
```

## 🎯 Datos Importantes para la UI

### Para mostrar lista de jugadores:
```kotlin
game.players.values.forEach { player ->
    // Mostrar nombre, emoji (si no es el jugador actual), estado vivo/muerto
}
```

### Para ocultar el emoji del jugador actual:
```kotlin
val currentUserId = repository.getCurrentUserId()
val myPlayer = game.players[currentUserId]
// Mostrar "?" en lugar de myPlayer.emoji
```

### Para mostrar el temporizador:
```kotlin
val remainingTime = repository.getRemainingTime(game)
// Actualizar cada segundo
```

### Para verificar si es mi turno:
```kotlin
val isMyTurn = game.currentTurnPlayerId == repository.getCurrentUserId()
```

### Para verificar si soy el host:
```kotlin
val currentUserId = repository.getCurrentUserId()
val isHost = game.players[currentUserId]?.isHost == true
```

---

**Esta estructura está optimizada para sincronización en tiempo real y escalabilidad.**
