package egger.software.restexamples;

import com.fasterxml.jackson.jaxrs.xml.JacksonJaxbXMLProvider;
import egger.software.restexamples.entity.Flight;
import repository.FlightsRepository;
import repository.PassengersRepository;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.ApplicationPath;
import java.util.Collections;
import java.util.List;


@ApplicationPath("/api")
public class Application extends ResourceConfig {

    public Application() {
        this(Collections.emptyList());
    }

    public Application(List<Flight> initialFlights) {

        FlightsRepository flightsRepository = new FlightsRepository(initialFlights);
        PassengersRepository passengersRepository = new PassengersRepository(Collections.emptyList());

        register(FlightNumberAlreadyExistsExceptionMapper.class);
        register(FlightsResource.class);
        register(PassengersResource.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(flightsRepository).to(FlightsRepository.class);
                bind(passengersRepository).to(PassengersRepository.class);
            }
        });

        register(JacksonJaxbXMLProvider.class);

        register(OpenApiResource.class);
        register(OpenApiDefinition.class);

        register(DeclarativeLinkingFeature.class);
        register(RolesAllowedDynamicFeature.class);
    }

}