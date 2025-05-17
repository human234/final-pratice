import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * BlockBreakerPanel 負責處理遊戲的畫面繪製與邏輯更新。
 */
public class BlockBreakerPanel extends JPanel implements ActionListener, MouseMotionListener {

	private BufferedImage myImage;
	private Graphics myBuffer;
	private Timer timer;
	private Ball ball;
	private Paddle paddle;
	private List<Block> blocks;
	private Iterator<Block> blockIterator;
	private List<Explosion> explosions;
	private BufferedImage background;

	/**
	 * 建構子：初始化遊戲元件與計時器。
	 */
	public BlockBreakerPanel() {
		myImage = new BufferedImage(Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT, BufferedImage.TYPE_INT_RGB);
		myBuffer = myImage.getGraphics();
		setPreferredSize(new Dimension(Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT));
		setFocusable(true);
		addMouseMotionListener(this);

		ball = new Ball();
		paddle = new Paddle();
		blocks = new ArrayList<Block>();
		explosions = new ArrayList<Explosion>();

		int spacing = 15;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 9; j++) {
				blocks.add(new Block(spacing + (Block.WIDTH + spacing) * j, (Block.HEIGHT + 10) * i));
			}
		}
		loadImage();
		timer = new Timer(16, this);
		timer.start();
	}

	/**
	 * Swing 繪圖方法，將緩衝影像畫出。
	 */
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(myImage, 0, 0, Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT, null);
	}

	/**
	 * 每次計時器觸發時更新遊戲邏輯並重繪畫面。
	 * 
	 * @param e ActionEvent（未使用）
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		myBuffer.drawImage(background, 0, 0, Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT, null);

		ball.move();

		blockIterator = blocks.iterator();
		while (blockIterator.hasNext()) {
			Block b = blockIterator.next();
			b.drawShape(myBuffer);
		}

		ball.drawShape(myBuffer);
		paddle.drawShape(myBuffer);

		for (Explosion explosion : explosions) {
			explosion.render(myBuffer);
			explosion.update();
		}

		explosions.removeIf(Explosion::isFinished);

		// 邊界碰撞檢查
		if (ball.getX() <= 0 || ball.getX() >= getWidth() - 2 * Ball.RADIUS) {
			ball.setDx(ball.getDx() * -1);
		}
		if (ball.getY() <= 0) {
			ball.setDy(ball.getDy() * -1);
		}

		// 擋板碰撞檢查
		if (ball.getBound().intersects(paddle.getBound())) {
			paddle.handleCollision(ball);
		}

		// 磚塊碰撞檢查
		blockIterator = blocks.iterator();
		while (blockIterator.hasNext()) {
			Block block = blockIterator.next();
			if (block.getBound().intersects(ball.getBound())) {
				block.hitted();
				if (block.death()) {
					explosions.add(new Explosion(block.getX() + Block.WIDTH / 2, block.getY() + Block.HEIGHT / 2));
					blockIterator.remove();
				}

				Rectangle blockRect = block.getBound();
				int ballPrevX = ball.getPrevX();
				int ballPrevY = ball.getPrevY();
				Rectangle prevBallRect = new Rectangle(ballPrevX, ballPrevY, Ball.RADIUS * 2, Ball.RADIUS * 2);

				boolean fromLeftOrRight = ballPrevX + Ball.RADIUS * 2 <= blockRect.x
						|| ballPrevX >= blockRect.x + blockRect.width;
				boolean fromTopOrBottom = ballPrevY + Ball.RADIUS * 2 <= blockRect.y
						|| ballPrevY >= blockRect.y + blockRect.height;

				if (fromLeftOrRight) {
					ball.setDx(-ball.getDx());
				}
				if (fromTopOrBottom) {
					ball.setDy(-ball.getDy());
				}

				break; // 一次只消一個磚塊
			}
		}

		// 球掉到底部，遊戲失敗
		if (ball.getY() >= Setting.PANEL_HEIGHT) {
			timer.stop();
			JOptionPane.showMessageDialog(this, "lost");
			System.exit(0);
		}

		// 所有磚塊消失，遊戲成功
		if (blocks.isEmpty()) {
			timer.stop();
			JOptionPane.showMessageDialog(this, "full recall");
			System.exit(0);
		}

		repaint();
	}

	/**
	 * 滑鼠移動事件：控制擋板的 X 座標。
	 * 
	 * @param e 滑鼠事件
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		paddle.setX(e.getX() - Paddle.WIDTH / 2);
		if (paddle.getX() < 0)
			paddle.setX(0);
		if (paddle.getX() > getWidth() - Paddle.WIDTH)
			paddle.setX(getWidth() - Paddle.WIDTH);
	}

	/**
	 * 滑鼠拖曳事件（未使用）。
	 * 
	 * @param e 滑鼠事件
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
	}

	/**
	 * 載入遊戲圖像（球、擋板、磚塊與背景）。
	 */
	public void loadImage() {
		Ball.loadImage();
		Paddle.loadImage();
		for (Block block : blocks) {
			block.loadImage();
		}
		background = new BufferedImage(Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT, BufferedImage.TYPE_INT_RGB);
		try {
			background = ImageIO.read(getClass().getResource("/bg2.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
