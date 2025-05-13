import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class MapPage extends JPanel {
    private AppFrame parent;
    private JLabel mapLabel;
    private JPanel rightPanel;
    private JTextField searchField;
    private JLabel buildingImageLabel;
    private JTextArea buildingDetailsText;
    private JPanel categoryMenu;
    private HashMap<String, Building> buildingMap;

    public MapPage(AppFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // === LEFT MENU PANEL ===
        categoryMenu = new JPanel();
        categoryMenu.setLayout(new BoxLayout(categoryMenu, BoxLayout.Y_AXIS));
        categoryMenu.setBackground(Color.BLACK);
        categoryMenu.setPreferredSize(new Dimension(80, 0));

        // Add menu buttons (icons only)
        String[] categories = {"Buildings", "Cafes", "Restaurants", "Parking_Lots", "Stores",
                "Dormitories", "Administrations", "Sports_Areas", "Auditoriums"};
        for (String cat : categories) {
            JButton btn = new JButton(new ImageIcon("logos/" + cat.toLowerCase() + ".png"));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setBackground(Color.BLACK);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            btn.setFocusable(false);
            btn.setPreferredSize(new Dimension(70, 70));
            categoryMenu.add(btn);
            categoryMenu.add(Box.createVerticalStrut(5));
        }
        add(categoryMenu, BorderLayout.WEST);

        // === MAIN CONTENT PANEL ===
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.BLACK);

        // === MAP PANEL ===
        mapLabel = new JLabel();
        mapLabel.setLayout(null);
        mapLabel.setBackground(Color.BLACK);

        // Load and scale the map image
        try {
            ImageIcon originalMapIcon = new ImageIcon("background/map.png");
            Image scaledMap = originalMapIcon.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
            mapLabel.setIcon(new ImageIcon(scaledMap));
        } catch (Exception e) {
            mapLabel.setText("Map image not found");
            mapLabel.setForeground(Color.WHITE);
        }

        JScrollPane mapScroll = new JScrollPane(mapLabel);
        mapScroll.setBorder(null);
        mapScroll.getViewport().setBackground(Color.BLACK);
        contentPanel.add(mapScroll, BorderLayout.CENTER);

        // === RIGHT PANEL (Search and Info) ===
        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(300, 0));
        rightPanel.setBackground(Color.BLACK);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // Exit button
        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        exitPanel.setBackground(Color.BLACK);
        JButton exitBtn = new JButton("< EXIT | MAP");
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setBackground(Color.BLACK);
        exitBtn.setBorder(null);
        exitBtn.setFocusable(false);
        exitBtn.addActionListener(e -> parent.showPage("main"));
        exitPanel.add(exitBtn);
        rightPanel.add(exitPanel);

        rightPanel.add(Box.createVerticalStrut(20));

        // Search section
        JLabel searchLabel = new JLabel("Search anywhere:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(searchLabel);

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.BLACK);
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchField = new JTextField();
        searchField.setMaximumSize(new Dimension(200, 30));
        searchField.setPreferredSize(new Dimension(200, 30));

        JButton searchBtn = new JButton(new ImageIcon("logos/enter-icon.png"));
        searchBtn.setBackground(Color.BLACK);
        searchBtn.setBorder(null);
        searchBtn.addActionListener(e -> performSearch());

        // Add enter key listener to search field
        searchField.addActionListener(e -> performSearch());

        searchPanel.add(searchField);
        searchPanel.add(Box.createHorizontalStrut(5));
        searchPanel.add(searchBtn);
        rightPanel.add(searchPanel);

        rightPanel.add(Box.createVerticalStrut(20));

        // Building info section
        buildingImageLabel = new JLabel();
        buildingImageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(buildingImageLabel);

        buildingDetailsText = new JTextArea();
        buildingDetailsText.setForeground(Color.WHITE);
        buildingDetailsText.setBackground(Color.BLACK);
        buildingDetailsText.setEditable(false);
        buildingDetailsText.setLineWrap(true);
        buildingDetailsText.setWrapStyleWord(true);
        buildingDetailsText.setMaximumSize(new Dimension(280, 150));
        rightPanel.add(buildingDetailsText);

        contentPanel.add(rightPanel, BorderLayout.EAST);
        add(contentPanel, BorderLayout.CENTER);

        // === BUILDINGS ===
        buildingMap = new HashMap<>();
        addBuilding("B", "Law Faculty & Computer Center", new Point(300, 400),
                "- Classes\n- Law Department\n- Cafe\n- 7/24 Computer Labs");
        // Add other buildings here...

        for (String key : buildingMap.keySet()) {
            Building b = buildingMap.get(key);
            JButton marker = new JButton();
            marker.setBounds(b.location.x, b.location.y, 24, 24);
            marker.setOpaque(false);
            marker.setContentAreaFilled(false);
            marker.setBorderPainted(false);
            marker.setToolTipText(b.name);
            marker.addActionListener(e -> showBuildingInfo(b));
            mapLabel.add(marker);
        }
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            // Implement your search logic here
            // For example:
            for (Building b : buildingMap.values()) {
                if (b.name.toLowerCase().contains(searchText.toLowerCase())) {
                    showBuildingInfo(b);
                    break;
                }
            }
        }
    }

    private void addBuilding(String id, String name, Point location, String info) {
        buildingMap.put(id, new Building(id, name, location, info));
    }

    private void showBuildingInfo(Building b) {
        try {
            ImageIcon originalIcon = new ImageIcon("building_images/" + b.id + ".jpeg");
            Image scaledImage = originalIcon.getImage().getScaledInstance(280, 150, Image.SCALE_SMOOTH);
            buildingImageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            buildingImageLabel.setText("Image not found");
            buildingImageLabel.setForeground(Color.WHITE);
        }
        buildingDetailsText.setText(b.name + "\n\nWhat's in it?\n" + b.info);
    }

    static class Building {
        String id, name, info;
        Point location;
        Building(String id, String name, Point location, String info) {
            this.id = id;
            this.name = name;
            this.location = location;
            this.info = info;
        }
    }
}