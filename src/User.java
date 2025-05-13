import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String email;
    private ArrayList<SecondHandItem> favorites;
    private boolean isTutor;

    /**
     * Constructor for a regular user (not a tutor).
     * @param username the user's username
     * @param password the user's password
     * @param email    the user's email address
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isTutor = false;              // default to non-tutor
        this.favorites = new ArrayList<>();
    }

    /**
     * Constructor for a user with tutor flag.
     * @param username the user's username
     * @param password the user's password
     * @param email    the user's email address
     * @param isTutor  whether the user is a tutor
     */
    public User(String username, String password, String email, boolean isTutor) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isTutor = isTutor;
        this.favorites = new ArrayList<>();
    }

    // Getters and setters (optional)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<SecondHandItem> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<SecondHandItem> favorites) {
        this.favorites = favorites;
    }

    public boolean isTutor() {
        return isTutor;
    }

    public void setTutor(boolean tutor) {
        isTutor = tutor;
    }
}
