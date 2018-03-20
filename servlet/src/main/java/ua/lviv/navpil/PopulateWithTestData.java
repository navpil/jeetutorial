package ua.lviv.navpil;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebListener
public class PopulateWithTestData implements ServletContextListener {

    // declarations
    @Resource(name = "jdbc/mydb")
    private DataSource ds;

    @Resource(name = "mySpecialValue")
    private Integer mySpecialValue;


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("My special value is " + mySpecialValue);
        Connection connection = null;
        try {
            connection = ds.getConnection();

            TestData.populateDb(connection, "");
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (connection != null) try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
