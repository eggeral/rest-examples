package egger.software.restexamples;

import egger.software.restexamples.entity.Flight;
import egger.software.restexamples.repository.FlightsRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    @Produces(MediaType.APPLICATION_JSON)
    public List<Flight> all() {
        return repository.findAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get a flight by it's id",
            responses = {
                    @ApiResponse(description = "The flight",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Flight.class))),
                    @ApiResponse(responseCode = "400", description = "Flight not found")})
    public Flight one(@PathParam("id") Long id) {
        return repository.findById(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Flight add(Flight newFlight) {
        return repository.add(newFlight);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
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
    @Produces(MediaType.APPLICATION_JSON)
    public List<Flight> findByNumber(@QueryParam("number") String number) {
        return repository.find(flight -> Objects.equals(flight.getNumber(), number));
    }

}
