package egger.software.restexamples;

import egger.software.restexamples.repository.Database;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainSystemTests {

    private static Server server;
    private static Client client;
    private static final int port = 8091;

    @BeforeClass
    public static void startServer() {
        Database database = mock(Database.class);
        when(database.get()).thenReturn("[{\"name\":\"task1\"},{\"name\":\"task2\"}]");
        server = Main.startServer(port, database);
        client = ClientBuilder.newClient();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        client.close();
        server.stop();
    }

    @Test
    public void it_can_fetch_tasks() {
        // given
        WebTarget target = client.target("http://localhost:" + port).path("tasks");

        // when
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        // then
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("[{\"name\":\"task1\"},{\"name\":\"task2\"}]")));

    }

}
