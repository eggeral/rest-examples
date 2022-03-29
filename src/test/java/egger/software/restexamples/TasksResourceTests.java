package egger.software.restexamples;

import egger.software.restexamples.entity.Task;
import egger.software.restexamples.repository.TasksRepositoryIoc;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TasksResourceTests {

    @Test
    public void it_should_return_the_current_tasks() throws Exception {
        // given
        TasksRepositoryIoc repository = mock(TasksRepositoryIoc.class);
        when(repository.fetchTasks()).thenReturn(Arrays.asList(
                new Task("task1"),
                new Task("task2")
        ));
        TasksResource resource = new TasksResource(repository);

        // when
        List<Task> result = resource.getTasks();

        // then
        assertThat(result, is(equalTo(Arrays.asList(
                new Task("task1"),
                new Task("task2")
        ))));
    }

}


