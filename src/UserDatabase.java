import java.sql.*;
import java.util.ArrayList;

public class UserDatabase {

    private ArrayList<User> users = new ArrayList<>();

    public UserDatabase() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/user_schema",
                    "root",
                    "newpassword"
            );

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user");

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                boolean isTutor = resultSet.getBoolean("isTutor");

                // Instantiate User object using the appropriate constructor
                User user = new User(username, password, email, isTutor);

                 //If you have favorites stored as comma-separated IDs:
                 String favString = resultSet.getString("favorites");
                 if (favString != null && !favString.isEmpty()) {
                     String[] ids = favString.split(",");
                     for (String id : ids) {
                         // item should be added through item ıd
                         //user.getFavorites().add(retrievedItem);
                     }
                }

                users.add(user);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the list of users loaded from the database.
     */
    public ArrayList<User> getUsers() {
        return users;
    }
}
