import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Ball {

	public static final int RADIUS = 30;
	private int x, y, dx, dy;
	private int prevX, prevY;
	private static Image[] imageFrames;
	private int currentFrame = 0;

	public Ball() {
		this.x = 300;
		this.y = 300;
		this.dx = 3;
		this.dy = -10;
	}

	public static void loadImage() {
		imageFrames = new Image[60];
		BufferedImage origin = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < 60; i++) {
			try {
				String fileName = String.format("%04d.png", i);
				origin = ImageIO.read(Ball.class.getResource(fileName));
				imageFrames[i] = origin.getScaledInstance(RADIUS * 2, RADIUS * 2, Image.SCALE_SMOOTH);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		if (currentFrame < 59) {
			currentFrame++;
		} else {
			currentFrame = 0;
		}
	}

	private void render(Graphics g) {
		g.drawImage(imageFrames[currentFrame], x, y, RADIUS * 2, RADIUS * 2, null);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(4));
		g2d.setColor(Color.black);
		g2d.drawOval(x, y, RADIUS * 2, RADIUS * 2);
	}

	public Rectangle getBound() {
		return new Rectangle(x, y, RADIUS * 2, RADIUS * 2);
	}
}
