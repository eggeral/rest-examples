package egger.software.restexamples.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class CustomInstantSerializer extends StdSerializer<Instant> {

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("HH:mm:ss / dd-MM-yyyy").withZone(ZoneOffset.UTC);

    public CustomInstantSerializer() {
        this(null);
    }

    public CustomInstantSerializer(Class<Instant> t) {
        super(t);
    }

    @Override
    public void serialize(
            Instant value,
            JsonGenerator generator,
            SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        generator.writeString(formatter.format(value));
    }
}
