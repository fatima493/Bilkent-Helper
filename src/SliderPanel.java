import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

public class SliderPanel extends JPanel {
    private JLabel imageLabel;
    private JLabel prevLabel;
    private JLabel nextLabel;
    private String[] slides;
    private int currentIndex = 0;

    // Size constants
    private static final int SLIDE_WIDTH = 600;
    private static final int SLIDE_HEIGHT = 490;

    public SliderPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setupUI();
        loadSlideImages();
        showCurrentImage();
    }

    private ImageIcon loadImageResource(String path) {
        try {
            URL resourceUrl = getClass().getResource(path);
            if (resourceUrl != null) {
                return new ImageIcon(resourceUrl);
            } else {
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
        // Center panel with slide image
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(imageLabel);
        add(centerPanel, BorderLayout.CENTER);

        // Navigation panel
        JPanel navPanel = createNavigationPanel();
        add(navPanel, BorderLayout.SOUTH);
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
