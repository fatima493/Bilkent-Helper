import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private Image circlesImage;

    // Sabit eklenecek resmin boyutları
    private static final int CIRCLES_WIDTH = 910;
    private static final int CIRCLES_HEIGHT = 770;
    // İstenen merkez koordinatlarından ofsetler
    private static final int CENTER_OFFSET_X = 123;
    private static final int CENTER_OFFSET_Y = 65;
    // Ek ofset: sola ve yukarı hareket
    private static final int EXTRA_OFFSET_X = 45;
    private static final int EXTRA_OFFSET_Y = 20;

    public BackgroundPanel() {
        try {
            backgroundImage = ImageIO.read(new File("backgrounds/background-1.png"));
        } catch (IOException e) {
            System.err.println("Background resmi yüklenemedi.");
            e.printStackTrace();
        }
        try {
            circlesImage = ImageIO.read(new File("backgrounds/circles2.png"));
        } catch (IOException e) {
            System.err.println("Circles resmi yüklenemedi.");
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int panelWidth  = getWidth();
        int panelHeight = getHeight();

        // 1) Ana arka planı tüm panel boyutuna ölçekleyerek çiz
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, panelWidth, panelHeight, this);
        }

        // 2) circles.png resmini, sabit boyutta (650×550) ve
        //    merkezini (panelWidth-123, panelHeight-65) noktasına yerleştir
        if (circlesImage != null) {
            // İstenen merkez koordinatları
            int centerX = panelWidth  - CENTER_OFFSET_X;
            int centerY = panelHeight - CENTER_OFFSET_Y;
            // Resmin çizimde kullanacağımız üst-sol köşesi:
            int drawX = centerX - (CIRCLES_WIDTH  / 2);
            int drawY = centerY - (CIRCLES_HEIGHT / 2);
            // Ek kaydırma: sola ve yukarı hareket
            drawX -= EXTRA_OFFSET_X;
            drawY -= EXTRA_OFFSET_Y;
            // Çizim
            g.drawImage(circlesImage, drawX, drawY, CIRCLES_WIDTH, CIRCLES_HEIGHT, this);
        }
    }
}
