import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {
    CardLayout cardLayout;
    JPanel cards;
    private final int MIN_WIDTH = 1200;
    private final int MIN_HEIGHT = 800;

    // currentUser: Giriş yapmış kullanıcıyı tutar.
    private User currentUser;

    public AppFrame() {
        setTitle("Bilkent Helper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tam ekran yapılandırması
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));

        // CardLayout yapılandırması
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // Sayfaları ekleyelim
        cards.add(new WelcomePage(this), "welcome");
        cards.add(new EntrancePage(this), "entrance");
        cards.add(new LoginPage(this), "login");
        cards.add(new SignUpPage(this), "signup");
        cards.add(new MainPage(this), "main");
        cards.add(new MapPage(this), "map");
        cards.add(new TutoringPage(this), "tutoring");
        cards.add(new StorePage(this), "store");
        cards.add(new SurveyPage(this), "survey");
        cards.add(new ProfilePage(this), "profile");

        // Cards panelini frame'e ekleyelim
        add(cards);
        setVisible(true);

        // İlk olarak WelcomePage'i göster
        showPage("welcome");
    }

    // Sayfaları göstermek için metod
    public void showPage(String name) {
        cardLayout.show(cards, name);
    }

    // currentUser'ı almak için metod
    public User getCurrentUser() {
        return currentUser;
    }

    // currentUser'ı güncellemek için metod
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
