package ua.lviv.navpil.jeetutorial.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsersStringDao {

    private final Connection c;

    public UsersStringDao(Connection c) {
        this.c = c;
    }

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

    public String read(int id) throws SQLException {
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

    public boolean delete(int id) throws SQLException {
        try (Statement s = c.createStatement()) {
            return s.execute("delete from users where id = " + id);
        }
    }

    public boolean add(String name, int age) throws SQLException {
        try (Statement s = c.createStatement()) {
            String format = String.format("insert into users (name, age) values ('%s', %d)", name, age);

            return s.execute(format);
        }
    }

}
