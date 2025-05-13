import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends BackgroundPanel2 {

    public LoginPage(AppFrame frame) {
        setLayout(new BorderLayout());

        int marginX = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.04);
        int marginY = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.05);
        setBorder(BorderFactory.createEmptyBorder(marginY, marginX, marginY, marginX));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel topLeftLabel = new JLabel("BILKENT HELPER | LOGIN");
        topLeftLabel.setFont(new Font("Avenir Next", Font.PLAIN, 20));
        topLeftLabel.setForeground(Color.WHITE);
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(topLeftLabel);
        topPanel.add(leftPanel, BorderLayout.WEST);

        ImageIcon backRaw = new ImageIcon("logos/go-back-logo.png");
        Image backImg = backRaw.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton backBtn = new JButton(new ImageIcon(backImg));
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

        add(topPanel, BorderLayout.NORTH);

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
        mainBox.setLayout(new GridLayout(3, 1));

        JPanel topSection = new JPanel(new GridBagLayout());
        topSection.setOpaque(false);
        JLabel entranceLabel = new JLabel("Login");
        entranceLabel.setFont(new Font("Avenir Next", Font.PLAIN, 22));
        entranceLabel.setForeground(Color.WHITE);
        topSection.add(entranceLabel);

        JPanel middleSection = new JPanel(new GridBagLayout());
        middleSection.setOpaque(false);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(250, 125));
        layeredPane.setOpaque(false);

        RoundedTextField emailField = new RoundedTextField(250, 55);
        emailField.setFont(new Font("Avenir Next", Font.PLAIN, 14));
        emailField.setForeground(Color.WHITE);
        emailField.setCaretColor(Color.WHITE);
        emailField.setText("Email:");
        emailField.setBounds(0, 0, 250, 55);
        layeredPane.add(emailField, Integer.valueOf(1));

        RoundedPasswordField passwordField = new RoundedPasswordField(250, 55);
        passwordField.setFont(new Font("Avenir Next", Font.PLAIN, 14));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setEchoChar((char) 0);
        passwordField.setText("Password:");
        passwordField.setBounds(0, 70, 250, 55);
        layeredPane.add(passwordField, Integer.valueOf(1));

        JButton enterBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1));
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
            }
        };

        enterBtn.setLayout(new BorderLayout());
        enterBtn.setBounds(200, 75, 45, 45);
        enterBtn.setFocusPainted(false);
        enterBtn.setBorderPainted(false);
        enterBtn.setContentAreaFilled(false);
        enterBtn.setOpaque(false);
        enterBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel arrowLabel = new JLabel("➔", SwingConstants.CENTER);
        arrowLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        arrowLabel.setForeground(Color.BLACK);
        arrowLabel.setOpaque(false);
        enterBtn.add(arrowLabel, BorderLayout.CENTER);

        enterBtn.addActionListener(e -> {
            String inputEmail = emailField.getText().trim();
            String inputPassword = new String(passwordField.getPassword()).trim();

            if (inputEmail.equals("Email:")) inputEmail = "";
            if (inputPassword.equals("Password:")) inputPassword = "";

            UserDatabase userDB = new UserDatabase();
            boolean loginSuccess = false;

            for (User user : userDB.getUsers()) {
                if (user.getEmail().equalsIgnoreCase(inputEmail) && user.getPassword().equals(inputPassword)) {
                    // Giriş başarılı, currentUser'ı güncelle
                    user.setUsername(user.getEmail().substring(0, user.getEmail().indexOf('@')));
                    frame.setCurrentUser(user);
                    loginSuccess = true;
                    break;
                }
            }

            if (loginSuccess) {
                frame.showPage("main");
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        layeredPane.add(enterBtn, Integer.valueOf(2));
        middleSection.add(layeredPane);

        JPanel bottomSection = new JPanel(new GridBagLayout());
        bottomSection.setOpaque(false);
        JPanel bottomInner = new JPanel();
        bottomInner.setLayout(new BoxLayout(bottomInner, BoxLayout.Y_AXIS));
        bottomInner.setOpaque(false);
        bottomInner.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon logoRaw = new ImageIcon("logos/bilkent-logo.png");
        Image logoImg = logoRaw.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImg));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel infoLabel = new JLabel("You can sign up only with Bilkent Mail");
        infoLabel.setFont(new Font("Avenir Next", Font.PLAIN, 14));
        infoLabel.setForeground(Color.LIGHT_GRAY);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomInner.add(logoLabel);
        bottomInner.add(Box.createVerticalStrut(10));
        bottomInner.add(infoLabel);
        bottomSection.add(bottomInner);

        mainBox.add(topSection);
        mainBox.add(middleSection);
        mainBox.add(bottomSection);

        JPanel centerHolder = new JPanel(new GridBagLayout());
        centerHolder.setOpaque(false);
        centerHolder.add(mainBox);
        add(centerHolder, BorderLayout.CENTER);
    }

    // RoundedTextField: Yuvarlak kenarlı metin alanı
    class RoundedTextField extends JTextField {
        public RoundedTextField(int width, int height) {
            setPreferredSize(new Dimension(width, height));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.BLACK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            super.paintComponent(g);
            g2.dispose();
        }

        @Override protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, getHeight(), getHeight());
            g2.dispose();
        }
    }

    // RoundedPasswordField: Yuvarlak kenarlı şifre alanı
    class RoundedPasswordField extends JPasswordField {
        public RoundedPasswordField(int width, int height) {
            setPreferredSize(new Dimension(width, height));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.BLACK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            super.paintComponent(g);
            g2.dispose();
        }

        @Override protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, getHeight(), getHeight());
            g2.dispose();
        }
    }
}
