package ua.lviv.navpil.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConnectionFactory {

    public static Connection getConnection(Properties dbProps) throws SQLException, IOException {

        Properties p = new Properties();
        p.setProperty("username", dbProps.getProperty("db.username"));
        p.setProperty("password", dbProps.getProperty("db.password"));

        Connection c = null;

        try {
            c = DriverManager.getConnection(dbProps.getProperty("db.url"), p);

            executeScript(c, getPathToScript(dbProps, "init.sql"));
            executeScript(c, getPathToScript(dbProps, "test-data.sql"));

            try (Statement s = c.createStatement()) {
                ResultSet resultSet = s.executeQuery("select * from users");
                while (resultSet.next()) {
                    System.out.printf("ID: %1$d, Name: %2$s, Age: %3$d %n",
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("age"));
                }
            }
        } catch (Exception e) {
            if (c != null) {
                c.close();
            }
            throw e;
        }
        return c;
    }

    private static String getPathToScript(Properties dbProps, String scriptName) {
        String property = dbProps.getProperty("db.scripts.path");
        if (property.trim().isEmpty()) {
            return scriptName;
        } else {
            return property + "/" + scriptName;
        }
    }

    private static void executeScript(Connection c, String name) throws SQLException, IOException {
        String sql = getSql(name);
        try (Statement s = c.createStatement()) {
            s.execute(sql);
        }
    }

    private static String getSql(String name) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ConnectionFactory.class.getClassLoader().getResourceAsStream(name)))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }


}
