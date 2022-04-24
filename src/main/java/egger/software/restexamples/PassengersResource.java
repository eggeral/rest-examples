package egger.software.restexamples;

import com.fasterxml.jackson.core.JsonProcessingException;
import egger.software.restexamples.entity.Passenger;
import repository.PassengersRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@Path("/passengers")
public class PassengersResource {
    private final PassengersRepository repository;
    private final EtagCalculator etagCalculator = new EtagCalculator();
    private final Long flightId;

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
    public Response one(@PathParam("id") Long id, @Context Request request)
            throws NoSuchAlgorithmException, JsonProcessingException {
        Passenger passenger = repository.findById(id);
        Response.ResponseBuilder builder = request.evaluatePreconditions(etagCalculator.calculate(passenger));
        if (builder != null) {
            return builder.build();
        }
        return Response.ok(passenger).tag(etagCalculator.calculate(passenger)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Passenger newPassenger) throws NoSuchAlgorithmException, JsonProcessingException {
        Passenger passenger = repository.add(newPassenger);
        return Response.ok(passenger).tag(etagCalculator.calculate(passenger)).build();
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response replace(@PathParam("id") Long id,
                            Passenger newPassenger,
                            @HeaderParam(HttpHeaders.IF_MATCH) String ifMatch,
                            @Context Request request
    ) throws NoSuchAlgorithmException, JsonProcessingException {

        Passenger oldPassenger = repository.findById(id);

        if (ifMatch == null)
            return Response.status(Response.Status.PRECONDITION_REQUIRED).build();
        Response.ResponseBuilder builder = request.evaluatePreconditions(etagCalculator.calculate(oldPassenger));
        if (builder != null) {
            return builder.build();
        }
        Passenger passenger = repository.update(newPassenger.copy(id, null, null));
        return Response.ok(passenger).tag(etagCalculator.calculate(passenger)).build();
    }

    @DELETE
    @Path("{id}")
    public Response deletePassenger(@PathParam("id") Long id,
                                    @HeaderParam(HttpHeaders.IF_MATCH) String ifMatch,
                                    @Context Request request
    ) throws NoSuchAlgorithmException, JsonProcessingException {
        Passenger oldPassenger = repository.findById(id);

        if (ifMatch == null)
            return Response.status(Response.Status.PRECONDITION_REQUIRED).build();
        Response.ResponseBuilder builder = request.evaluatePreconditions(etagCalculator.calculate(oldPassenger));
        if (builder != null) {
            return builder.build();
        }
        repository.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
