package egger.software.restexamples;

import com.fasterxml.jackson.core.JsonProcessingException;
import egger.software.restexamples.entity.Task;
import egger.software.restexamples.repository.Database;
import egger.software.restexamples.repository.TasksRepositoryIoc;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TasksRepositoryIocTests {

    @Test
    public void fetchTasks_should_get_the_current_tasks_from_the_database() throws JsonProcessingException {
        // given
        Database database = mock(Database.class);
        when(database.get()).thenReturn("[{\"name\":\"task1\"},{\"name\":\"task2\"}]");
        TasksRepositoryIoc repository = new TasksRepositoryIoc(database);

        // when
        List<Task> result = repository.fetchTasks();

        // then
        assertThat(result, is(equalTo(Arrays.asList(
                new Task("task1"),
                new Task("task2")
        ))));
    }
}
