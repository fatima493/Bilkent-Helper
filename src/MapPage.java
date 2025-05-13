
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

public class MapPage extends BackgroundPanel {
    private JTextField searchField;
    private JButton openPdfButton;
    private JLabel mapLabel;
    private Image mapImage;
    private List<Building> buildings;
    private JTextArea buildingDetails;
    private JPanel sidePanel;
    private JButton toggleButton;
    private boolean isSidePanelVisible = true;
    private JButton navigateButton; // Added for navigation

    private ImageIcon goLogo;
    private ImageIcon goBackLogo;

    // Added for user location tracking
    private Point userLocation = null;
    private JButton locateMeButton;

    private String[] buildingNames = {
            "A", "B", "C", "D", "EA", "EB", "EE", "F", "G", "H", "J",
            "KM", "L", "M", "N", "P", "R", "S", "SC", "SI", "SL", "SM",
            "SN", "ST", "SU", "U", "V", "Y", "ODN"
    };

    private String[] serviceTypes = {
            "All", "Buildings", "Cafes", "Restaurants", "Parking Lots", "Stores",
            "Administrations", "Dormitories", "Sports Areas", "Library", "Auditoriums"
    };

    // Added these fields to track current filters
    private String selectedBuilding = "All Buildings";
    private String selectedService = "All";

