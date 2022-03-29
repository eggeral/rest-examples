package egger.software.restexamples;

import egger.software.restexamples.repository.Database;
import egger.software.restexamples.repository.TasksRepositoryIoc;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TasksResourceIntegrationTests extends JerseyTest {
    @Override
    protected javax.ws.rs.core.Application configure() {
        ResourceConfig config = new ResourceConfig(TasksResource.class);

        Database database = mock(Database.class);
        when(database.get()).thenReturn("[{\"name\":\"task1\"},{\"name\":\"task2\"}]");

        config.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(database).to(Database.class);
                bindAsContract(TasksRepositoryIoc.class);
            }
        });
        return config;
    }


    @Test
    public void it_should_return_the_current_tasks() {
        // given
        WebTarget target = target().path("tasks");

        // when
        List<TaskDto> tasks = target.request(MediaType.APPLICATION_JSON).get(new GenericType<List<TaskDto>>() {
        });

        // then
        assertThat(tasks, is(equalTo(Arrays.asList(
                new TaskDto("task1"),
                new TaskDto("task2")
        ))));
    }


    private static class TaskDto {
        public String name;

        public TaskDto() {
        }

        public TaskDto(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TaskDto taskDto = (TaskDto) o;

            return name != null ? name.equals(taskDto.name) : taskDto.name == null;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "TaskDto{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
