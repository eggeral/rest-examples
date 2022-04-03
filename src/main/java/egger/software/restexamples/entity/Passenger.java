package egger.software.restexamples.entity;

public class Passenger {
    private Long id;
    private String name;

    public Passenger() {
    }

    public Passenger(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
