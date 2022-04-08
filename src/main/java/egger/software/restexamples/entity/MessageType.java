package egger.software.restexamples.entity;

public enum MessageType {
    ITEM_NOT_FOUND("error", 901),
    PROCESSING_FAILED("warning", 501),
    PROCESSING_DONE("info", 101),
    ;

    private final String level;
    private final int code;

    MessageType(String level, int code) {
        this.level = level;
        this.code = code;
    }

}
