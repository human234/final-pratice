import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MazeExplorer {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze Explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.add(new MazePanel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class MazePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private final int CELL_SIZE = 40; // 每個格子大小
    private int[][] map = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,1,0,0,0,0,0,1,0,0,1},
        {1,0,1,0,1,0,1,1,1,0,1,0,1,1},
        {1,0,1,0,0,0,0,1,0,0,0,0,0,1},
        {1,0,1,1,1,1,0,1,1,1,1,1,0,1},
        {1,0,0,0,0,1,0,0,0,0,0,1,0,1},
        {1,1,1,1,0,1,1,1,1,1,0,1,0,1},
        {1,0,0,1,0,0,0,0,0,1,0,1,0,1},
        {1,0,1,1,1,1,1,1,0,1,0,1,0,1},
        {1,0,0,0,0,0,0,1,0,0,0,1,2,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };
    private int playerX = 1, playerY = 1; // 玩家座標 (格子編號)

    public MazePanel() {
        setFocusable(true);
        addKeyListener(this);
        timer = new Timer(30, this); // 每30毫秒刷新畫面
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 畫地圖
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == 1) {
                    g.setColor(Color.GRAY); // 牆壁
                } else if (map[y][x] == 2) {
                    g.setColor(Color.GREEN); // 出口
                } else {
                    g.setColor(Color.WHITE); // 道路
                }
                g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE); // 畫格子線
            }
        }

        // 畫玩家
        g.setColor(Color.RED);
        g.fillOval(playerX * CELL_SIZE + 5, playerY * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        int nextX = playerX;
        int nextY = playerY;

        if (key == KeyEvent.VK_UP) nextY--;
        if (key == KeyEvent.VK_DOWN) nextY++;
        if (key == KeyEvent.VK_LEFT) nextX--;
        if (key == KeyEvent.VK_RIGHT) nextX++;

        // 判斷是否能走
        if (map[nextY][nextX] != 1) {
            playerX = nextX;
            playerY = nextY;
        }

        // 判斷是否到達出口
        if (map[playerY][playerX] == 2) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "恭喜你找到出口了！");
            System.exit(0); // 結束遊戲
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
