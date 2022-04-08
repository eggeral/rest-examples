package egger.software.restexamples.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InheritanceExample {

    @JsonProperty
    private List<Person> personsOnFlight;

    public InheritanceExample() {
    }

    public InheritanceExample(List<Person> personsOnFlight) {
        this.personsOnFlight = personsOnFlight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InheritanceExample that = (InheritanceExample) o;

        return personsOnFlight != null ? personsOnFlight.equals(that.personsOnFlight) : that.personsOnFlight == null;
    }

    @Override
    public int hashCode() {
        return personsOnFlight != null ? personsOnFlight.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "InheritanceExample{" +
                "personsOnFlight=" + personsOnFlight +
                '}';
    }
}
