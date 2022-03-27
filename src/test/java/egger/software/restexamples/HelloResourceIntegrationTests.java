package egger.software.restexamples;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HelloResourceIntegrationTests extends JerseyTest {
    @Override
    protected javax.ws.rs.core.Application configure() {
        return new ResourceConfig(HelloResource.class);
    }

    @Test
    public void it_should_return_the_hello_world_greeting_for_the_given_parameter() {
        // given
        WebTarget target = target()
                .path("hello-world")
                .queryParam("name", "Alexander");

        // when
        MessageDto message = target.request(MediaType.APPLICATION_JSON).get(MessageDto.class);

        // then
        assertThat(message, is(equalTo(new MessageDto(1L, "Hello, Alexander!"))));
    }

    @Test
    public void it_should_return_the_default_greeting_without_parameter() {
        // given
        WebTarget target = target().path("hello-world");

        // when
        MessageDto message = target.request(MediaType.APPLICATION_JSON).get(MessageDto.class);

        // then
        assertThat(message, is(equalTo(new MessageDto(1L, "Hello, World!"))));
    }

    @Test
    public void it_should_return_the_current_count() {
        // given
        WebTarget target = target().path("hello-world");

        // when
        MessageDto message = target.request(MediaType.APPLICATION_JSON).get(MessageDto.class);

        // then
        assertThat(message.count, is(equalTo(1L)));

        // and
        // when
        message = target.request(MediaType.APPLICATION_JSON).get(MessageDto.class);

        // then
        assertThat(message.count, is(equalTo(1L)));
    }

    private static class MessageDto {
        public Long count;
        public String value;

        @SuppressWarnings("unused")
        // needed for Jackson
        public MessageDto() {
        }

        public MessageDto(Long count, String value) {
            this.count = count;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MessageDto that = (MessageDto) o;

            if (count != null ? !count.equals(that.count) : that.count != null) return false;
            return value != null ? value.equals(that.value) : that.value == null;
        }

        @Override
        public int hashCode() {
            int result = count != null ? count.hashCode() : 0;
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "MessageDto{" +
                    "count=" + count +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

}
