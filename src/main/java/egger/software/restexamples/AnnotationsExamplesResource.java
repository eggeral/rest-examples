package egger.software.restexamples;

import egger.software.restexamples.entity.Flight;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriInfo;

@Path("/annotations")
public class AnnotationsExamplesResource {

    @POST
    @Path("consumes/string")
    @Consumes(MediaType.APPLICATION_JSON)
    public String consumesJson(String input) {
        return "consumesJson";
    }

    @POST
    @Path("consumes/string")
    @Consumes(MediaType.APPLICATION_XML)
    public String consumesXml(String input) {
        return "consumesXml";
    }

    @POST
    @Path("consumes/flight")
    @Consumes(MediaType.APPLICATION_JSON)
    public String consumesJsonFlight(Flight flight) {
        return "consumesJsonFlight: " + flight;
    }

    @POST
    @Path("consumes/flight")
    @Consumes(MediaType.APPLICATION_XML)
    public String consumesXmlFlight(Flight flight) {
        return "consumesXmlFlight: " + flight;
    }

    @GET
    @Path("pathparams/{param1}/{param2}")
    public String pathParams(@PathParam("param1") String param1, @PathParam("param2") Integer param2) {
        return "pathParams -  param1: " + param1 + ", param2: " + param2;
    }

    @GET
    @Path("queryparams")
    public String queryParams(@QueryParam("param1") String param1, @QueryParam("param2") Integer param2) {
        return "queryParams -  param1: " + param1 + ", param2: " + param2;
    }

    @GET
    @Path("matrixparams/flight")
    public String matrixParams(@MatrixParam("flightnumber") String flightNumber) {
        return "matrixParams -  flightNumber: " + flightNumber;
    }

    @GET
    @Path("matrixparams/{flightPathSegment:flight}/passenger")
    public String matrixParams(@PathParam("flightPathSegment") PathSegment flightPathSegment, @MatrixParam("passengerid") String passengerId) {
        String flightNumber = flightPathSegment.getMatrixParameters().getFirst("flightnumber");
        return "matrixParams -  flightNumber: " + flightNumber + ", passengerId: " + passengerId;
    }

    @GET
    @Path("defaultvalues/default/{pathparam:.*}something")
    public String matrixParams(@DefaultValue("pathParamDefaultValue") @PathParam("pathparam") String pathParam, @DefaultValue("queryParamDefaultValue") @QueryParam("queryparam") String queryParam) {
        return "defaultvalues -  pathParam: " + pathParam + ", queryParam: " + queryParam;
    }

    @GET
    @Path("cookieparams")
    public String cookieParams(@CookieParam("param") String param) {
        return "cookieParams: " + param;
    }

    @GET
    @Path("headerparams")
    public String headerParams(@HeaderParam("param") String param) {
        return "headerParams: " + param;
    }


    public static class ExampleBeanParam {
        private final String pathParam;
        private final String queryParam;

        public ExampleBeanParam(
            @PathParam("pathparam") String pathParam,
            @QueryParam("queryparam") String queryParam
        ) {

            this.pathParam = pathParam;
            this.queryParam = queryParam;
        }

        @Override
        public String toString() {
            return "ExampleBeanParam{" +
                    "pathParam='" + pathParam + '\'' +
                    ", queryParam='" + queryParam + '\'' +
                    '}';
        }
    }

    @GET
    @Path("beanparams/{pathparam}")
    public String beanParams(@BeanParam ExampleBeanParam param) {
        return param.toString();
    }

    @POST
    @Path("formparams")
    public String formParams(@FormParam("param") String param) {
        return "formParams: " + param;
    }

    @GET
    @Path("context/{pathparam}")
    public String context(@Context UriInfo uriInfo) {
        return "context: " +
                uriInfo.getPathParameters() +
                ", " +
                uriInfo.getQueryParameters();
    }

    @GET
    @Path("encoded")
    public String beanParams(
            @QueryParam("decoded") String decodedParam, @QueryParam("encoded") @Encoded String encodedParam) {
        return "encoded: Decoded param value: " +
                decodedParam +
                ", encoded param value: " +
                encodedParam;
    }
}
