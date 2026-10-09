package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/food_delivery";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Nitish@12345";

    public static Connection getConnection() {

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(
                    URL,
                    USERNAME,
                    PASSWORD
            );

            System.out.println("Database Connected Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}