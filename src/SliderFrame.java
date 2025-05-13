
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

public class SliderFrame extends JFrame {
    private JLabel imageLabel;
    private JLabel prevLabel;
    private JLabel nextLabel;
    private JLabel closeLabel;
    private JLabel surveyLabel;
    private String[] slides;
    private int currentIndex = 0;

    // Size constants
    private static final int SLIDE_WIDTH = 600;
    private static final int SLIDE_HEIGHT = 490;
    private static final int CLOSE_BUTTON_SIZE = 30;
    private static final int SURVEY_BUTTON_SIZE = 30;

    public SliderFrame() {
        initializeFrame();
        setupUI();
        loadSlideImages();
        showCurrentImage();
    }

    private void initializeFrame() {
        setTitle("Image Slider");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setLayout(new BorderLayout());
    }

    private ImageIcon loadImageResource(String path) {
        try {
            URL resourceUrl = getClass().getResource(path);
            if (resourceUrl != null) {
                return new ImageIcon(resourceUrl);
            } else {
                //System.err.println(path);
                // Try loading from file system as fallback
                File file = new File(path);
                if (file.exists()) {
                    return new ImageIcon(file.getAbsolutePath());
                }
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupUI() {
        // Background panel with image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bgIcon = loadImageResource("resources/images/background.jpg");
                if (bgIcon != null) {
                    g.drawImage(bgIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel);

        // Top panel with survey and close buttons
        JPanel topPanel = createTopPanel();
        backgroundPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel with slide image
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(imageLabel);
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with navigation
        JPanel navPanel = createNavigationPanel();
        backgroundPanel.add(navPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left panel for survey button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);

        // Survey button
        surveyLabel = new JLabel();
        surveyLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        surveyLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(SliderFrame.this,
                        "Survey button clicked!", "Survey", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        ImageIcon surveyIcon = loadImageResource("resources/images/BSurvey.png");
        if (surveyIcon != null) {
            Image scaledSurvey = surveyIcon.getImage().getScaledInstance(
                    280, SURVEY_BUTTON_SIZE, Image.SCALE_SMOOTH);
            surveyLabel.setIcon(new ImageIcon(scaledSurvey));
        }

        leftPanel.add(surveyLabel);

        // Right panel for close button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);

        // Close button
        closeLabel = new JLabel();
        closeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // Just close this window instead of System.exit(0)
            }
        });

        ImageIcon closeIcon = loadImageResource("resources/images/close.png");
        if (closeIcon != null) {
            Image scaledClose = closeIcon.getImage().getScaledInstance(
                    CLOSE_BUTTON_SIZE, CLOSE_BUTTON_SIZE, Image.SCALE_SMOOTH);
            closeLabel.setIcon(new ImageIcon(scaledClose));
        }

        rightPanel.add(closeLabel);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setOpaque(false);

        // Previous button
        prevLabel = new JLabel();
        ImageIcon prevIcon = loadImageResource("resources/images/previous.png");
        if (prevIcon != null) {
            prevLabel.setIcon(prevIcon);
        }
        prevLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        prevLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showPrevious();
            }
        });

        // Next button
        nextLabel = new JLabel();
        ImageIcon nextIcon = loadImageResource("resources/images/next.png");
        if (nextIcon != null) {
            nextLabel.setIcon(nextIcon);
        }
        nextLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nextLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showNext();
            }
        });

        panel.add(prevLabel);
        panel.add(nextLabel);
        return panel;
    }

    private void loadSlideImages() {
        // First try to load from resources
        URL slidesUrl = getClass().getResource("resources/slides");
        if (slidesUrl != null && slidesUrl.getProtocol().equals("file")) {
            File slideDir = new File(slidesUrl.getPath());
            slides = slideDir.list((dir, name) -> name.toLowerCase().endsWith(".jpg"));
        } else {
            // Fallback to file system path
            File slideDir = new File("resources/slides");
            slides = slideDir.list((dir, name) -> name.toLowerCase().endsWith(".jpg"));
        }

        if (slides == null || slides.length == 0) {
            slides = new String[0];
            JOptionPane.showMessageDialog(this, "No slides found in resources/slides folder",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Arrays.sort(slides, (a, b) -> {
            int numA = extractNumber(a);
            int numB = extractNumber(b);
            return Integer.compare(numA, numB);
        });
    }

    private int extractNumber(String filename) {
        try {
            String num = filename.replaceAll("\\D+", "");
            return num.isEmpty() ? 0 : Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void showCurrentImage() {
        if (slides.length == 0) {
            imageLabel.setText("No slides available");
            return;
        }

        try {
            String imagePath = "resources/slides/" + slides[currentIndex];
            ImageIcon icon = loadImageResource(imagePath);

            if (icon != null) {
                Image scaled = icon.getImage().getScaledInstance(
                        SLIDE_WIDTH, SLIDE_HEIGHT, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
                imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            } else {
                imageLabel.setText("Error loading slide: " + imagePath);
            }
        } catch (Exception e) {
            imageLabel.setText("Error loading slide");
            e.printStackTrace();
        }
    }

    private void showNext() {
        if (slides.length == 0) return;
        currentIndex = (currentIndex + 1) % slides.length;
        showCurrentImage();
    }

    private void showPrevious() {
        if (slides.length == 0) return;
        currentIndex = (currentIndex - 1 + slides.length) % slides.length;
        showCurrentImage();
    }
}
