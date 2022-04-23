package egger.software.restexamples.entity;

public class Query {
    private Flight flight;

    private Query() {}

    public Query(Flight flight) {
        this.flight = flight;
    }

    public Flight getFlight() {
        return flight;
    }
}
