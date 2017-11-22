package ua.lviv.navpil.jeetutorial.jdbc;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.ShellFactory;
import ua.lviv.navpil.jeetutorial.jdbc.connections.ConnectionFactory;
import ua.lviv.navpil.jeetutorial.jdbc.connections.DefaultConnectionFactory;
import ua.lviv.navpil.jeetutorial.jdbc.connections.JndiDataSourcFromFileDemo;
import ua.lviv.navpil.jeetutorial.jdbc.connections.ManualJndiDataSourceDemo;
import ua.lviv.navpil.jeetutorial.jdbc.connections.ManualPersistentHsqdb;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class CLI {

    private final UsersStringDao users;
    private final Connection c;
    private final String scriptPath;

    public CLI(Connection c, String scriptPath) {
        users = new UsersStringDao(c);
        this.c = c;
        this.scriptPath = scriptPath;
    }

    @Command(description = "Lists all users from DB")
    public String list() throws SQLException {
        return users.list();
    }

    @Command(description = "same as 'read'")
    public String get(int id) throws SQLException {
        return read(id);
    }

    @Command
    public String help() {
        return "For help please print: ?help";
    }

    @Command(description = "Find user with id")
    public String read(@Param(name = "id") int id) throws SQLException {
        return users.read(id);
    }

    @Command(description = "Delete user with id")
    public String delete(@Param(name = "id") int id) throws SQLException {
        return users.delete(id);
    }

    @Command(description = "Add user")
    public String add(@Param(name = "name") String name, @Param(name = "age") int age) throws SQLException {
        return users.add(name, age);
    }

    @Command(description = "Populates Db with test data")
    public void populate() throws IOException, SQLException {
        TestData.populateDb(c, scriptPath);
    }

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory;
        String prompt;

        Properties dbProps = new Properties();
        try (InputStream resourceAsStream = DefaultConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")) {
            dbProps.load(resourceAsStream);
        }

        if (args.length == 0) {
            prompt = String.format("%s-jdbc", dbProps.getProperty("db.name"));
            factory = () -> DefaultConnectionFactory.getConnection(dbProps);
        } else {
            switch (args[0]) {
                case "manual-jndi":
                    factory = new ManualJndiDataSourceDemo();
                    break;
                case "file-jndi":
                    factory = new JndiDataSourcFromFileDemo();
                    break;
                case "manual-ds":
                    factory = new ManualPersistentHsqdb();
                    break;
                default:
                    System.out.println("Please use one of the following arguments: manual-jndi, file-jndi, manual-ds\n" +
                            "The only DB supported is hsqldb");
                    return;
            }
            prompt = args[0];
        }

        try (Connection c = factory.create()) {
            ShellFactory.createConsoleShell(prompt, "Cli interface for JDBC", new CLI(c, dbProps.getProperty("db.scripts.path"))).commandLoop();
        }
    }
}
