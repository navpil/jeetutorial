package ua.lviv.navpil.jeetutorial;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    public static final String PRE_PERSIST = "PrePersist";
    public static final String PRE_UPDATE = "PreUpdate";
    @Id
    @GeneratedValue
    private int id;

    private String name;

    @ElementCollection
    private List<String> colors;

    @ElementCollection
    private Set<String> hooksCalled;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public Set<String> getHooksCalled() {
        if (hooksCalled == null) {
            hooksCalled = new HashSet<>();
        }
        return hooksCalled;
    }

    public void setHooksCalled(Set<String> hooksCalled) {
        this.hooksCalled = hooksCalled;
    }

    @PrePersist
    public void prePersistHook() {
        getHooksCalled().add(PRE_PERSIST);
    }

    @PreUpdate
    public void preUpdateHook() {
        getHooksCalled().add(PRE_UPDATE);
    }
}
