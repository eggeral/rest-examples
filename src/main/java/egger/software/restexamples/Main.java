package egger.software.restexamples;

import egger.software.restexamples.entity.Flight;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static Server startServer(int port, List<Flight> initialFlights) throws Exception {
        Application application = new Application(initialFlights);
        ResourceConfig config = ResourceConfig.forApplication(application);

        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");

        DefaultServlet staticResources = new DefaultServlet();
        ServletHolder staticResourcesHolder = new ServletHolder("static", staticResources);
        URL resourcesUri = Main.class.getClassLoader().getResource("webapp");
        staticResourcesHolder.setInitParameter("resourceBase", resourcesUri.toString());
        contextHandler.addServlet(staticResourcesHolder, "/swagger-ui/*");

        ServletContainer jerseyServlet = new ServletContainer(config);
        ServletHolder jerseyServletHolder = new ServletHolder("jersey", jerseyServlet);
        contextHandler.addServlet(jerseyServletHolder, "/api/*");

        Server server = new Server(port);
        server.setHandler(contextHandler);
        server.start();
        return server;
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