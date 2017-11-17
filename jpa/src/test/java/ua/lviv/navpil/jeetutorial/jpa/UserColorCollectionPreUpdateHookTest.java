package ua.lviv.navpil.jeetutorial.jpa;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.lviv.navpil.jeetutorial.jpa.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User color collection will trigger the PreUpdate hook")
class UserColorCollectionPreUpdateHookTest {

    private static EntityManagerFactory factory;
    private EntityManager em;

    @BeforeAll
    static void beforeAll() {
        factory = Persistence.createEntityManagerFactory("Users");
    }

    @BeforeEach
    void beforeEach() {
        em = factory.createEntityManager();
    }

    @AfterEach
    void afterEach() {
        em.close();
    }

    @AfterAll
    static void afterAll() {
        factory.close();
    }

    @Test
    @DisplayName("when colors are changed with setter method")
    void resetCollectionShouldWork() {
        testUpdateCollectionInEntity(
                user -> user.setColors(Arrays.asList("green", "black")));
    }

    @Test
    @DisplayName("when collection is altered with add method")
    void alterCollectionShouldWorkWithoutRefresh() {
        testUpdateCollectionInEntity(
                user -> {
                    user.getColors().clear();
                    user.getColors().addAll(Arrays.asList("silver", "yellow"));
                });
    }


    private void testUpdateCollectionInEntity(Consumer<User> doWithUser) {
        //persist user
        User user = persistUser();

        assertThat(em.find(User.class, user.getId()).getHooksCalled())
                .describedAs("All called hooks")
                .contains(User.PRE_PERSIST)
                .doesNotContain(User.PRE_UPDATE);

        em.refresh(user);

        //update user
        em.getTransaction().begin();
        User u = em.find(User.class, user.getId());
        doWithUser.accept(u);
        em.getTransaction().commit();


        assertThat(em.find(User.class, user.getId()).getHooksCalled())
                .describedAs("All called hooks")
                .contains(User.PRE_PERSIST, User.PRE_UPDATE);

        //remove user
        removeUser(user);
    }

    private User persistUser() {
        em.getTransaction().begin();
        User user = new User();
        user.setName("jim@mail.com");
        user.setColors(Arrays.asList("red", "blue"));
        em.persist(user);
        em.getTransaction().commit();
        return user;
    }

    private void removeUser(User user) {
        em.getTransaction().begin();
        em.remove(user);
        em.getTransaction().commit();
    }

}