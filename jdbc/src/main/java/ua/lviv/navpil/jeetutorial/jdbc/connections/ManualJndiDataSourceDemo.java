package ua.lviv.navpil.jeetutorial.jdbc.connections;

import ua.lviv.navpil.jeetutorial.jdbc.TestData;
import ua.lviv.navpil.jeetutorial.jdbc.UsersStringDao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 * Configuration of DataSource is done in-memory jndi
 */
public class ManualJndiDataSourceDemo implements ConnectionFactory{

    public static void main(String[] args) throws SQLException, NamingException, IOException {
        try(Connection c = new ManualJndiDataSourceDemo().create()) {

            boolean shouldPopulate = true;

            if (shouldPopulate) {
                TestData.populateDb(c, "");
            }

            UsersStringDao usersStringDao = new UsersStringDao(c);
            System.out.println(usersStringDao.list());

            c.close();
        }

    }

    @Override
    public Connection create() throws SQLException, IOException, NamingException {
        //This creates a persistent DB
        new ManuallyPopulateJndiDataSource().run();

        Hashtable<String, Object> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.osjava.sj.SimpleContextFactory");        InitialContext initialContext = new InitialContext(env);
        Context subcontext = (Context)initialContext.lookup("my-subcontext");
        DataSource ds = (DataSource) subcontext.lookup(ManuallyPopulateJndiDataSource.JDBC_COFFEESHOP_JNDI_NAME);

        return ds.getConnection("su", "");

    }
}
