package egger.software.restexamples;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import egger.software.restexamples.entity.Flight;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;

import static org.glassfish.jersey.client.authentication.HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD;
import static org.glassfish.jersey.client.authentication.HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueryExamplesTests {

    private static Server server;
    private static Client client;
    private static final int port = 8091;

    @Before
    public void startServer() throws Exception {
        server = Main.startServer(port, Arrays.asList(
                new Flight(1L, "OS2001", "VIE", "DRS"),
                new Flight(2L, "LH1234", "GRZ", "BER"),
                new Flight(3L, "KM6712", "MUN", "FRA")
        ));
        client = ClientBuilder.newClient();
        HttpAuthenticationFeature authenticationFeature = HttpAuthenticationFeature.basicBuilder().build();
        client.register(authenticationFeature);
    }

    @After
    public void stopServer() throws Exception {
        client.close();
        server.stop();
    }

    @Test
    public void search_using_post() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        WebTarget target = client.target("http://localhost:" + port).path("/api/flights/search");
        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(
                "{" +
                        "\"flight\": {" +
                        "\"from\": \"GRZ\"," +
                        "\"to\": \"BER\"" +
                        "}" +
                        "}", MediaType.APPLICATION_JSON_TYPE));
        JsonNode json = objectMapper.readTree(response.readEntity(String.class));
        assertThat(response.getStatus(), is(200));
        System.out.println(json);
        assertThat(json.size(), is(1));
    }

}
