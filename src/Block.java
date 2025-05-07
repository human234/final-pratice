import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Block {
	private int x, y;
	public static final int WIDTH = 70, HEIGHT = 20;

	Block(int x, int y) {
		this.x = x;
		this.y = y;
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
		g.setColor(Color.blue);
		g.fillRect(x, y, Block.WIDTH, Block.HEIGHT);
	}
	
	public Rectangle getBound() {
		return new Rectangle(x,y,Block.WIDTH,Block.HEIGHT);
	}
}
