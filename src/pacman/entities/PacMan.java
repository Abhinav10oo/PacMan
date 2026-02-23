package pacman.entities;

import pacman.game.GameMap;
import pacman.utils.Constants;
import pacman.utils.Direction;

import java.awt.*;

public class PacMan {
    public float x, y;
    public Direction dir, nextDir;
    public int animFrame;
    public int animTick;
    public boolean alive;
    public int lives;

    private static final float SPEED = Constants.PACMAN_SPEED;
    private static final int TILE = Constants.TILE_SIZE;

    public PacMan() {
        reset();
    }

    public void reset() {
        x = 10 * TILE;
        y = 16 * TILE;
        dir = Direction.NONE;
        nextDir = Direction.NONE;
        animFrame = 0;
        animTick = 0;
        alive = true;
        if (lives == 0) lives = 3;
    }

    public void update(GameMap map) {
        // Try queued direction
        if (nextDir != Direction.NONE && canMove(nextDir, map)) {
            dir = nextDir;
            nextDir = Direction.NONE;
        }

        if (dir != Direction.NONE && canMove(dir, map)) {
            x += dir.dx * SPEED;
            y += dir.dy * SPEED;
        }

        // Tunnel wrap (row 10)
        if (x < -TILE) x = Constants.SCREEN_WIDTH;
        if (x > Constants.SCREEN_WIDTH) x = -TILE;

        // Animation
        animTick++;
        if (animTick >= 5) {
            animTick = 0;
            animFrame = (animFrame + 1) % 3;
        }
    }

    private boolean canMove(Direction d, GameMap map) {
        // Align to grid for smoother turning
        float nx = x + d.dx * SPEED;
        float ny = y + d.dy * SPEED;
        int margin = 4;
        int col1 = (int)(nx + margin) / TILE;
        int col2 = (int)(nx + TILE - margin - 1) / TILE;
        int row1 = (int)(ny + margin) / TILE;
        int row2 = (int)(ny + TILE - margin - 1) / TILE;
        return !map.isWall(row1, col1) && !map.isWall(row1, col2)
            && !map.isWall(row2, col1) && !map.isWall(row2, col2);
    }

    public int getCol() { return (int)(x + TILE/2) / TILE; }
    public int getRow() { return (int)(y + TILE/2) / TILE; }

    public Rectangle getBounds() {
        return new Rectangle((int)x + 2, (int)y + 2, TILE - 4, TILE - 4);
    }

    public void draw(Graphics2D g) {
        int px = (int)x, py = (int)y;
        g.setColor(Color.YELLOW);

        double mouthOpen = (animFrame == 1) ? 20 : (animFrame == 0 ? 5 : 35);
        double startAngle = getStartAngle() + mouthOpen / 2;
        double arcAngle = 360 - mouthOpen;

        g.fillArc(px + 1, py + 1, TILE - 2, TILE - 2, (int)startAngle, (int)arcAngle);

        // Eye
        g.setColor(Color.BLACK);
        int ex = px + TILE/2 + (int)(dir.dx * 3) + (dir.dy != 0 ? 4 : 0);
        int ey = py + 4 + (int)(dir.dy * 3);
        g.fillOval(ex, ey, 3, 3);
    }

    private int getStartAngle() {
        return switch (dir) {
            case RIGHT -> 0;
            case LEFT -> 180;
            case UP -> 90;
            case DOWN -> 270;
            default -> 0;
        };
    }
}
