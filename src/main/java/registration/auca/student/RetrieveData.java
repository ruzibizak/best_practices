package registration.auca.student;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RetrieveData {

    private final String url = "jdbc:postgresql://localhost:5432/bestpractice";
    private final String user = "postgres";
    private final String password = "12345";

    private static final String SELECT_ALL_USERS_SQL = "SELECT id, fname, lname FROM users";

    public static void main(String[] argv) throws SQLException {
        RetrieveData retrieveData = new RetrieveData();
        List<User> users = retrieveData.retrieveAllUsers();
        users.forEach(user -> System.out.println(user));
    }

    public List<User> retrieveAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS_SQL)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("fname");
                String lastName = resultSet.getString("lname");
                users.add(new User(id, firstName, lastName));
            }

        } catch (SQLException e) {
            printSQLException(e);
        }

        return users;
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
