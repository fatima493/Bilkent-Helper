import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class MapPage extends JPanel {
    private JLabel mapLabel;
    private JComboBox<String> filterComboBox;
    private JPanel infoPanel;
    private JLabel buildingImageLabel;
    private JTextArea buildingInfoTextArea;

    private HashMap<String, Building> buildingMap;

    public MapPage() {
        setLayout(new BorderLayout());

        // Top Panel with filter and navigation
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterComboBox = new JComboBox<>(new String[]{
                "All", "Buildings", "Academic", "Administration", "Auditoriums"
        });
        topPanel.add(new JLabel("Filter:"));
        topPanel.add(filterComboBox);

        JButton goBackButton = new JButton(new ImageIcon("logos/go-back-logo.png"));
        topPanel.add(goBackButton);

        add(topPanel, BorderLayout.NORTH);

        // Map display (center)
        mapLabel = new JLabel(new ImageIcon("logos/map-icon.png"));
        mapLabel.setLayout(null);
        JScrollPane mapScrollPane = new JScrollPane(mapLabel);
        add(mapScrollPane, BorderLayout.CENTER);

        // Info panel (right)
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(250, 0));
        buildingImageLabel = new JLabel();
        buildingInfoTextArea = new JTextArea();
        buildingInfoTextArea.setEditable(false);
        buildingInfoTextArea.setLineWrap(true);
        buildingInfoTextArea.setWrapStyleWord(true);

        infoPanel.add(buildingImageLabel);
        infoPanel.add(new JScrollPane(buildingInfoTextArea));
        add(infoPanel, BorderLayout.EAST);

        // Load buildings
        buildingMap = new HashMap<>();
        addBuilding("B", "Faculty of Law", "Buildings", new Point(100, 200),
                "Services: ...");
        addBuilding("EE", "Electrical- Electronics Engineering", "Buildings", new Point(150, 200),
                "Departments: Electrical-Electronics Engineering\nContacts: ee@bilkent.edu.tr");
        addBuilding("EA", "Faculty of Engineering", "Academic", new Point(250, 180),
                "Departments: Computer Science\nContacts: cs@bilkent.edu.tr");

        // Load markers
        for (String key : buildingMap.keySet()) {
            Building b = buildingMap.get(key);
            JButton marker = new JButton();
            marker.setBounds(b.location.x, b.location.y, 20, 20);
            marker.setBackground(Color.RED);
            marker.setToolTipText(b.name);
            marker.addActionListener(e -> showBuildingInfo(b));
            mapLabel.add(marker);
        }
    }

    private void addBuilding(String id, String name, String category, Point location, String info) {
        buildingMap.put(id, new Building(id, name, category, location, info));
    }

    private void showBuildingInfo(Building b) {
        ImageIcon icon = new ImageIcon("building_images/" + b.id + ".jpeg");
        buildingImageLabel.setIcon(icon);
        buildingInfoTextArea.setText(b.name + "\n\n" + b.info);
    }

    static class Building {
        String id;
        String name;
        String category;
        Point location;
        String info;

        Building(String id, String name, String category, Point location, String info) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.location = location;
            this.info = info;
        }
    }
}
