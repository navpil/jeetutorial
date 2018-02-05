package ua.lviv.navpil.arquilliandemo;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.UUID;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@RunWith(Arquillian.class)
public class ArquillianDemo {

    @Inject
    private OneResourceUsingAdmin resource;

    @Inject
    private AnotherResourceUsingNameService resourceUsingAdmin;

    @Deployment
    public static JavaArchive createJar() {
        JavaArchive javaArchive = ShrinkWrap.create(JavaArchive.class)
                .addClasses(
//                        OneResourceUsingAdmin.class,
//                        AnotherResourceUsingNameService.class,
//                        CachingNameService.class,
//                        NameService.class
                )
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(javaArchive.toString(true));
        return javaArchive;
    }

    @Test
    public void name() throws Exception {
        System.out.println(resource.getNameService().getName());
        System.out.println(resourceUsingAdmin.getNameService().getName());
    }

    public static class AnotherResourceUsingNameService {

        @Inject
        private NameService nameService;

        @Inject@Any
        private Instance<NameService> services;

        @Inject@RealImplementation
        private NameService service2;

        public NameService getNameService() {
            return nameService;
        }
    }

    public interface NameService {
        String getName();
    }

    @ApplicationScoped
    public static class CachingNameService implements NameService {

        @Inject@RealImplementation
        private NameService delegate;

        private String name;

        public String getName() {
            if (name == null) {
                name = delegate.getName();
            }
            return name;
        }
    }

    @RealImplementation
    public static class NameServiceImpl implements NameService {

        private String userName = UUID.randomUUID().toString();

        public String getName() {
            return userName;
        }
    }

    public static class OneResourceUsingAdmin {

        @Inject
        private NameService nameService;

        public NameService getNameService() {
            return nameService;
        }
    }


    @Qualifier
    @Retention(RUNTIME)
    @Target({METHOD, FIELD, PARAMETER, TYPE})
    public @interface RealImplementation {
    }

    @Qualifier
    @Retention(RUNTIME)
    @Target({METHOD, FIELD, PARAMETER, TYPE})
    public @interface CachedImplementation {
    }

}
