package ua.lviv.navpil.jeetutorial.jdbc.connections;

import org.hsqldb.jdbc.JDBCDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

//https://github.com/h-thurow/Simple-JNDI
class ManuallyPopulateJndiDataSource implements Runnable {

    static final String JDBC_COFFEESHOP_JNDI_NAME = "jdbc/cofeeshop";

    @Override
    public void run() {
        JDBCDataSource jdbcDataSource = new JDBCDataSource();
        jdbcDataSource.setDatabase(ConnectionFactory.DB_NAME);
        try {

            Hashtable<String, Object> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "org.osjava.sj.SimpleContextFactory");
            InitialContext initialContext = new InitialContext(env);
            Context subcontext = initialContext.createSubcontext("my-subcontext");
            subcontext.bind(JDBC_COFFEESHOP_JNDI_NAME, jdbcDataSource );
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
