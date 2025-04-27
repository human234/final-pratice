import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BlockBreaker {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Block Breaker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.add(new BlockBreakerPanel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class BlockBreakerPanel extends JPanel implements ActionListener, MouseMotionListener {
    private final int BALL_SIZE = 20;
    private final int PADDLE_WIDTH = 100;
    private final int PADDLE_HEIGHT = 10;
    private final int BLOCK_WIDTH = 50;
    private final int BLOCK_HEIGHT = 20;
    
    private Timer timer;
    private int ballX = 300, ballY = 300;
    private int ballDX = 2, ballDY = -2;
    private int paddleX = 250;
    private boolean[][] blocks;
    
    public BlockBreakerPanel() {
        setFocusable(true);
        addMouseMotionListener(this);
        timer = new Timer(5, this); // 每5毫秒更新畫面
        timer.start();
        blocks = new boolean[6][10]; // 6行10列磚塊
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                blocks[i][j] = true; // 初始所有磚塊存在
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);

        // 畫磚塊
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                if (blocks[i][j]) {
                    g.setColor(Color.CYAN);
                    g.fillRect(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
                }
            }
        }

        // 畫球
        g.setColor(Color.RED);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // 畫板子
        g.setColor(Color.GREEN);
        g.fillRect(paddleX, getHeight() - PADDLE_HEIGHT - 10, PADDLE_WIDTH, PADDLE_HEIGHT);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 球的移動
        ballX += ballDX;
        ballY += ballDY;

        // 牆壁碰撞檢測
        if (ballX <= 0 || ballX >= getWidth() - BALL_SIZE) {
            ballDX = -ballDX; // 彈回
        }
        if (ballY <= 0) {
            ballDY = -ballDY; // 彈回
        }

        // 球與板子的碰撞檢測
        if (ballY + BALL_SIZE >= getHeight() - PADDLE_HEIGHT - 10 && ballX + BALL_SIZE >= paddleX && ballX <= paddleX + PADDLE_WIDTH) {
            ballDY = -ballDY;
        }

        // 球掉出畫面
        if (ballY >= getHeight()) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "遊戲結束！");
            System.exit(0);
        }

        // 球與磚塊的碰撞檢測
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                if (blocks[i][j]) {
                    Rectangle block = new Rectangle(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
                    Rectangle ball = new Rectangle(ballX, ballY, BALL_SIZE, BALL_SIZE);

                    if (ball.intersects(block)) {
                        blocks[i][j] = false; // 碰到磚塊，將其移除
                        ballDY = -ballDY; // 改變方向
                        break;
                    }
                }
            }
        }

        // 如果所有磚塊被消除
        boolean allBroken = true;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                if (blocks[i][j]) {
                    allBroken = false;
                    break;
                }
            }
        }

        if (allBroken) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "pure recall");
            System.exit(0);
        }

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // 控制板子左右移動
        paddleX = e.getX() - PADDLE_WIDTH / 2;
        if (paddleX < 0) paddleX = 0;
        if (paddleX > getWidth() - PADDLE_WIDTH) paddleX = getWidth() - PADDLE_WIDTH;
    }

    @Override
    public void mouseDragged(MouseEvent e) {}
}
