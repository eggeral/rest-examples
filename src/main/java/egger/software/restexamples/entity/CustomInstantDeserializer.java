package egger.software.restexamples.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class CustomInstantDeserializer extends StdDeserializer<Instant> {

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("HH:mm:ss / dd-MM-yyyy").withZone(ZoneOffset.UTC);

    public CustomInstantDeserializer() {
        this(null);
    }

    public CustomInstantDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Instant deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return Instant.from(formatter.parse(jsonparser.getText()));
    }
}