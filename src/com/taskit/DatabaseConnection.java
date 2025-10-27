package com.taskit;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/taskitdb";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static Connection connection;

    public static Connection getConnection ()
    {
        try
        {
            if (connection == null || connection.isClosed())
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        return connection;
    }
}
