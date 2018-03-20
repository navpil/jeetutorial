package ua.lviv.navpil;

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
        Statement s = null;
        try {
            s = c.createStatement();
            s.execute(sql);
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    private static String getSql(String name) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(TestData.class.getClassLoader().getResourceAsStream(name)));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

}
