package egger.software.restexamples;

import egger.software.restexamples.entity.Flight;
import egger.software.restexamples.repository.FlightsRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
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

    @Path("{id}/passengers")
    public PassengersResource passengers(@PathParam("id") Long id) {
        return new PassengersResource(id);
    }

}
