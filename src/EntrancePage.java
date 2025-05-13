import javax.swing.*;
import java.awt.*;

public class EntrancePage extends BackgroundPanel2 {

    public EntrancePage(AppFrame frame) {
        setLayout(new BorderLayout());

        // Ekran boyutuna göre margin ayarla
        int marginX = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.04);
        int marginY = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.05);
        setBorder(BorderFactory.createEmptyBorder(marginY, marginX, marginY, marginX));

        // Sol üst köşe – Başlık
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

// Sol: Başlık
        JLabel topLeftLabel = new JLabel("BILKENT HELPER | ENTRANCE");
        topLeftLabel.setFont(new Font("Avenir Next", Font.PLAIN, 20));
        topLeftLabel.setForeground(Color.WHITE);
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(topLeftLabel);
        topPanel.add(leftPanel, BorderLayout.WEST);

// Sağ: Geri butonu
        ImageIcon backIcon = new ImageIcon("logos/go-back-logo.png");
        JButton backBtn = new JButton(backIcon);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setOpaque(false);
        backBtn.setPreferredSize(new Dimension(40, 40));
        backBtn.addActionListener(e -> frame.showPage("welcome"));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(backBtn);
        topPanel.add(rightPanel, BorderLayout.EAST);

// Top panel'i ana sayfaya ekle
        add(topPanel, BorderLayout.NORTH);

        // Ortada kutu panel
        JPanel mainBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.BLACK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 60, 60);
                g2.dispose();
            }
        };
        mainBox.setOpaque(false);
        mainBox.setPreferredSize(new Dimension(400, 600));
        mainBox.setMaximumSize(new Dimension(400, 600));
        mainBox.setLayout(new GridLayout(3, 1));

        // 1. ÜST: Entrance yazısı
        JPanel topSection = new JPanel(new GridBagLayout());
        topSection.setOpaque(false);
        JLabel entranceLabel = new JLabel("Entrance");
        entranceLabel.setFont(new Font("Avenir Next", Font.PLAIN, 22));
        entranceLabel.setForeground(Color.WHITE);
        topSection.add(entranceLabel);

        // 2. ORTA: Butonlar
        JPanel middleSection = new JPanel(new GridBagLayout());
        middleSection.setOpaque(false);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton login = createStyledButton("LOGIN");
        login.addActionListener(e -> frame.showPage("login"));

        JButton signup = createStyledButton("SIGN UP");
        signup.addActionListener(e -> frame.showPage("signup"));

        buttonPanel.add(login);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(signup);
        middleSection.add(buttonPanel);

        // 3. ALT: Logo ve açıklama
        JPanel bottomSection = new JPanel(new GridBagLayout());
        bottomSection.setOpaque(false);
        JPanel bottomInner = new JPanel();
        bottomInner.setLayout(new BoxLayout(bottomInner, BoxLayout.Y_AXIS));
        bottomInner.setOpaque(false);
        bottomInner.setAlignmentX(Component.CENTER_ALIGNMENT);

        // LOGO: ImageIcon ile JLabel olarak ekleniyor
        ImageIcon logoIcon = new ImageIcon("logos/bilkent-logo.png"); // ← Buraya dosya yolunu yaz
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setPreferredSize(new Dimension(100, 100));
        logoLabel.setMaximumSize(new Dimension(100, 100));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel infoLabel = new JLabel("You can sign up only with Bilkent Mail");
        infoLabel.setFont(new Font("Avenir Next", Font.PLAIN, 14));
        infoLabel.setForeground(Color.LIGHT_GRAY);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomInner.add(logoLabel);
        bottomInner.add(Box.createVerticalStrut(10));
        bottomInner.add(infoLabel);
        bottomSection.add(bottomInner);

        // Ana kutuya bölümleri ekle
        mainBox.add(topSection);
        mainBox.add(middleSection);
        mainBox.add(bottomSection);

        // Kutuyu ortalamak için GridBagLayout kullanan holder panel
        JPanel centerHolder = new JPanel(new GridBagLayout());
        centerHolder.setOpaque(false);
        centerHolder.add(mainBox);
        add(centerHolder, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.BLACK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                g2.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight / 2) / 2);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
                g2.dispose();
            }
        };

        button.setText(text);
        button.setFont(new Font("Avenir Next", Font.PLAIN, 14));
        Dimension size = new Dimension(250, 55);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMargin(new Insets(10, 20, 10, 20));

        return button;
    }
}
