import javax.swing.*;

/**
 * BlockBreaker 主程式類別，用於啟動遊戲視窗。
 */
public class BlockBreakerDriver {
	/**
	 * 程式進入點，建立 JFrame 並啟動 BlockBreakerPanel。
	 * @param args 命令列參數（未使用）
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Block Breaker");
		frame.setLocation(100, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new BlockBreakerPanel());
		frame.pack();
		frame.setVisible(true);
	}
}

