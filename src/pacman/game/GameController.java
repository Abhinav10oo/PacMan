package pacman.game;

import pacman.entities.Ghost;
import pacman.entities.PacMan;
import pacman.utils.Constants;
import pacman.utils.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    public enum State { MENU, PLAYING, PAUSED, DYING, GAME_OVER, LEVEL_CLEAR }

    public GameMap map;
    public PacMan pacman;
    public List<Ghost> ghosts;
    public State state;
    public int score, level, pelletsEaten;
    public int stateTimer;

    private static final int TILE = Constants.TILE_SIZE;

    public GameController() {
        map = new GameMap();
        pacman = new PacMan();
        ghosts = new ArrayList<>();
        initGhosts();
        state = State.MENU;
        score = 0; level = 1;
    }

    private void initGhosts() {
        ghosts.clear();
        // releaseDelay staggers exit so they leave the house one at a time
        ghosts.add(new Ghost(10 * TILE, 10 * TILE, Color.RED,                "Blinky",  30));
        ghosts.add(new Ghost(9  * TILE, 10 * TILE, new Color(255, 180, 255), "Pinky",   90));
        ghosts.add(new Ghost(11 * TILE, 10 * TILE, new Color(0, 255, 255),   "Inky",    180));
        ghosts.add(new Ghost(10 * TILE, 11 * TILE, new Color(255, 180, 0),   "Clyde",   270));
    }

    public void startGame() {
        map.reset();
        pacman = new PacMan();
        pacman.lives = 3;
        initGhosts();
        score = 0; level = 1; pelletsEaten = 0;
        state = State.PLAYING;
    }

    public void update() {
        if (state != State.PLAYING) {
            if (state == State.DYING) {
                stateTimer--;
                if (stateTimer <= 0) {
                    int remainingLives = pacman.lives - 1;
                    if (remainingLives <= 0) {
                        state = State.GAME_OVER;
                    } else {
                        resetPositions(remainingLives);
                        state = State.PLAYING;
                    }
                }
            }
            return;
        }

        pacman.update(map);
        for (Ghost g : ghosts) g.update(map, pacman);

        // Collect pellets
        int r = pacman.getRow(), c = pacman.getCol();
        int tile = map.getTile(r, c);
        if (tile == 2) {
            map.setTile(r, c, 0);
            score += Constants.PELLET_SCORE;
            pelletsEaten++;
        } else if (tile == 3) {
            map.setTile(r, c, 0);
            score += Constants.POWER_PELLET_SCORE;
            pelletsEaten++;
            for (Ghost g : ghosts) g.setFrightened();
        }

        // Check level clear
        if (pelletsEaten >= map.getTotalPellets()) {
            level++;
            state = State.LEVEL_CLEAR;
            stateTimer = 120;
            return;
        }

        // Collisions
        for (Ghost g : ghosts) {
            if (g.getBounds().intersects(pacman.getBounds())) {
                if (g.frightened) {
                    g.eaten = true; g.frightened = false;
                    score += Constants.GHOST_SCORE;
                } else if (!g.eaten) {
                    state = State.DYING;
                    stateTimer = 90;
                    break;
                }
            }
        }
    }

    private void resetPositions(int remainingLives) {
        pacman = new PacMan();
        pacman.lives = remainingLives;
        initGhosts();
    }

    // Fix: store lives before reset
    public void resetAfterDeath(int lives) {
        pacman = new PacMan();
        pacman.lives = lives;
        initGhosts();
    }

    public void setDirection(Direction d) {
        if (state == State.PLAYING) pacman.nextDir = d;
    }

    public void togglePause() {
        if (state == State.PLAYING) state = State.PAUSED;
        else if (state == State.PAUSED) state = State.PLAYING;
    }
}
