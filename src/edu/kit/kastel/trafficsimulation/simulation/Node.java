package edu.kit.kastel.trafficsimulation.simulation;

import java.util.ArrayList;
import java.util.List;

import edu.kit.kastel.trafficsimulation.exceptions.FailedBuildException;

/**
 * This class represents a street node. It acts as a link between streets.
 * 
 * @author ulqch
 * @version 1.0
 */
public abstract class Node implements Entity {

    /**
     * This is the maximum number of outgoing streets a node can have.
     */
    public static final int MAX_NUMBER_OUTGOING_STREETS = 4;

    private static final int MIN_NUMBER_OUTGOING_STREETS = 1;
    private static final int MIN_NUMBER_INCOMING_STREETS = 1;
    private static final int MAX_NUMBER_INCOMING_STREETS = 4;
    private static final int MIN_ID = 0;

    private static final String ERROR_TO_MANY_STREETS = "Cannot add more than %d incoming and %d outgoing streets. ";
    private static final String ERROR_ID_INVALID = "Id must be higher than %d";

    private final int id;
    private final List<Street> outgoingStreets;
    private final List<Street> incomingStreets;

    /**
     * This constructor creates a new node. It takes an ID that is used to identify
     * this entity.
     * 
     * @param id ID that is used to identify this entity
     * @throws FailedBuildException if ID was not valid
     */
    public Node(int id) throws FailedBuildException {
        if (id < MIN_ID) {
            throw new FailedBuildException(ERROR_ID_INVALID.formatted(MIN_ID));
        }
        this.id = id;
        this.outgoingStreets = new ArrayList<>();
        this.incomingStreets = new ArrayList<>();
    }

    /**
     * This method adds an outgoing street to this node.
     * 
     * @param street Street to be added as an outgoing street to this node.
     * @throws FailedBuildException if there are too many outgoing streets at this
     *                              node
     */
    public void addOutgoingStreet(Street street) throws FailedBuildException {
        if (this.outgoingStreets.size() + 1 > MAX_NUMBER_OUTGOING_STREETS) {
            String message = ERROR_TO_MANY_STREETS.formatted(MAX_NUMBER_INCOMING_STREETS, MAX_NUMBER_OUTGOING_STREETS);
            throw new FailedBuildException(message);
        }
        this.outgoingStreets.add(street);
    }

    /**
     * This method adds an incoming street to this node.
     * 
     * @param street Street to be added as an incoming street to this node
     * @throws FailedBuildException if there are too many incoming streets at this
     *                              node
     */
    public void addIncomingStreet(Street street) throws FailedBuildException {
        if (this.incomingStreets.size() + 1 > MAX_NUMBER_INCOMING_STREETS) {
            String message = ERROR_TO_MANY_STREETS.formatted(MAX_NUMBER_INCOMING_STREETS, MAX_NUMBER_OUTGOING_STREETS);
            throw new FailedBuildException(message);
        }
        this.incomingStreets.add(street);
    }

    /**
     * This method returns the ID of this node.
     * 
     * @return ID of this node
     */
    public int getId() {
        return this.id;
    }

    /**
     * This method returns the list of incoming streets.
     * 
     * @return List of incoming streets
     */
    protected List<Street> getIncomingStreets() {
        return new ArrayList<>(this.incomingStreets);
    }

    /**
     * This method checks if this node has enough incoming and outgoing streets to
     * be valid.
     * 
     * @return True if this node has enough incoming and outgoing streets
     */
    public boolean hasEnoughStreets() {
        return this.incomingStreets.size() >= MIN_NUMBER_INCOMING_STREETS
                && this.outgoingStreets.size() >= MIN_NUMBER_OUTGOING_STREETS;
    }

    /**
     * This method checks whether a street has the right of way.
     * 
     * @param street Street to check wether it has the right of way
     * @return True if the street has the right of way
     */
    protected abstract boolean hasRightOfWay(Street street);

    /**
     * This method returns the street that a car should to turn onto. If the street
     * the car is traveling on does not have priority or the car does not fit in the
     * new street, null is returned.
     * 
     * @param car The car to calculate which road to turn onto
     * @return Street that the car should to turn onto or null if the street the car
     *         is traveling on does not have priority or if the car does not fit in
     *         the new street
     */
    public Street getStreetToTurn(Car car) {
        Street street = car.getStreet();
        if (!hasRightOfWay(street))
            return null;

        int direction = car.getNextDirection();
        if (direction >= this.outgoingStreets.size()) {
            direction = 0;
        }

        Street nextStreet = this.outgoingStreets.get(direction);

        if (nextStreet.isFull())
            return null;
        return nextStreet;
    }

}
