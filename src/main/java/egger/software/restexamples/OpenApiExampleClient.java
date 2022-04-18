package egger.software.restexamples;

import egger.software.restexamples.invoker.ApiClient;
import egger.software.restexamples.invoker.ApiException;
import egger.software.restexamples.invoker.Configuration;
import egger.software.restexamples.model.*;
import egger.software.restexamples.api.DefaultApi;

public class OpenApiExampleClient {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost");

        DefaultApi apiInstance = new DefaultApi(defaultClient);
        Flight flight = new Flight(); // Flight |
        try {
            Flight result = apiInstance.add(flight);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#add");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
