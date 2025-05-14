import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import java.util.List;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MapPage extends JPanel {
    private AppFrame parentFrame;
    private JTextField searchField;
    private JButton openPdfButton;
    private JLabel mapLabel;
    private Image mapImage;
    private List<Building> buildings;
    private JPanel sidePanel;
    private JButton navigateButton;
    private JButton locateMeButton;
    private JButton backToMainButton;
    private JComboBox<String> buildingFilter;
    private ButtonGroup serviceButtonGroup;

    private Point userLocation = null;
    private Building selectedBuilding = null;

    /*private String[] buildingNames = {
        "A", "B", "C", "D", "EA", "EB", "EE", "F", "G", "H", "J",
        "KM", "L", "M", "N", "P", "R", "S", "SC", "SI", "SL", "SM",
        "SN", "ST", "SU", "U", "V", "Y", "ODN"
    };*/

    private String[] buildingNames = {
        "B","EE", "F","M", "S", "V", "ODN"
    };

    private String[] serviceTypes = {
        "All", "Buildings", "Cafes", "Restaurants", "Parking Lots", "Stores",
        "Administrations", "Dormitories", "Sports Areas", "Library", "Auditoriums"
    };

    public MapPage(AppFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initializeBuildings();

        // Load map image
        try {
            mapImage = ImageIO.read(new File("backgrounds/map.png"));
        } catch (Exception e) {
            System.err.println("Error loading map image: " + e.getMessage());
            mapImage = null;
        }

        // Main content area
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // Map display area
        mapLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (mapImage != null) {
                    g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
                }
                drawBuildingMarkers(g);
                drawUserLocation(g);
                if (selectedBuilding != null) {
                    drawSelectedBuilding(g);
                }
            }
        };
        mapLabel.setHorizontalAlignment(JLabel.CENTER);
        mapLabel.setBackground(Color.WHITE);
        mapLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMapClick(e.getPoint());
            }
        });

        contentPanel.add(new JScrollPane(mapLabel), BorderLayout.CENTER);

        // Side panel (right side)
        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidePanel.setPreferredSize(new Dimension(250, 0));
        sidePanel.setBackground(Color.WHITE);

        // Back to Main button
        backToMainButton = new JButton("← Back to Main");
        backToMainButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backToMainButton.setForeground(Color.BLACK);
        backToMainButton.setBackground(Color.WHITE);
        backToMainButton.setBorder(BorderFactory.createEmptyBorder());
        backToMainButton.addActionListener(e -> parentFrame.showPage("main"));
        sidePanel.add(backToMainButton);
        sidePanel.add(Box.createVerticalStrut(10));

        // Search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField();
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height));
        searchField.setForeground(Color.BLACK);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { filterBuildings(); }
            public void removeUpdate(DocumentEvent e) { filterBuildings(); }
            public void insertUpdate(DocumentEvent e) { filterBuildings(); }
        });

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.BLACK);
        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchPanel.add(searchLabel);
        searchPanel.add(Box.createVerticalStrut(5));
        searchPanel.add(searchField);
        searchPanel.add(Box.createVerticalStrut(15));

        // Filter panel
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(Color.WHITE);

        JLabel buildingLabel = new JLabel("Building:");
        buildingLabel.setForeground(Color.BLACK);
        buildingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        buildingFilter = new JComboBox<>(buildingNames);
        buildingFilter.insertItemAt("All Buildings", 0);
        buildingFilter.setSelectedIndex(0);
        buildingFilter.setMaximumSize(new Dimension(Integer.MAX_VALUE, buildingFilter.getPreferredSize().height));
        buildingFilter.setForeground(Color.BLACK);
        buildingFilter.addActionListener(e -> {
            String selected = (String) buildingFilter.getSelectedItem();
            if (!"All Buildings".equals(selected)) {
                selectBuildingByCode(selected);
            } else {
                selectedBuilding = null;
                showBuildingDetails(null);
                mapLabel.repaint();
            }
        });

        // Service buttons
        JPanel serviceButtonsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        serviceButtonsPanel.setBackground(Color.WHITE);
        serviceButtonGroup = new ButtonGroup();

        for (String serviceType : serviceTypes) {
            JToggleButton serviceButton = new JToggleButton(serviceType);
            serviceButton.setBackground(Color.WHITE);
            serviceButton.setForeground(Color.BLACK);
            serviceButton.setActionCommand(serviceType);
            serviceButton.addActionListener(e -> filterBuildings());
            serviceButtonGroup.add(serviceButton);
            serviceButtonsPanel.add(serviceButton);

            if (serviceType.equals("All")) {
                serviceButton.setSelected(true);
            }
        }

        filterPanel.add(buildingLabel);
        filterPanel.add(Box.createVerticalStrut(5));
        filterPanel.add(buildingFilter);
        filterPanel.add(Box.createVerticalStrut(10));
        
        JLabel serviceLabel = new JLabel("Service:");
        serviceLabel.setForeground(Color.BLACK);
        filterPanel.add(serviceLabel);
        filterPanel.add(Box.createVerticalStrut(5));
        filterPanel.add(serviceButtonsPanel);
        filterPanel.add(Box.createVerticalStrut(15));

        // PDF button
        openPdfButton = new JButton("Open Full Map PDF");
        openPdfButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        openPdfButton.setForeground(Color.BLACK);
        openPdfButton.addActionListener(e -> {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(new File("maps/full_campus_map.pdf"));
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Could not open PDF file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Building details
        JLabel detailsLabel = new JLabel("Building Details:");
        detailsLabel.setForeground(Color.BLACK);
        detailsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create initial empty details panel
        JPanel initialDetailsPanel = new JPanel();
        initialDetailsPanel.setLayout(new BoxLayout(initialDetailsPanel, BoxLayout.X_AXIS));
        initialDetailsPanel.add(new JLabel("Select a building to view details"));
        JScrollPane detailsScrollPane = new JScrollPane(initialDetailsPanel);
        detailsScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Navigate button
        navigateButton = new JButton("Navigate to Location");
        navigateButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        navigateButton.setForeground(Color.BLACK);
        navigateButton.addActionListener(e -> {
            if (selectedBuilding != null) {
                openInMaps(selectedBuilding);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a building first");
            }
        });

        // Add components to side panel
        sidePanel.add(searchPanel);
        sidePanel.add(filterPanel);
        sidePanel.add(openPdfButton);
        sidePanel.add(Box.createVerticalStrut(15));
        sidePanel.add(detailsLabel);
        sidePanel.add(Box.createVerticalStrut(5));
        sidePanel.add(detailsScrollPane);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(navigateButton);

        contentPanel.add(sidePanel, BorderLayout.EAST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void selectBuildingByCode(String code) {
        for (Building b : buildings) {
            if (b.getCode().equals(code)) {
                selectedBuilding = b;
                showBuildingDetails(b);
                mapLabel.repaint();
                return;
            }
        }
        selectedBuilding = null;
        showBuildingDetails(null);
    }

    private void openInMaps(Building building) {
        try {
            // Coordinates for Bilkent University (approximate center)
            double latitude = 39.8688;
            double longitude = 32.7501;
            
            // Adjust coordinates based on building
            switch(building.getCode()) {
                case "A":
                    latitude = 39.8690; longitude = 32.7505;
                    break;
                case "B":
                    latitude = 39.8685; longitude = 32.7500;
                    break;
                case "EA":
                    latitude = 39.8695; longitude = 32.7495;
                    break;
                case "KM":
                    latitude = 39.8670; longitude = 32.7520;
                    break;
                // Add more buildings as needed
                default:
                    // Use default coordinates
            }
            
            String uriString = String.format("https://www.google.com/maps/search/?api=1&query=%.6f,%.6f(%s)", 
                                          latitude, longitude, 
                                          building.getName().replace(" ", "+"));
            
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(uriString));
            } else {
                // Fallback: show URL that can be copied
                JOptionPane.showMessageDialog(this, 
                    "Google Maps URL:\n" + uriString, 
                    "Navigation", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error opening maps: " + e.getMessage(), 
                "Navigation Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void drawSelectedBuilding(Graphics g) {
        if (selectedBuilding == null || mapImage == null) return;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double scaleX = (double) mapLabel.getWidth() / mapImage.getWidth(null);
        double scaleY = (double) mapLabel.getHeight() / mapImage.getHeight(null);

        int x = (int) (selectedBuilding.getLocation().x * scaleX);
        int y = (int) (selectedBuilding.getLocation().y * scaleY);

        // Draw a highlighted circle around selected building
        g2d.setColor(new Color(255, 255, 0, 150));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(x - 15, y - 15, 30, 30);

        g2d.dispose();
    }

    private void drawUserLocation(Graphics g) {
        if (userLocation != null && mapImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double scaleX = (double) mapLabel.getWidth() / mapImage.getWidth(null);
            double scaleY = (double) mapLabel.getHeight() / mapImage.getHeight(null);

            int x = (int) (userLocation.x * scaleX);
            int y = (int) (userLocation.y * scaleY);

            g2d.setColor(Color.BLUE);
            g2d.fillOval(x - 10, y - 10, 20, 20);

            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(x - 10, y - 10, 20, 20);

            g2d.dispose();
        }
    }

    private void initializeBuildings() {
        buildings = new ArrayList<>();

        // Academic Buildings
        /*buildings.add(new Building("A", "Faculty of Economics, Administrative, and Social Sciences", "Buildings",
                new Point(100, 150), "Services: Registrar, Student Affairs\nContacts: registrar@bilkent.edu.tr"));
        buildings.add(new Building("B", "Faculty of Law", "Buildings",
                new Point(100, 200), "Services: Law library, classrooms"));
        buildings.add(new Building("EA", "Faculty of Engineering", "Buildings",
                new Point(250, 180), "Departments: Computer Science\nContacts: cs@bilkent.edu.tr"));
        buildings.add(new Building("EB", "Mithat Çoruh Auditorium", "Auditoriums",
                new Point(200, 180), "Large auditorium for events"));
        buildings.add(new Building("EE", "Electrical-Electronics Engineering", "Buildings",
                new Point(150, 200), "EE Department offices and labs"));
        buildings.add(new Building("F", "Faculty of Art, Design and Architecture", "Buildings",
                new Point(200, 300), "Art studios and design labs"));
        buildings.add(new Building("G", "Faculty of Education", "Buildings",
                new Point(200, 250), "Education department"));
        buildings.add(new Building("H", "Faculty of Humanities and Letters", "Buildings",
                new Point(200, 260), "Humanities departments"));
        buildings.add(new Building("KM", "Library (Main Campus)", "Library",
                new Point(580, 600), "Services: Book loans, study spaces\nOpening hours: 8:00-22:00"));
        buildings.add(new Building("L", "Department of Translation and Interpretation", "Buildings",
                new Point(200, 290), "Translation department"));
        buildings.add(new Building("M", "Faculty of Business Administration", "Buildings",
                new Point(150, 150), "Business school"));
        buildings.add(new Building("S", "Faculty of Science", "Buildings",
                new Point(100, 300), "Science departments"));
        buildings.add(new Building("SL", "Advanced Research Laboratory", "Buildings",
                new Point(300, 200), "Research labs"));
        buildings.add(new Building("ODN", "Bilkent ODEON", "Auditoriums",
                new Point(100, 300), "Concert hall and events"));*/


         buildings.add(new Building("B", "Faculty of Law", "Buildings",
                new Point(100, 200), "Services: Law library, classrooms"));
        buildings.add(new Building("EE", "Electrical-Electronics Engineering", "Buildings",
                new Point(150, 200), "EE Department offices and labs"));
        buildings.add(new Building("F", "Faculty of Art, Design and Architecture", "Buildings",
                new Point(200, 300), "Art studios and design labs"));
        buildings.add(new Building("M", "Faculty of Business Administration", "Buildings",
                new Point(150, 150), "Business school"));
        buildings.add(new Building("S", "Faculty of Science", "Buildings",
                new Point(100, 300), "Science departments"));

        // Cafes and Restaurants
        buildings.add(new Building("CAFE1", "Main Campus Cafe", "Cafes",
                new Point(400, 400), "Coffee and snacks\nOpen 8:00-20:00"));
        buildings.add(new Building("CAFE2", "Engineering Cafe", "Cafes",
                new Point(260, 190), "Coffee near EA building"));
        buildings.add(new Building("REST1", "Main Restaurant", "Restaurants",
                new Point(500, 500), "Full meals\nOpen 11:00-15:00, 17:00-19:00"));

        // Parking Lots
        buildings.add(new Building("PARK1", "North Parking", "Parking Lots",
                new Point(150, 100), "Main parking area"));
        buildings.add(new Building("PARK2", "Library Parking", "Parking Lots",
                new Point(600, 620), "Near library"));

        // Administrations
        buildings.add(new Building("ADMIN1", "Rectorate", "Administrations",
                new Point(200, 150), "University administration"));
        buildings.add(new Building("ADMIN2", "Student Affairs", "Administrations",
                new Point(180, 160), "Student services"));

        // Sports Areas
        buildings.add(new Building("SPORT1", "Sports Center", "Sports Areas",
                new Point(700, 700), "Gym and sports facilities"));
        buildings.add(new Building("SPORT2", "Outdoor Fields", "Sports Areas",
                new Point(720, 720), "Football and basketball courts"));
    }

    private void filterBuildings() {
        String searchText = searchField.getText().toLowerCase();
        String selectedService = serviceButtonGroup.getSelection().getActionCommand();

        for (Building b : buildings) {
            boolean matches = true;

            // Check service type filter
            if (!selectedService.equals("All") && !b.getServiceType().equals(selectedService)) {
                matches = false;
            }

            // Check search text
            if (!searchText.isEmpty() &&
                    !b.getName().toLowerCase().contains(searchText) &&
                    !b.getCode().toLowerCase().contains(searchText)) {
                matches = false;
            }

            b.setVisible(matches);
        }

        mapLabel.repaint();
    }

    private void drawBuildingMarkers(Graphics g) {
        if (buildings == null || mapImage == null) return;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double scaleX = (double) mapLabel.getWidth() / mapImage.getWidth(null);
        double scaleY = (double) mapLabel.getHeight() / mapImage.getHeight(null);

        for (Building b : buildings) {
            if (b.isVisible()) {
                int x = (int) (b.getLocation().x * scaleX);
                int y = (int) (b.getLocation().y * scaleY);

                // Different colors for different service types
                Color markerColor = Color.BLUE;
                if (b.getServiceType().equals("Cafes")) markerColor = new Color(0, 150, 0); // Green
                else if (b.getServiceType().equals("Restaurants")) markerColor = new Color(200, 0, 0); // Red
                else if (b.getServiceType().equals("Parking Lots")) markerColor = new Color(150, 150, 0); // Yellow
                else if (b.getServiceType().equals("Administrations")) markerColor = new Color(150, 0, 150); // Purple
                else if (b.getServiceType().equals("Sports Areas")) markerColor = new Color(0, 150, 150); // Cyan
                else if (b.getServiceType().equals("Auditoriums")) markerColor = new Color(200, 100, 0); // Orange

                g2d.setColor(markerColor);
                g2d.fillOval(x - 8, y - 8, 16, 16);
                g2d.setColor(Color.WHITE);
                g2d.drawString(b.getCode(), x - 5, y + 5);
            }
        }
        g2d.dispose();
    }

    private void handleMapClick(Point clickPoint) {
        if (buildings == null || mapImage == null) return;

        double scaleX = (double) mapLabel.getWidth() / mapImage.getWidth(null);
        double scaleY = (double) mapLabel.getHeight() / mapImage.getHeight(null);

        for (Building b : buildings) {
            if (!b.isVisible()) continue;

            int x = (int) (b.getLocation().x * scaleX);
            int y = (int) (b.getLocation().y * scaleY);

            if (clickPoint.distance(x, y) < 15) {
                selectedBuilding = b;
                showBuildingDetails(b);
                buildingFilter.setSelectedItem(b.getCode());
                mapLabel.repaint();
                return;
            }
        }

        // Set user location when clicking on empty space
        userLocation = new Point(
                (int)(clickPoint.x / scaleX),
                (int)(clickPoint.y / scaleY)
        );
        mapLabel.repaint();
    }

    private void showBuildingDetails(Building building) {
        // Create a panel to hold both image and text
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        if (building == null) {
            JLabel emptyLabel = new JLabel("Select a building to view details");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            detailsPanel.add(emptyLabel);
        } else {
            if (building.getImage() != null) {
                ImageIcon icon = building.getImage();
                Image scaledImage = icon.getImage().getScaledInstance(280, 140, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                detailsPanel.add(imageLabel);
                detailsPanel.add(Box.createVerticalStrut(10));
            } else {
                JLabel noImageLabel = new JLabel("No image available");
                noImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                detailsPanel.add(noImageLabel);
                detailsPanel.add(Box.createVerticalStrut(10));
            }

            String detailsText = String.format(
                "Building Code: %s\nName: %s\nType: %s\n\nDetails:\n%s",
                building.getCode(),
                building.getName(),
                building.getServiceType(),
                building.getDetails()
            );

            JTextArea textArea = new JTextArea(detailsText);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setFont(new Font("Arial", Font.PLAIN, 14));
            textArea.setBackground(Color.WHITE);
            textArea.setForeground(Color.BLACK);

            JScrollPane textScrollPane = new JScrollPane(textArea);
            textScrollPane.setPreferredSize(new Dimension(280, 160)); // matches side panel width
            textScrollPane.setBorder(BorderFactory.createEmptyBorder());

            detailsPanel.add(textScrollPane);
        }

        JScrollPane detailsScrollPane = (JScrollPane) sidePanel.getComponent(8);
        detailsScrollPane.setViewportView(detailsPanel);
        detailsScrollPane.setPreferredSize(new Dimension(280, 260));
        detailsScrollPane.revalidate();
        detailsScrollPane.repaint();
    }

    private class Building {
        private String code;
        private String name;
        private String serviceType;
        private Point location;
        private String details;
        private boolean visible;
        private ImageIcon image;

        public Building(String code, String name, String serviceType, Point location, String details) {
            this.code = code;
            this.name = name;
            this.serviceType = serviceType;
            this.location = location;
            this.details = details;
            this.visible = true;
            
            // Try to load building image
            try {
                String imagePath = "building_images/" + code + ".jpeg";
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    this.image = new ImageIcon(ImageIO.read(imageFile));
                } else {
                    // Try with different extensions if JPEG not found
                    imagePath = "building_images/" + code + ".jpg";
                    imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        this.image = new ImageIcon(ImageIO.read(imageFile));
                    } else {
                        imagePath = "building_images/" + code + ".png";
                        imageFile = new File(imagePath);
                        if (imageFile.exists()) {
                            this.image = new ImageIcon(ImageIO.read(imageFile));
                        } else {
                            this.image = null;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error loading image for building " + code + ": " + e.getMessage());
                this.image = null;
            }
        }

        public String getCode() { return code; }
        public String getName() { return name; }
        public String getServiceType() { return serviceType; }
        public Point getLocation() { return location; }
        public String getDetails() { return details; }
        public boolean isVisible() { return visible; }
        public void setVisible(boolean visible) { this.visible = visible; }
        public ImageIcon getImage() { return image; }
    }
}