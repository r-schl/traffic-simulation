package edu.kit.kastel.trafficsimulation.simulation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.kit.kastel.trafficsimulation.exceptions.FailedBuildException;

/**
 * This class provides methods to build a new valid network. Adding elements
 * that lead to an invalid network results in an error. If the network is
 * incomplete at the end, an error is also thrown.
 * 
 * @author ulqch
 * @version 1.0
 */
public class NetworkBuilder {

    private static final String ERROR_NODE_ID_NOT_UNIQUE = "Node id %d is not unique. ";
    private static final String ERROR_NODE_DOESNT_EXIST = "Referenced node id %d does not exist. ";
    private static final String ERROR_STREET_DOESNT_EXIST = "Street %d does not exist. ";
    private static final String ERROR_NOT_ENOUGH_STREETS = "Node %d does not have enough streets. ";
    private static final String ERROR_CAR_ID_NOT_UNIQUE = "Car id %d is not unique. ";
    private static final String ERROR_SAME_START_AND_END = "Street %d cannot have same start and end node. ";

    private final Map<Integer, Node> nodes;
    private final Map<Integer, Street> streets;
    private final Map<Integer, Car> cars;
    private int availableStreetId;

    /**
     * This constructor creates a new network builder. Initially there are no nodes,
     * streets or cars.
     */
    public NetworkBuilder() {
        this.availableStreetId = 0;
        this.nodes = new HashMap<>();
        this.streets = new HashMap<>();
        this.cars = new HashMap<>();
    }

    /**
     * This method adds a node to the network.
     * 
     * @param id       Node ID
     * @param duration Green phase duration (0 represents a roundabout)
     * @throws FailedBuildException if node ID is not unique or node-specific
     *                              properties (such as ID, green phase duration)
     *                              are outside the permitted range.
     */
    public void addNode(int id, int duration) throws FailedBuildException {
        if (nodes.containsKey(id)) {
            throw new FailedBuildException(ERROR_NODE_ID_NOT_UNIQUE.formatted(id));
        }
        if (duration == 0) {
            this.nodes.put(id, new Roundabout(id));
        } else {
            this.nodes.put(id, new Intersection(id, duration));
        }
    }

    /**
     * This method adds a street to the network that connects two nodes.
     * 
     * @param startNodeId   Start node ID
     * @param endNodeId     End node ID
     * @param length        Length of the street in meters
     * @param numberOfLanes Number of lanes
     * @param speedLimit    Speed limit
     * @throws FailedBuildException if the referenced node ID does not exist, the
     *                              street would lead to a loop or street-specific
     *                              properties (such as length, number of lanes,
     *                              speed limit) are outside the permitted range.
     */
    public void connect(int startNodeId, int endNodeId, int length, int numberOfLanes, int speedLimit)
            throws FailedBuildException {
        if (!nodes.containsKey(startNodeId)) {
            throw new FailedBuildException(ERROR_NODE_DOESNT_EXIST.formatted(startNodeId));
        }
        if (!nodes.containsKey(endNodeId)) {
            throw new FailedBuildException(ERROR_NODE_DOESNT_EXIST.formatted(endNodeId));
        }
        if (startNodeId == endNodeId) {
            throw new FailedBuildException(ERROR_SAME_START_AND_END.formatted(availableStreetId));
        }

        Node startNode = nodes.get(startNodeId);
        Node endNode = nodes.get(endNodeId);

        Street street = new Street(availableStreetId, endNode, length, numberOfLanes, speedLimit);

        startNode.addOutgoingStreet(street);
        endNode.addIncomingStreet(street);

        this.streets.put(availableStreetId, street);
        availableStreetId++;
    }

    /**
     * This method puts a new car on a street.
     * 
     * @param id              Car ID
     * @param streetId        ID of the street on which the car should be placed
     * @param desiredVelocity Desired velocity of the car
     * @param acceleration    Acceleration of the car
     * @throws FailedBuildException if car ID is not unique, referrenced street ID
     *                              does not exist or the car-specific properties
     *                              (such as desired velocity, acceleration) are
     *                              outside of the permitted range.
     */
    public void putCar(int id, int streetId, int desiredVelocity, int acceleration) throws FailedBuildException {
        if (this.cars.containsKey(id)) {
            throw new FailedBuildException(ERROR_CAR_ID_NOT_UNIQUE.formatted(id));
        }
        if (!this.streets.containsKey(streetId)) {
            throw new FailedBuildException(ERROR_STREET_DOESNT_EXIST.formatted(streetId));
        }
        Street street = this.streets.get(streetId);
        // Put car on the street
        Car car = street.putNewCar(id, desiredVelocity, acceleration);
        this.cars.put(id, car);
    }

    /**
     * This method builds a valid network with the data constructed by this network
     * builder. If this network is invalid an exception gets thrown.
     * 
     * @return A valid network
     * @throws FailedBuildException if the network is invalid
     */
    public Network build() throws FailedBuildException {

        for (Node node : this.nodes.values()) {
            if (!node.hasEnoughStreets()) {
                String message = ERROR_NOT_ENOUGH_STREETS.formatted(node.getId());
                throw new FailedBuildException(message);
            }
        }

        List<Street> streetsArray = new ArrayList<>(this.streets.values());
        streetsArray.sort(Comparator.comparingInt(Street::getId));
        List<Node> nodesArray = new ArrayList<>(this.nodes.values());
        nodesArray.sort(Comparator.comparingInt(Node::getId));
        return new Network(nodesArray, streetsArray, this.cars);
    }

}
