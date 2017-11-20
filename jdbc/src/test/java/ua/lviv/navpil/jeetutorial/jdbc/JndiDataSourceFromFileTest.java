package ua.lviv.navpil.jeetutorial.jdbc;

import org.junit.jupiter.api.Test;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

class JndiDataSourceFromFileTest {

    @Test
    void main() throws NamingException, SQLException, IOException {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource)ctx.lookup("ds.CoffeeShopDS");

        try(Connection c = ds.getConnection()){
            TestData.populateDb(c, "");
            System.out.println(new UsersStringDao(c).list());
        }

    }

}