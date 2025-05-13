import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TutoringPage extends BackgroundPanel {

    private ArrayList<Tutor> tutors;
    private JPanel tutorsPanel;
    private JTextField searchField;

    public TutoringPage(AppFrame frame) {
        this.tutors = new ArrayList<>();
        setLayout(new BorderLayout());

        int marginX = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.04);
        int marginY = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.05);
        setBorder(BorderFactory.createEmptyBorder(marginY, marginX, marginY, marginX));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Left label
        JLabel topLeftLabel = new JLabel("BILKENT HELPER | TUTORING                     ");
        topLeftLabel.setFont(new Font("Avenir Next", Font.PLAIN, 20));
        topLeftLabel.setForeground(Color.WHITE);
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(topLeftLabel);
        topPanel.add(leftPanel, BorderLayout.WEST);

        // Search section
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setOpaque(false);

        searchField = new JTextField(20);
        searchField.setText("Search by name");
        searchField.setFont(new Font("Avenir Next", Font.PLAIN, 14));
        searchField.setForeground(Color.WHITE);
        searchField.setOpaque(false);

        ImageIcon searchIcon = new ImageIcon("logos/search-icon.png");
        Image scaledImage = searchIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledSearchIcon = new ImageIcon(scaledImage);
        JButton searchButton = new JButton(scaledSearchIcon);
        searchButton.setPreferredSize(new Dimension(40, 40));
        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setOpaque(false);

        searchButton.addActionListener(e -> performSearch());

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        topPanel.add(searchPanel);

        // Right profile buttons
        ImageIcon backIcon = new ImageIcon("logos/go-back-logo.png");
        JButton backBtn = new JButton(backIcon);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setOpaque(false);
        backBtn.setPreferredSize(new Dimension(40, 40));
        backBtn.addActionListener(e -> frame.showPage("main"));

        ImageIcon profileIcon = new ImageIcon("logos/profile-icon.png");
        JButton profileBtn = new JButton(profileIcon);
        profileBtn.setContentAreaFilled(false);
        profileBtn.setBorderPainted(false);
        profileBtn.setFocusPainted(false);
        profileBtn.setOpaque(false);
        profileBtn.setPreferredSize(new Dimension(40, 40));
        profileBtn.addActionListener(e -> frame.showPage("profile"));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(backBtn);
        rightPanel.add(profileBtn);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // MIDDLE
        JPanel middlePanel = new JPanel(new GridLayout(1,2));
        middlePanel.setOpaque(false);

        tutorsPanel = new JPanel();
        tutorsPanel.setOpaque(false);

        JPanel filterMoverPanel = new JPanel();
        filterMoverPanel.setOpaque(false);

        JPanel filterPanel = new JPanel(new GridLayout(3, 1));
        filterPanel.setOpaque(false);

        JPanel byPanel = new JPanel(new GridLayout(3,2));
        byPanel.setOpaque(false);

        JLabel title = new JLabel();
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Avenir Next", Font.PLAIN, 20));
        title.setText("Search");
        JLabel empty1 = new JLabel("");
        JLabel empty2 = new JLabel("");
        JLabel empty3 = new JLabel("");
        JLabel empty4 = new JLabel("");
        JLabel empty5 = new JLabel("");

        JLabel subTitle = new JLabel();
        subTitle.setForeground(Color.WHITE);
        subTitle.setFont(new Font("Avenir Next", Font.PLAIN, 14));
        subTitle.setText("By Lecture or Code");

        JMenu lecturesMenu = new JMenu("Lectures");
        lecturesMenu.setForeground(Color.WHITE);
        JMenu codeMenu = new JMenu("Code");
        codeMenu.setForeground(Color.WHITE);

        byPanel.add(title);
        byPanel.add(empty1);
        byPanel.add(subTitle);
        byPanel.add(empty2);
        byPanel.add(lecturesMenu);
        byPanel.add(codeMenu);

        JPanel filterbyPanel = new JPanel(new GridLayout(3,2));
        filterbyPanel.setOpaque(false);

        JLabel title2 = new JLabel();
        title2.setForeground(Color.WHITE);
        title2.setFont(new Font("Avenir Next", Font.PLAIN, 20));
        title2.setText("Filters");

        JLabel subtitle2 = new JLabel();
        subtitle2.setForeground(Color.WHITE);
        subtitle2.setFont(new Font("Avenir Next", Font.PLAIN, 14));
        subtitle2.setText("Minimum Semester Count");

        JLabel subtitle3 = new JLabel();
        subtitle3.setForeground(Color.WHITE);
        subtitle3.setFont(new Font("Avenir Next", Font.PLAIN, 14));
        subtitle3.setText("Minimum GPA");

        

        JButton semesterButton = new JButton();

        JTextField gpaInputField = new JTextField();
        gpaInputField.setFont(new Font("Avenir Next", Font.PLAIN, 14));
        gpaInputField.setForeground(Color.WHITE);
        gpaInputField.setOpaque(false);
        gpaInputField.setPreferredSize(new Dimension(100, 25));

        gpaInputField.addActionListener(e -> {
            String inputText = gpaInputField.getText().trim();
            tutorsPanel.removeAll();
            
            if (inputText.isEmpty()) {
                for (Tutor tutor : tutors) {
                    tutorsPanel.add(tutor);
                    tutorsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            } else {
                try {
                    double minGpa = Double.parseDouble(inputText);
                    for (Tutor tutor : tutors) {
                        if (tutor.getGpa() >= minGpa) {
                            tutorsPanel.add(tutor);
                            tutorsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid GPA (e.g., 2.5)", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        
            tutorsPanel.revalidate();
            tutorsPanel.repaint();
        });

        JTextField semesterInputField = new JTextField();
        semesterInputField.setFont(new Font("Avenir Next", Font.PLAIN, 14));
        semesterInputField.setForeground(Color.WHITE);
        semesterInputField.setOpaque(false);
        semesterInputField.setPreferredSize(new Dimension(100, 25));

        semesterInputField.addActionListener(e -> {
            String inputText = semesterInputField.getText().trim();
            tutorsPanel.removeAll();
            
            if (inputText.isEmpty()) {
                for (Tutor tutor : tutors) {
                    tutorsPanel.add(tutor);
                    tutorsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            } else {
                try {
                    double minSem = Double.parseDouble(inputText);
                    for (Tutor tutor : tutors) {
                        if (tutor.getSemester() >= minSem) {
                            tutorsPanel.add(tutor);
                            tutorsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid GPA (e.g., 2.5)", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        
            tutorsPanel.revalidate();
            tutorsPanel.repaint();
        });

        

        filterbyPanel.add(title2);
        filterbyPanel.add(empty3);
        filterbyPanel.add(subtitle2);
        filterbyPanel.add(semesterInputField);
        filterbyPanel.add(subtitle3);
        filterbyPanel.add(gpaInputField);


        JPanel arrangePanel = new JPanel(new GridLayout(1,3));
        arrangePanel.setOpaque(false);

        JLabel sub3 = new JLabel();
        sub3.setForeground(Color.WHITE);
        sub3.setFont(new Font("Avenir Next", Font.PLAIN, 14));
        sub3.setText("GPA");

        JButton priceInc = new JButton("Increasing");
        JButton priceDec = new JButton("Decreasing");
        JButton starsInc = new JButton("Increasing");
        JButton starsDec = new JButton("Decreasing");
        JButton gpaInc = new JButton("Increasing");
        gpaInc.addActionListener(e -> {
            tutors.sort((t1, t2) -> Double.compare(t1.getGpa(), t2.getGpa()));
            tutorsPanel.removeAll();
            for (Tutor tutor : tutors) {
                tutorsPanel.add(tutor);
                tutorsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
            tutorsPanel.revalidate();
            tutorsPanel.repaint();
        });
        JButton gpaDec = new JButton("Decreasing");
        gpaDec.addActionListener(e -> {
            tutors.sort((t1, t2) -> Double.compare(t2.getGpa(), t1.getGpa()));
            tutorsPanel.removeAll();
            for (Tutor tutor : tutors) {
                tutorsPanel.add(tutor);
                tutorsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
            tutorsPanel.revalidate();
            tutorsPanel.repaint();
        });

        arrangePanel.add(sub3);
        arrangePanel.add(gpaInc);
        arrangePanel.add(gpaDec);

        filterPanel.add(byPanel);
        filterPanel.add(filterbyPanel);
        filterPanel.add(arrangePanel);
        
        tutorsPanel.setLayout(new BoxLayout(tutorsPanel, BoxLayout.Y_AXIS));
        tutorsPanel.setOpaque(false);

        JScrollPane tutorsScrollPane = new JScrollPane(tutorsPanel);
        tutorsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tutorsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tutorsScrollPane.setOpaque(false);
        tutorsScrollPane.getViewport().setOpaque(false);
        tutorsScrollPane.setBorder(BorderFactory.createEmptyBorder());

        tutorsScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(80, 80, 80); 
                this.trackColor = new Color(30, 30, 30);
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        Tutor t1 = new Tutor("Mehmet Önal", 2.9, "mehmet.onal@bilkent.edu.tr", "Available", 100,3);
        t1.AddCourse("MATH101");
        t1.AddCourse("CS102");

        Tutor t2 = new Tutor("Ayşe Yılmaz", 3.4, "ayse.yilmaz@bilkent.edu.tr", "Busy", 80,1);
        t2.AddCourse("PHYS101");

        Tutor t3 = new Tutor("Can Demir", 3.8, "can.demir@bilkent.edu.tr", "Available", 60,2);
        t3.AddCourse("CS201");
        t3.AddCourse("CS202");

        Tutor t4 = new Tutor("Elif Kaya", 2.7, "elif.kaya@bilkent.edu.tr", "Available", 90,4);
        t4.AddCourse("CHEM101");

        Tutor t5 = new Tutor("Burak Şahin", 3.0, "burak.sahin@bilkent.edu.tr", "Busy", 70,2);
        t5.AddCourse("MATH102");

        tutors.add(t1);
        tutors.add(t2);
        tutors.add(t3);
        tutors.add(t4);
        tutors.add(t5);

        for (Tutor tutor : tutors) {
            tutorsPanel.add(tutor);
            tutorsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Tutorlar arasında boşluk
        }


        middlePanel.add(tutorsScrollPane);


        filterMoverPanel.add(filterPanel,BorderLayout.EAST);
        middlePanel.add(filterMoverPanel);

        add(middlePanel,BorderLayout.CENTER);
    }

    private void performSearch() {
        String input = searchField.getText().trim().toLowerCase();
        tutorsPanel.removeAll();

        if (input.isEmpty() || input.equals("Search...")) {
            renderTutors(tutors);
        } else {
            for (Tutor tutor : tutors) {
                if (tutor.getName().toLowerCase().contains(input)) {
                    tutorsPanel.add(tutor);
                    tutorsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        }

        tutorsPanel.revalidate();
        tutorsPanel.repaint();
    }

    private void renderTutors(ArrayList<Tutor> tutorList) {
        tutorsPanel.removeAll();
        for (Tutor tutor : tutorList) {
            tutorsPanel.add(tutor);
            tutorsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    class Tutor extends JPanel {
        private String name;
        private double gpa;
        private String email;
        private String status;
        private int tutoringHours;
        private ArrayList<String> coursesGiven;
        private int semester;

        public Tutor(String name, double gpa, String email, String status, int tutoringHours, int semester) {
            this.name = name;
            this.gpa = gpa;
            this.email = email;
            this.status = status;
            this.tutoringHours = tutoringHours;
            this.coursesGiven = new ArrayList<>();
            this.semester=semester;

            setLayout(new BorderLayout());
            setMaximumSize(new Dimension(800, 150));
            setBackground(new Color(0, 0, 0, 150));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setOpaque(true);

            JPanel imagePanel = new JPanel();
            imagePanel.setPreferredSize(new Dimension(100, 100));
            imagePanel.setBackground(Color.LIGHT_GRAY);
            imagePanel.setOpaque(true);

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);

            JLabel nameLabel = new JLabel(name + " | GPA: " + gpa);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("Avenir Next", Font.BOLD, 16));

            JLabel emailLabel = new JLabel(email);
            emailLabel.setForeground(Color.LIGHT_GRAY);
            emailLabel.setFont(new Font("Avenir Next", Font.PLAIN, 14));

            JLabel departmentLabel = new JLabel("Department: Example Department ("+semester+". year)");
            departmentLabel.setForeground(Color.LIGHT_GRAY);
            departmentLabel.setFont(new Font("Avenir Next", Font.PLAIN, 14));

            infoPanel.add(nameLabel);
            infoPanel.add(emailLabel);
            infoPanel.add(departmentLabel);

            JPanel coursesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            coursesPanel.setOpaque(false);

            for (String course : coursesGiven) {
                JLabel courseLabel = new JLabel(course);
                courseLabel.setForeground(Color.WHITE);
                courseLabel.setFont(new Font("Avenir Next", Font.PLAIN, 14));
                courseLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                coursesPanel.add(courseLabel);
            }

            add(imagePanel, BorderLayout.WEST);
            add(infoPanel, BorderLayout.CENTER);
            add(coursesPanel, BorderLayout.EAST);
        }

        public void AddCourse(String course) {
            coursesGiven.add(course);
        }

        public String getName() { return name; }
        public double getGpa() { return gpa; }
        public String getEmail() { return email; }
        public String getStatus() { return status; }
        public int getTutoringHours() { return tutoringHours; }
        public ArrayList<String> getCoursesGiven() { return coursesGiven; }

        public int getSemester() {
            return semester;
        }

        public void setSemester(int semester) {
            this.semester = semester;
        }

        
    }
}
