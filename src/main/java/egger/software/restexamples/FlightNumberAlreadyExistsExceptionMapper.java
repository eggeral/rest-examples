package egger.software.restexamples;

import repository.FlightNumberAlreadyExistsException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FlightNumberAlreadyExistsExceptionMapper implements ExceptionMapper<FlightNumberAlreadyExistsException>
{
        @Override
        public Response toResponse(FlightNumberAlreadyExistsException exception)
        {
            return Response.status(409).entity(exception.getMessage())
                    .type("text/plain").build();
        }
}
