package egger.software.restexamples.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import egger.software.restexamples.entity.Task;

import javax.inject.Inject;
import java.util.List;

public class TasksRepositoryIoc {

    private final Database database;
    private final ObjectMapper mapper = new ObjectMapper();

    @Inject
    public TasksRepositoryIoc(Database database) {
        this.database = database;
    }

    public List<Task> fetchTasks() throws JsonProcessingException {
        return mapper.readValue(database.get(), new TypeReference<List<Task>>() {
        });
    }

}
