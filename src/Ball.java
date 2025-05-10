import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Ball {

	public static final int RADIUS = 24;
	private int x, y, dx, dy;
	private int prevX, prevY;
	private static Image[] imageFrames;
	private int currentFrame = 0;

	public Ball() {
		this.x = 300;
		this.y = 300;
		this.dx = 3;
		this.dy = -6;
	}

	public static void loadImage() {
		imageFrames = new Image[94];
		BufferedImage sprite;
		try {
			sprite = ImageIO.read(Ball.class.getResource("earth.png"));
			for (int i = 0; i < 94; i++) {
				imageFrames[i] = sprite.getSubimage(i % 10 * 48, i / 10 * 48, 48, 48);
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

	public int getDx() {
		return dx;
	}

	public int getDy() {
		return dy;
	}

	public int getPrevX() {
		return prevX;
	}

	public int getPrevY() {
		return prevY;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	public void move() {
		prevX = x;
		prevY = y;
		x += dx;
		y += dy;
	}

	public void drawShape(Graphics g) {
		render(g);
		update();
	}

	private void update() {
		if (currentFrame < 93) {
			currentFrame++;
		} else {
			currentFrame = 0;
		}
	}

	private void render(Graphics g) {
		g.drawImage(imageFrames[currentFrame], x, y, RADIUS * 2, RADIUS * 2, null);

	}

	public Rectangle getBound() {
		return new Rectangle(x, y, RADIUS * 2, RADIUS * 2);
	}
}
