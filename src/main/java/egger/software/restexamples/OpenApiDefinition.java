package egger.software.restexamples;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@OpenAPIDefinition(info = @Info(
        title = "Flight API",
        version = "0.1",
        description = "API for flights"
))
@Path("/openapidefinition")
public class OpenApiDefinition {
    @GET
    public String foo() {
       return "foo";
    }
}
