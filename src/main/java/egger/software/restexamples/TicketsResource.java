package egger.software.restexamples;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Path("tickets")
public class TicketsResource {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response one(final @PathParam("id") String id) {
        return Response
                .ok("{\"id\":" + id + "}", MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response find(final @PathParam("id") String id) {

        StreamingOutput fileStream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException {
                InputStream input = getClass().getClassLoader().getResourceAsStream("A Sample PDF.pdf");
                if (input == null)
                    throw new FileNotFoundException("A Sample PDF.pdf");
                byte[] buf = new byte[1024];
                int length;
                while ((length = input.read(buf)) > 0) {
                    output.write(buf, 0, length);
                }
                output.flush();
            }
        };

        return Response
                .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition", "attachment; filename = ticket-" + id)
                .build();
    }
}

