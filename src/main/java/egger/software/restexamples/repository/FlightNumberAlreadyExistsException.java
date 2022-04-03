package egger.software.restexamples.repository;

public class FlightNumberAlreadyExistsException extends RuntimeException {
    public FlightNumberAlreadyExistsException(String number) {
        super("Flight with number " + number + " already exists");
    }
}

