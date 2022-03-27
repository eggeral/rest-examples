package egger.software.restexamples;

import egger.software.restexamples.entity.Message;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

@Path("/hello-world")
public class HelloResource {

    private final AtomicLong counter = new AtomicLong();
    private static final String template = "Hello, %s!";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Message hello(@QueryParam("name") @DefaultValue("World") String name) {
        return new Message(counter.incrementAndGet(), String.format(template, name));
    }

}