package egger.software.restexamples;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LinkExamples {

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
    public void programmatically_add_links_in_entities() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        WebTarget target = client.target("http://localhost:" + port).path("/api/flights");
        target.request(MediaType.APPLICATION_JSON).post(Entity.entity("" +
                "{" +
                "  \"number\": \"OS1234\"," +
                "  \"from\": \"FRA\"," +
                "  \"to\": \"DUS\"" +
                "}", MediaType.APPLICATION_JSON_TYPE));

        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), is(200));
        JsonNode json = objectMapper.readTree(response.readEntity(String.class));
        System.out.println(objectMapper.writeValueAsString(json));
    }

    @Test
    public void programmatically_add_links_in_headers() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        WebTarget target = client.target("http://localhost:" + port).path("/api/flights");
        target.request(MediaType.APPLICATION_JSON).post(Entity.entity("" +
                "{" +
                "  \"number\": \"OS1234\"," +
                "  \"from\": \"FRA\"," +
                "  \"to\": \"DUS\"" +
                "}", MediaType.APPLICATION_JSON_TYPE));

        Response response = target.path("1").request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), is(200));
        System.out.println(response.getHeaders());
    }
}
