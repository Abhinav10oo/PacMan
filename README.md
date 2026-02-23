# 🟡 Pac-Man in Java

A fully functional **Pac-Man arcade game** built using pure Java and Java Swing (GUI). No external libraries, no game engines — just core Java from scratch.

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=flat-square&logo=java)
![GUI](https://img.shields.io/badge/GUI-Java%20Swing-blue?style=flat-square)
![Status](https://img.shields.io/badge/Status-Working-brightgreen?style=flat-square)

---

## 🎮 Gameplay Preview

```
┌─────────────────────┐
│ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ │
│ · · · · · · · · · · │
│ · ■ ■ O ■ ■ O ■ ■ · │
│ · · · · C · · · · · │  ← Ghosts chasing Pac-Man
│ · ■ · ■ ■ ■ ■ · ■ · │
│ · · · ·😮· · · · · │  ← Pac-Man eating pellets
│ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ │
│  SCORE: 340  LIVES: ●●  │
└─────────────────────┘
```

---

## ✨ Features

- Classic Pac-Man maze with walls, pellets, and power pellets
- 4 ghosts (Blinky, Pinky, Inky, Clyde) with chase AI
- Ghost frightened mode — eat ghosts after collecting a power pellet
- Lives system — 3 lives, lose one each time a ghost catches you
- Score tracking — pellets, power pellets, and eating ghosts all give points
- Level progression — maze resets and difficulty increases each level
- Tunnel wrap-around on the sides of the map
- Smooth 60 FPS game loop using `javax.swing.Timer`
- Menu screen, pause screen, and game over screen
- Staggered ghost release from the ghost house

---

## 📁 Project Structure

```
PacManGame/
├── bin/                        ← Compiled .class files go here (auto-generated)
├── src/
│   └── pacman/
│       ├── Main.java           ← Entry point, launches the game window
│       ├── entities/
│       │   ├── PacMan.java     ← Player movement, animation, drawing
│       │   └── Ghost.java      ← Ghost AI, exit logic, frightened mode
│       ├── game/
│       │   ├── GameMap.java    ← Maze layout, tile types, pellet tracking
│       │   └── GameController.java ← Game state, collisions, scoring, lives
│       ├── ui/
│       │   ├── GameWindow.java ← JFrame window setup
│       │   └── GamePanel.java  ← JPanel renderer, game loop, keyboard input
│       └── utils/
│           ├── Constants.java  ← All game settings (speed, tile size, FPS...)
│           └── Direction.java  ← UP, DOWN, LEFT, RIGHT enum
└── README.md
```

---

## 🚀 How to Run

### Prerequisites
- Java JDK **17 or higher** installed
- Check your version: `java -version`
- Download Java: https://www.oracle.com/java/technologies/downloads/

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/your-username/PacManGame.git
cd PacManGame
```

**2. Compile the source code**

Windows (PowerShell / CMD):
```cmd
javac -d bin src\pacman\utils\*.java src\pacman\game\*.java src\pacman\entities\*.java src\pacman\ui\*.java src\pacman\Main.java
```

Linux / Mac:
```bash
javac -d bin src/pacman/utils/*.java src/pacman/game/*.java src/pacman/entities/*.java src/pacman/ui/*.java src/pacman/Main.java
```

**3. Run the game**

Windows:
```cmd
java -cp bin pacman.Main
```

Linux / Mac:
```bash
java -cp bin pacman.Main
```

> **Note:** The `bin/` folder must exist before compiling. If it doesn't, create it first:
> - Windows: `mkdir bin`
> - Linux/Mac: `mkdir -p bin`

---

## 🕹️ Controls

| Key | Action |
|-----|--------|
| `W` or `↑` | Move Up |
| `S` or `↓` | Move Down |
| `A` or `←` | Move Left |
| `D` or `→` | Move Right |
| `ENTER` | Start / Restart game |
| `P` or `ESC` | Pause / Resume |

---

## 🏆 Scoring

| Action | Points |
|--------|--------|
| Eat a pellet `·` | 10 pts |
| Eat a power pellet `O` | 50 pts |
| Eat a frightened ghost | 200 pts |

---

## 🧠 What I Learned From This Project

### Java Concepts
- **Object-Oriented Programming (OOP)** — every game element (PacMan, Ghost, Map) is its own class with clear responsibilities
- **Inheritance & Encapsulation** — entities have private state and public methods, keeping code clean and modular
- **Enums** — used for `Direction` (UP/DOWN/LEFT/RIGHT) and `GameState` (PLAYING/PAUSED/DYING etc.)
- **Collections** — `ArrayList<Ghost>` to manage multiple ghost objects dynamically

### Java GUI (Swing)
- **JFrame** — how to create and configure a desktop window
- **JPanel + paintComponent()** — how to draw graphics, shapes, arcs, and text on screen
- **Graphics2D** — drawing with anti-aliasing, colors, fills, arcs for smooth visuals
- **KeyListener / KeyAdapter** — capturing real-time keyboard input for player movement
- **javax.swing.Timer** — building a 60 FPS game loop that updates and redraws every frame

### Game Development Concepts
- **Game loop** — the core loop of update → draw → repeat
- **Tile-based maps** — representing a maze as a 2D integer array where each number means something (wall, pellet, empty)
- **Collision detection** — using `Rectangle.intersects()` to detect when Pac-Man touches a ghost or pellet
- **State machines** — managing game states (MENU → PLAYING → DYING → GAME OVER) cleanly
- **Entity movement & grid alignment** — snapping positions to a tile grid for smooth, accurate movement
- **AI pathfinding (basic)** — ghosts use a greedy best-first approach, always picking the direction that brings them closest to their target
- **Sprite animation** — cycling through animation frames using tick counters

### Software Design
- **Separation of concerns** — logic, rendering, input, and data are all in separate files
- **Constants file** — centralising magic numbers (tile size, speed, FPS) so changing one value updates the whole game
- **Package structure** — organising code into `entities`, `game`, `ui`, `utils` packages for clarity

---

## 🐛 Known Issues / Future Improvements

- [ ] Add sound effects
- [ ] Add high score saving to a file
- [ ] Improve ghost AI (scatter mode, different personalities per ghost)
- [ ] Add a start animation like the original arcade game
- [ ] Make the maze more visually polished with rounded wall corners

---

## 🛠️ Built With

- **Java 17**
- **Java Swing** (javax.swing)
- **Java AWT** (java.awt)
- No external libraries or game engines

---

## 📄 License

This project is open source and free to use for learning purposes.

---

## 👨‍💻 Author

Made with ❤️ and Java.  
Feel free to fork, improve, and star ⭐ the repo if you found it useful!
