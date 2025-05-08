import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlockBreaker {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Block Breaker");
		frame.setLocation(100, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new BlockBreakerPanel());
		frame.pack();
		frame.setVisible(true);
	}
}

class BlockBreakerPanel extends JPanel implements ActionListener, MouseMotionListener {

   private BufferedImage myImage;
	private Graphics myBuffer;
	private Timer timer;
	private Ball ball;
	private Paddle paddle;
	private List<Block> blocks;
	private Iterator<Block> blockIterator;

	public BlockBreakerPanel() {
		myImage = new BufferedImage(Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT, BufferedImage.TYPE_INT_RGB);
		myBuffer = myImage.getGraphics();
		setPreferredSize(new Dimension(Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT));
		setFocusable(true);
		addMouseMotionListener(this);

		ball = new Ball();
		paddle = new Paddle();
		blocks = new ArrayList<Block>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 10; j++) {
				blocks.add(new Block((Block.WIDTH + 10) * j, (Block.HEIGHT + 10) * i));
			}
		}

		timer = new Timer(10, this);
		timer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(myImage, 0, 0, Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		myBuffer.setColor(Color.BLACK);
		myBuffer.fillRect(0, 0, Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT);

		ball.move();

		blockIterator = blocks.iterator();
		while (blockIterator.hasNext()) {
			Block b = blockIterator.next();
			b.drawShape(myBuffer);  // 修正方法名稱
		}

		ball.drawShape(myBuffer);
		paddle.drawShape(myBuffer);

		// 檢查邊界碰撞
		if (ball.getX() <= 0 || ball.getX() >= getWidth() - 2 * Ball.RADIUS) {
			ball.setDx(ball.getDx() * -1);
		}
		if (ball.getY() <= 0) {
			ball.setDy(ball.getDy() * -1);
		}

		// 檢查板子碰撞
		if (ball.getBound().intersects(paddle.getBound())) {
			paddle.handleCollision(ball);  // 根據碰撞位置調整反彈角度
		}

		// 碰撞磚塊邏輯
		blockIterator = blocks.iterator();
		while (blockIterator.hasNext()) {
			Block block = blockIterator.next();
			if (block.getBound().intersects(ball.getBound())) {
				blockIterator.remove(); // 刪除已碰撞的磚塊

				// 確定從磚塊哪一邊反彈
				Rectangle blockRect = block.getBound();
				int ballPrevX = ball.getPrevX();
				int ballPrevY = ball.getPrevY();
				Rectangle prevBallRect = new Rectangle(ballPrevX, ballPrevY, Ball.RADIUS * 2, Ball.RADIUS * 2);

				boolean fromLeftOrRight = 
					ballPrevX + Ball.RADIUS * 2 <= blockRect.x || 
					ballPrevX >= blockRect.x + blockRect.width;
				boolean fromTopOrBottom = 
					ballPrevY + Ball.RADIUS * 2 <= blockRect.y || 
					ballPrevY >= blockRect.y + blockRect.height;

				if (fromLeftOrRight) {
					ball.setDx(-ball.getDx());
				}
				if (fromTopOrBottom) {
					ball.setDy(-ball.getDy());
				}

				break; // 一次只消一個磚塊，避免連續碰撞問題
			}
		}

		// 如果球落到螢幕底部，遊戲結束
		if (ball.getY() >= getHeight()) {
			timer.stop();
			JOptionPane.showMessageDialog(this, "遊戲結束！");
			System.exit(0);
		}

		// 如果所有磚塊消失，遊戲結束
		if (blocks.isEmpty()) {
			timer.stop();
			JOptionPane.showMessageDialog(this, "pure recall");
			System.exit(0);
		}

		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		paddle.setX(e.getX() - Paddle.WIDTH / 2);
		if (paddle.getX() < 0)
			paddle.setX(0);
		if (paddle.getX() > getWidth() - Paddle.WIDTH)
			paddle.setX(getWidth() - Paddle.WIDTH);
	}

	@Override
	public void mouseDragged(MouseEvent e) {}
}
