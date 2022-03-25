package egger.software.restexamples;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

@Path("/hello-world")
public class HelloResource {

    private final AtomicLong counter = new AtomicLong();
    private static final String template = "Hello, %s!";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Message hello(@QueryParam("name") String name) {
        return new Message(counter.incrementAndGet(), String.format(template, name));
    }

    private static class Message {
        public Long count;
        public String value;

        public Message(Long count, String value) {
            this.count = count;
            this.value = value;
        }
    }
}