import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;
import java.util.ArrayList;

public class StorePage extends BackgroundPanel {

    private ArrayList<Book> books;
    public StorePage(AppFrame frame) {
        this.books=new ArrayList<>();
        setLayout(new BorderLayout());

        // Ekran boyutuna göre margin ayarla
        int marginX = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.04);
        int marginY = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.05);
        setBorder(BorderFactory.createEmptyBorder(marginY, marginX, marginY, marginX));

        // Sol üst köşe – Başlık
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setOpaque(false);

// Sol: Başlık
        JLabel topLeftLabel = new JLabel("BILKENT HELPER | STORE                                                               ");
        topLeftLabel.setFont(new Font("Avenir Next", Font.PLAIN, 20));
        topLeftLabel.setForeground(Color.WHITE);
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(topLeftLabel);
        topPanel.add(leftPanel);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);

        JTextField searchField = new JTextField(20);
        searchField.setText("Search...");
        searchField.setFont(new Font("Avenir Next", Font.PLAIN, 14));
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

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        topPanel.add(searchPanel);
// Sağ: Menu butonu
        ImageIcon backIcon = new ImageIcon("logos/go-back-logo.png");
        JButton backBtn = new JButton(backIcon);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setOpaque(false);
        backBtn.setPreferredSize(new Dimension(40, 40));
        backBtn.addActionListener(e -> frame.showPage("main")); //TODO:PROFIL

        ImageIcon profileIcon = new ImageIcon("logos/profile-icon.png");
        JButton profileBtn = new JButton(profileIcon);
        profileBtn.setContentAreaFilled(false);
        profileBtn.setBorderPainted(false);
        profileBtn.setFocusPainted(false);
        profileBtn.setOpaque(false);
        profileBtn.setPreferredSize(new Dimension(40, 40));
        profileBtn.addActionListener(e -> frame.showPage("profile")); //TODO:PROFIL

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(backBtn);
        rightPanel.add(profileBtn);
        topPanel.add(rightPanel, BorderLayout.EAST);

    

// Top panel'i ana sayfaya ekle
        add(topPanel, BorderLayout.NORTH);

