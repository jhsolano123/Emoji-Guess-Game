# División de Trabajo - Emoji Guess Game
## Proyecto: Aplicación Android con Kotlin para juego multijugador en línea


## 📋 PERSONA 1 - Backend & Firebase 

### Responsabilidades Principales:
1. **Integración completa con Firebase (1.0 punto)**
   - Configurar Firebase en el proyecto Android
   - Implementar Firebase Realtime Database para sincronización en tiempo real
   - Crear estructura de datos para:
     - Salas de juego (game rooms)
     - Jugadores (players)
     - Estado del juego (game state)
     - Turnos y rondas
   - Implementar listeners para cambios en tiempo real
   - Gestionar autenticación anónima de jugadores

2. **Lógica del Juego - Core (1.0 punto)**
   - Sistema de asignación aleatoria de emojis
   - Control de turnos entre jugadores
   - Temporizador de ronda (countdown)
   - Validación de respuestas (correcta/incorrecta)
   - Sistema de eliminación de jugadores
   - Detección de victoria (último jugador en pie)
   - Reasignación de emojis en cada ronda

3. **Pruebas Unitarias (0.25 puntos)**
   - Crear pruebas para la lógica de asignación de emojis
   - Probar validación de respuestas
   - Probar detección de victoria


## 📋 PERSONA 2 - Chat & Comunicación

### Responsabilidades Principales:
1. **Sistema de Chat Completo (1.5 puntos)**
   - Implementar chat global en tiempo real con Firebase
   - Diseño de interfaz del chat (lista de mensajes)
   - Input de texto para enviar mensajes
   - Mostrar nombre/ID del jugador que envía mensaje
   - Timestamp de mensajes
   - Auto-scroll al último mensaje
   - Sincronización en tiempo real de mensajes

2. **Interfaz de Usuario - Pantalla de Juego (0.5 puntos)**
   - Diseño de la pantalla principal del juego
   - Visualización de emojis de otros jugadores
   - Mostrar el emoji oculto del jugador actual (con "?")
   - Grid/lista de jugadores con sus emojis
   - Indicador visual del turno actual
   - Temporizador visible en pantalla
   - Selector de emoji para adivinar
   - Integrar el chat en la interfaz



## 📋 PERSONA 3 - UI/UX & Flujo de Navegación 

### Responsabilidades Principales:
1. **Pantallas de Navegación y Lobby (0.5 puntos)**
   - Pantalla de inicio/bienvenida
   - Pantalla para crear sala
   - Pantalla para unirse a sala (código de sala)
   - Pantalla de lobby (espera de jugadores)
   - Lista de jugadores en espera
   - Botón para iniciar partida (host)

2. **Manejo de Eventos y Flujo del Juego (0.5 puntos)**
   - Botón para salir del juego
   - Diálogo de confirmación para salir
   - Pantalla de victoria/derrota
   - Animaciones de transición entre pantallas
   - Feedback visual cuando un jugador es eliminado
   - Feedback visual cuando se acierta/falla
   - Manejo de estados de conexión/desconexión

3. **Efectos y Animaciones Opcionales (0.25 puntos)**
   - Animación de countdown del temporizador
   - Animación cuando un jugador es eliminado
   - Animación de victoria
   - Transiciones suaves entre rondas
   - Efectos visuales al enviar mensajes

4. **Código Limpio y Documentado (0.25 puntos)**
   - Documentar todas las clases y funciones principales
   - Crear README.md con instrucciones
   - Comentarios en código complejo
   - Seguir convenciones de Kotlin
