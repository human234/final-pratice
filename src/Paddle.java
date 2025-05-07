import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Paddle {

	private int x, y, dx;
	public static final int WIDTH = 100, HEIGHT = 30;

	public Paddle() {
		this.x = 350;
		this.y = 585;
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

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public void drawShape(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(x, y, Paddle.WIDTH, Paddle.HEIGHT);
	}
	
	public Rectangle getBound() {
		return new Rectangle(x, y, Paddle.WIDTH, Paddle.HEIGHT);
	}
	
}
