import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 代表玩家操控的球板，能反彈球並影響其角度。
 */
public class Paddle {
	private int x, y, dx;
	public static final int WIDTH = 100, HEIGHT = 30;
	private static Image image;

	/**
	 * 載入球板圖像並縮放。
	 */
	public static void loadImage() {
		try {
			BufferedImage origin = ImageIO.read(Paddle.class.getResource("/block1.png"));
			image = origin.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 建構初始球板並設定起始位置。
	 */
	public Paddle() {
		this.x = 350;
		this.y = 570;
	}

	public int getX() { return x; }
	public int getY() { return y; }
	public int getDx() { return dx; }

	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	public void setDx(int dx) { this.dx = dx; }

	/**
	 * 繪製球板圖像。
	 */
	public void drawShape(Graphics g) {
		g.drawImage(image, x, y, WIDTH, HEIGHT, null);
	}

	/**
	 * 取得球板的邊界區域，用於碰撞檢測。
	 */
	public Rectangle getBound() {
		return new Rectangle(x, y, Paddle.WIDTH, Paddle.HEIGHT);
	}

	/**
	 * 根據球與球板的相對位置調整反彈角度。
	 * @param ball 被碰撞的球物件
	 */
	public void handleCollision(Ball ball) {
		int ballCenter = ball.getX() + Ball.RADIUS;
		int paddleCenter = this.x + Paddle.WIDTH / 2;
		int offset = ballCenter - paddleCenter;

		double maxAngle = Math.toRadians(60);
		double ratio = offset / (double)(Paddle.WIDTH / 2);
		double angle = ratio * maxAngle;

		double speed = Math.sqrt(ball.getDx() * ball.getDx() + ball.getDy() * ball.getDy());

		int newDx = (int)Math.round(speed * Math.sin(angle));
		int newDy = (int)Math.round(-speed * Math.cos(angle));

		if (newDx == 0) {
			newDx = (offset < 0) ? -1 : 1;
		}

		ball.setDx(newDx);
		ball.setDy(newDy);
	}
}
