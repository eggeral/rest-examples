package egger.software.restexamples;

import egger.software.restexamples.entity.Flight;
import egger.software.restexamples.repository.FlightsRepository;
import egger.software.restexamples.repository.PassengersRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public List<Flight> all() {
        return flightsRepository.findAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response one(@PathParam("id") Long id) {
        return Response
                .ok()
                .links(
                        Link.fromUri("{flightId}/passengers").rel("passengers").build(id),
                        Link.fromUri("{flightId}/pilot").rel("pilot").build(id)
                )
                .entity(flightsRepository.findById(id))
                .build();
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
        return flightsRepository.update(newFlight.copy(id, null, null, null));
    }

    @DELETE
    @Path("{id}")
    public void deleteFlight(@PathParam("id") Long id) {
        flightsRepository.delete(id);
    }

    @GET
    @Path("flight")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Flight> findByNumber(@QueryParam("number") String number) {
        return flightsRepository.find(flight -> Objects.equals(flight.getNumber(), number));
    }

    @Path("{id}/passengers")
    public PassengersResource getPassengersResource(@PathParam("id") Long id) {
        return new PassengersResource(passengersRepository, id);
    }

}
