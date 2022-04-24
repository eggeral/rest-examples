package egger.software.restexamples.entity;

import egger.software.restexamples.PassengersResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;

public class Flight {
    private Long id;
    private String number;
    private String from;
    private String to;

    private Long version;

    public Flight() {
    }

    public Flight(Long id, String number, String from, String to, Long version) {
        this.id = id;
        this.number = number;
        this.from = from;
        this.to = to;
        this.version = version;
    }

    public Link passengers;

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Long getVersion() { return version; }
    public Flight copy() {
        return copy(null, null, null, null, null);
    }

    public Flight copy(Long newId, String newNumber, String newFrom, String newTo, Long newVersion) {
        Long id = newId != null ? newId : this.id;
        String number = newNumber != null ? newNumber : this.number;
        String from = newFrom != null ? newFrom : this.from;
        String to = newTo != null ? newTo : this.to;
        Long version = newVersion != null ? newVersion : this.version;
        return new Flight(id, number, from, to, version);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flight flight = (Flight) o;

        if (id != null ? !id.equals(flight.id) : flight.id != null) return false;
        if (number != null ? !number.equals(flight.number) : flight.number != null) return false;
        if (from != null ? !from.equals(flight.from) : flight.from != null) return false;
        if (to != null ? !to.equals(flight.to) : flight.to != null) return false;
        if (version != null ? !version.equals(flight.version) : flight.version != null) return false;
        return passengers != null ? passengers.equals(flight.passengers) : flight.passengers == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (passengers != null ? passengers.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", version=" + version +
                ", passengers=" + passengers +
                '}';
    }
}
