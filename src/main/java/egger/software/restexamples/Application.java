package egger.software.restexamples;

import egger.software.restexamples.entity.Flight;
import egger.software.restexamples.repository.FlightsRepository;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import java.util.List;

@ApplicationPath("/api")
public class Application extends ResourceConfig {

    public Application() {
    }

    public Application(List<Flight> initialFlights) {

        FlightsRepository repository = new FlightsRepository(initialFlights);

        register(FlightNumberAlreadyExistsExceptionMapper.class);
        register(FlightsResource.class);
        register(TicketsResource.class);
        register(AnnotationsExamplesResource.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(repository).to(FlightsRepository.class);
            }
        });

        register(com.fasterxml.jackson.jaxrs.xml.JacksonJaxbXMLProvider.class);

    }

}