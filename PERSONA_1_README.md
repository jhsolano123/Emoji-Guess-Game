# Persona 1 - Backend & Firebase - COMPLETADO ✅

## 🎯 Trabajo Realizado

He completado toda la implementación del backend, Firebase y lógica del juego. Aquí está todo lo que se ha creado:

## 📁 Archivos Creados

### Modelos de Datos (`models/`)
- ✅ `Player.kt` - Modelo de jugador con métodos de conversión Firebase
- ✅ `Game.kt` - Modelo del juego con toda la información de la partida
- ✅ `GameState.kt` - Enum con los estados del juego
- ✅ `Message.kt` - Modelo de mensajes del chat

### Lógica del Juego (`logic/`)
- ✅ `EmojiManager.kt` - Gestión de emojis (100+ emojis disponibles)
  - Asignación aleatoria de emojis únicos
  - Obtención de opciones para el selector
- ✅ `GameEngine.kt` - Motor completo del juego
  - Control de turnos
  - Validación de respuestas
  - Detección de victoria
  - Gestión de rondas
  - Eliminación de jugadores
  - Temporizador

### Capa de Datos (`data/`)
- ✅ `FirebaseManager.kt` - Gestor completo de Firebase
  - Autenticación anónima
  - CRUD de salas
  - Sincronización en tiempo real con Flow
  - Gestión de chat
- ✅ `GameRepository.kt` - Repositorio que centraliza toda la lógica
  - Patrón Singleton
  - API simplificada para la UI

### Pruebas (`test/`)
- ✅ `GameEngineTest.kt` - 9 pruebas unitarias
  - Validación de respuestas correctas/incorrectas
  - Detección de victoria
  - Rotación de turnos
  - Asignación de emojis
  - Conteo de jugadores vivos

## 🔧 Configuración de Firebase

### Paso 1: Crear Proyecto en Firebase
1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Crea un nuevo proyecto llamado "Emoji Guess"
3. Habilita Google Analytics (opcional)

### Paso 2: Agregar App Android
1. En la consola de Firebase, haz clic en "Agregar app" → Android
2. Package name: `com.example.emojiguess`
3. Descarga el archivo `google-services.json`
4. Coloca el archivo en `app/google-services.json`

### Paso 3: Habilitar Servicios
1. **Realtime Database**:
   - Ve a "Build" → "Realtime Database"
   - Crea una base de datos
   - Inicia en modo de prueba (reglas abiertas por ahora)
   
2. **Authentication**:
   - Ve a "Build" → "Authentication"
   - Habilita "Anonymous" en la pestaña "Sign-in method"

### Reglas de Seguridad Recomendadas (Realtime Database)
```json
{
  "rules": {
    "games": {
      "$roomCode": {
        ".read": true,
        ".write": true
      }
    },
    "messages": {
      "$roomCode": {
        ".read": true,
        ".write": true
      }
    }
  }
}
```

## 🏗️ Arquitectura Implementada

```
┌─────────────────────────────────────────┐
│           UI Layer (Persona 2 y 3)      │
│         Activities / ViewModels          │
└──────────────────┬──────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────┐
│         GameRepository (Singleton)       │
│    API unificada para toda la lógica    │
└──────────────────┬──────────────────────┘
                   │
        ┌──────────┴──────────┐
        ▼                     ▼
┌──────────────┐    ┌──────────────────┐
│  GameEngine  │    │ FirebaseManager  │
│   (Lógica)   │    │   (Datos RT)     │
└──────────────┘    └──────────────────┘
        │                     │
        ▼                     ▼
┌──────────────┐    ┌──────────────────┐
│ EmojiManager │    │  Firebase Cloud  │
└──────────────┘    └──────────────────┘
```

## 📊 Funcionalidades Implementadas

### ✅ Gestión de Salas
- Crear sala con código único de 6 caracteres
- Unirse a sala existente
- Observar cambios en tiempo real
- Salir de sala
- Eliminar sala

### ✅ Lógica del Juego
- Asignación aleatoria de emojis únicos
- Control de turnos entre jugadores
- Temporizador de 30 segundos por turno
- Validación de respuestas
- Eliminación automática por respuesta incorrecta
- Eliminación por timeout
- Detección de victoria (último jugador en pie)
- Sistema de rondas con reasignación de emojis

