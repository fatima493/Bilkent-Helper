import javax.swing.SwingUtilities;

public class MainApp {

    public static void main(String[] args) {
        UserDatabase userDatabase = new UserDatabase();
        SwingUtilities.invokeLater(() -> new AppFrame());
    }
}
