import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Ball {

	public static final int RADIUS = 20;
	private int x, y, dx, dy;
	private int prevX, prevY;

	public Ball() {
		this.x = 300;
		this.y = 300;
		this.dx = 3;
		this.dy = -6;
	}

	public int getX() { return x; }
	public int getY() { return y; }
	public int getDx() { return dx; }
	public int getDy() { return dy; }
	public int getPrevX() { return prevX; }
	public int getPrevY() { return prevY; }

	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	public void setDx(int dx) { this.dx = dx; }
	public void setDy(int dy) { this.dy = dy; }

	public void move() {
		prevX = x;
		prevY = y;
		x += dx;
		y += dy;
	}

	public void drawShape(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(x, y, 2 * Ball.RADIUS, 2 * Ball.RADIUS);
	}

	public Rectangle getBound() {
		return new Rectangle(x, y, Ball.RADIUS * 2, Ball.RADIUS * 2);
	}
}
