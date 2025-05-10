import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
	private BufferedImage background;

	public BlockBreakerPanel() {
		myImage = new BufferedImage(Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT, BufferedImage.TYPE_INT_RGB);
		myBuffer = myImage.getGraphics();
		setPreferredSize(new Dimension(Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT));
		setFocusable(true);
		addMouseMotionListener(this);

		ball = new Ball();
		paddle = new Paddle();
		blocks = new ArrayList<Block>();
		int spacing = 15;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 9; j++) {
				blocks.add(new Block(spacing + (Block.WIDTH + spacing) * j, (Block.HEIGHT + 10) * i));
			}
		}
		loadImage();
		timer = new Timer(10, this);
		timer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(myImage, 0, 0, Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		myBuffer.drawImage(background, 0, 0, Setting.PANEL_WIDTH, Setting.PANEL_HEIGHT, null);

		ball.move();

		blockIterator = blocks.iterator();
		while (blockIterator.hasNext()) {
			Block b = blockIterator.next();
			b.drawShape(myBuffer); // ä¿®æ­£æ–¹æ³•å��ç¨±
		}

		ball.drawShape(myBuffer);
		paddle.drawShape(myBuffer);

		// æª¢æŸ¥é‚Šç•Œç¢°æ’ž
		if (ball.getX() <= 0 || ball.getX() >= getWidth() - 2 * Ball.RADIUS) {
			ball.setDx(ball.getDx() * -1);
		}
		if (ball.getY() <= 0) {
			ball.setDy(ball.getDy() * -1);
		}

		// æª¢æŸ¥æ�¿å­�ç¢°æ’ž
		if (ball.getBound().intersects(paddle.getBound())) {
			paddle.handleCollision(ball); // æ ¹æ“šç¢°æ’žä½�ç½®èª¿æ•´å��å½ˆè§’åº¦
		}

		// ç¢°æ’žç£šå¡Šé‚�è¼¯
		blockIterator = blocks.iterator();
		while (blockIterator.hasNext()) {
			Block block = blockIterator.next();
			if (block.getBound().intersects(ball.getBound())) {
				block.hitted();
				if(block.death()) {
					blockIterator.remove(); // åˆªé™¤å·²ç¢°æ’žçš„ç£šå¡Š
				}
				// ç¢ºå®šå¾žç£šå¡Šå“ªä¸€é‚Šå��å½ˆ
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

				break; // ä¸€æ¬¡å�ªæ¶ˆä¸€å€‹ç£šå¡Šï¼Œé�¿å…�é€£çºŒç¢°æ’žå•�é¡Œ
			}
		}

		// å¦‚æžœç�ƒè�½åˆ°èž¢å¹•åº•éƒ¨ï¼Œé�Šæˆ²çµ�æ�Ÿ
		if (ball.getY() >= Setting.PANEL_HEIGHT) {
			timer.stop();
			JOptionPane.showMessageDialog(this, "lost");
			System.exit(0);
		}

		// å¦‚æžœæ‰€æœ‰ç£šå¡Šæ¶ˆå¤±ï¼Œé�Šæˆ²çµ�æ�Ÿ
		if (blocks.isEmpty()) {
			timer.stop();
			JOptionPane.showMessageDialog(this, "full recall");
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
	public void mouseDragged(MouseEvent e) {
	}

	public void loadImage() {
		Ball.loadImage();
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
