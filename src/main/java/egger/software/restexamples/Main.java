package egger.software.restexamples;

import egger.software.restexamples.entity.Flight;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static Server startServer(int port, List<Flight> initialFlights) {
        URI baseUri = UriBuilder.fromUri("http://localhost").port(port).build();
        Application application = new Application(initialFlights);
        ResourceConfig config = ResourceConfig.forApplication(application);
        return JettyHttpContainerFactory.createServer(baseUri, config);
    }

    public static void main(String[] args) {
        try {
            List<Flight> initialFlights = Arrays.asList(
                    new Flight(1L, "OS2001", "VIE", "DRS"),
                    new Flight(2L, "LH1234", "GRZ", "BER"),
                    new Flight(3L, "KM6712", "MUN", "FRA")
            );
            Server server = startServer(80, initialFlights);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("Shutting down the server...");
                    server.stop();
                    System.out.println("Done.");
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }));

            System.out.println("Server started.\nCTRL+C to exit.");
            server.join();

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

}