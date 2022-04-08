package egger.software.restexamples.entity;

public class Passenger implements Person {
    private Long id;
    private String name;
    private String flightNumber;

    public Passenger() {
    }

    public Passenger(Long id, String name, String flightNumber) {
        this.id = id;
        this.name = name;
        this.flightNumber = flightNumber;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Passenger passenger = (Passenger) o;

        if (id != null ? !id.equals(passenger.id) : passenger.id != null) return false;
        if (name != null ? !name.equals(passenger.name) : passenger.name != null) return false;
        return flightNumber != null ? flightNumber.equals(passenger.flightNumber) : passenger.flightNumber == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (flightNumber != null ? flightNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                '}';
    }
}
