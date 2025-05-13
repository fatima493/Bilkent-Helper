import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SignUpPage extends BackgroundPanel2 {

    public SignUpPage(AppFrame frame) {
        setLayout(new BorderLayout());

        int marginX = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.04);
        int marginY = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.05);
        setBorder(BorderFactory.createEmptyBorder(marginY, marginX, marginY, marginX));

        // Üst panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel topLeftLabel = new JLabel("BILKENT HELPER | SIGN UP");
        topLeftLabel.setFont(new Font("Avenir Next", Font.PLAIN, 20));
        topLeftLabel.setForeground(Color.WHITE);
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(topLeftLabel);
        topPanel.add(leftPanel, BorderLayout.WEST);

        ImageIcon backIcon = new ImageIcon("logos/go-back-logo.png");
        JButton backBtn = new JButton(backIcon);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setOpaque(false);
        backBtn.setPreferredSize(new Dimension(40, 40));
        backBtn.addActionListener(e -> frame.showPage("entrance"));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(backBtn);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Ortadaki kutu
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

        // Başlık
        JPanel topSection = new JPanel(new GridBagLayout());
        topSection.setOpaque(false);
        JLabel entranceLabel = new JLabel("SIGN UP");
        entranceLabel.setFont(new Font("Avenir Next", Font.PLAIN, 22));
        entranceLabel.setForeground(Color.WHITE);
        topSection.add(entranceLabel);

        // Ortadaki textfieldlar
        JPanel middleSection = new JPanel(new GridBagLayout());
        middleSection.setOpaque(false);
        JPanel fieldPanel = new JPanel();
        fieldPanel.setOpaque(false);
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedTextField emailField = new RoundedTextField(250, 55);
        emailField.setFont(new Font("Avenir Next", Font.PLAIN, 14));
        emailField.setForeground(Color.WHITE);
        emailField.setCaretColor(Color.WHITE);
        emailField.setText("Email:");

        RoundedPasswordField passwordField = new RoundedPasswordField(250, 55);
        passwordField.setFont(new Font("Avenir Next", Font.PLAIN, 14));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setEchoChar((char) 0);
        passwordField.setText("Password:");

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
        enterBtn.setFocusPainted(false);
        enterBtn.setBorderPainted(false);
        enterBtn.setContentAreaFilled(false);
        enterBtn.setOpaque(false);
        enterBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        enterBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (email.endsWith("@bilkent.edu.tr") || email.endsWith("@ug.bilkent.edu.tr")) {
                String username = email.substring(0, email.indexOf('@'));
                addUser(username, password, email, false);
                JOptionPane.showMessageDialog(this, "User created successfully!");
                frame.showPage("main");
            } else {
                JOptionPane.showMessageDialog(this, "Please use a Bilkent email address.", "Invalid Email", JOptionPane.ERROR_MESSAGE);
            }
        });

        JLabel arrowLabel = new JLabel("➔", SwingConstants.CENTER);
        arrowLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        arrowLabel.setForeground(Color.BLACK);
        arrowLabel.setOpaque(false);
        enterBtn.add(arrowLabel, BorderLayout.CENTER);

        // Şifre alanı container (JLayeredPane)
        JLayeredPane passwordFieldContainer = new JLayeredPane();
        passwordFieldContainer.setPreferredSize(new Dimension(250, 55));
        passwordField.setBounds(0, 0, 250, 55);
        enterBtn.setBounds(200, 5, 45, 45);
        passwordFieldContainer.add(passwordField, Integer.valueOf(1));
        passwordFieldContainer.add(enterBtn, Integer.valueOf(2));

        fieldPanel.add(emailField);
        fieldPanel.add(Box.createVerticalStrut(15));
        fieldPanel.add(passwordFieldContainer);
        middleSection.add(fieldPanel);

        // Alt bölüm
        JPanel bottomSection = new JPanel(new GridBagLayout());
        bottomSection.setOpaque(false);
        JPanel bottomInner = new JPanel();
        bottomInner.setLayout(new BoxLayout(bottomInner, BoxLayout.Y_AXIS));
        bottomInner.setOpaque(false);
        bottomInner.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon logoIcon = new ImageIcon("logos/bilkent-logo.png");
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

        mainBox.add(topSection);
        mainBox.add(middleSection);
        mainBox.add(bottomSection);

        JPanel centerHolder = new JPanel(new GridBagLayout());
        centerHolder.setOpaque(false);
        centerHolder.add(mainBox);
        add(centerHolder, BorderLayout.CENTER);
    }

    public void addUser(String username, String password, String email, boolean isTutor) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/user_schema",
                    "root",
                    "newpassword"
            );

            String insertSQL = "INSERT INTO user (username, password, email, isTutor) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setBoolean(4, isTutor);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

            System.out.println("Yeni kullanıcı eklendi.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while saving the user.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class RoundedTextField extends JTextField {
        public RoundedTextField(int width, int height) {
            setPreferredSize(new Dimension(width, height));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.BLACK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
            g2.dispose();
        }
    }

    class RoundedPasswordField extends JPasswordField {
        public RoundedPasswordField(int width, int height) {
            setPreferredSize(new Dimension(width, height));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.BLACK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
            g2.dispose();
        }
    }
}
