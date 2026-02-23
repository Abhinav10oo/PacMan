package pacman.utils;

public class Constants {
    public static final int TILE_SIZE = 24;
    public static final int COLS = 21;
    public static final int ROWS = 23;
    public static final int SCREEN_WIDTH = COLS * TILE_SIZE;
    public static final int SCREEN_HEIGHT = ROWS * TILE_SIZE;

    public static final int PACMAN_SPEED = 2;
    public static final int GHOST_SPEED = 2;

    public static final int FPS = 60;
    public static final long FRAME_TIME = 1000 / FPS;

    public static final int PELLET_SCORE = 10;
    public static final int POWER_PELLET_SCORE = 50;
    public static final int GHOST_SCORE = 200;

    public static final int POWER_PELLET_DURATION = 300; // frames ~5 seconds
}
