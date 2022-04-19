package egger.software.restexamples;

import egger.software.restexamples.entity.Flight;
import egger.software.restexamples.entity.Passenger;
import egger.software.restexamples.repository.FlightsRepository;
import egger.software.restexamples.repository.PassengersRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Objects;

@Path("/passengers")
public class PassengersResource {
    private final PassengersRepository repository;
    private Long flightId;

    @Inject
    public PassengersResource(PassengersRepository repository, Long flightId) {
        this.repository = repository;
        this.flightId = flightId;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Passenger> all() {
        return repository.find(passenger -> Objects.equals(passenger.getFlightId(), flightId));
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Passenger one(@PathParam("id") Long id) {
        return repository.findById(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Passenger add(Passenger newPassenger) {
        return repository.add(newPassenger);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Passenger replace(@PathParam("id") Long id, Passenger newPassenger) {
        return repository.update(newPassenger.copy(id, null, null));
    }

    @DELETE
    @Path("{id}")
    public void deletePassenger(@PathParam("id") Long id) {
        repository.delete(id);
    }

}
