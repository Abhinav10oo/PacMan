package pacman.entities;

import pacman.game.GameMap;
import pacman.utils.Constants;
import pacman.utils.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ghost {
    public float x, y;
    public Direction dir;
    public Color color;
    public boolean frightened;
    public boolean eaten;
    public int frightenTimer;
    public String name;

    // Ghost house exit logic
    private boolean exitingHouse;
    private int releaseDelay;   // frames to wait before starting to exit
    private float exitTargetX;  // X to align to before going up
    private static final int DOOR_ROW = 9;  // row of ghost house door
    private static final int EXIT_ROW = 8;  // row just outside the house (open corridor)
    private static final int HOUSE_EXIT_COL = 10; // centre column of the exit

    private float startX, startY;
    private static final float SPEED = Constants.GHOST_SPEED;
    private static final int TILE = Constants.TILE_SIZE;
    private static final Random rand = new Random();

    private int animTick, animFrame;

    /**
     * @param releaseDelay frames before this ghost starts leaving the house (0 = immediate)
     */
    public Ghost(float x, float y, Color color, String name, int releaseDelay) {
        this.startX = x;
        this.startY = y;
        this.color = color;
        this.name = name;
        this.releaseDelay = releaseDelay;
        reset();
    }

    public void reset() {
        x = startX;
        y = startY;
        dir = Direction.LEFT;
        frightened = false;
        eaten = false;
        frightenTimer = 0;
        animFrame = 0;
        animTick = 0;
        exitingHouse = false;
        exitTargetX = HOUSE_EXIT_COL * TILE;
    }

    public void update(GameMap map, PacMan pacman) {
        // Count down release timer
        if (releaseDelay > 0) {
            releaseDelay--;
            return; // stay still while waiting
        }

        if (frightenTimer > 0) {
            frightenTimer--;
            if (frightenTimer == 0) {
                frightened = false;
                eaten = false;
            }
        }

        float speed = eaten ? SPEED * 2 : (frightened ? SPEED * 0.7f : SPEED);

        // --- PHASE 1: exit the ghost house ---
        if (!exitingHouse) {
            exitHouse(speed);
            return;
        }

        // --- PHASE 2: normal movement ---
        // Snap to nearest tile grid before moving to avoid drift
        x = snapToGrid(x, speed);
        y = snapToGrid(y, speed);

        // Move one step
        x += dir.dx * speed;
        y += dir.dy * speed;

        // Tunnel wrap (row 10 corridor)
        if (x < -TILE) x = Constants.SCREEN_WIDTH;
        if (x > Constants.SCREEN_WIDTH) x = -TILE;

        // At tile boundary → choose next direction
        if (isAlignedToGrid(speed)) {
            chooseDirection(map, pacman);
        }

        animTick++;
        if (animTick >= 8) {
            animTick = 0;
            animFrame = (animFrame + 1) % 2;
        }
    }

    /**
     * Simple two-step house exit:
     *  1. Slide horizontally to the exit column centre.
     *  2. Move upward until reaching the open corridor (EXIT_ROW).
     */
    private void exitHouse(float speed) {
        float targetX = exitTargetX;
        float targetY = EXIT_ROW * TILE;

        if (Math.abs(x - targetX) > speed) {
            // Step 1 – align column
            x += (x < targetX) ? speed : -speed;
            dir = (x < targetX) ? Direction.RIGHT : Direction.LEFT;
        } else {
            x = targetX;
            if (y > targetY) {
                // Step 2 – move up through door
                y -= speed;
                dir = Direction.UP;
            } else {
                // Exited!
                y = targetY;
                exitingHouse = true;
                dir = Direction.LEFT;
            }
        }
    }

    // ── Grid helpers ──────────────────────────────────────────────────────────

    /** True when position is within `speed` pixels of a tile boundary. */
    private boolean isAlignedToGrid(float speed) {
        float cx = x % TILE;
        float cy = y % TILE;
        return cx < speed && cy < speed;
    }

    /** Pull coordinate onto the nearest tile boundary if very close. */
    private float snapToGrid(float v, float speed) {
        float r = v % TILE;
        if (r < speed) return v - r;
        if (r > TILE - speed) return v - r + TILE;
        return v;
    }

    // ── Direction choosing ────────────────────────────────────────────────────

    private void chooseDirection(GameMap map, PacMan pacman) {
        // Snap cleanly to grid first
        x = ((int)(x / TILE)) * TILE;
        y = ((int)(y / TILE)) * TILE;

        int row = getRow();
        int col = getCol();
        List<Direction> options = new ArrayList<>();
        Direction opposite = getOpposite(dir);

        for (Direction d : new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT}) {
            if (d == opposite) continue;
            int nr = row + d.dy;
            int nc = col + d.dx;
            // Normal ghosts cannot re-enter the ghost house through the door
            if (!map.isWall(nr, nc) && map.getTile(nr, nc) != 4) {
                options.add(d);
            }
        }

        if (options.isEmpty()) {
            dir = opposite;
            return;
        }

        if (frightened) {
            dir = options.get(rand.nextInt(options.size()));
        } else if (eaten) {
            // Head back to house entrance
            int tr = EXIT_ROW;
            int tc = HOUSE_EXIT_COL;
            dir = getBestDirection(options, row, col, tr, tc);
        } else {
            // Chase pacman
            dir = getBestDirection(options, row, col, pacman.getRow(), pacman.getCol());
        }
    }

    private Direction getBestDirection(List<Direction> options, int fromR, int fromC,
                                       int targetR, int targetC) {
        Direction best = options.get(0);
        double bestDist = Double.MAX_VALUE;
        for (Direction d : options) {
            int nr = fromR + d.dy;
            int nc = fromC + d.dx;
            double dist = Math.hypot(nr - targetR, nc - targetC);
            if (dist < bestDist) {
                bestDist = dist;
                best = d;
            }
        }
        return best;
    }

    private Direction getOpposite(Direction d) {
        return switch (d) {
            case UP    -> Direction.DOWN;
            case DOWN  -> Direction.UP;
            case LEFT  -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
            default    -> Direction.NONE;
        };
    }

    // ── Public helpers ────────────────────────────────────────────────────────

    public int getCol() { return (int)(x + TILE / 2) / TILE; }
    public int getRow() { return (int)(y + TILE / 2) / TILE; }

    public Rectangle getBounds() {
        return new Rectangle((int)x + 2, (int)y + 2, TILE - 4, TILE - 4);
    }

    public void setFrightened() {
        frightened = true;
        frightenTimer = Constants.POWER_PELLET_DURATION;
    }

    // ── Drawing ───────────────────────────────────────────────────────────────

    public void draw(Graphics2D g) {
        int px = (int)x, py = (int)y;
        int w = TILE - 2, h = TILE - 2;
        int ox = px + 1, oy = py + 1;

        Color bodyColor;
        if (frightened) {
            bodyColor = (frightenTimer < 80 && animFrame == 1) ? Color.WHITE : new Color(0, 0, 180);
        } else if (eaten) {
            drawEyes(g, ox, oy, w, h, true);
            return;
        } else {
            bodyColor = color;
        }

        g.setColor(bodyColor);
        g.fillArc(ox, oy, w, h, 0, 180);          // dome top
        g.fillRect(ox, oy + h / 2, w, h / 2);     // body

        // Wavy bottom
        int waveW = w / 3;
        for (int i = 0; i < 3; i++) {
            g.setColor(Color.BLACK);
            g.fillArc(ox + i * waveW, oy + h - waveW / 2, waveW, waveW, 0, -180);
            g.setColor(bodyColor);
        }

        drawEyes(g, ox, oy, w, h, false);
    }

    private void drawEyes(Graphics2D g, int ox, int oy, int w, int h, boolean onlyPupils) {
        if (!onlyPupils) {
            g.setColor(Color.WHITE);
            g.fillOval(ox + w / 5,       oy + h / 5, w / 4, h / 3);
            g.fillOval(ox + w / 2,       oy + h / 5, w / 4, h / 3);
        }
        g.setColor(new Color(0, 0, 200));
        int pdx = dir.dx * 2, pdy = dir.dy * 2;
        g.fillOval(ox + w / 5 + 2 + pdx, oy + h / 5 + 2 + pdy, w / 7, h / 5);
        g.fillOval(ox + w / 2 + 2 + pdx, oy + h / 5 + 2 + pdy, w / 7, h / 5);
    }
}
