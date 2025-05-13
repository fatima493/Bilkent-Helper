// CampusMap.java - Main application class matching the UML diagram with side panel and back navigation
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

public class CampusMap extends JFrame {
    private String currentLocation;
    private List<Building> buildings;
    private MapPage mapPage;

    public CampusMap() {
        setTitle("Bilkent University Campus Map");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        buildings = initializeBuildings();
        currentLocation = "Main Gate";

        mapPage = new MapPage(this, buildings);
        add(mapPage);

        setVisible(true);
    }

    private List<Building> initializeBuildings() {
        List<Building> buildings = new ArrayList<>();

        buildings.add(new Building("A", "Faculty of Economics, Administrative, and Social Sciences",
                new String[]{"Economics", "Political Science", "International Relations"},
                new String[]{"Registrar", "Student Affairs"}));

        buildings.add(new Building("B", "Faculty of Law",
                new String[]{"Law"},
                new String[]{"Legal Clinic"}));

        buildings.add(new Building("EA", "Faculty of Engineering",
                new String[]{"Computer Engineering", "Electrical-Electronics Engineering", "Mechanical Engineering"},
                new String[]{"Computer Labs", "Engineering Library"}));

        buildings.add(new Building("KM", "Main Library",
                new String[]{},
                new String[]{"Book Loans", "Study Spaces", "Computer Lab"}));

        buildings.add(new Building("ODN", "Bilkent ODEON",
                new String[]{},
                new String[]{"Concert Hall", "Theater"}));

        return buildings;
    }

    public Building searchBuilding(String name) {
        for (Building building : buildings) {
            if (building.getName().equalsIgnoreCase(name) ||
                    building.getCode().equalsIgnoreCase(name)) {
                return building;
            }
        }
        return null;
    }

    public Path getDirections(String from, String to) {
        Building fromBuilding = searchBuilding(from);
        Building toBuilding = searchBuilding(to);

        if (fromBuilding == null || toBuilding == null) {
            return null;
        }

        Path path = new Path();
        path.addPoint(fromBuilding.getLocation());
        path.addPoint(toBuilding.getLocation());
        return path;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String location) {
        this.currentLocation = location;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CampusMap());
    }
}

class Building {
    private String code;
    private String name;
    private String[] departments;
    private String[] services;
    private Point location;
    private String details;

    public Building(String code, String name, String[] departments, String[] services) {
        this.code = code;
        this.name = name;
        this.departments = departments;
        this.services = services;
        this.location = new Point((int)(Math.random() * 500), (int)(Math.random() * 500));
        this.details = String.format("%s (%s)\nDepartments: %s\nServices: %s",
                name, code, String.join(", ", departments), String.join(", ", services));
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String[] getDepartments() { return departments; }
    public String[] getServices() { return services; }
    public Point getLocation() { return location; }
    public String getDetails() { return details; }
    public void setLocation(Point location) { this.location = location; }
}

class Path {
    private List<Point> points;

    public Path() {
        points = new ArrayList<>();
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public List<Point> getPoints() {
        return points;
    }

    public void draw(Graphics g) {
        if (points.size() < 2) return;
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(3));
        Point prev = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            Point current = points.get(i);
            g2d.drawLine(prev.x, prev.y, current.x, current.y);
            prev = current;
        }
        g2d.dispose();
    }
}

class MapPage extends JPanel {
    private CampusMap campusMap;
    private List<Building> buildings;
    private JTextField searchField;
    private JLabel mapLabel;
    private Image mapImage;
    private JTextArea buildingDetails;
    private Path currentPath;
    private JPanel sidePanel;

    public MapPage(CampusMap campusMap, List<Building> buildings) {
        this.campusMap = campusMap;
        this.buildings = buildings;
        setLayout(new BorderLayout());

        try {
            mapImage = ImageIO.read(new File("backgrounds/map.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        createUI();
    }

    private void createUI() {
        mapLabel = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (mapImage != null) g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
                drawBuildings(g);
                if (currentPath != null) currentPath.draw(g);
            }
        };
        mapLabel.setHorizontalAlignment(JLabel.CENTER);
        add(new JScrollPane(mapLabel), BorderLayout.CENTER);

        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(300, 0));
        sidePanel.setBackground(Color.DARK_GRAY);

        JPanel searchPanel = new JPanel();
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchBuilding());
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel navPanel = new JPanel();
        JTextField fromField = new JTextField(campusMap.getCurrentLocation(), 8);
        JTextField toField = new JTextField(8);
        JButton getDirectionsButton = new JButton("Get Directions");
        getDirectionsButton.addActionListener(e -> {
            Path path = campusMap.getDirections(fromField.getText(), toField.getText());
            if (path != null) {
                currentPath = path;
                mapLabel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Could not find path between locations");
            }
        });
        navPanel.add(new JLabel("From:"));
        navPanel.add(fromField);
        navPanel.add(new JLabel("To:"));
        navPanel.add(toField);
        navPanel.add(getDirectionsButton);

        buildingDetails = new JTextArea(10, 25);
        buildingDetails.setEditable(false);
        JScrollPane detailsScroll = new JScrollPane(buildingDetails);

        JButton backButton = new JButton("Back to Home");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            // In real usage, you could open a new home page JFrame here
        });

        sidePanel.add(searchPanel);
        sidePanel.add(navPanel);
        sidePanel.add(detailsScroll);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(backButton);

        add(sidePanel, BorderLayout.EAST);
    }

    private void searchBuilding() {
        String searchText = searchField.getText();
        Building found = campusMap.searchBuilding(searchText);
        if (found != null) {
            buildingDetails.setText(found.getDetails());
            mapLabel.scrollRectToVisible(new Rectangle(found.getLocation()));
        } else {
            JOptionPane.showMessageDialog(this, "Building not found: " + searchText);
        }
    }

    private void drawBuildings(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Building building : buildings) {
            Point loc = building.getLocation();
            g2d.setColor(Color.BLUE);
            g2d.fillOval(loc.x - 5, loc.y - 5, 10, 10);
            g2d.setColor(Color.WHITE);
            g2d.drawString(building.getCode(), loc.x + 8, loc.y + 5);
        }

        g2d.dispose();
    }
}
