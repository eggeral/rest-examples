package egger.software.restexamples;

import org.eclipse.jetty.http.MultiPartFormInputStream;
import org.eclipse.jetty.server.Server;
import org.junit.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Form;
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
    public void startServer() {
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
        WebTarget target = client.target("http://localhost:" + port).path("flights");

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
        WebTarget target = client.target("http://localhost:" + port).path("flights");
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
    public void tickets_can_be_fetched_as_json() {
        // given
        WebTarget target = client.target("http://localhost:" + port).path("tickets");

        // when
        Response response = target.path("1").request(MediaType.APPLICATION_JSON).get();

        // then
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo(
                "{\"id\":1}"
        )));
    }

    @Test
    public void tickets_can_be_downloaded_as_pdf() {
        // given
        WebTarget target = client.target("http://localhost:" + port).path("tickets");

        // when
        Response response = target.path("1").request(MediaType.APPLICATION_OCTET_STREAM).get();

        // then
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(startsWith(
                "%PDF"
        )));
    }

    @Test
    public void consumes_annotation_example() {
        WebTarget target = client.target("http://localhost:" + port).path("annotations");

        // @Consumes(MediaType.APPLICATION_JSON)
        Response response = target.path("consumes/string").request(MediaType.APPLICATION_JSON).post(Entity.entity("test", MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("consumesJson")));

        // @Consumes(MediaType.XML)
        response = target.path("consumes/string").request(MediaType.APPLICATION_JSON).post(Entity.entity("test", MediaType.APPLICATION_XML));
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("consumesXml")));

        // @Consumes(MediaType.APPLICATION_JSON) with automatic Entity conversion
        response = target.path("consumes/flight").request(MediaType.APPLICATION_JSON).post(Entity.entity("" +
                        "{" +
                        "  \"number\": \"OS1234\"," +
                        "  \"from\": \"FRA\"," +
                        "  \"to\": \"DUS\"" +
                        "}",
                MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("consumesJsonFlight: Flight{id=null, number='OS1234', from='FRA', to='DUS'}")));

        // @Consumes(MediaType.APPLICATION_XML) with automatic Entity conversion
        response = target.path("consumes/flight").request(MediaType.APPLICATION_JSON).post(Entity.entity("" +
                        "<Flight>" +
                        "   <number>OS1234</number>" +
                        "   <from>FRA</from>" +
                        "   <to>DUS</to>" +
                        "</Flight>",
                MediaType.APPLICATION_XML));
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("consumesXmlFlight: Flight{id=null, number='OS1234', from='FRA', to='DUS'}")));

    }

    @Test
    public void param_annotations_example() {
        WebTarget target = client.target("http://localhost:" + port).path("annotations");

        // @Path("pathparams/{param1}/{param2}")
        WebTarget pathParamsTarget = target.path("pathparams/strparam/5432");
        Response response = pathParamsTarget.request(MediaType.APPLICATION_JSON).get();
        assertThat(pathParamsTarget.getUri().toString(), is(equalTo("http://localhost:8091/annotations/pathparams/strparam/5432")));
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("pathParams -  param1: strparam, param2: 5432")));

        // @Path("queryparams")
        WebTarget queryParamsTarget = target.path("queryparams")
                .queryParam("param1", "strparam").queryParam("param2", "5432");
        response = queryParamsTarget.request(MediaType.APPLICATION_JSON).get();
        assertThat(queryParamsTarget.getUri().toString(), is(equalTo("http://localhost:8091/annotations/queryparams?param1=strparam&param2=5432")));
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("queryParams -  param1: strparam, param2: 5432")));

        // @Path("matrixparams/flight")")
        WebTarget matrixParamsTarget = target.path("matrixparams/flight")
                .matrixParam("flightnumber", "OS1234");
        response = matrixParamsTarget.request(MediaType.APPLICATION_JSON).get();
        assertThat(matrixParamsTarget.getUri().toString(), is(equalTo("http://localhost:8091/annotations/matrixparams/flight;flightnumber=OS1234")));
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("matrixParams -  flightNumber: OS1234")));

        // @Path("matrixparams/flight/passenger")")
        matrixParamsTarget = target.path("matrixparams/flight")
                .matrixParam("flightnumber", "OS1234")
                .path("passenger")
                .matrixParam("passengerid", "p12_c453");
        response = matrixParamsTarget.request(MediaType.APPLICATION_JSON).get();
        assertThat(matrixParamsTarget.getUri().toString(), is(equalTo("http://localhost:8091/annotations/matrixparams/flight;flightnumber=OS1234/passenger;passengerid=p12_c453")));
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("matrixParams -  flightNumber: OS1234, passengerId: p12_c453")));

        // @Path("defaultvalues/default")")
        WebTarget defaultValuesTarget = target.path("defaultvalues/default")
                .path("pathParamValuesomething")
                .queryParam("queryparam", "queryParamValue");
        response = defaultValuesTarget.request(MediaType.APPLICATION_JSON).get();
        assertThat(defaultValuesTarget.getUri().toString(), is(equalTo("http://localhost:8091/annotations/defaultvalues/default/pathParamValuesomething?queryparam=queryParamValue")));
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("defaultvalues -  pathParam: pathParamValue, queryParam: queryParamValue")));

        defaultValuesTarget = target.path("defaultvalues/default")
                .path("something");
        response = defaultValuesTarget.request(MediaType.APPLICATION_JSON).get();
        assertThat(defaultValuesTarget.getUri().toString(), is(equalTo("http://localhost:8091/annotations/defaultvalues/default/something")));
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("defaultvalues -  pathParam: , queryParam: queryParamDefaultValue")));

        // @Path("cookieparams")
        response = target.path("cookieparams").request(MediaType.APPLICATION_JSON).cookie("param", "value").get();
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("cookieParams: value")));

        // @Path("headerparams")
        response = target.path("headerparams").request(MediaType.APPLICATION_JSON).header("param", "value").get();
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("headerParams: value")));

        // @Path("beanparams/{pathparam}")
        WebTarget beanParamsTarget = target.path("beanparams").path("pathparamvalue").queryParam("queryparam", "queryparamvalue");
        response = beanParamsTarget.request(MediaType.APPLICATION_JSON).get();
        assertThat(beanParamsTarget.getUri().toString(), is(equalTo("http://localhost:8091/annotations/beanparams/pathparamvalue?queryparam=queryparamvalue")));
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("ExampleBeanParam{pathParam='pathparamvalue', queryParam='queryparamvalue'}")));

        // @Path("formparams")
        Form form = new Form();
        form.param("param", "value");
        response = target.path("formparams").request(MediaType.APPLICATION_JSON).post(Entity.form(form));
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("formParams: value")));

        // @Path("context/{pathparam}")
        WebTarget contextTarget = target.path("context").path("pathparamvalue").queryParam("queryparam", "queryparamvalue");
        response = contextTarget.request(MediaType.APPLICATION_JSON).get();
        assertThat(contextTarget.getUri().toString(), is(equalTo("http://localhost:8091/annotations/context/pathparamvalue?queryparam=queryparamvalue")));
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("context: {pathparam=[pathparamvalue]}, {queryparam=[queryparamvalue]}")));

        // @Path("encoded")
        WebTarget encodedTarget = target.path("encoded")
                .queryParam("decoded", "hello world öäü")
                .queryParam("encoded", "hello world öäü");
        response = encodedTarget.request(MediaType.APPLICATION_JSON).get();
        assertThat(encodedTarget.getUri().toString(), is(equalTo("http://localhost:8091/annotations/encoded?decoded=hello+world+%C3%B6%C3%A4%C3%BC&encoded=hello+world+%C3%B6%C3%A4%C3%BC")));
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class), is(equalTo("encoded: Decoded param value: hello world öäü, encoded param value: hello+world+%C3%B6%C3%A4%C3%BC")));

    }

    @Test
    public void sub_resources_example() {
        WebTarget target = client.target("http://localhost:" + port).path("flights");
        Response response = target.path("1").path("passengers").request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class),
                is(equalTo("[{\"id\":1,\"name\":\"Harald Test\"},{\"id\":2,\"name\":\"Max Muster\"}]")));
        response = target.path("1").path("passengers").path("2").request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.readEntity(String.class),
                is(equalTo("{\"id\":1,\"name\":\"Harald Test\"}")));
    }
}