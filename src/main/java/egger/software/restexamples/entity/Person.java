package egger.software.restexamples.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Pilot.class, name = "pilot"),
        @JsonSubTypes.Type(value = Passenger.class, name = "passenger")
})
public interface Person {
    Long getId();

    String getName();
}
