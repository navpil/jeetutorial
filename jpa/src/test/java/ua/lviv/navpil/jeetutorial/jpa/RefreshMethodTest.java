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
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("EntityManager::refresh method is used to not create the entity managers for every operation")
class RefreshMethodTest {

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
    @DisplayName(", otherwise it may return null for empty collections")
    void nullCollectionsWithoutRefreshAreNull() {
        testUpdateCollectionInEntity(
                null,
                false,
                user -> {
                    assertThat(user.getColors())
                            .describedAs("List of User colors")
                            .isNull();
                });
    }

    @Test
    @DisplayName(", so collections are empty, not null")
    void nullCollectionWithRefreshIsNull() {
        testUpdateCollectionInEntity(
                null,
                true,
                user -> {
                    assertThat(user.getColors())
                            .describedAs("List of User colors")
                            .isNotNull();
                });
    }

    @Test
    @DisplayName(", so that we can alter collections")
    void alterCollectionShouldWorkWithRefresh() {
        testUpdateCollectionInEntity(
                Arrays.asList("red", "blue"),
                true,
                user -> user.getColors().add("silver"));
    }

    @Test
    @DisplayName(", otherwise we will get an exception while altering originally immutable collection")
    void alterCollectionShouldFailWithoutRefresh() {
        testUpdateCollectionInEntity(
                Arrays.asList("red", "blue"),
                false,
                user -> assertThatExceptionOfType(UnsupportedOperationException.class)
                        .isThrownBy(() -> user.getColors().add("silver")));
    }


    private void testUpdateCollectionInEntity(List<String> initialColors, boolean shouldRefresh, Consumer<User> doWithUser) {
        //persist user
        User user = persistUser(initialColors);

        if(shouldRefresh) {
            em.refresh(user);
        }

        //fetch user and do something with it
        em.getTransaction().begin();
        User u = em.find(User.class, user.getId());
        doWithUser.accept(u);
        em.getTransaction().commit();

        //remove user
        removeUser(em.find(User.class, user.getId()));
    }

    private void removeUser(User user) {
        em.getTransaction().begin();
        em.remove(user);
        em.getTransaction().commit();
    }

    private User persistUser(List<String> colors) {
        em.getTransaction().begin();
        User user = new User();
        user.setName("jim@mail.com");
        user.setColors(colors);
        em.persist(user);
        em.getTransaction().commit();
        return user;
    }

}