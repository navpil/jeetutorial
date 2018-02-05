package ua.lviv.navpil.jeetutorial.jaxrs;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/myapp/*")
public class Application extends ResourceConfig {

    public Application() {
        super(
                MyResource.class
        );
    }

}
