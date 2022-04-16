package egger.software.restexamples;

import com.fasterxml.jackson.jaxrs.xml.JacksonJaxbXMLProvider;
import egger.software.restexamples.entity.Flight;
import egger.software.restexamples.repository.FlightsRepository;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.Collections;
import java.util.List;

public class Application extends ResourceConfig {

    public Application() {
        this(Collections.emptyList());
    }

    public Application(List<Flight> initialFlights) {

        FlightsRepository repository = new FlightsRepository(initialFlights);

        register(FlightNumberAlreadyExistsExceptionMapper.class);
        register(FlightsResource.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(repository).to(FlightsRepository.class);
            }
        });

        register(JacksonJaxbXMLProvider.class);

        register(OpenApiResource.class);
    }

}