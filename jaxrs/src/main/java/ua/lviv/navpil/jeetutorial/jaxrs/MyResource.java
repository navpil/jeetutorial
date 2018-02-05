package ua.lviv.navpil.jeetutorial.jaxrs;

import org.glassfish.jersey.internal.util.Base64;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.util.StringTokenizer;

@Path("my-res")
public class MyResource {

    @POST
    @Path("one")
    public Response one(@HeaderParam("Authorization") String authorization) {
        if (authorization != null && !authorization.isEmpty()) {
            String encodedUserPassword = authorization.replaceFirst("Basic ", "");
            String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));
            int firstColon = usernameAndPassword.indexOf(":");
            if (firstColon >= 0) {
                String username = usernameAndPassword.substring(0, firstColon);
                String password = usernameAndPassword.substring(firstColon + 1);
                if ("admin".equals(username) && "root".equals(password)) {
                    return Response.ok().entity("Hello you").build();
                }
            }
        }

        return Response
                .status(401)
                .header("WWW-Authenticate", "Basic realm=\"My Realm\"")
                .entity("You can't access the resource")
                .build();

    }

    @GET
    @Path("two")
    public Response two() {
        return Response.status(200).entity("All is good").build();
    }
}
