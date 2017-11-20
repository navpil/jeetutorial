package ua.lviv.navpil.jeetutorial.jdbc.connections;

import ua.lviv.navpil.jeetutorial.jdbc.UsersStringDao;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manual creation of the persistent DataSource connection using driver manager
 */
public class ManualPersistentHsqdb implements ConnectionFactory {

    public static void main(String[] args) throws SQLException, IOException, NamingException {

        try (Connection c = new ManualPersistentHsqdb().create()) {
            UsersStringDao usersStringDao = new UsersStringDao(c);
            System.out.println(usersStringDao.list());
            c.commit();
            c.close();
        }
    }


    @Override
    public Connection create() throws SQLException, IOException, NamingException {
        Properties p = new Properties();
        p.setProperty("user", "su");
        p.setProperty("password", "");

        return DriverManager.getConnection("jdbc:hsqldb:" + ConnectionFactory.DB_NAME, p);
    }
}
