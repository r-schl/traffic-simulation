package edu.kit.kastel.trafficsimulation.simulation;

import java.util.List;
import java.util.Map;

import edu.kit.kastel.trafficsimulation.exceptions.ParseException;

/**
 * This class represents a network of streets, nodes and cars.
 * 
 * @author ulqch
 * @version 1.0
 */
public class Network {

    private static final String ERROR_CAR_ID_NOT_EXIST = "Car with id %d does not exist. ";

    private final List<Node> nodes;
    private final List<Street> streets;
    private final Map<Integer, Car> carsById;

    /**
     * This constructor creates a new network from a list of nodes, a list of
     * streets and a map of cars by id. This constructor must only be called by
     * {@link NetworkBuilder}.
     * 
     * @param nodes    List of nodes
     * @param streets  List of streets
     * @param carsById Map of cars by id
     */
    protected Network(List<Node> nodes, List<Street> streets, Map<Integer, Car> carsById) {
        this.nodes = nodes;
        this.streets = streets;
        this.carsById = carsById;
    }

    /**
     * This method returns a car with the given ID.
     * 
     * @param id ID of the car that should be returned
     * @return The car with the given ID
     * @throws ParseException if there is no car with the specified ID
     */
    public Car getCar(int id) throws ParseException {
        if (!this.carsById.containsKey(id)) {
            String message = ERROR_CAR_ID_NOT_EXIST.formatted(id);
            throw new ParseException(message);
        }
        return this.carsById.get(id);
    }

    /**
     * This method lets a certain number of ticks elapse.
     * 
     * @param ticks Number of ticks that should elapse
     */
    public void simulate(int ticks) {
        for (int i = 0; i < ticks; i++) {
            this.update();
        }
    }

    private void update() {
        for (Car car : this.carsById.values()) {
            car.setHasBeenUpdated(false);
        }

        for (Street street : this.streets) {
            street.update();
        }

        for (Node node : this.nodes) {
            node.update();
        }
    }

}
