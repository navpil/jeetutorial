package ua.lviv.navpil.jdbc;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.ShellFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class CLI {

    private final Connection c;

    public CLI(Connection c) {
        this.c = c;
    }

    @Command(description = "Lists all users from DB")
    public String list() throws SQLException {
        StringBuilder sb = new StringBuilder();
        try (Statement s = c.createStatement()) {
            ResultSet resultSet = s.executeQuery("select * from users");
            while (resultSet.next()) {
                sb.append(String.format("ID: %1$d, Name: %2$s, Age: %3$d %n",
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age")));
            }
        }
        return sb.toString();
    }

    @Command(description = "Find user with id")
    public String read(@Param(name = "id") int id) throws SQLException {
        StringBuilder sb = new StringBuilder();
        try (Statement s = c.createStatement()) {
            ResultSet resultSet = s.executeQuery("select * from users where id = " + id);
            while (resultSet.next()) {
                sb.append(String.format("ID: %1$d, Name: %2$s, Age: %3$d %n",
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age")));
            }
        }
        return sb.toString();
    }

    @Command(description = "Delete user with id")
    public boolean delete(@Param(name = "id") int id) throws SQLException {
        try (Statement s = c.createStatement()) {
            return s.execute("delete from users where id = " + id);
        }
    }

    @Command(description = "Add user")
    public boolean add(@Param(name = "name") String name, @Param(name = "age") int age) throws SQLException {
        try (Statement s = c.createStatement()) {
            String format = String.format("insert into users (name, age) values ('%s', %d)", name, age);
            return s.execute(format);
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        Properties dbProps = new Properties();
        try (InputStream resourceAsStream = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")) {
            dbProps.load(resourceAsStream);
        }
        try(Connection c = ConnectionFactory.getConnection(dbProps)) {
            ShellFactory.createConsoleShell(String.format("%s-jdbc", dbProps.getProperty("db.name")), "Cli interface for JDBC", new CLI(c)).commandLoop();

        }
    }
}
