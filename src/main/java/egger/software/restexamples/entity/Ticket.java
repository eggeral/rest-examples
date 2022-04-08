package egger.software.restexamples.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Ticket {

    @JsonProperty("ticket_id") // JsonProperty can change the name of the field in the JSON output/input
    private String id;
    private String passengerFirstName; // Handled by Jackson because it has a Getter
    @JsonProperty() // Mark a property to be handled by Jackson even if there is no Getter
    private String passengerLastName;
    private String passengerName;

    public Ticket() {}

    public Ticket(String id, String passengerFirstName, String passengerLastName) {
        this.id = id;
        this.passengerFirstName = passengerFirstName;
        this.passengerLastName = passengerLastName;
        // Attention this is not called on deserialization because the default constructor is used
        passengerName = passengerFirstName + " " + passengerLastName;
    }

    public String getPassengerFirstName() {
        return passengerFirstName;
    }

    @JsonIgnore // Ignore this property
    public String getPassengerName() {
        return passengerName;
    }

    @JsonSetter("passengerFirstName") // In case we want to control the setting of properties
    private void jsonSetPassengerFirstName(String value) {
        passengerFirstName = value;
        passengerName = passengerFirstName + " " + passengerLastName;
    }

    @JsonSetter("passengerLastName")
    private void jsonSetPassengerLastName(String value) {
        passengerLastName = value;
        passengerName = passengerFirstName + " " + passengerLastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (id != null ? !id.equals(ticket.id) : ticket.id != null) return false;
        if (passengerFirstName != null ? !passengerFirstName.equals(ticket.passengerFirstName) : ticket.passengerFirstName != null)
            return false;
        if (passengerLastName != null ? !passengerLastName.equals(ticket.passengerLastName) : ticket.passengerLastName != null)
            return false;
        return passengerName != null ? passengerName.equals(ticket.passengerName) : ticket.passengerName == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (passengerFirstName != null ? passengerFirstName.hashCode() : 0);
        result = 31 * result + (passengerLastName != null ? passengerLastName.hashCode() : 0);
        result = 31 * result + (passengerName != null ? passengerName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", passengerFirstName='" + passengerFirstName + '\'' +
                ", passengerLastName='" + passengerLastName + '\'' +
                ", passengerName='" + passengerName + '\'' +
                '}';
    }
}