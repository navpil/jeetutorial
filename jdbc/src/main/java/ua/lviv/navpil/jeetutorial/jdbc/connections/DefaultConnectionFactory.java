package ua.lviv.navpil.jeetutorial.jdbc.connections;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This will create whatever is provided in db.properties file.
 *
 * Currently only in-memory DBs are supported.
 */
public class DefaultConnectionFactory {

    public static Connection getConnection(Properties dbProps) throws SQLException, IOException {

        Properties p = new Properties();
        p.setProperty("username", dbProps.getProperty("db.username"));
        p.setProperty("password", dbProps.getProperty("db.password"));

        return DriverManager.getConnection(dbProps.getProperty("db.url"), p);
    }



}
