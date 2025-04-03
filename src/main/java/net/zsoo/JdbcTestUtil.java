package net.zsoo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcTestUtil {

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        String driver = System.getenv("jdbc.driver");
        String url = System.getenv("jdbc.url");
        String username = System.getenv("jdbc.username");
        String password = System.getenv("jdbc.password");

        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }
}
