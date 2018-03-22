package ua.lviv.navpil;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

@WebServlet(name = "showPeopleServlet",
        //urlPatterns have to be specified even if they are overridden by web.xml
        urlPatterns = {"/annotation-list"},
        initParams = {
                @WebInitParam(name = "whoWins", value = "ANNOTATION"),
                @WebInitParam(name = "specialTextValue", value = "Special"),
        }
)
public class ShowPeopleServlet extends HttpServlet {

    @Resource(name = "jdbc/mydb")
    private DataSource ds;

    @Resource(name = "mySpecialValue")
    private Integer mySpecialValue;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("My special value is " + mySpecialValue);
        System.out.println("My special text value is " + getInitParameter("specialTextValue"));
        System.out.println("Polite sentence is " + getInitParameter("politeSentence"));
        System.out.println(MessageFormat.format("When value is specified in both web and annotation {0} takes precedence", getInitParameter("whoWins")));


        PrintWriter writer = resp.getWriter();

        try (Connection connection = ds.getConnection();
             Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet = statement.executeQuery("select * from myschema.person");
            while (resultSet.next()) {
                String guid = resultSet.getString(1);
                String name = resultSet.getString(2);
                int age = resultSet.getInt(3);

                writer.append(new User(guid, name, age).toString());
            }
        } catch (SQLException e) {
            writer.append("Something went wrong");
        } finally {
            writer.flush();
        }


    }

    private static class User {


        private final String guid;
        private final String name;
        private final int age;

        public User(String guid, String name, int age) {
            this.guid = guid;
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "guid='" + guid + '\'' +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
