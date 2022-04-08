package egger.software.restexamples.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Instant;

public class DateTimeExample {
    @JsonProperty
    private Instant instant;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant instantWithCustomDateFormat;

    @JsonProperty
    @JsonDeserialize(using = CustomInstantDeserializer.class)
    @JsonSerialize(using = CustomInstantSerializer.class)
    private Instant instantWithCustomSerializer;

    public DateTimeExample() {}

    public DateTimeExample(Instant instant,
                           Instant instantWithCustomDateFormat,
                           Instant instantWithCustomSerializer) {
        this.instant = instant;
        this.instantWithCustomDateFormat = instantWithCustomDateFormat;
        this.instantWithCustomSerializer = instantWithCustomSerializer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateTimeExample that = (DateTimeExample) o;

        if (instant != null ? !instant.equals(that.instant) : that.instant != null) return false;
        if (instantWithCustomDateFormat != null ? !instantWithCustomDateFormat.equals(that.instantWithCustomDateFormat) : that.instantWithCustomDateFormat != null)
            return false;
        return instantWithCustomSerializer != null ? instantWithCustomSerializer.equals(that.instantWithCustomSerializer) : that.instantWithCustomSerializer == null;
    }

    @Override
    public int hashCode() {
        int result = instant != null ? instant.hashCode() : 0;
        result = 31 * result + (instantWithCustomDateFormat != null ? instantWithCustomDateFormat.hashCode() : 0);
        result = 31 * result + (instantWithCustomSerializer != null ? instantWithCustomSerializer.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DateTimeExample{" +
                "instant=" + instant +
                ", instantWithCustomDateFormat=" + instantWithCustomDateFormat +
                ", instantWithCustomSerializer=" + instantWithCustomSerializer +
                '}';
    }
}