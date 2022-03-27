package egger.software.restexamples.entity;

public class Message {
    public Long count;
    public String value;

    public Message(Long count, String value) {
        this.count = count;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (count != null ? !count.equals(message.count) : message.count != null) return false;
        return value != null ? value.equals(message.value) : message.value == null;
    }

    @Override
    public int hashCode() {
        int result = count != null ? count.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "count=" + count +
                ", value='" + value + '\'' +
                '}';
    }
}
