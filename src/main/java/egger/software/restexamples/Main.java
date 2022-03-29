package egger.software.restexamples;

import egger.software.restexamples.repository.Database;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static Server startServer(int port, Database databaseOverride) {
        URI baseUri = UriBuilder.fromUri("http://localhost").port(port).build();
        Application application = new Application(databaseOverride);
        ResourceConfig config = ResourceConfig.forApplication(application);
        return JettyHttpContainerFactory.createServer(baseUri, config);
    }

    public static void main(String[] args) {
        
        try {
            Server server = startServer(8090, null);

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