/*
Java Rule-Based AI Game Demo
One player controlled by arrow keys
One enemy that makes decisions every frame based on rule-based AI
It has a basic game loop, and a simple enemy AI that patrols, chases, attacks, or flees based on its state.
Enemy patrols unless:
It sees the player → chases
It's near the player → attacks
It’s low on health (<30) → flees
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RuleBasedGame extends JPanel implements KeyListener, Runnable {

    // Player & Enemy state
    int playerX = 100, playerY = 100;
    int enemyX = 300, enemyY = 300;
    int playerHealth = 100;
    int enemyHealth = 100;
    boolean[] keys = new boolean[256];

    // Game loop control
    boolean running = true;

    public RuleBasedGame() {
        setPreferredSize(new Dimension(500, 500));
        setBackground(Color.BLACK);
        JFrame frame = new JFrame("Rule-Based AI Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(this);
        new Thread(this).start();
    }

    // Game loop
    public void run() {
        while (running) {
            updateGame();
            repaint();
            try {
                Thread.sleep(33); // ~30 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Update player/enemy
    void updateGame() {
        // Move player
        if (keys[KeyEvent.VK_LEFT]) playerX -= 5;
        if (keys[KeyEvent.VK_RIGHT]) playerX += 5;
        if (keys[KeyEvent.VK_UP]) playerY -= 5;
        if (keys[KeyEvent.VK_DOWN]) playerY += 5;

        // AI Decision
        int dx = playerX - enemyX;
        int dy = playerY - enemyY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (enemyHealth < 30) {
            // Flee logic
            if (distance < 200) {
                enemyX -= dx / 10;
                enemyY -= dy / 10;
            }
        } else if (distance < 50) {
            // Attack
            playerHealth -= 1;
            System.out.println("Enemy attacks! Player health: " + playerHealth);
        } else if (distance < 200) {
            // Chase
            enemyX += dx / 20;
            enemyY += dy / 20;
        } else {
            // Patrol (move in circle)
            enemyX += Math.sin(System.currentTimeMillis() / 500.0) * 2;
            enemyY += Math.cos(System.currentTimeMillis() / 500.0) * 2;
        }
    }

    // Draw
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Player
        g.setColor(Color.GREEN);
        g.fillRect(playerX, playerY, 20, 20);
        g.drawString("Player HP: " + playerHealth, 10, 20);

        // Enemy
        g.setColor(Color.RED);
        g.fillRect(enemyX, enemyY, 20, 20);
        g.drawString("Enemy HP: " + enemyHealth, 10, 40);
    }

    // Key control
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    public void keyTyped(KeyEvent e) {}

    // Main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RuleBasedGame::new);
    }
}
