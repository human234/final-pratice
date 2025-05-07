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

	// 根據球與板子的碰撞位置調整反彈角度
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
   
   	// 避免 dx = 0 導致垂直反彈
   	if (newDx == 0) {
   		newDx = (offset < 0) ? -1 : 1;
   	}
   
   	ball.setDx(newDx);
   	ball.setDy(newDy);
   }
}
