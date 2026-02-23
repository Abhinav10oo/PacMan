package pacman.ui;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("PAC-MAN");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel panel = new GamePanel();
        add(panel);
        pack();

        // Center on screen
        setLocationRelativeTo(null);

        // Set icon color (just title bar stuff, optional)
        getContentPane().setBackground(Color.BLACK);
    }
}
