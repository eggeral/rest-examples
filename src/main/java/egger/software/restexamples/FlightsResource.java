package egger.software.restexamples;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import egger.software.restexamples.entity.Flight;
import egger.software.restexamples.repository.FlightsRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URL;
import java.util.List;
import java.util.Objects;

@Path("/flights")
public class FlightsResource {
    private final FlightsRepository repository;

    @Inject
    public FlightsResource(FlightsRepository repository) {
        this.repository = repository;
    }

    @GET
    public List<Flight> all() {
        return repository.findAll();
    }

    @GET
    @Path("{id}")
    public Flight one(@PathParam("id") Long id) {
        return repository.findById(id);
    }

    @POST
    public Flight add(Flight newFlight) {
        return repository.add(newFlight);
    }

    @PUT
    @Path("{id}")
    public Flight replace(@PathParam("id") Long id, Flight newFlight) {
        return repository.update(newFlight.copy(id, null, null, null));
    }

    @DELETE
    @Path("{id}")
    public void deleteFlight(@PathParam("id") Long id) {
        repository.delete(id);
    }

    @GET
    @Path("flight")
    public List<Flight> findByNumber(@QueryParam("number") String number) {
        return repository.find(flight -> Objects.equals(flight.getNumber(), number));
    }

}
