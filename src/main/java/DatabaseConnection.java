import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
        private static final String URL = "jdbc:mysql://localhost:3306/webcrawler";
        private static final String USER = "root";
        private static final String PASSWORD = "1234";

        public static Connection getConnection() {
            try {
                // Establish connection
                return DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                Logger.getGlobal().log(Level.WARNING, "Database connection failed", e);
                return null;
            }
        }
}
