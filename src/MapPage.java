import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.imageio.ImageIO;
import javax.swing.event.DocumentListener;

public class MapPage extends JPanel {
    private AppFrame parent;
    private JLabel mapLabel;
    private JPanel rightPanel, buttonPanel, infoPanel;
    private JTextField searchField;
    private JTextArea buildingDetailsText;
    private JLabel buildingImageLabel;
    private JButton navigateButton, zoomInBtn, zoomOutBtn;
    private List<Building> buildings;
    private JComboBox<String> buildingFilter;
    private DefaultListModel<Building> resultsListModel;
    private JList<Building> resultsList;
    private Image mapImage;
    private double zoomFactor = 1.0;
    private String currentCategory = "All";
    private Set<String> visibleBuildingCodes = new HashSet<>();
    private Building currentSelectedBuilding = null;

    private static final String[] CATEGORIES = {
        "All", "Buildings", "Cafes", "Restaurants", "Parking_Lots", 
        "Stores", "Dormitories", "Administrations", "Sports_Areas", "Auditoriums"
    };

    public MapPage(AppFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        try {
            mapImage = ImageIO.read(new File("backgrounds/map1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        initializeBuildings();

        mapLabel = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (mapImage != null) {
                    int width = (int)(mapImage.getWidth(null) * zoomFactor);
                    int height = (int)(mapImage.getHeight(null) * zoomFactor);
                    setPreferredSize(new Dimension(width, height));
                    g.drawImage(mapImage, 0, 0, width, height, this);
                    drawBuildingMarkers(g, width, height);
                }
            }
        };
        mapLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                handleMapClick(e.getPoint());
            }
        });

        JScrollPane mapScroll = new JScrollPane(mapLabel);
        mapScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        mapScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mapScroll.getViewport().setBackground(Color.BLACK);
        add(mapScroll, BorderLayout.CENTER);

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(350, 0));
        rightPanel.setBackground(Color.BLACK);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.BLACK);
        JButton goBackBtn = new JButton(new ImageIcon("logos/go-back-logo.png"));
        goBackBtn.setPreferredSize(new Dimension(40, 40));
        goBackBtn.setBackground(Color.BLACK);
        goBackBtn.setBorder(BorderFactory.createEmptyBorder());
        goBackBtn.addActionListener(e -> parent.showPage("main"));
        JLabel exitLabel = new JLabel("EXIT | MAP");
        exitLabel.setForeground(Color.WHITE);
        exitLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(goBackBtn);
        topPanel.add(exitLabel);
        rightPanel.add(topPanel, BorderLayout.NORTH);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setPreferredSize(new Dimension(100, 0));

        // Add "All" button first
        JButton allBtn = new JButton(new ImageIcon(new ImageIcon("logos/all.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        allBtn.setPreferredSize(new Dimension(80, 80));
        allBtn.setMaximumSize(new Dimension(80, 80));
        allBtn.setBackground(Color.BLACK);
        allBtn.setBorder(BorderFactory.createEmptyBorder());
        allBtn.setToolTipText("All");
        allBtn.addActionListener(e -> {
            currentCategory = "All";
            currentSelectedBuilding = null;
            clearBuildingDetails();
            updateResults();
            mapLabel.repaint();
        });
        buttonPanel.add(allBtn);
        buttonPanel.add(Box.createVerticalStrut(8));

        // Add other category buttons
        for (String cat : CATEGORIES) {
            if (cat.equals("All")) continue; // Skip "All" as we already added it
            
            JButton btn = new JButton(new ImageIcon(new ImageIcon("logos/" + cat.toLowerCase() + ".png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
            btn.setPreferredSize(new Dimension(80, 80));
            btn.setMaximumSize(new Dimension(80, 80));
            btn.setBackground(Color.BLACK);
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setToolTipText(cat);
            btn.addActionListener(e -> {
                currentCategory = cat.replace("_", " ");
                currentSelectedBuilding = null;
                clearBuildingDetails();
                updateResults();
                mapLabel.repaint();
            });
            buttonPanel.add(btn);
            buttonPanel.add(Box.createVerticalStrut(8));
        }

        // Add zoom buttons at the bottom
        buttonPanel.add(Box.createVerticalGlue());
        
        zoomInBtn = new JButton("+");
zoomInBtn.setFont(new Font("Arial", Font.BOLD, 20)); // Sembol büyüklüğü artırıldı
zoomInBtn.setPreferredSize(new Dimension(80, 30));
zoomInBtn.setMaximumSize(new Dimension(80, 30));
zoomInBtn.setBackground(Color.BLACK);
zoomInBtn.setForeground(Color.BLACK);
zoomInBtn.addActionListener(e -> {
    zoomFactor *= 1.1;
    mapLabel.revalidate();
    mapLabel.repaint();
});

zoomOutBtn = new JButton("-");
zoomOutBtn.setFont(new Font("Arial", Font.BOLD, 20)); // Sembol büyüklüğü artırıldı
zoomOutBtn.setPreferredSize(new Dimension(80, 30));
zoomOutBtn.setMaximumSize(new Dimension(80, 30));
zoomOutBtn.setBackground(Color.BLACK);
zoomOutBtn.setForeground(Color.BLACK);
zoomOutBtn.addActionListener(e -> {
    zoomFactor /= 1.1;
    mapLabel.revalidate();
    mapLabel.repaint();
});


        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(zoomInBtn);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(zoomOutBtn);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.BLACK);

        searchField = new JTextField();
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        searchField.setBackground(Color.WHITE);
        searchField.setForeground(Color.BLACK);
        searchField.setCaretColor(Color.BLACK);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
        });

        buildingFilter = new JComboBox<>();
        buildingFilter.addItem("All");
        for (Building b : buildings) buildingFilter.addItem(b.getCode());
        buildingFilter.setBackground(Color.WHITE);
        buildingFilter.setForeground(Color.BLACK);
        buildingFilter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        buildingFilter.addActionListener(e -> updateResults());

        resultsListModel = new DefaultListModel<>();
        resultsList = new JList<>(resultsListModel);
        resultsList.setBackground(Color.BLACK);
        resultsList.setForeground(Color.WHITE);
        resultsList.setSelectionBackground(Color.DARK_GRAY);
        resultsList.setBorder(null);
        resultsList.addListSelectionListener(e -> showBuildingDetails(resultsList.getSelectedValue()));
        JScrollPane resultScroll = new JScrollPane(resultsList);
        resultScroll.setPreferredSize(new Dimension(60, 70));
        resultScroll.setBorder(null);

        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.BLACK);

        buildingImageLabel = new JLabel();
        buildingImageLabel.setPreferredSize(new Dimension(280, 140));
        buildingDetailsText = new JTextArea();
        buildingDetailsText.setEditable(false);
        buildingDetailsText.setLineWrap(true);
        buildingDetailsText.setWrapStyleWord(true);
        buildingDetailsText.setForeground(Color.WHITE);
        buildingDetailsText.setBackground(Color.BLACK);
        JScrollPane infoScroll = new JScrollPane(buildingDetailsText);
        infoScroll.setPreferredSize(new Dimension(280, 120));
        infoScroll.setBorder(BorderFactory.createEmptyBorder());

        navigateButton = new JButton("Git");
navigateButton.setMaximumSize(new Dimension(280, 30));
navigateButton.setAlignmentX(Component.LEFT_ALIGNMENT);
navigateButton.addActionListener(e -> {
    if (currentSelectedBuilding != null) {
        try {
            String url = switch (currentSelectedBuilding.getCode()) {
                case "EE" -> "https://www.google.com/maps/place/Department+of+Electrical+and+Electronics+Engineering/@39.8720666,32.7506535,18z";
                case "B" -> "https://www.google.com/maps/place/Bilkent+University+Faculty+of+Law/@39.8705112,32.7487016,18z";
                case "EA" -> "https://www.google.com/maps/place/Bilkent+University+Faculty+of+Engineering/@39.8722382,32.7491635,18z";
                case "parking_gsf" -> "https://www.google.com/maps/place/Bilkent+University+Faculty+of+Art,+Design+and+Architecture/@39.8658995,32.7489042,18z";
                case "stad" -> "https://www.google.com/maps/place/Bilkent+Stadium/@39.8642296,32.7432825,18z";
                case "meteksan_ee" -> "https://www.google.com/maps/place/BilMarket/@39.8725141,32.7513408,18z";
                case "coffeebreak_merk" -> "https://www.google.com/maps/place/Coffee+Break/@39.8717686,32.7498181,18z";
                default -> null;
            };
            if (url != null) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                JOptionPane.showMessageDialog(this, "Bu bina için bağlantı bulunamadı.");
            }
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Harita bağlantısı açılamadı.");
        }
    }
});


        infoPanel.add(buildingImageLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(infoScroll);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(navigateButton);

        contentPanel.add(searchField);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(buildingFilter);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(resultScroll);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(infoPanel);

        rightPanel.add(buttonPanel, BorderLayout.WEST);
        rightPanel.add(contentPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        updateResults();
    }

    private void clearBuildingDetails() {
        buildingImageLabel.setIcon(null);
        buildingDetailsText.setText("");
    }

    private void drawBuildingMarkers(Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        for (Building b : buildings) {
            if (!visibleBuildingCodes.contains(b.getCode())) continue;
            int x = (int)(b.getLocation().x * zoomFactor);
            int y = (int)(b.getLocation().y * zoomFactor);

            Color color = switch (b.getServiceType().toLowerCase()) {
                case "cafes" -> Color.ORANGE;
                case "restaurants" -> Color.GREEN;
                case "stores" -> Color.BLUE;
                case "sports_areas" -> Color.CYAN;
                case "dormitories" -> Color.MAGENTA;
                case "administrations" -> Color.PINK;
                case "parking_lots" -> Color.GRAY;
                case "auditoriums" -> Color.YELLOW;
                default -> Color.RED;
            };

            g2d.setColor(color);
            g2d.fillOval(x - 6, y - 6, 12, 12);
        }
        g2d.dispose();
    }

    private void initializeBuildings() {
        buildings = new ArrayList<>();
        buildings.add(new Building("EE", "Electrical Engineering", "Buildings", new Point(700, 50), "Labs, Offices, Classrooms"));
        buildings.add(new Building("B", "Faculty of Law", "Buildings", new Point(260,700), "Law Faculty with classrooms and offices"));
        buildings.add(new Building("EA", "Faculty of Engineering", "Buildings", new Point(550, 250), "Engineering Faculty with labs and offices"));
        buildings.add(new Building("coffeebreak_merk", "Coffee Break Merkez", "Cafes", new Point(500, 950), "Main Campus Cafe"));
        buildings.add(new Building("parking_gsf", "GSF Parking", "Parking_Lots", new Point(900, 1000), "Parking lot near Fine Arts"));
        buildings.add(new Building("stad", "Stadium", "Sports_Areas", new Point(900, 700), "Football Stadium"));
        buildings.add(new Building("meteksan_ee", "Meteksan", "Stores", new Point(320,1150), "Convenience store near EE"));
    }

    private void handleMapClick(Point click) {
        for (Building b : buildings) {
            if (!visibleBuildingCodes.contains(b.getCode())) continue;
            int x = (int)(b.getLocation().x * zoomFactor);
            int y = (int)(b.getLocation().y * zoomFactor);
            Rectangle hitBox = new Rectangle(x - 8, y - 8, 16, 16);
            if (hitBox.contains(click)) {
                showBuildingDetails(b);
                return;
            }
        }
    }

    private void updateResults() {
        resultsListModel.clear();
        visibleBuildingCodes.clear();
        String codeFilter = (String) buildingFilter.getSelectedItem();
        String query = searchField.getText().trim().toLowerCase();

        for (Building b : buildings) {
            boolean matchesFilter = codeFilter.equals("All") || b.getCode().equalsIgnoreCase(codeFilter);
            boolean matchesCategory = currentCategory.equals("All") || b.getServiceType().equalsIgnoreCase(currentCategory.replace(" ", "_"));
            boolean matchesSearch = query.isEmpty() ||
                b.getName().toLowerCase().contains(query) ||
                b.getCode().toLowerCase().contains(query);

            if (matchesFilter && matchesCategory && matchesSearch) {
                resultsListModel.addElement(b);
                visibleBuildingCodes.add(b.getCode());
            }
        }

        if (resultsListModel.isEmpty()) {
            resultsListModel.addElement(new Building("NA", "No results found", "", new Point(0, 0), ""));
        }

        mapLabel.repaint();
    }

    private void showBuildingDetails(Building b) {
    if (b == null || b.getCode().equals("NA")) return;
    currentSelectedBuilding = b;

    // Resmi JLabel boyutuna göre ölçekle
    if (b.getImage() != null) {
        int labelWidth = buildingImageLabel.getWidth();
        int labelHeight = buildingImageLabel.getHeight();

        if (labelWidth > 0 && labelHeight > 0) {
            Image scaledImage = b.getImage().getImage().getScaledInstance(
                labelWidth, labelHeight, Image.SCALE_SMOOTH);
            buildingImageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            Image scaledImage = b.getImage().getImage().getScaledInstance(
                280, 140, Image.SCALE_SMOOTH);
            buildingImageLabel.setIcon(new ImageIcon(scaledImage));
        }
    } else {
        buildingImageLabel.setIcon(null);
    }

    buildingDetailsText.setText("Code: " + b.getCode() + "\n" +
        "Name: " + b.getName() + "\n" +
        "Service: " + b.getServiceType() + "\n\n" +
        "Details:\n" + b.getDetails());
}


    private class Building {
        private String code, name, serviceType, details;
        private Point location;
        private ImageIcon image;

        public Building(String code, String name, String serviceType, Point location, String details) {
            this.code = code;
            this.name = name;
            this.serviceType = serviceType;
            this.location = location;
            this.details = details;
            try {
                image = new ImageIcon(ImageIO.read(new File("building_images/" + code + ".jpeg")));
            } catch (Exception e) {
                image = null;
            }
        }

        public String getCode() { return code; }
        public String getName() { return name; }
        public String getServiceType() { return serviceType; }
        public String getDetails() { return details; }
        public Point getLocation() { return location; }
        public ImageIcon getImage() { return image; }
        public String toString() { return name + " [" + code + "]"; }
    }
}