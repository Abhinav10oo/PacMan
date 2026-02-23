package pacman.ui;

import pacman.entities.Ghost;
import pacman.game.GameController;
import pacman.game.GameMap;
import pacman.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener {

    private GameController controller;
    private Timer gameTimer;

    private static final int TILE = Constants.TILE_SIZE;
    private static final Color WALL_COLOR = new Color(33, 33, 255);
    private static final Color PELLET_COLOR = new Color(255, 200, 150);
    private static final Color POWER_COLOR = new Color(255, 200, 50);
    private static final Color BG_COLOR = Color.BLACK;

    public GamePanel() {
        controller = new GameController();
        setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT + 40));
        setBackground(BG_COLOR);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                handleKey(e.getKeyCode());
            }
        });

        gameTimer = new Timer((int) Constants.FRAME_TIME, this);
        gameTimer.start();
    }

    private void handleKey(int key) {
        switch (key) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A  -> controller.setDirection(pacman.utils.Direction.LEFT);
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> controller.setDirection(pacman.utils.Direction.RIGHT);
            case KeyEvent.VK_UP, KeyEvent.VK_W    -> controller.setDirection(pacman.utils.Direction.UP);
            case KeyEvent.VK_DOWN, KeyEvent.VK_S  -> controller.setDirection(pacman.utils.Direction.DOWN);
            case KeyEvent.VK_ENTER -> {
                if (controller.state == GameController.State.MENU
                        || controller.state == GameController.State.GAME_OVER) {
                    controller.startGame();
                }
            }
            case KeyEvent.VK_P, KeyEvent.VK_ESCAPE -> controller.togglePause();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.update();

        // Handle level clear auto-transition
        if (controller.state == GameController.State.LEVEL_CLEAR) {
            controller.stateTimer--;
            if (controller.stateTimer <= 0) {
                controller.map.reset();
                int lives = controller.pacman.lives;
                controller.pacman = new pacman.entities.PacMan();
                controller.pacman.lives = lives;
                controller.pelletsEaten = 0;
                for (Ghost g : controller.ghosts) g.reset();
                controller.state = GameController.State.PLAYING;
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawMap(g2);

        if (controller.state == GameController.State.MENU) {
            drawMenu(g2);
        } else if (controller.state == GameController.State.GAME_OVER) {
            drawEntities(g2);
            drawHUD(g2);
            drawOverlay(g2, "GAME OVER", "Press ENTER to restart", Color.RED);
        } else if (controller.state == GameController.State.PAUSED) {
            drawEntities(g2);
            drawHUD(g2);
            drawOverlay(g2, "PAUSED", "Press P or ESC to resume", Color.CYAN);
        } else if (controller.state == GameController.State.LEVEL_CLEAR) {
            drawHUD(g2);
            drawOverlay(g2, "LEVEL " + (controller.level - 1) + " CLEAR!", "Get ready...", Color.YELLOW);
        } else {
            drawEntities(g2);
            drawHUD(g2);
        }
    }

    private void drawMap(Graphics2D g) {
        GameMap map = controller.map;
        for (int r = 0; r < Constants.ROWS; r++) {
            for (int c = 0; c < Constants.COLS; c++) {
                int tile = map.getTile(r, c);
                int px = c * TILE, py = r * TILE;

                switch (tile) {
                    case 1 -> {
                        g.setColor(WALL_COLOR);
                        g.fillRoundRect(px + 1, py + 1, TILE - 2, TILE - 2, 6, 6);
                        g.setColor(new Color(80, 80, 255));
                        g.drawRoundRect(px + 1, py + 1, TILE - 2, TILE - 2, 6, 6);
                    }
                    case 2 -> {
                        g.setColor(PELLET_COLOR);
                        g.fillOval(px + TILE/2 - 2, py + TILE/2 - 2, 5, 5);
                    }
                    case 3 -> {
                        g.setColor(POWER_COLOR);
                        g.fillOval(px + TILE/2 - 5, py + TILE/2 - 5, 10, 10);
                    }
                    case 4 -> {
                        g.setColor(new Color(255, 180, 180, 80));
                        g.fillRect(px, py, TILE, TILE);
                    }
                }
            }
        }
    }

    private void drawEntities(Graphics2D g) {
        for (Ghost ghost : controller.ghosts) ghost.draw(g);
        if (controller.pacman.alive) controller.pacman.draw(g);
    }

    private void drawHUD(Graphics2D g) {
        int hudY = Constants.SCREEN_HEIGHT + 6;

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.drawString("SCORE: " + controller.score, 10, hudY + 14);

        g.setColor(Color.CYAN);
        g.drawString("LVL: " + controller.level, Constants.SCREEN_WIDTH / 2 - 30, hudY + 14);

        // Lives
        g.setColor(Color.YELLOW);
        g.drawString("LIVES:", Constants.SCREEN_WIDTH - 120, hudY + 14);
        for (int i = 0; i < controller.pacman.lives; i++) {
            g.fillArc(Constants.SCREEN_WIDTH - 60 + i * 18, hudY + 2, 12, 12, 30, 300);
        }
    }

    private void drawMenu(Graphics2D g) {
        // Dark overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Title
        g.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        String title = "PAC-MAN";
        int tx = (getWidth() - fm.stringWidth(title)) / 2;
        g.setColor(new Color(255, 220, 0));
        g.drawString(title, tx + 2, 100 + 2); // shadow
        g.setColor(Color.YELLOW);
        g.drawString(title, tx, 100);

        // Subtitle
        g.setFont(new Font("Monospaced", Font.PLAIN, 18));
        fm = g.getFontMetrics();
        String sub = "Press ENTER to Start";
        g.setColor(new Color(200, 200, 255));
        g.drawString(sub, (getWidth() - fm.stringWidth(sub)) / 2, 160);

        // Controls
        String[] controls = {
            "Controls:",
            "WASD or Arrow Keys - Move",
            "P / ESC - Pause",
            "ENTER - Start / Restart"
        };
        g.setFont(new Font("Monospaced", Font.PLAIN, 14));
        fm = g.getFontMetrics();
        int sy = 220;
        for (String line : controls) {
            g.setColor(line.equals("Controls:") ? Color.CYAN : Color.LIGHT_GRAY);
            g.drawString(line, (getWidth() - fm.stringWidth(line)) / 2, sy);
            sy += 22;
        }

        // Scores info
        g.setFont(new Font("Monospaced", Font.PLAIN, 13));
        fm = g.getFontMetrics();
        String[] pts = {
            "· = 10 pts",
            "O = 50 pts  (Power Pellet)",
            "Ghost = 200 pts"
        };
        sy = 330;
        for (String pt : pts) {
            g.setColor(Color.ORANGE);
            g.drawString(pt, (getWidth() - fm.stringWidth(pt)) / 2, sy);
            sy += 20;
        }
    }

    private void drawOverlay(Graphics2D g, String title, String sub, Color titleColor) {
        int cx = getWidth() / 2, cy = getHeight() / 2 - 20;

        g.setColor(new Color(0, 0, 0, 160));
        g.fillRoundRect(cx - 160, cy - 50, 320, 100, 20, 20);

        g.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
        FontMetrics fm = g.getFontMetrics();
        g.setColor(titleColor);
        g.drawString(title, cx - fm.stringWidth(title) / 2, cy);

        g.setFont(new Font("Monospaced", Font.PLAIN, 15));
        fm = g.getFontMetrics();
        g.setColor(Color.WHITE);
        g.drawString(sub, cx - fm.stringWidth(sub) / 2, cy + 28);
    }
}
