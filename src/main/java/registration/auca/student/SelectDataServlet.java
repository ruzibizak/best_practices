package registration.auca.student;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/selectData")
public class SelectDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database URL, username, and password
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/bestpractice";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "12345";

    // JDBC driver name
    private static final String JDBC_DRIVER = "org.postgresql.Driver";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Load JDBC driver
            Class.forName(JDBC_DRIVER);

            // Establish connection
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Create SQL query
            String sql = "SELECT id, name, surname FROM users";
            stmt = conn.prepareStatement(sql);

            // Execute query
            rs = stmt.executeQuery();

            // Process result set
            out.println("<html><body>");
            out.println("<h2>Users</h2>");
            out.println("<table border='1'><tr><th>ID</th><th>firstname</th><th>lastname</th></tr>");
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstname = rs.getString("fname");
                String lastname = rs.getString("lname");
                out.println("<tr><td>" + id + "</td><td>" + firstname + "</td><td>" + lastname + "</td></tr>");
            }
            out.println("</table>");
            out.println("</body></html>");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(out);
        } finally {
            // Clean up
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace(out);
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
