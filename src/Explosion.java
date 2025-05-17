import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Explosion 類別負責在畫面上顯示爆炸動畫效果。
 * 每個 Explosion 物件會根據精靈圖產生一系列動畫幀，並使用透明遮罩呈現淡出效果。
 */
public class Explosion {
	private int x, y, delayCount = 0;
	private BufferedImage[] frames;
	private int currentFrame;
	private boolean finished;
	private int DELAY = 3;
	public static final int FRAME_SIZE = 192;
	public static final int TOTAL_FRAMES = 22;
	private static BufferedImage spriteSheet;

	// 載入爆炸動畫的精靈圖
	static {
		try {
			spriteSheet = ImageIO.read(Explosion.class.getResource("resources/explosions/explosion-5.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 建構子：根據中心點位置初始化 Explosion 動畫。
	 * @param centerX 爆炸中心的 X 座標
	 * @param centerY 爆炸中心的 Y 座標
	 */
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

	/**
	 * 繪製爆炸動畫並更新狀態。
	 * @param g 用來繪圖的 Graphics 物件
	 */
	public void drawShape(Graphics g) {
		render(g);
		update();
	}

	/**
	 * 更新動畫的當前幀與完成狀態。
	 */
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

	/**
	 * 繪製目前幀，並使用漸層遮罩產生淡出的視覺效果。
	 * @param g 用來繪圖的 Graphics 物件
	 */
	public void render(Graphics g) {
		if (!finished && currentFrame < TOTAL_FRAMES) {
			BufferedImage frame = frames[currentFrame];

			// 建立遮罩圖層
			BufferedImage masked = new BufferedImage(FRAME_SIZE, FRAME_SIZE, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = masked.createGraphics();

			// 建立中心透明、邊緣淡出的遮罩
			float radius = FRAME_SIZE / 2f;
			Point2D center = new Point2D.Float(radius, radius);
			float[] dist = {0.0f, 1.0f};
			Color[] colors = {new Color(0f, 0f, 0f, 1f), new Color(0f, 0f, 0f, 0f)};
			RadialGradientPaint mask = new RadialGradientPaint(center, radius, dist, colors);

			// 套用遮罩並繪製圖片
			g2.setPaint(mask);
			g2.fillRect(0, 0, FRAME_SIZE, FRAME_SIZE);
			g2.setComposite(AlphaComposite.SrcIn);
			g2.drawImage(frame, 0, 0, null);
			g2.dispose();

			// 將結果繪製到主畫布上
			g.drawImage(masked, x, y, null);
		}
	}

	/**
	 * 檢查爆炸動畫是否播放完畢。
	 * @return 若動畫已播放完則回傳 true，否則回傳 false
	 */
	public boolean isFinished() {
		return finished;
	}
}
