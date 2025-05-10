import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Explosion {
	private int x, y, delayCount = 0;
	private BufferedImage[] frames;
	private int currentFrame;
	private boolean finished;
	private int DELAY = 3;
	public static final int FRAME_SIZE = 192;
	public static final int TOTAL_FRAMES = 22;
	private static BufferedImage spriteSheet;

	static {
		try {
			spriteSheet = ImageIO.read(Explosion.class.getResource("/explosion-5.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Explosion(int centerX, int centerY) {
		this.x = centerX - FRAME_SIZE / 2;
		this.y = centerY - FRAME_SIZE / 2;
		this.frames = new BufferedImage[TOTAL_FRAMES];
		this.currentFrame = 0;
		this.finished = false;

		for (int i = 0; i < TOTAL_FRAMES; i++) {
			frames[i] = spriteSheet.getSubimage(i * FRAME_SIZE, 0, FRAME_SIZE, FRAME_SIZE);
		}
	}

	public void drawShape(Graphics g) {
		render(g);
		update();
	}

	public void update() {
		if (delayCount < DELAY) {
			delayCount++;
		} else {
			if (!finished) {
				currentFrame++;
				if (currentFrame >= TOTAL_FRAMES) {
					finished = true;
				}
			}
			delayCount = 0;
		}
	}

	public void render(Graphics g) {
		if (!finished && currentFrame < TOTAL_FRAMES) {
			BufferedImage frame = frames[currentFrame];

			// 建立一個新的 BufferedImage 做為遮罩後的輸出
			BufferedImage masked = new BufferedImage(FRAME_SIZE, FRAME_SIZE, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = masked.createGraphics();

			// 建立一個中心透明、邊緣淡出的遮罩
			float radius = FRAME_SIZE / 2f;
			Point2D center = new Point2D.Float(radius, radius);
			float[] dist = {0.0f, 1.0f};
			Color[] colors = {new Color(0f, 0f, 0f, 1f), new Color(0f, 0f, 0f, 0f)};  // alpha從1降到0
			RadialGradientPaint mask = new RadialGradientPaint(center, radius, dist, colors);

			// 套用遮罩
			g2.setPaint(mask);
			g2.fillRect(0, 0, FRAME_SIZE, FRAME_SIZE);
			g2.setComposite(AlphaComposite.SrcIn);
			g2.drawImage(frame, 0, 0, null);
			g2.dispose();

			// 將結果畫到主畫布上
			g.drawImage(masked, x, y, null);
		}
	}


	public boolean isFinished() {
		return finished;
	}
}
