package ua.lviv.navpil.jeetutorial.jdbc;

import ua.lviv.navpil.jeetutorial.jdbc.connections.DefaultConnectionFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TestData {

    public static void populateDb(Connection c, String scriptsPath) throws SQLException, IOException {
        executeScript(c, getPathToScript("init.sql", scriptsPath));
        executeScript(c, getPathToScript("test-data.sql", scriptsPath));
    }

    private static String getPathToScript(String scriptName, String configPath) {
        if (configPath.trim().isEmpty()) {
            return scriptName;
        } else {
            return configPath + "/" + scriptName;
        }
    }

    private static void executeScript(Connection c, String name) throws SQLException, IOException {
        String sql = getSql(name);
        try (Statement s = c.createStatement()) {
            s.execute(sql);
        }
    }

    private static String getSql(String name) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(DefaultConnectionFactory.class.getClassLoader().getResourceAsStream(name)))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

}
