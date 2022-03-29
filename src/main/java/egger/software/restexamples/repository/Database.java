package egger.software.restexamples.repository;

public class Database {
    private String store = "[{\"name\":\"buy eggs\"},{\"name\":\"clean house\"}]";

    public void update(String newStore) {
        store = newStore;
    }

    public String get() {
        return store;
    }
}