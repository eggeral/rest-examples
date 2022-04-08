package egger.software.restexamples.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum JsonValueMessageType {
    ITEM_NOT_FOUND("error", 901),
    PROCESSING_FAILED("warning", 501),
    PROCESSING_DONE("info", 101),
    ;

    private final String level;
    @JsonValue
    private final int code;

    JsonValueMessageType(String level, int code) {
        this.level = level;
        this.code = code;
    }

    @JsonCreator
    public static JsonValueMessageType forValue(int code) {
        for (JsonValueMessageType messageType : JsonValueMessageType.values()) {
            if (messageType.code == code) {
                return messageType;
            }
        }
        return null;
    }

}
