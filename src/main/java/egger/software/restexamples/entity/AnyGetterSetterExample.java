package egger.software.restexamples.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class AnyGetterSetterExample {

    private Integer id;
    private String from;
    private String to;

    private AnyGetterSetterExample() {
    }

    public AnyGetterSetterExample(Integer id, String from, String to) {
        this.id = id;
        this.from = from;
        this.to = to;
    }

    @JsonAnyGetter
    private Map<String, Object> getAllProperties() {
        return new HashMap<String, Object>() {{
            put("id", id);
            put("from", from);
            put("to", to);
        }};
    }

    @JsonAnySetter
    private void setAllProperties(String name, Object value) {
        switch (name) {
            case "id":
                id = (Integer) value;
                break;
            case "from":
                from = (String) value;
                break;
            case "to":
                to = (String) value;
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnyGetterSetterExample that = (AnyGetterSetterExample) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        return to != null ? to.equals(that.to) : that.to == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AnyGetterSetterExample{" +
                "id=" + id +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
