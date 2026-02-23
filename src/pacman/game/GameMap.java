package pacman.game;

import pacman.utils.Constants;

public class GameMap {

    // 0=empty, 1=wall, 2=pellet, 3=power pellet, 4=ghost house door
    public static final int[][] MAP = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,2,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,2,1},
        {1,3,1,1,2,1,1,1,2,1,1,1,2,1,1,1,2,1,1,3,1},
        {1,2,1,1,2,1,1,1,2,1,1,1,2,1,1,1,2,1,1,2,1},
        {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1,2,1,1,2,1,2,1,1,1,1,1,1,1,2,1,2,1,1,2,1},
        {1,2,2,2,2,1,2,2,2,2,1,2,2,2,2,1,2,2,2,2,1},
        {1,1,1,1,2,1,1,1,0,1,1,1,0,1,1,1,2,1,1,1,1},
        {1,1,1,1,2,1,0,0,0,0,0,0,0,0,0,1,2,1,1,1,1},
        {1,1,1,1,2,1,0,1,1,4,1,4,1,1,0,1,2,1,1,1,1},
        {0,0,0,0,2,0,0,1,0,0,0,0,0,1,0,0,2,0,0,0,0},
        {1,1,1,1,2,1,0,1,1,1,1,1,1,1,0,1,2,1,1,1,1},
        {1,1,1,1,2,1,0,0,0,0,0,0,0,0,0,1,2,1,1,1,1},
        {1,1,1,1,2,1,0,1,1,1,1,1,1,1,0,1,2,1,1,1,1},
        {1,2,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,2,1},
        {1,2,1,1,2,1,1,1,2,1,1,1,2,1,1,1,2,1,1,2,1},
        {1,3,2,1,2,2,2,2,2,2,0,2,2,2,2,2,2,1,2,3,1},
        {1,1,2,1,2,1,2,1,1,1,1,1,1,1,2,1,2,1,2,1,1},
        {1,2,2,2,2,1,2,2,2,2,1,2,2,2,2,1,2,2,2,2,1},
        {1,2,1,1,1,1,1,1,2,1,1,1,2,1,1,1,1,1,1,2,1},
        {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1,2,1,1,2,1,1,1,1,1,1,1,1,1,1,1,2,1,1,2,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    };

    private int[][] tiles;
    private int totalPellets;

    public GameMap() {
        reset();
    }

    public void reset() {
        tiles = new int[ROWS][COLS];
        totalPellets = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                tiles[r][c] = MAP[r][c];
                if (tiles[r][c] == 2 || tiles[r][c] == 3) totalPellets++;
            }
        }
    }

    public int getTile(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) return 1;
        return tiles[row][col];
    }

    public void setTile(int row, int col, int val) {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS)
            tiles[row][col] = val;
    }

    public boolean isWall(int row, int col) {
        int t = getTile(row, col);
        return t == 1;
    }

    public int getTotalPellets() { return totalPellets; }

    private static final int ROWS = Constants.ROWS;
    private static final int COLS = Constants.COLS;
}
