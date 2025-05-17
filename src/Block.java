import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * 代表遊戲中的磚塊，每個磚塊可承受多次撞擊並根據損壞程度顯示不同圖案。
 */
public class Block {
	private int x, y, hitCount;
	private Image[] allState;
	public static final int WIDTH = 72, HEIGHT = 32;

	/**
	 * 建立磚塊並設定其位置。
	 * @param x 磚塊左上角 x 座標
	 * @param y 磚塊左上角 y 座標
	 */
	Block(int x, int y) {
		hitCount = 0;
		this.x = x;
		this.y = y;
	}

	/**
	 * 隨機載入一組顏色圖片，並分割成四個狀態代表受損程度。
	 */
	public void loadImage() {
		allState = new Image[4];
		int ref = (int) (Math.random() * 5);
		BufferedImage origin = null;
		try {
			if (ref == 0) {
				origin = ImageIO.read(getClass().getResource("resources/blocks/blue.png"));
			} else if (ref == 1) {
				origin = ImageIO.read(getClass().getResource("resources/blocks/green.png"));
			} else if (ref == 2) {
				origin = ImageIO.read(getClass().getResource("resources/blocks/pink.png"));
			} else if (ref == 3) {
				origin = ImageIO.read(getClass().getResource("resources/blocks/red.png"));
			} else if (ref == 4) {
				origin = ImageIO.read(getClass().getResource("resources/blocks/yellow.png"));
			}
			for (int i = 0; i < 4; i++) {
				BufferedImage sub = origin.getSubimage(0, origin.getHeight() / 4 * i, origin.getWidth(), origin.getHeight() / 4);
				allState[i] = sub.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getX() { return x; }
	public int getY() { return y; }
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }

	/**
	 * 繪製磚塊圖像。
	 */
	public void drawShape(Graphics g) {
		g.drawImage(allState[hitCount], x, y, WIDTH, HEIGHT, null);
	}

	/**
	 * 磚塊被擊中一次，提升損壞等級。
	 */
	public void hitted() {
		hitCount++;
		SoundManager.playSoundEffect("resources/sound/hitwithbrick.wav",-20f);
	}

	/**
	 * 判斷磚塊是否毀壞。
	 * @return true 表示磚塊已經完全損壞
	 */
	public boolean death() {
		return hitCount > 3;
	}

	/**
	 * 取得磚塊邊界，用於碰撞檢測。
	 */
	public Rectangle getBound() {
		return new Rectangle(x, y, Block.WIDTH, Block.HEIGHT);
	}
}
