package egger.software.restexamples;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import egger.software.restexamples.entity.*;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JacksonExamplesTests {

    @Test
    public void read_json() throws IOException {
        URL source = getClass().getResource("/json-example.json");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(source);

        System.out.println("== rootNode ==");
        rootNode.fields().forEachRemaining(entry -> {
            System.out.println("key: " + entry.getKey() + " -> value: " + entry.getValue());
        });

        System.out.println("\n== JsonPointer ==");
        System.out.println(rootNode.at("/object/id"));

        System.out.println("\n== Path ==");
        System.out.println(rootNode.path("object").path("name"));
    }

    @Test
    public void write_json() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("string", "some.string");
        ArrayNode array = rootNode.putArray("array");
        array.add("value1");
        array.add("value2");

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String result = objectMapper.writeValueAsString(rootNode);
        System.out.println("== rootNode ==");
        System.out.println(result);
    }

    @Test
    public void object_mapping() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        Flight flight = new Flight(1L, "OS1234", "GRZ", "MUN");
        String jsonString = objectMapper.writeValueAsString(flight);
        assertThat(jsonString, is(equalTo(
                "{\"id\":1,\"number\":\"OS1234\",\"from\":\"GRZ\",\"to\":\"MUN\"}"
        )));

        Flight readFlight = objectMapper.readValue(jsonString, Flight.class);
        assertThat(readFlight, is(equalTo(flight)));

        Map<String, Object> objectMap = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
        });
        assertThat(objectMap.get("from"), is(equalTo("GRZ")));

    }

    @Test
    public void collections_mapping() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        List<Flight> flights = Arrays.asList(
                new Flight(1L, "OS1234", "GRZ", "MUN"),
                new Flight(2L, "LH5432", "FRA", "BER")
        );
        String jsonString = objectMapper.writeValueAsString(flights);
        assertThat(jsonString, is(equalTo(
                "[" +
                        "{\"id\":1,\"number\":\"OS1234\",\"from\":\"GRZ\",\"to\":\"MUN\"}," +
                        "{\"id\":2,\"number\":\"LH5432\",\"from\":\"FRA\",\"to\":\"BER\"}" +
                        "]"
        )));

        // Funktioniert nicht. Ergibt nur eine Liste von Maps
        List<Flight> readFlights = objectMapper.readValue(jsonString, List.class);

        readFlights = objectMapper.readValue(jsonString, new TypeReference<List<Flight>>() {
        });
        assertThat(readFlights, is(equalTo(flights)));

        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, Flight.class);
        readFlights = objectMapper.readValue(jsonString, collectionType);
        assertThat(readFlights, is(equalTo(flights)));

    }

    @Test
    public void jackson_annotations() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Ticket ticket = new Ticket(
                "id_1234",
                "Muster",
                "Max"
        );
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String result = objectMapper.writeValueAsString(ticket);
        System.out.println(result);
        assertThat(result, is(equalTo("" +
                "{\n" +
                "  \"passengerFirstName\" : \"Muster\",\n" +
                "  \"passengerLastName\" : \"Max\",\n" +
                "  \"ticket_id\" : \"id_1234\"\n" +
                "}"
        )));
        Ticket readTicket = objectMapper.readValue(result, Ticket.class);
        assertThat(readTicket, is(equalTo(ticket)));
    }


    @Test
    public void date_time() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        DateTimeExample example = new DateTimeExample(
                Instant.parse("2020-04-22T11:34:00Z"),
                Instant.parse("2021-03-21T01:45:00Z"),
                Instant.parse("2022-02-20T22:34:00Z")
        );
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String result = objectMapper.writeValueAsString(example);
        System.out.println(result);
        assertThat(result, is(equalTo("" +
                "{\n" +
                "  \"instant\" : 1587555240.000000000,\n" +
                "  \"instantWithCustomDateFormat\" : \"2021-03-21 01:45:00\",\n" +
                "  \"instantWithCustomSerializer\" : \"22:34:00 / 20-02-2022\"\n" +
                "}"
        )));
        DateTimeExample readExample = objectMapper.readValue(result, DateTimeExample.class);
        assertThat(readExample, is(equalTo(example)));
    }

    @Test
    public void enum_example() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        EnumExample example = new EnumExample(
                MessageType.ITEM_NOT_FOUND,
                ShapeObjectMessageType.PROCESSING_DONE,
                MessageType.PROCESSING_FAILED,
                JsonValueMessageType.PROCESSING_DONE
        );
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String result = objectMapper.writeValueAsString(example);
        System.out.println(result);
        assertThat(result, is(equalTo("" +
                "{\n" +
                "  \"defaultJsonHandling\" : \"ITEM_NOT_FOUND\",\n" +
                "  \"shapeObject\" : {\n" +
                "    \"level\" : \"info\",\n" +
                "    \"code\" : 101\n" +
                "  },\n" +
                "  \"shapeNumber\" : 1,\n" +
                "  \"jsonValue\" : 101\n" +
                "}"
        )));
        EnumExample readExample = objectMapper.readValue(result, EnumExample.class);
        assertThat(readExample, is(equalTo(example)));
    }

    @Test
    public void any_getter_setter_example() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        AnyGetterSetterExample example = new AnyGetterSetterExample(
                4, "GRZ", "FRA"
        );
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String result = objectMapper.writeValueAsString(example);
        System.out.println(result);
        assertThat(result, is(equalTo("" +
                "{\n" +
                "  \"from\" : \"GRZ\",\n" +
                "  \"id\" : 4,\n" +
                "  \"to\" : \"FRA\"\n" +
                "}"
        )));
        AnyGetterSetterExample readExample = objectMapper.readValue(result, AnyGetterSetterExample.class);
        assertThat(readExample, is(equalTo(example)));
    }

    @Test
    public void inheritance_example() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        InheritanceExample example = new InheritanceExample(Arrays.asList(
            new Passenger(1L, "Max Muster", "OS1234"),
            new Pilot(3L, "Otto Bruch", "DETC-XUZ")
        ));
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String result = objectMapper.writeValueAsString(example);
        System.out.println(result);
        assertThat(result, is(equalTo("" +
                "{\n" +
                "  \"personsOnFlight\" : [ {\n" +
                "    \"type\" : \"passenger\",\n" +
                "    \"id\" : 1,\n" +
                "    \"name\" : \"Max Muster\",\n" +
                "    \"flightNumber\" : \"OS1234\"\n" +
                "  }, {\n" +
                "    \"type\" : \"pilot\",\n" +
                "    \"id\" : 3,\n" +
                "    \"name\" : \"Otto Bruch\",\n" +
                "    \"licence\" : \"DETC-XUZ\"\n" +
                "  } ]\n" +
                "}"
        )));
        InheritanceExample readExample = objectMapper.readValue(result, InheritanceExample.class);
        assertThat(readExample, is(equalTo(example)));
    }

}