//TUTORS AND FILTER PANELS
        JPanel middlePanel = new JPanel(new GridLayout(1,2));
        middlePanel.setOpaque(false);

        JPanel booksPanel = new JPanel();
        booksPanel.setOpaque(false);
        booksPanel.setLayout(new BoxLayout(booksPanel, BoxLayout.Y_AXIS));

        JScrollPane tutorsScrollPane = new JScrollPane(booksPanel);
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


        JPanel filterMoverPanel = new JPanel();
        filterMoverPanel.setOpaque(false);

        JPanel filterPanel = new JPanel(new GridLayout(2, 1));
        filterPanel.setOpaque(false);

        // ==== SEARCH PANEL ====
        JPanel byPanel = new JPanel(new GridLayout(3, 1));
        byPanel.setOpaque(false);

        JLabel title = new JLabel("Search");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Avenir Next", Font.PLAIN, 20));

        JLabel subTitle = new JLabel("By Lecture or Code");
        subTitle.setForeground(Color.WHITE);
        subTitle.setFont(new Font("Avenir Next", Font.PLAIN, 14));

        JTextField searchField1 = new JTextField();
        searchField1.addActionListener(e -> {
            String keyword = searchField1.getText().trim().toLowerCase();

            if (keyword.isEmpty()) {
                refreshBooksPanelWithFilteredList(booksPanel, books);
                return;
            }

            ArrayList<Book> filtered = new ArrayList<Book>();
            for (int i = 0; i < books.size(); i++) {
                String t = books.get(i).getTitle().toLowerCase();
                String a = books.get(i).getAuthor().toLowerCase();
                if (t.contains(keyword) || a.contains(keyword)) {
                    filtered.add(books.get(i));
                }
            }
            refreshBooksPanelWithFilteredList(booksPanel, filtered);
        });

        byPanel.add(title);
        byPanel.add(subTitle);
        byPanel.add(searchField1);

        // ==== SORTING PANEL ====
        JPanel arrangePanel = new JPanel(new GridLayout(2, 2));
        arrangePanel.setOpaque(false);

        JLabel sortTitle = new JLabel("Sort by Price");
        sortTitle.setForeground(Color.WHITE);
        sortTitle.setFont(new Font("Avenir Next", Font.PLAIN, 20));

        JButton priceInc = new JButton("Increasing");
        priceInc.addActionListener(e -> {
            for (int i = 0; i < books.size(); i++) {
                for (int j = i + 1; j < books.size(); j++) {
                    if (books.get(i).getPrice() > books.get(j).getPrice()) {
                        Book temp = books.get(i);
                        books.set(i, books.get(j));
                        books.set(j, temp);
                    }
                }
            }
            refreshBooksPanel(booksPanel);
        });

        JButton priceDec = new JButton("Decreasing");
        priceDec.addActionListener(e -> {
            for (int i = 0; i < books.size(); i++) {
                for (int j = i + 1; j < books.size(); j++) {
                    if (books.get(i).getPrice() < books.get(j).getPrice()) {
                        Book temp = books.get(i);
                        books.set(i, books.get(j));
                        books.set(j, temp);
                    }
                }
            }
            refreshBooksPanel(booksPanel);
        });

        arrangePanel.add(sortTitle);
        arrangePanel.add(new JLabel()); // boşluk için
        arrangePanel.add(priceInc);
        arrangePanel.add(priceDec);

        // === PANELLERİ BİRLEŞTİR ===
        filterPanel.add(byPanel);
        filterPanel.add(arrangePanel);


        books.add(new Book("İnce Memed", "Yaşar Kemal", "Good", 300, profileIcon,"example@bilkent.edu.tr"));
        books.add(new Book("İnce Memed", "Yaşar Kemal", "Good", 250, profileIcon,"example@bilkent.edu.tr"));
        books.add(new Book("İnce Memed", "Yaşar Kemal", "Good", 3, profileIcon,"example@bilkent.edu.tr"));
        books.add(new Book("İnce Memed", "Yaşar Kemal", "Good", 30, profileIcon,"example@bilkent.edu.tr"));
        books.add(new Book("İnce Memed", "Yaşar Kemal", "Good", 300, profileIcon,"example@bilkent.edu.tr"));
        books.add(new Book("İnce Memed", "Yaşar Kemal", "Good", 300, profileIcon,"example@bilkent.edu.tr"));
        books.add(new Book("İnce Memed", "Yaşar Kemal", "Good", 300, profileIcon,"example@bilkent.edu.tr"));
        books.add(new Book("İnce Memed", "Yaşar Kemal", "Good", 300, profileIcon,"example@bilkent.edu.tr"));
        books.add(new Book("İnce Memed", "Yaşar Kemal", "Good", 300, profileIcon,"example@bilkent.edu.tr"));
        books.add(new Book("İnce Memed", "Yaşar Kemal", "Good", 300, profileIcon,"example@bilkent.edu.tr"));



        for (Book book : books) {
            booksPanel.add(book);
            booksPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Tutorlar arasında boşluk
        }


        middlePanel.add(tutorsScrollPane);


        filterMoverPanel.add(filterPanel,BorderLayout.EAST);
        middlePanel.add(filterMoverPanel);

        add(middlePanel,BorderLayout.CENTER);
    }

    private void refreshBooksPanel(JPanel booksPanel) {
        booksPanel.removeAll();
        for (Book book : books) {
            booksPanel.add(book);
            booksPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        booksPanel.revalidate();
        booksPanel.repaint();
    }

        private void refreshBooksPanelWithFilteredList(JPanel booksPanel, ArrayList<Book> filteredBooks) {
            booksPanel.removeAll();
            for (int i = 0; i < filteredBooks.size(); i++) {
                booksPanel.add(filteredBooks.get(i));
            }
            booksPanel.revalidate();
            booksPanel.repaint();
        }


    public class Book extends JPanel {
    private String title;
    private String author;
    private String condition;
    private int price;
    private String contactMail;
    private ImageIcon coverImage;

    public Book(String title, String author, String condition, int price, ImageIcon coverImage, String contactMail) {
        this.title = title;
        this.author = author;
        this.condition = condition;
        this.price = price;
        this.coverImage = coverImage;
        this.contactMail = contactMail;

        setLayout(new BorderLayout());
        setMaximumSize(new Dimension(800, 150));
        setBackground(new Color(0, 0, 0, 230));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setOpaque(true);

        // Sol: Kitap kapağı
        JLabel imageLabel = new JLabel();
        if (coverImage != null) {
            Image img = coverImage.getImage().getScaledInstance(100, 120, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } else {
            imageLabel.setPreferredSize(new Dimension(100, 120));
            imageLabel.setOpaque(true);
            imageLabel.setBackground(Color.DARK_GRAY);
        }

        JPanel imagePanel = new JPanel();
        imagePanel.setOpaque(false);
        imagePanel.add(imageLabel);

        // Orta: Bilgiler
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Avenir Next", Font.BOLD, 16));

        JLabel authorLabel = new JLabel("Author: " + author);
        authorLabel.setForeground(Color.LIGHT_GRAY);
        authorLabel.setFont(new Font("Avenir Next", Font.PLAIN, 14));

        JLabel conditionLabel = new JLabel("Condition: " + condition);
        conditionLabel.setForeground(Color.LIGHT_GRAY);
        conditionLabel.setFont(new Font("Avenir Next", Font.PLAIN, 14));

        JLabel priceLabel = new JLabel("Price: " + price + "TL");
        priceLabel.setForeground(Color.LIGHT_GRAY);
        priceLabel.setFont(new Font("Avenir Next", Font.PLAIN, 14));

        JLabel mailLabel = new JLabel("Contact: " + contactMail);
        mailLabel.setForeground(Color.LIGHT_GRAY);
        mailLabel.setFont(new Font("Avenir Next", Font.PLAIN, 14));

        infoPanel.add(titleLabel);
        infoPanel.add(authorLabel);
        infoPanel.add(conditionLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(mailLabel);

        add(imagePanel, BorderLayout.WEST);
        add(infoPanel, BorderLayout.CENTER);
    }

    // Getter methods
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCondition() { return condition; }
    public int getPrice() { return price; }
    public String getContactMail() { return contactMail; }
}


    
}
