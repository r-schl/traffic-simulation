package edu.kit.kastel.trafficsimulation.simulation;

import edu.kit.kastel.trafficsimulation.exceptions.FailedBuildException;

/**
 * This class represents a crossing that allows cars on all streets to turn at
 * the same time.
 * 
 * @author ulqch
 * @version 1.0
 */
public class Roundabout extends Node {

    /**
     * This constructor creates a new roundabout. It takes an ID that is used to
     * identify this entity.
     * 
     * @param id ID that is used to identify this entity
     * @throws FailedBuildException if the ID is not valid
     */
    public Roundabout(int id) throws FailedBuildException {
        super(id);
    }

    @Override
    public void update() {
        // Do nothing
    }

    @Override
    public boolean hasRightOfWay(Street street) {
        return true;
    }

}
