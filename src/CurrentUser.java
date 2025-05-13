public class CurrentUser {
    private static CurrentUser instance;
    private User user;

    // Private constructor: dışarıdan nesne oluşturulmasın
    private CurrentUser() {}

    // Singleton erişim metodu
    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    // Kullanıcıyı set et
    public void setUser(User user) {
        this.user = user;
    }

    // Kullanıcıyı al
    public User getUser() {
        return user;
    }

    // Kullanıcıyı sıfırla (çıkış yapıldığında kullanılabilir)
    public void clearUser() {
        this.user = null;
    }
}