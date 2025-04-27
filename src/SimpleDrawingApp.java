import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SimpleDrawingApp extends JFrame {
    private Color currentColor = Color.BLACK;
    private ArrayList<Shape> shapes = new ArrayList<>();
    private Point startPoint = null;

    public SimpleDrawingApp() {
        // 設定窗口
        setTitle("簡單繪圖應用");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 創建面板
        DrawingPanel drawingPanel = new DrawingPanel();
        add(drawingPanel, BorderLayout.CENTER);

        // 創建控制面板
        JPanel controlPanel = new JPanel();
        JButton colorButton = new JButton("選擇顏色");
        JButton clearButton = new JButton("清除畫布");
        
        colorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "選擇顏色", currentColor);
            if (color != null) {
                currentColor = color;
            }
        });

        clearButton.addActionListener(e -> {
            shapes.clear(); // 清空畫布
            repaint(); // 重新繪製畫布
        });

        controlPanel.add(colorButton);
        controlPanel.add(clearButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private class DrawingPanel extends JPanel {
        public DrawingPanel() {
            setBackground(Color.WHITE);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    startPoint = e.getPoint();
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (startPoint != null) {
                        int x = startPoint.x;
                        int y = startPoint.y;
                        int width = Math.abs(x - e.getX());
                        int height = Math.abs(y - e.getY());
                        
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            shapes.add(new Rectangle(x, y, width, height));
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            shapes.add(new Ellipse2D.Float(x, y, width, height));
                        }
                    }
                    startPoint = null;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(currentColor);
            for (Shape shape : shapes) {
                if (shape instanceof Rectangle) {
                    g2d.fill((Rectangle) shape);
                } else if (shape instanceof Ellipse2D) {
                    g2d.fill((Ellipse2D) shape);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleDrawingApp app = new SimpleDrawingApp();
            app.setVisible(true);
        });
    }
}
