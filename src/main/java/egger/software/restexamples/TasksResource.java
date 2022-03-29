package egger.software.restexamples;

import egger.software.restexamples.entity.Task;
import egger.software.restexamples.repository.TasksRepositoryIoc;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/tasks")
public class TasksResource {
    private final TasksRepositoryIoc repository;

    @Inject
    public TasksResource(TasksRepositoryIoc repository) {
        this.repository = repository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Task> getTasks() throws Exception {
        return repository.fetchTasks();
    }

}
