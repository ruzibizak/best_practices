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

@WebServlet("/insertData")
public class InsertDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName("org.postgresql.Driver"); 
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bestpractice", "postgres", "12345");

            // Insert data into the users table
            String sqlInsert = "INSERT INTO users (id, fname, lname) VALUES (?, ?, ?)";
            pst = conn.prepareStatement(sqlInsert);
            pst.setString(1, id);
            pst.setString(2, fname);
            pst.setString(3, lname);

            int rowsInserted = pst.executeUpdate();

            if (rowsInserted > 0) {
                out.println("<h2>Data successfully inserted!</h2>");
            } else {
                out.println("<h2>Failed to insert data.</h2>");
            }

            // Display the inserted data
            String sqlSelect = "SELECT id, fname, lname FROM users WHERE id = ?";
            pst = conn.prepareStatement(sqlSelect);
            pst.setString(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                String retrievedId = rs.getString("id");
                String retrievedFname = rs.getString("fname");
                String retrievedLname = rs.getString("lname");

                out.println("<h2>Inserted Data:</h2>");
                out.println("<p>ID: " + retrievedId + "</p>");
                out.println("<p>First Name: " + retrievedFname + "</p>");
                out.println("<p>Last Name: " + retrievedLname + "</p>");
            } else {
                out.println("<h2>No data found.</h2>");
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            out.println("<h2>Error: " + e.getMessage() + "</h2>");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
