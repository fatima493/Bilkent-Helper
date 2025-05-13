import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WelcomePage extends BackgroundPanel {

    public WelcomePage(AppFrame frame) {
        setLayout(new BorderLayout());

        // %5 margin için boşluk ekle
        int marginX = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.04);
        int marginY = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.05);
        setBorder(new EmptyBorder(marginY, marginX, marginY, marginX));

        // Sol üst köşe – Başlık
        JLabel topLeftLabel = new JLabel("BILKENT HELPER");
        topLeftLabel.setFont(new Font("Avenir Next", Font.PLAIN, 20));
        topLeftLabel.setForeground(Color.WHITE);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(topLeftLabel);
        add(topPanel, BorderLayout.NORTH);

        // Sol alt köşe – Yazılar
        JPanel bottomLeftPanel = new JPanel();
        bottomLeftPanel.setLayout(new BoxLayout(bottomLeftPanel, BoxLayout.Y_AXIS));
        bottomLeftPanel.setOpaque(false);
        bottomLeftPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel bigText = new JLabel("<html>The new <br>Platform for<br>All your questions about <br>Bilkent University.</html>");
        bigText.setFont(new Font("Avenir Next", Font.BOLD, 40));
        bigText.setForeground(Color.WHITE);
        bigText.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel smallText = new JLabel("Where to find tutors, buildings, classes, secondhand books, materials and more...");
        smallText.setFont(new Font("Avenir Next", Font.PLAIN, 18));
        smallText.setForeground(Color.LIGHT_GRAY);
        smallText.setAlignmentX(Component.LEFT_ALIGNMENT);

        bottomLeftPanel.add(bigText);
        bottomLeftPanel.add(Box.createVerticalStrut(10));
        bottomLeftPanel.add(smallText);

        JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftWrapper.setOpaque(false);
        leftWrapper.add(bottomLeftPanel);

        // Buton – Sağ alt köşe
        JButton getStartedBtn = new JButton("Get Started") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                int arc = getHeight();
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                g2.setColor(Color.BLACK);
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                g2.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 2);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Border çizilmesin
            }
        };
        getStartedBtn.setFont(new Font("Avenir Next", Font.PLAIN, 20));
        getStartedBtn.setPreferredSize(new Dimension(210, 70));
        getStartedBtn.setContentAreaFilled(false);
        getStartedBtn.setFocusPainted(false);
        getStartedBtn.setBorderPainted(false);
        getStartedBtn.setOpaque(false);
        getStartedBtn.addActionListener(e -> frame.showPage("entrance"));

        // Sağ alt panel - FlowLayout kullanıldı
        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomRightPanel.setOpaque(false);
        bottomRightPanel.add(getStartedBtn);

        JPanel rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.setOpaque(false);
        rightWrapper.add(bottomRightPanel, BorderLayout.SOUTH);

        // Alt paneli oluştur (sol yazılar + sağ buton)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(leftWrapper, BorderLayout.WEST);
        bottomPanel.add(rightWrapper, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }
}
