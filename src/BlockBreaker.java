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
		// 圖片緩衝區
		myImage = new BufferedImage(Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT, BufferedImage.TYPE_INT_RGB);
		myBuffer = myImage.getGraphics();
		// 預設大小
		setPreferredSize(new Dimension(Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT));
		// 滑鼠控制設定
		setFocusable(true);
		addMouseMotionListener(this);
		// 程式物件生成
		ball = new Ball();
		paddle = new Paddle();
		blocks = new ArrayList<Block>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 10; j++) {
				blocks.add(new Block((Block.WIDTH + 10) * j, (Block.HEIGHT + 10) * i));
			}
		}
		// 計時器每10毫秒更新畫面
		timer = new Timer(10, this);
		timer.start();

	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(myImage, 0, 0, Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 清空暫存區畫面
		myBuffer.setColor(Color.BLACK);
		myBuffer.fillRect(0, 0, Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT);
		// 球的移動
		ball.move();
		// 畫磚塊
		blockIterator = blocks.iterator();
		while (blockIterator.hasNext()) {
			Block b = blockIterator.next();
			b.drawSheape(myBuffer);
		}
		// 畫球
		ball.drawShape(myBuffer);
		// 畫板子
		paddle.drawShape(myBuffer);
		// 牆壁碰撞檢測
		if (ball.getX() <= 0 || ball.getX() >= getWidth() - 2 * Ball.RADIUS) {
			ball.setDx(ball.getDx() * -1); // 彈回
		}
		if (ball.getY() <= 0) {
			ball.setDy(ball.getDy() * -1);// 彈回
		}

		// 球與板子的碰撞檢測
		if (ball.getBound().intersects(paddle.getBound())) {
			ball.setDy(ball.getDy() * -1);
		}
		// 球與磚塊的碰撞檢測
		blockIterator = blocks.iterator();
		while (blockIterator.hasNext()) {
			Block block = blockIterator.next();
			if (block.getBound().intersects(ball.getBound())) {
				blockIterator.remove();
				ball.setDy(ball.getDy() * -1);
			}

		}

		// 球掉出畫面
		if (ball.getY() >= getHeight()) {
			timer.stop();
			JOptionPane.showMessageDialog(this, "遊戲結束！");
			System.exit(0);
		}

		// 如果所有磚塊被消除
		if (blocks.isEmpty()) {
			timer.stop();
			JOptionPane.showMessageDialog(this, "pure recall");
			System.exit(0);
		}

		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// 控制板子左右移動
		paddle.setX(e.getX() - Paddle.WIDTH / 2);
		if (paddle.getX() < 0)
			paddle.setX(0);
		if (paddle.getX() > getWidth() - Paddle.WIDTH)
			paddle.setX(getWidth() - Paddle.WIDTH);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}
}
