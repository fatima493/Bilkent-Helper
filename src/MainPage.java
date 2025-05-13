import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainPage extends BackgroundPanel {
    private AppFrame frame;
    private JPanel mainContent;

    public MainPage(AppFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        // margins
        int mx = (int)(Toolkit.getDefaultToolkit().getScreenSize().width * 0.04);
        int my = (int)(Toolkit.getDefaultToolkit().getScreenSize().height * 0.05);
        setBorder(new EmptyBorder(my, mx, my, mx));

        createMainContent();
        add(mainContent, BorderLayout.CENTER);
    }

    private void createMainContent() {
        mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);

        // --- TOP BAR ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        // top-left title
        JLabel title = new JLabel("BILKENT HELPER");
        title.setFont(new Font("Avenir Next", Font.PLAIN, 20));
        title.setForeground(Color.WHITE);
        JPanel tl = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tl.setOpaque(false);
        tl.add(title);
        topBar.add(tl, BorderLayout.WEST);

        // top-right: back / profile
        JPanel tr = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        tr.setOpaque(false);
        tr.add(iconButton("logos/go-back-logo.png", () -> frame.showPage("welcome")));
        tr.add(iconButton("logos/profile-icon.png", () -> frame.showPage("profile")));
        topBar.add(tr, BorderLayout.EAST);

        mainContent.add(topBar, BorderLayout.NORTH);

        // --- BOTTOM BAR ---
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setOpaque(false);

        // bottom-left: 3 panels in one row
        JPanel grid = new JPanel(new GridLayout(1, 3, 10, 0));
        grid.setOpaque(false);
        grid.add(sidePanel("Map", "logos/map-icon.png", () -> frame.showPage("map")));
        grid.add(sidePanel("Tutoring", "logos/tutoring-icon.png", () -> frame.showPage("tutoring")));
        grid.add(sidePanel("Store", "logos/store-icon.png", () -> frame.showPage("store")));
        bottomBar.add(grid, BorderLayout.WEST);

        // bottom-right: Survey button
        JButton survey = new JButton("Bilkent Survey") {
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
            protected void paintBorder(Graphics g) {}
        };
        survey.setFont(new Font("Avenir Next", Font.PLAIN, 20));
        survey.setPreferredSize(new Dimension(210, 70));
        survey.setContentAreaFilled(false);
        survey.setFocusPainted(false);
        survey.setBorderPainted(false);
        survey.setOpaque(false);
        survey.addActionListener(e -> showSurvey());

        JPanel bottomRightPanel = new JPanel(new GridBagLayout());
        bottomRightPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        bottomRightPanel.add(survey, gbc);

        bottomBar.add(bottomRightPanel, BorderLayout.CENTER);
        mainContent.add(bottomBar, BorderLayout.SOUTH);

        // --- Welcome Text ---
        String userName = CurrentUser.getInstance().getUser() != null
                ? CurrentUser.getInstance().getUser().getUsername()
                : "Guest";
        JLabel welcomeText = new JLabel("<html><div style='text-align:left;'>Welcome,<br/>" + userName + "!</div></html>");
        welcomeText.setFont(new Font("Avenir Next", Font.BOLD, 50));
        welcomeText.setForeground(Color.WHITE);
        welcomeText.setOpaque(false);

        // Welcome panel – left aligned
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        welcomePanel.setOpaque(false);
        welcomePanel.add(welcomeText);

        // Center panel with welcome text
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(0, 0, 130, 0));
        centerPanel.add(welcomePanel, BorderLayout.SOUTH);
        mainContent.add(centerPanel, BorderLayout.CENTER);
    }

    private void showSurvey() {
        removeAll();
        
        // Create back button panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(iconButton("logos/go-back-logo.png", () -> {
            removeAll();
            add(mainContent, BorderLayout.CENTER);
            revalidate();
            repaint();
        }));
        
        // Create container for survey content
        JPanel surveyContainer = new JPanel(new BorderLayout());
        surveyContainer.setOpaque(false);
        surveyContainer.add(topPanel, BorderLayout.NORTH);
        surveyContainer.add(new SliderPanel(), BorderLayout.CENTER);
        
        add(surveyContainer, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // creates a 40×40 icon button
    private JButton iconButton(String path, Runnable onClick) {
        ImageIcon raw = new ImageIcon(path);
        Image img = raw.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton btn = new JButton(new ImageIcon(img));
        btn.setPreferredSize(new Dimension(40, 40));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.addActionListener(e -> onClick.run());
        return btn;
    }

    // creates a 100×120 panel with a 14pt label on top and a 100×100 icon button below
    private JPanel sidePanel(String text, String iconPath, Runnable onClick) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(100, 130));

        JLabel lbl = new JLabel(text, SwingConstants.LEFT);
        lbl.setFont(new Font("Avenir Next", Font.PLAIN, 14));
        lbl.setForeground(Color.WHITE);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0));
        p.add(lbl, BorderLayout.NORTH);

        ImageIcon raw = new ImageIcon(iconPath);
        Image img = raw.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JButton btn = new JButton(new ImageIcon(img));
        btn.setPreferredSize(new Dimension(100, 100));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.addActionListener(e -> onClick.run());
        p.add(btn, BorderLayout.SOUTH);

        return p;
    }
}
