package egger.software.restexamples;

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

public class MainSystemTests {

    private static Server server;
    private static Client client;
    private static final int port = 8091;

    @BeforeClass
    public static void startServer() {
        server = Main.startServer(port);
        client = ClientBuilder.newClient();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        client.close();
        server.stop();
    }

    @Test
    public void it_should_start_the_server_and_make_the_REST_service_available() {
        // given
        WebTarget target = client
                .target("http://localhost:" + port)
                .path("hello-world")
                .queryParam("name", "Alexander");

        // when
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        // then
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("{\"count\":1,\"value\":\"Hello, Alexander!\"}")));

    }

}
