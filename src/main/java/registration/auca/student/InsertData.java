package registration.auca.student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InsertData {

    private final String url = "jdbc:postgresql://localhost/bestpractice";
    private final String user = "postgres";
    private final String password = "12345";

    private static final String INSERT_USERS_SQL = "INSERT INTO users (id, fname, lname) VALUES (?, ?, ?);";

    public static void main(String[] argv) throws SQLException {
        InsertData insertData = new InsertData();
        List<User> users = List.of(
            new User(111, "Tony", "Aguero"),
            new User(222, "Bruce", "Wayne"),
            new User(333, "Clark", "Kent")
        );
        insertData.insertRecords(users);
    }

    public void insertRecords(List<User> users) throws SQLException {
        System.out.println(INSERT_USERS_SQL);

        // Step 1: Establishing a Connection
        try (Connection connection = DriverManager.getConnection(url, user, password);

             // Step 2: Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {

            // Disable auto-commit for batch processing
            connection.setAutoCommit(false);

            for (User user : users) {
                preparedStatement.setInt(1, user.getId());
                preparedStatement.setString(2, user.getFirstName());
                preparedStatement.setString(3, user.getLastName());
                preparedStatement.addBatch();
            }

            // Step 3: Execute the batch update
            preparedStatement.executeBatch();

            // Commit the transaction
            connection.commit();

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }

        // Step 4: try-with-resource statement will auto close the connection.
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

class User {
    private int id;
    private String firstName;
    private String lastName;

    public User(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
