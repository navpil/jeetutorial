package ua.lviv.navpil.jeetutorial.jdbc.connections;

import ua.lviv.navpil.jeetutorial.jdbc.TestData;
import ua.lviv.navpil.jeetutorial.jdbc.UsersStringDao;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Creates connection from a DataSource which is provided in the jndi-config/ds/CoffeeShopDS
 */
public class JndiDataSourcFromFileDemo implements ConnectionFactory{

    public static void main(String[] args) throws NamingException, SQLException, IOException {
        try (Connection connection = new JndiDataSourcFromFileDemo().create()) {
            if ("yes".endsWith(args[0])) {
                TestData.populateDb(connection, "");
            }
            connection.commit();
            System.out.println(new UsersStringDao(connection).list());

        }
    }

    @Override
    public Connection create() throws SQLException, IOException, NamingException {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("ds.CoffeeShopDS");
        return ds.getConnection();
    }
}
