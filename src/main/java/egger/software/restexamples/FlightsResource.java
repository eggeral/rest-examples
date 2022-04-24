package egger.software.restexamples;

import egger.software.restexamples.entity.Flight;
import egger.software.restexamples.entity.Query;
import egger.software.restexamples.repository.FlightsRepository;
import egger.software.restexamples.repository.PassengersRepository;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Objects;

@Path("/flights")
public class FlightsResource {
    private final FlightsRepository flightsRepository;
    private final PassengersRepository passengersRepository;

    @Inject
    public FlightsResource(FlightsRepository flightsRepository, PassengersRepository passengersRepository) {
        this.flightsRepository = flightsRepository;
        this.passengersRepository = passengersRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Flight> all(@Context SecurityContext securityContext) {
        return flightsRepository.findAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Flight one(@PathParam("id") Long id) {
        return flightsRepository.findById(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Flight add(Flight newFlight) {
        return flightsRepository.add(newFlight);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Flight replace(@PathParam("id") Long id, Flight newFlight) {
        return flightsRepository.update(newFlight.copy(id, null, null, null, null));
    }

    @DELETE
    @Path("{id}")
    public void deleteFlight(@PathParam("id") Long id, @QueryParam("version") Long version) {
        flightsRepository.delete(id, version);
    }

    @GET
    @Path("flight")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Flight> findByNumber(@QueryParam("number") String number) {
        return flightsRepository.find(flight -> Objects.equals(flight.getNumber(), number));
    }

    @POST
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Flight> search(Query query) {
        Flight queryFlight = query.getFlight();
        return flightsRepository.find(flight ->
                (queryFlight.getFrom() == null || Objects.equals(flight.getFrom(), queryFlight.getFrom())) &&
                (queryFlight.getTo() == null || Objects.equals(flight.getTo(), queryFlight.getTo())) &&
                (queryFlight.getNumber() == null || Objects.equals(flight.getNumber(), queryFlight.getNumber()))
        );
    }

    @Path("{id}/passengers")
    public PassengersResource getPassengersResource(@PathParam("id") Long id) {
        return new PassengersResource(passengersRepository, id);
    }

}
