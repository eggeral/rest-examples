package egger.software.restexamples;

import egger.software.restexamples.entity.Passenger;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.Arrays;
import java.util.List;

@Path("/passengers")
public class PassengersResource {

    private Long flightId;

    @Inject
    public PassengersResource(Long flightId) {
        this.flightId = flightId;
    }

    @GET
    public List<Passenger> all() {
        return Arrays.asList(
                new Passenger(1L, "Harald Test", "OS1234"),
                new Passenger(2L, "Max Muster", "LH5678")
        );
    }

    @GET
    @Path("{id}")
    public Passenger one(@PathParam("id") Long id) {
        return new Passenger(1L, "Harald Test", "OS1234");
    }


}
