import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.add(new SnakePanel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class SnakePanel extends JPanel implements ActionListener, KeyListener {
    private final int TILE_SIZE = 20;
    private final int WIDTH = 30, HEIGHT = 30;
    private final int INITIAL_LENGTH = 5;
    
    private LinkedList<Point> snake;
    private Point food;
    private Timer timer;
    private int direction;
    private boolean gameOver;

    // Directions
    private static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    
    public SnakePanel() {
        setFocusable(true);
        addKeyListener(this);
        setBackground(Color.BLACK);

        snake = new LinkedList<>();
        for (int i = INITIAL_LENGTH - 1; i >= 0; i--) {
            snake.add(new Point(i, 0));  // Initial snake at the top left
        }

        direction = RIGHT;
        spawnFood();
        
        timer = new Timer(100, this);  // Update every 100ms
        timer.start();
        gameOver = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw snake
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Game over message
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.drawString("Game Over! Press R to restart.", WIDTH * TILE_SIZE / 4, HEIGHT * TILE_SIZE / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        // Move the snake
        Point head = snake.getFirst();
        Point newHead = null;

        switch (direction) {
            case UP:
                newHead = new Point(head.x, head.y - 1);
                break;
            case RIGHT:
                newHead = new Point(head.x + 1, head.y);
                break;
            case DOWN:
                newHead = new Point(head.x, head.y + 1);
                break;
            case LEFT:
                newHead = new Point(head.x - 1, head.y);
                break;
        }

        // Check for collisions with walls or self
        if (newHead.x < 0 || newHead.x >= WIDTH || newHead.y < 0 || newHead.y >= HEIGHT || snake.contains(newHead)) {
            gameOver = true;
            timer.stop();
            return;
        }

        // Move the snake
        snake.addFirst(newHead);

        // Check if snake eats food
        if (newHead.equals(food)) {
            spawnFood();  // Spawn new food
        } else {
            snake.removeLast();  // Remove tail if not eating food
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                restartGame();
            }
            return;
        }

        // Control direction with arrow keys
        if (e.getKeyCode() == KeyEvent.VK_UP && direction != DOWN) {
            direction = UP;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && direction != LEFT) {
            direction = RIGHT;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && direction != UP) {
            direction = DOWN;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && direction != RIGHT) {
            direction = LEFT;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed
    }

    private void spawnFood() {
        Random rand = new Random();
        int x = rand.nextInt(WIDTH);
        int y = rand.nextInt(HEIGHT);
        food = new Point(x, y);
        while (snake.contains(food)) {
            x = rand.nextInt(WIDTH);
            y = rand.nextInt(HEIGHT);
            food = new Point(x, y);
        }
    }

    private void restartGame() {
        snake.clear();
        for (int i = INITIAL_LENGTH - 1; i >= 0; i--) {
            snake.add(new Point(i, 0));  // Initial snake at the top left
        }
        direction = RIGHT;
        spawnFood();
        gameOver = false;
        timer.start();
        repaint();
    }
}
