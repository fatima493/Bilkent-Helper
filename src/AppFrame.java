import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {
    private CardLayout cardLayout;
    JPanel cards;
    private final int MIN_WIDTH = 1200;
    private final int MIN_HEIGHT = 800;

    // Giriş yapmış kullanıcı
    private User currentUser;

    public AppFrame() {
        setTitle("Bilkent Helper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));

        // Kart yapısı
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // Sayfaları ekle (MainPage dışında hepsi başta eklenebilir)
        cards.add(new WelcomePage(this), "welcome");
        cards.add(new EntrancePage(this), "entrance");
        cards.add(new LoginPage(this), "login");
        cards.add(new SignUpPage(this), "signup");
        // MainPage DAHİL DEĞİL — dinamik olarak eklenecek
        cards.add(new MapPage(this), "map");
        cards.add(new TutoringPage(this), "tutoring");
        cards.add(new StorePage(this), "store");
        cards.add(new SurveyPage(this), "survey");
        cards.add(new ProfilePage(this), "profile");

        add(cards);
        setVisible(true);

        showPage("welcome");
    }

    public void showPage(String name) {
        if (name.equals("main")) {
            // Eski MainPage varsa sil
            for (Component comp : cards.getComponents()) {
                if (comp instanceof MainPage) {
                    cards.remove(comp);
                    break;
                }
            }
            // Yeni MainPage oluştur ve ekle
            cards.add(new MainPage(this), "main");
        }

        cardLayout.show(cards, name);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        // Singleton güncelle
        CurrentUser.getInstance().setUser(currentUser);
    }
}
