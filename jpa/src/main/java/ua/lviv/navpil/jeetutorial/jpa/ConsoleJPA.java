package ua.lviv.navpil.jeetutorial.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class ConsoleJPA {

    private static EntityManager em;

    public static void main(String[] args) {

        System.out.println("Welcome to the console JPA");
        Scanner in = new Scanner(System.in);
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("Users");
        em = factory.createEntityManager();
        try {
            noFailLoop(() -> {
                        System.out.print("> ");
                        String line = in.nextLine();
                        String[] commandLine = line.split(" ");

                        String command = commandLine[0];

                        switch (command) {
                            case "exit":
                            case "quit":
                                return false;
                            case "save":
                                saveUser(commandLine[1]);
                                break;
                            case "get":
                                showUser(Integer.parseInt(commandLine[1]));
                                break;
                            case "alter":
                                int userId = Integer.parseInt(commandLine[1]);
                                String colorAction = commandLine[2];
                                String color = commandLine[3];
                                changeColors(userId, colorAction, color);
                                break;
                            case "help":
                            case "?":
                                System.out.println("save <name>");
                                System.out.println("get <id>");
                                System.out.println("alter <id> [add|remove] <color>");
                                System.out.println("quit|exit");

                        }
                        return true;
                    }
            );
        } finally {
            in.close();
            em.close();
            factory.close();
        }




    }

    private static void noFailLoop(Callable<Boolean> run) {
        boolean shouldContinue;
        do {
            try {
                shouldContinue = run.call();
            } catch (Exception e) {
                System.out.println("Caught exception " + e);
                shouldContinue = true;
            }
        } while (shouldContinue);
    }

    private static void changeColors(int userId, String colorAction, String color) {
        User u;
        em.getTransaction().begin();
        u = em.find(User.class, userId);
        if ("add".equals(colorAction)) {
            u.getColors().add(color);
        } else if ("remove".equals(colorAction)) {
            u.getColors().remove(color);
        }
        em.merge(u);
        em.getTransaction().commit();
    }

    private static void showUser(int userId) {
        User u;
        u = em.find(User.class, userId);
        System.out.println("Found user " + u);
    }

    private static void saveUser(String name) {
        User u;
        u = new User();
        u.setName(name);
        em.getTransaction().begin();
        em.persist(u);
        em.getTransaction().commit();
        //Eclipse link will live without this statement, but hibernate will throw NPE on null collection later
        em.refresh(u);
        System.out.println("Saved user with id " + u.getId());
    }

}
