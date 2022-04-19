package egger.software.restexamples;

import com.fasterxml.jackson.jaxrs.xml.JacksonJaxbXMLProvider;
import egger.software.restexamples.entity.Flight;
import egger.software.restexamples.repository.FlightsRepository;
import egger.software.restexamples.repository.PassengersRepository;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

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
    }

}