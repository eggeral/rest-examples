package egger.software.restexamples.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import egger.software.restexamples.entity.Task;

import java.util.List;

public class TasksRepository {
    private final Database database = new Database();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Task> fetchTasks() throws JsonProcessingException {
        return mapper.readValue(database.get(), new TypeReference<List<Task>>() {
        });
    }
}
