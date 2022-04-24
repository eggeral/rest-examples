package repository;

import egger.software.restexamples.entity.Flight;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FlightsRepository {
    private final List<Flight> flights = new ArrayList<>();

    public FlightsRepository(List<Flight> initialFlights) {
        flights.addAll(initialFlights.stream().map(Flight::copy).collect(Collectors.toList()));
    }

    public List<Flight> findAll() {
        return flights.stream().map(Flight::copy).collect(Collectors.toList());
    }

    public Flight findById(Long id) {
        return find(flight -> Objects.equals(flight.getId(), id)).stream().findAny().orElse(null);
    }

    public List<Flight> find(Predicate<Flight> flightPredicate) {
        return flights.stream().filter(flightPredicate).map(Flight::copy).collect(Collectors.toList());
    }

    public Flight add(Flight newFlight) {
        if (find(flight -> Objects.equals(flight.getNumber(), newFlight.getNumber())).size() > 0)
            throw new FlightNumberAlreadyExistsException(newFlight.getNumber());

        Long newId = flights.stream().map(Flight::getId).max(Long::compareTo).orElse(0L) + 1;
        Flight flight = newFlight.copy(newId, null, null, null, 1L);
        flights.add(flight);
        return flight;
    }

    public void delete(Long id, Long version) {
        Flight existingFlight = findById(id);
        if (!Objects.equals(existingFlight.getVersion(), version))
            throw new OptimisticLockException("Flight version conflict");
        flights.removeIf(flight -> Objects.equals(flight.getId(), id));
    }

    public Flight update(Flight updatedFlight) {
        Flight existingFlight = findById(updatedFlight.getId());
        if (!Objects.equals(existingFlight.getVersion(), updatedFlight.getVersion()))
            throw new OptimisticLockException("Flight version conflict");
        delete(updatedFlight.getId(), updatedFlight.getVersion());
        Flight flight = updatedFlight.copy(updatedFlight.getId(), null, null, null, existingFlight.getVersion() + 1);
        flights.add(flight);
        return flight;
    }
}
