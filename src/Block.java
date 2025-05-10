import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Block {
	private int x, y, hitCount;
	private Image[] allState;
	public static final int WIDTH = 72, HEIGHT = 32;

	Block(int x, int y) {
		hitCount = 0;
		this.x = x;
		this.y = y;
	}

	public void loadImage() {
		allState = new Image[4];
		int ref = (int) (Math.random() * 5);
		BufferedImage origin = null;
		try {
			if (ref == 0) {
				origin = ImageIO.read(getClass().getResource("/blue.png"));
			} else if (ref == 1) {
				origin = ImageIO.read(getClass().getResource("/green.png"));
			} else if (ref == 2) {
				origin = ImageIO.read(getClass().getResource("/pink.png"));
			} else if (ref == 3) {
				origin = ImageIO.read(getClass().getResource("/red.png"));
			} else if (ref == 4) {
				origin = ImageIO.read(getClass().getResource("/yellow.png"));
			}
			for (int i = 0; i < 4; i++) {
				BufferedImage sub = origin.getSubimage(0, origin.getHeight() / 4 * i, origin.getWidth(), origin.getHeight() / 4);
				allState[i] = sub.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void drawShape(Graphics g) {
		g.drawImage(allState[hitCount], x, y, WIDTH, HEIGHT, null);
	}

	public void hitted() {
		hitCount++;
	}

	public boolean death() {
		return hitCount > 3;
	}

	public Rectangle getBound() {
		return new Rectangle(x, y, Block.WIDTH, Block.HEIGHT);
	}
}