    public MapPage(AppFrame frame) {
        setLayout(new BorderLayout());
        initializeBuildings();

        // Load map image
        try {
            mapImage = ImageIO.read(new File("backgrounds/map.png"));
        } catch (Exception e) {
            System.out.println("Map image could not be loaded.");
            e.printStackTrace();
        }

        // Load toggle icons
        try {
            goLogo = new ImageIcon("backgrounds/go-logo.png");
            goBackLogo = new ImageIcon("backgrounds/go-back-logo.png");
        } catch (Exception e) {
            System.out.println("Toggle icons could not be loaded.");
            e.printStackTrace();
            goLogo = null;
            goBackLogo = null;
        }

        // Main content area
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        // Map display area
        mapLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (mapImage != null) {
                    g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
                }
                drawBuildingMarkers(g);
                drawUserLocation(g); // Draw user's location
            }
        };
        mapLabel.setHorizontalAlignment(JLabel.CENTER);
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
        sidePanel.setPreferredSize(new Dimension(300, 0));
        sidePanel.setOpaque(false);

        // Search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setOpaque(false);

        searchField = new JTextField();
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { filterBuildings(); }
            public void removeUpdate(DocumentEvent e) { filterBuildings(); }
            public void insertUpdate(DocumentEvent e) { filterBuildings(); }
        });

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchPanel.add(searchLabel);
        searchPanel.add(Box.createVerticalStrut(5));
        searchPanel.add(searchField);
        searchPanel.add(Box.createVerticalStrut(15));

        // Added Locate Me button
        locateMeButton = new JButton("Locate Me");
        locateMeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        locateMeButton.addActionListener(e -> {
            // Simulate getting user's location
            userLocation = new Point(300, 200); // Default location (EA Building)
            mapLabel.repaint();
            JOptionPane.showMessageDialog(MapPage.this, "Your location has been set to EA Building");
        });
        searchPanel.add(locateMeButton);
        searchPanel.add(Box.createVerticalStrut(15));

        // Filter panel with buttons for service types
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setOpaque(false);

        JLabel buildingLabel = new JLabel("Building:");
        buildingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JComboBox<String> buildingFilter = new JComboBox<>(buildingNames);
        buildingFilter.insertItemAt("All Buildings", 0);
        buildingFilter.setSelectedIndex(0);
        buildingFilter.setMaximumSize(new Dimension(Integer.MAX_VALUE, buildingFilter.getPreferredSize().height));
        buildingFilter.addActionListener(e -> {
            selectedBuilding = (String) buildingFilter.getSelectedItem();
            filterBuildings();
        });

        // Service buttons arranged in a grid (2 columns)
        JPanel serviceButtonsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        serviceButtonsPanel.setOpaque(false);
        ButtonGroup serviceButtonGroup = new ButtonGroup();

        for (String serviceType : serviceTypes) {
            JToggleButton serviceButton = new JToggleButton(serviceType);
            serviceButton.setActionCommand(serviceType);
            serviceButton.addActionListener(e -> {
                selectedService = serviceButton.getActionCommand();
                filterBuildings();
            });
            serviceButtonGroup.add(serviceButton);
            serviceButtonsPanel.add(serviceButton);

            // Select the "All" button by default
            if (serviceType.equals("All")) {
                serviceButton.setSelected(true);
            }
        }

        filterPanel.add(buildingLabel);
        filterPanel.add(Box.createVerticalStrut(5));
        filterPanel.add(buildingFilter);
        filterPanel.add(Box.createVerticalStrut(10));
        filterPanel.add(new JLabel("Service:"));
        filterPanel.add(Box.createVerticalStrut(5));
        filterPanel.add(serviceButtonsPanel);
        filterPanel.add(Box.createVerticalStrut(15));

        // PDF button
        openPdfButton = new JButton("Open Full Map PDF");
        openPdfButton.setAlignmentX(Component.LEFT_ALIGNMENT);
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
        detailsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        buildingDetails = new JTextArea();
        buildingDetails.setEditable(false);
        buildingDetails.setLineWrap(true);
        buildingDetails.setWrapStyleWord(true);
        buildingDetails.setFont(new Font("Arial", Font.PLAIN, 14));
        buildingDetails.setText("Select a building to view details");
        JScrollPane detailsScrollPane = new JScrollPane(buildingDetails);
        detailsScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Added Navigate button
        navigateButton = new JButton("Navigate to Location");
        navigateButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        navigateButton.addActionListener(e -> {
            String selectedDetails = buildingDetails.getText();
            Building target = getBuildingFromDetails(selectedDetails);
            if (target != null) {
                openInMaps(target);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a building to navigate.");
            }
        });

        // Add to side panel
        sidePanel.add(searchPanel);
        sidePanel.add(filterPanel);
        sidePanel.add(openPdfButton);
        sidePanel.add(Box.createVerticalStrut(15));
        sidePanel.add(detailsLabel);
        sidePanel.add(Box.createVerticalStrut(5));
        sidePanel.add(detailsScrollPane);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(navigateButton);

        // Add side panel to content panel
        contentPanel.add(sidePanel, BorderLayout.EAST);
        add(contentPanel, BorderLayout.CENTER);
    }

    // Added method to open in Google Maps
    private void openInMaps(Building building) {
        try {
            String locationQuery = building.getName().replace(" ", "+") + "+Bilkent+University";
            URI mapUri = new URI("https://www.google.com/maps/search/?api=1&query=" + locationQuery);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(mapUri);
            } else {
                JOptionPane.showMessageDialog(this, "Desktop browsing not supported.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not open Maps.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Added method to find building from details text
    private Building getBuildingFromDetails(String detailsText) {
        if (detailsText == null || detailsText.trim().isEmpty()) return null;
        for (Building b : buildings) {
            if (detailsText.contains(b.getName()) || detailsText.contains(b.getCode())) {
                return b;
            }
        }
        return null;
    }

    // Added method to draw user location
    private void drawUserLocation(Graphics g) {
        if (userLocation != null && mapImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double scaleX = (double) mapLabel.getWidth() / mapImage.getWidth(null);
            double scaleY = (double) mapLabel.getHeight() / mapImage.getHeight(null);

            int x = (int) (userLocation.x * scaleX);
            int y = (int) (userLocation.y * scaleY);

            // Draw a blue dot for user location
            g2d.setColor(Color.BLUE);
            g2d.fillOval(x - 10, y - 10, 20, 20);

            // Draw a white border around the dot
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(x - 10, y - 10, 20, 20);

            g2d.dispose();
        }
    }

    private void toggleSidePanel() {
        isSidePanelVisible = !isSidePanelVisible;
        sidePanel.setVisible(isSidePanelVisible);

        if (goLogo != null && goBackLogo != null) {
            toggleButton.setIcon(isSidePanelVisible ? goBackLogo : goLogo);
        } else {
            toggleButton.setText(isSidePanelVisible ? "Hide Panel" : "Show Panel");
        }

        revalidate();
        repaint();
    }

    private void initializeBuildings() {
        buildings = new ArrayList<>();

        buildings.add(new Building("A", "Faculty of Economics, Administrative, and Social Sciences", "Buildings",
                new Point(100, 150), "Services: Registrar, Student Affairs\nContacts: registrar@bilkent.edu.tr"));
        buildings.add(new Building("B", "Faculty of Law", "Buildings",
                new Point(100, 200), "Services: ..."));

        //Doğu Kampüs
        /*buildings.add(new Building("C", "English Language Preparatory Program", "Buildings",
                new Point(100, 300), "Services:);
        buildings.add(new Building("D", "English Language Preparatory Program", "Buildings",
                new Point(100, 400), "Services:);*/

        buildings.add(new Building("EA", "Faculty of Engineering", "Academic",
                new Point(250, 180), "Departments: Computer Science\nContacts: cs@bilkent.edu.tr"));
        buildings.add(new Building("EB", "Mithat Çoruh Auditorium and Classrooms", "Auditorium",
                new Point(200, 180), "Service:... \nContacts: cs@bilkent.edu.tr"));
        buildings.add(new Building("EE", "Electrical- Electronics Engineering", "Buildings",
                new Point(150, 200), "Departments: Electrical-Electronics Engineering\nContacts: ee@bilkent.edu.tr"));
        buildings.add(new Building("F", "Faculty of Art, Design, and Architecture (Blocks A-B-C-D-F)", "Buildings",
                new Point(200, 300), "Services: Book loans, study spaces\nOpening hours: 8:00-22:00"));
        buildings.add(new Building("G", "Faculty of Education", "Buildings",
                new Point(200, 250), "Services: Book loans, study spaces\nOpening hours: 8:00-22:00"));
        buildings.add(new Building("H", "Faculty of Humanities and Letters", "Buildings",
                new Point(200, 260), "Services: Book loans, study spaces\nOpening hours: 8:00-22:00"));
        //lojmanlar
        /*buildings.add(new Building("J", "Faculty Housing", "Dormitories",
                new Point(200, 270), "Services: Book loans, study spaces\nOpening hours: 8:00-22:00"));*/

        buildings.add(new Building("KM", "Library (Main Campus)", "Library",
                new Point(580, 600), "Services: Book loans, study spaces\nOpening hours: 8:00-22:00"));
        buildings.add(new Building("L", "Department of Translation and Interpretation (Blocks A-B-C)", "Buildings",
                new Point(200, 290), "Services: Book loans, study spaces\nOpening hours: 8:00-22:00"));
        buildings.add(new Building("M", "Faculty of Business Administration", "Buildings",
                new Point(150, 150), "Services: Book loans, study spaces\nOpening hours: 8:00-22:00"));

        //Doğu kampüs
        /*buildings.add(new Building("N", "English Language Preparatory Program (Blocks A-B-C)", "Buildings" + "Library",
                new Point(100, 300), "Services:..."));*/
        //MSSF
        /*buildings.add(new Building("P", "Faculty of Music and performing Arts", "Buildings",
                new Point(300, 100), "Services:..."));*/

        //Doğu
        /*buildings.add(new Building("R", "Faculty of Applied Sciences (Blocks A-B-C-D-E)", "Buildings",
                new Point(100, 300), "Services:..."));*/

        buildings.add(new Building("S", "Faculty of Science (Blocks A-B)", "Buildings",
                new Point(100, 300), "Services:..."));

        //Bilmarketin ilerisinde
        /*buildings.add(new Building("SC", "Aysel Sabuncu Brain Research Center", "Buildings",
                new Point(100, 300), "Services:..."));

         buildings.add(new Building("SI", "Communication and Spectrum Management Research Center", "Buildings",
                new Point(100, 300), "Services:..."));*/

        buildings.add(new Building("SL", "Advanced Research Laboratory", "Buildings",
                new Point(300, 200), "Services:..."));
        buildings.add(new Building("SM", "Nanotechnology Research Center", "Buildings",
                new Point(100, 300), "Services:..."));
        buildings.add(new Building("SN", "Nanotechnology Research Center", "Buildings",
                new Point(100, 300), "Services:..."));
        buildings.add(new Building("ST", "AB-MicroNano Technologies Industry and Trade Inc.", "Buildings",
                new Point(100, 300), "Services:..."));
        buildings.add(new Building("SU", "Institute of Materials Science and Nanotechnology", "Buildings",
                new Point(100, 300), "Services:..."));
        buildings.add(new Building("U", "Dean of Students' Office", "Buildings",
                new Point(100, 300), "Services:..."));
        buildings.add(new Building("V", "Lecture Halls", "Buildings",
                new Point(100, 300), "Services:..."));

        //yurtlar
        /*buildings.add(new Building("Y", "Student Dormitories", "Dormitories",
                new Point(100, 300), "Services:..."));*/

        buildings.add(new Building("ODN", "Bilkent ODEON", "Auditoriums",
                new Point(100, 300), "Services:..."));

    }

    private void filterBuildings() {
        String searchText = searchField.getText().toLowerCase();

        for (Building b : buildings) {
            boolean matches = true;

            // Check if building matches the selected filter
            if (!selectedBuilding.equals("All Buildings") && !b.getCode().equals(selectedBuilding)) {
                matches = false;
            }
            if (!selectedService.equals("All") && !b.getServiceType().equals(selectedService)) {
                matches = false;
            }
            // Check if building name/code matches search text
            if (!searchText.isEmpty() &&
                    !b.getName().toLowerCase().contains(searchText) &&
                    !b.getCode().toLowerCase().contains(searchText)) {
                matches = false;
            }

            b.setVisible(matches);
        }

        mapLabel.repaint();
    }

    private void filterBuildingsByService(String serviceType) {
        selectedService = serviceType;
        filterBuildings();
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

                g2d.setColor(new Color(0, 100, 255, 200));
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
            int x = (int) (b.getLocation().x * scaleX);
            int y = (int) (b.getLocation().y * scaleY);

            if (clickPoint.distance(x, y) < 15) {
                showBuildingDetails(b);
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
        String details = String.format(
                "Building Code: %s\nName: %s\nType: %s\nLocation: X=%d, Y=%d\n\nDetails:\n%s",
                building.getCode(),
                building.getName(),
                building.getServiceType(),
                building.getLocation().x,
                building.getLocation().y,
                building.getDetails()
        );
        buildingDetails.setText(details);
    }

    private class Building {
        private String code;
        private String name;
        private String serviceType;
        private Point location;
        private String details;
        private boolean visible;

        public Building(String code, String name, String serviceType, Point location, String details) {
            this.code = code;
            this.name = name;
            this.serviceType = serviceType;
            this.location = location;
            this.details = details;
            this.visible = true;
        }

        public String getCode() { return code; }
        public String getName() { return name; }
        public String getServiceType() { return serviceType; }
        public Point getLocation() { return location; }
        public String getDetails() { return details; }
        public boolean isVisible() { return visible; }
        public void setVisible(boolean visible) { this.visible = visible; }
    }
}
