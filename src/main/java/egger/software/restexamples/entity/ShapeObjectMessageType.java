package egger.software.restexamples.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ShapeObjectMessageType {
    ITEM_NOT_FOUND("error", 901),
    PROCESSING_FAILED("warning", 501),
    PROCESSING_DONE("info", 101),
    ;

    @JsonProperty
    private final String level;
    @JsonProperty
    private final int code;

    ShapeObjectMessageType(String level, int code) {
        this.level = level;
        this.code = code;
    }

    @JsonCreator
    public static ShapeObjectMessageType forValues(@JsonProperty("level") String level,
                                                   @JsonProperty("code") int code) {
        for (ShapeObjectMessageType messageType : ShapeObjectMessageType.values()) {
            if (
                    messageType.level.equals(level) && messageType.code == code) {
                return messageType;
            }
        }
        return null;
    }

}
