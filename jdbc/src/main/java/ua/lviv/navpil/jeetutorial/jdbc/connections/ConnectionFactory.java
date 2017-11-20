package ua.lviv.navpil.jeetutorial.jdbc.connections;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactory {

    String DB_NAME = "target/jndi/coffeeshop";

    Connection create() throws SQLException, IOException, NamingException;

}
