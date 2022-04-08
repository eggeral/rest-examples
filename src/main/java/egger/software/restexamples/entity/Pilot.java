package egger.software.restexamples.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Pilot implements Person {
    private final Long id;
    private final String name;
    private final String licence;

    @JsonCreator
    public Pilot(@JsonProperty("id") Long id,
                 @JsonProperty("name") String name,
                 @JsonProperty("licence") String licence) {
        this.id = id;
        this.name = name;
        this.licence = licence;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getLicence() {
        return licence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pilot pilot = (Pilot) o;

        if (id != null ? !id.equals(pilot.id) : pilot.id != null) return false;
        if (name != null ? !name.equals(pilot.name) : pilot.name != null) return false;
        return licence != null ? licence.equals(pilot.licence) : pilot.licence == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (licence != null ? licence.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Pilot{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", licence='" + licence + '\'' +
                '}';
    }
}