### ✅ Sistema de Chat
- Envío de mensajes
- Recepción en tiempo real
- Ordenamiento por timestamp

### ✅ Autenticación
- Login anónimo automático
- Gestión de sesión

## 🧪 Ejecutar Pruebas

```bash
./gradlew test
```

Las pruebas verifican:
- Validación de respuestas
- Lógica de turnos
- Detección de victoria
- Asignación de emojis
- Conteo de jugadores

## 📝 API para Persona 2 y 3

### Uso Básico del GameRepository

```kotlin
// Obtener instancia
val repository = GameRepository.getInstance()

// Autenticar usuario
val userId = repository.authenticateUser()

// Crear sala
val roomCode = repository.createRoom("NombreJugador")

// Unirse a sala
val success = repository.joinRoom("ABC123", "NombreJugador")

// Observar cambios del juego
repository.observeGame(roomCode).collect { game ->
    // Actualizar UI con el estado del juego
}

// Iniciar juego (solo host)
repository.startGame(roomCode)

// Asignar emojis al inicio de cada ronda
repository.assignEmojis(game)

// Enviar respuesta
val isCorrect = repository.submitAnswer(game, "😀")

// Observar mensajes del chat
repository.observeMessages(roomCode).collect { messages ->
    // Actualizar UI del chat
}

// Enviar mensaje
repository.sendMessage(roomCode, "Hola!")
```

### Flujo del Juego

1. **Lobby**: Estado `WAITING`
2. **Inicio**: Host llama `startGame()` → Estado `STARTING`
3. **Asignar Emojis**: `assignEmojis(game)`
4. **Ronda**: Estado `IN_PROGRESS`
5. **Turnos**: Cada jugador selecciona su emoji
6. **Validación**: Correcto = continúa, Incorrecto = eliminado
7. **Fin de Ronda**: `endRound()` → reasigna emojis
8. **Victoria**: Cuando queda 1 jugador → Estado `FINISHED`

## 🔄 Estados del Juego

- `WAITING`: Esperando jugadores en lobby
- `STARTING`: Iniciando partida
- `IN_PROGRESS`: Juego en curso
- `ROUND_END`: Fin de ronda
- `FINISHED`: Juego terminado

## 📦 Dependencias Agregadas

```kotlin
// Firebase
implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
implementation("com.google.firebase:firebase-database-ktx")
implementation("com.google.firebase:firebase-auth-ktx")

// Lifecycle
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
```

## ✅ Checklist de Completado

- [x] Configuración de Firebase en Gradle
- [x] Modelos de datos (Player, Game, GameState, Message)
- [x] FirebaseManager con sincronización en tiempo real
- [x] EmojiManager con 100+ emojis
- [x] GameEngine con toda la lógica del juego
- [x] GameRepository como API unificada
- [x] Pruebas unitarias (9 tests)
- [x] Documentación completa

## 🚀 Próximos Pasos (Para Persona 2 y 3)

**Persona 2** puede ahora:
- Crear las Activities y ViewModels
- Implementar la UI del juego
- Integrar el chat usando `observeMessages()` y `sendMessage()`
- Mostrar la lista de jugadores con sus emojis

**Persona 3** puede ahora:
- Crear pantallas de Welcome, Lobby y Result
- Implementar navegación entre pantallas
- Agregar animaciones
- Crear el selector de emoji

## 💡 Notas Importantes

1. **Singleton Pattern**: Tanto `FirebaseManager` como `GameRepository` usan Singleton
2. **Flows**: Todos los observables usan Kotlin Flow para reactividad
3. **Coroutines**: Todas las operaciones async usan suspend functions
4. **Thread-Safe**: Los Singletons están protegidos con synchronized
5. **Error Handling**: Todas las operaciones de Firebase usan try-catch implícito

## 🐛 Testing

Para probar sin Firebase real, puedes:
1. Usar Firebase Emulator Suite
2. Mockear FirebaseManager en tests
3. Las pruebas unitarias actuales no requieren Firebase

## 📞 Contacto

Si Persona 2 o 3 tienen dudas sobre cómo usar el backend:
- Revisar los comentarios en el código (todo está documentado)
- Ver ejemplos de uso en este README
- Los modelos tienen métodos `toMap()` y `fromMap()` para Firebase

---

**¡El backend está 100% listo para que el equipo continúe! 🎉**
