package egger.software.restexamples;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EtagExamplesTests {

    private static Server server;
    private static Client client;
    private static final int port = 8091;

    @Before
    public void startServer() throws Exception {
        server = Main.startServer(port, Collections.emptyList());
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
    public void optimistic_locking_with_version_numbers() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        WebTarget target = client.target("http://localhost:" + port).path("/api/flights");
        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(
                "{" +
                        "\"number\": \"OS1234\"," +
                        "\"from\": \"GRZ\"," +
                        "\"to\": \"BER\"" +
                        "}", MediaType.APPLICATION_JSON_TYPE));
        JsonNode json = objectMapper.readTree(response.readEntity(String.class));
        System.out.println(json);
        assertThat(response.getStatus(), is(200));
        response = target.path("1").request(MediaType.APPLICATION_JSON).put(Entity.entity(
                "{" +
                        "\"number\": \"OS1234\"," +
                        "\"from\": \"GRZ\"," +
                        "\"to\": \"BER\"" +
                        "}", MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(500));

        response = target.path("1").request(MediaType.APPLICATION_JSON).put(Entity.entity(
                "{" +
                        "\"number\": \"OS1234\"," +
                        "\"from\": \"GRZ\"," +
                        "\"to\": \"BER\"," +
                        "\"version\": 1" +
                        "}", MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
        json = objectMapper.readTree(response.readEntity(String.class));
        System.out.println(json);

        response = target.path("1").request(MediaType.APPLICATION_JSON).delete();
        assertThat(response.getStatus(), is(500));

        response = target.path("1").queryParam("version", 2).request(MediaType.APPLICATION_JSON).delete();
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void optimistic_locking_with_etags() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        WebTarget target = client.target("http://localhost:" + port).path("/api/flights/1/passengers");
        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(
                "{" +
                        "\"name\": \"Max Muster\"," +
                        "\"flightId\": 123," +
                        "\"type\": \"passenger\"" +
                        "}", MediaType.APPLICATION_JSON_TYPE));
        JsonNode json = objectMapper.readTree(response.readEntity(String.class));
        System.out.println(json);
        String eTag = response.getHeaderString("etag");
        System.out.println(eTag);
        assertThat(response.getStatus(), is(200));

        response = target.path("1").request(MediaType.APPLICATION_JSON).put(Entity.entity(
                "{" +
                        "\"name\": \"John Doe\"," +
                        "\"flightId\": 123," +
                        "\"type\": \"passenger\"" +
                        "}", MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(428));

        response = target.path("1").request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, new EntityTag("wrong").toString()).put(Entity.entity(
                        "{" +
                                "\"name\": \"John Doe\"," +
                                "\"flightId\": 123," +
                                "\"type\": \"passenger\"" +
                                "}", MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(412));

        response = target.path("1").request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, eTag).put(Entity.entity(
                        "{" +
                                "\"name\": \"John Doe\"," +
                                "\"flightId\": 123," +
                                "\"type\": \"passenger\"" +
                                "}", MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
        eTag = response.getHeaderString("etag");

        response = target.path("1").request(MediaType.APPLICATION_JSON).delete();
        assertThat(response.getStatus(), is(428));

        response = target.path("1").request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, new EntityTag("wrong").toString()).delete();
        assertThat(response.getStatus(), is(412));

        response = target.path("1").request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, eTag).delete();
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void caching_with_etags() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        WebTarget target = client.target("http://localhost:" + port).path("/api/flights/1/passengers");
        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(
                "{" +
                        "\"name\": \"Max Muster\"," +
                        "\"flightId\": 123," +
                        "\"type\": \"passenger\"" +
                        "}", MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
        String eTag = response.getHeaderString("etag");

        response = target.path("1").request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), is(200));

        response = target.path("1").request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_NONE_MATCH, new EntityTag("wrong").toString()).get();
        assertThat(response.getStatus(), is(200));

        response = target.path("1").request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_NONE_MATCH, eTag).get();
        assertThat(response.getStatus(), is(304));
    }

}
