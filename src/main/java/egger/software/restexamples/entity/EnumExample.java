package egger.software.restexamples.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EnumExample {

    @JsonProperty
    private MessageType defaultJsonHandling;

    @JsonProperty
    private ShapeObjectMessageType shapeObject;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private MessageType shapeNumber;

    @JsonProperty
    private JsonValueMessageType jsonValue;

    public EnumExample() {}

    public EnumExample(MessageType defaultJsonHandling,
                       ShapeObjectMessageType shapeObject,
                       MessageType shapeNumber,
                       JsonValueMessageType jsonValue) {
        this.defaultJsonHandling = defaultJsonHandling;
        this.shapeObject = shapeObject;
        this.shapeNumber = shapeNumber;
        this.jsonValue = jsonValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnumExample that = (EnumExample) o;

        if (defaultJsonHandling != that.defaultJsonHandling) return false;
        if (shapeObject != that.shapeObject) return false;
        if (shapeNumber != that.shapeNumber) return false;
        return jsonValue == that.jsonValue;
    }

    @Override
    public int hashCode() {
        int result = defaultJsonHandling != null ? defaultJsonHandling.hashCode() : 0;
        result = 31 * result + (shapeObject != null ? shapeObject.hashCode() : 0);
        result = 31 * result + (shapeNumber != null ? shapeNumber.hashCode() : 0);
        result = 31 * result + (jsonValue != null ? jsonValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EnumExample{" +
                "defaultJsonHandling=" + defaultJsonHandling +
                ", shapeObject=" + shapeObject +
                ", shapeNumber=" + shapeNumber +
                ", jsonValue=" + jsonValue +
                '}';
    }
}

