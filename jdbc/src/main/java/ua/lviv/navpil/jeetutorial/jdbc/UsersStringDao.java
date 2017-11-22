package ua.lviv.navpil.jeetutorial.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
            ResultSet resultSet = s.executeQuery("select id, name, age from users");
            while (resultSet.next()) {
                sb.append(String.format("ID: %1$d, Name: %2$s, Age: %3$d %n",
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3)));
            }
        }
        return sb.toString();
    }

    public String read(int id) throws SQLException {
        StringBuilder sb = new StringBuilder();
        try (PreparedStatement s = c.prepareStatement("select id, name, age from users where id = ?")) {
            s.setInt(1, id);
            ResultSet resultSet = s.executeQuery();
            while (resultSet.next()) {
                sb.append(String.format("ID: %1$d, Name: %2$s, Age: %3$d %n",
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age")));
            }
        }
        return sb.toString();
    }

    public String delete(int id) throws SQLException {
        try (PreparedStatement s = c.prepareStatement("delete from users where id = ?")) {
            s.setInt(1, id);
            int number = s.executeUpdate();
            return getUpdateResult(s, number);
        }
    }

    private String getUpdateResult(Statement s, int number) throws SQLException {
        if (s.getWarnings() == null) {
            return String.format("OK, %d of rows changed", number);
        }
        return s.getWarnings().getMessage();
    }

    public String add(String name, int age) throws SQLException {
        try (PreparedStatement s = c.prepareStatement("insert into users (name, age) values (?, ?)")) {
            s.setString(1, name);
            s.setInt(2, age);
            return getUpdateResult(s, s.executeUpdate());
        }
    }

}
