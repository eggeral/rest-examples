package egger.software.restexamples;

import egger.software.restexamples.entity.Message;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HelloWorldResourceTests {

    @Test
    public void it_should_return_the_default_greeting_if_no_parameter_is_given() {
        // given
        HelloResource resource = new HelloResource();

        // when
        Message result = resource.hello(null);

        // then
        assertThat(result, is(equalTo(new Message(1L, "Hello, null!"))));
    }

    @Test
    public void it_should_return_the_given_greeting() {
        // given
        HelloResource resource = new HelloResource();

        // when
        Message result = resource.hello("test");

        // then
        assertThat(result, is(equalTo(new Message(1L, "Hello, test!"))));
    }

    @Test
    public void it_should_increase_the_counter_with_each_request() {
        // given
        HelloResource resource = new HelloResource();

        // when
        // then
        assertThat(resource.hello("test").count, is(equalTo(1L)));

        // and
        // when
        // then
        assertThat(resource.hello("test").count, is(equalTo(2L)));
    }

}


