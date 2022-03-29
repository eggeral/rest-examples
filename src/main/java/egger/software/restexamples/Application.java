package egger.software.restexamples;

import egger.software.restexamples.repository.Database;
import egger.software.restexamples.repository.TasksRepositoryIoc;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class Application extends ResourceConfig {

    public Application() {
        this(null);
    }

    public Application(Database databaseOverride) {

        Database database = databaseOverride != null ? databaseOverride : new Database();
        registerClasses(TasksResource.class);

        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(database).to(Database.class);
                bindAsContract(TasksRepositoryIoc.class);
            }
        });
    }

}