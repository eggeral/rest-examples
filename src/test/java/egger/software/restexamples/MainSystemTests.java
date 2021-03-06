package egger.software.restexamples;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.server.Server;
import org.junit.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class MainSystemTests {

    private static Server server;
    private static Client client;
    private static final int port = 8091;

    @Before
    public void startServer() throws Exception {
        server = Main.startServer(port, Collections.emptyList());
        client = ClientBuilder.newClient();
    }

    @After
    public void stopServer() throws Exception {
        client.close();
        server.stop();
    }

    @Test
    public void flights_can_be_created_read_updated_and_deleted() {

        // given
        WebTarget target = client.target("http://localhost:" + port).path("/api/flights");

        // when
        // GET ALL
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        // then
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo(
                "[]"
        )));

        // and when
        // ADD FLIGHT
        response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity("" +
                "{" +
                "  \"number\": \"OS1234\"," +
                "  \"from\": \"FRA\"," +
                "  \"to\": \"DUS\"" +
                "}", MediaType.APPLICATION_JSON_TYPE));

        // then
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo(
                "{\"id\":1,\"number\":\"OS1234\",\"from\":\"FRA\",\"to\":\"DUS\"}"
        )));
        assertThat(target.request(MediaType.APPLICATION_JSON).get().readEntity(String.class), is(equalTo(
                "[{\"id\":1,\"number\":\"OS1234\",\"from\":\"FRA\",\"to\":\"DUS\"}]"
        )));

        // and when
        // UPDATE FLIGHT
        response = target.path("1").request(MediaType.APPLICATION_JSON).put(Entity.entity("" +
                "{" +
                "  \"number\": \"OS1234\"," +
                "  \"from\": \"BER\"," +
                "  \"to\": \"DUS\"" +
                "}", MediaType.APPLICATION_JSON_TYPE));

        // then
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo(
                "{\"id\":1,\"number\":\"OS1234\",\"from\":\"BER\",\"to\":\"DUS\"}"
        )));
        assertThat(target.request(MediaType.APPLICATION_JSON).get().readEntity(String.class), is(equalTo(
                "[{\"id\":1,\"number\":\"OS1234\",\"from\":\"BER\",\"to\":\"DUS\"}]"
        )));

        // and when
        // GET FLIGHT
        response = target.path("1").request(MediaType.APPLICATION_JSON).get();

        // then
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo(
                "{\"id\":1,\"number\":\"OS1234\",\"from\":\"BER\",\"to\":\"DUS\"}"
        )));

        // and when
        // FIND FLIGHTS BY NUMBER
        response = target.path("flight").queryParam("number", "OS1234").request(MediaType.APPLICATION_JSON).get();

        // then
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo(
                "[{\"id\":1,\"number\":\"OS1234\",\"from\":\"BER\",\"to\":\"DUS\"}]"
        )));

        // and when
        // DELETE FLIGHT
        response = target.path("1").request(MediaType.APPLICATION_JSON).delete();

        // then
        assertThat(response.getStatus(), is(equalTo(204)));
        assertThat(target.request(MediaType.APPLICATION_JSON).get().readEntity(String.class), is(equalTo(
                "[]"
        )));

    }

    @Test
    public void a_flight_with_the_same_number_can_not_be_created_twice() {
        // given
        WebTarget target = client.target("http://localhost:" + port).path("/api/flights");
        target.request(MediaType.APPLICATION_JSON).post(Entity.entity("" +
                "{" +
                "  \"number\": \"OS1234\"," +
                "  \"from\": \"FRA\"," +
                "  \"to\": \"DUS\"" +
                "}", MediaType.APPLICATION_JSON_TYPE));

        // when
        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity("" +
                "{" +
                "  \"number\": \"OS1234\"," +
                "  \"from\": \"FRA\"," +
                "  \"to\": \"DUS\"" +
                "}", MediaType.APPLICATION_JSON_TYPE));

        // then
        assertThat(response.getStatus(), is(equalTo(409)));
        assertThat(response.readEntity(String.class), is(equalTo(
                "Flight with number OS1234 already exists"
        )));
    }

    @Test
    public void a_flight_contains_a_link_to_its_passengers() throws JsonProcessingException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        WebTarget target = client.target("http://localhost:" + port).path("/api/flights");
        target.request(MediaType.APPLICATION_JSON).post(Entity.entity("" +
                "{" +
                "  \"number\": \"OS1234\"," +
                "  \"from\": \"FRA\"," +
                "  \"to\": \"DUS\"" +
                "}", MediaType.APPLICATION_JSON_TYPE));

        // when
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseString = response.readEntity(String.class);
        JsonNode json = objectMapper.readTree(responseString);

        // then
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(responseString, is(equalTo(
                "[" +
                        "{" +
                        "\"id\":1," +
                        "\"number\":\"OS1234\"," +
                        "\"from\":\"FRA\"," +
                        "\"to\":\"DUS\"," +
                        "\"passengersLink\":{\"uri\":\"1/passengers\",\"params\":{\"rel\":\"passengers\"},\"uriBuilder\":{\"absolute\":false},\"rels\":\"passengers\",\"title\":null,\"rels\":[\"passengers\"],\"type\":null}}" +
                        "}" +
                        "]"
        )));
        

    }
}