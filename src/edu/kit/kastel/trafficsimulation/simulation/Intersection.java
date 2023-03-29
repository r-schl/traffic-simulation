package edu.kit.kastel.trafficsimulation.simulation;

import edu.kit.kastel.trafficsimulation.exceptions.FailedBuildException;

/**
 * This class represents an intersection with traffic lights.
 * 
 * @author ulqch
 * @version 1.0
 */
public class Intersection extends Node {

    private static final int MIN_DURATION = 3;
    private static final int MAX_DURATION = 10;
    private static final int INITIAL_INDICATOR = 0;

    private static final String ERROR_DURATION_NEGATIVE = "Green phase duration must be between %d and %d. ";

    private final int duration;
    private int countdown;
    private int indicator;

    /**
     * This constructor creates a new intersection with traffic lights. It takes an
     * ID that is used to identify this entity and a green phase duration that
     * determines after how many ticks this intersection switches lights.
     * 
     * @param id       ID that is used to identify this entity
     * @param duration Green phase duration that determines after how many ticks
     *                 this intersection switches lights
     * @throws FailedBuildException if ID or green phase duration was not valid
     */
    public Intersection(int id, int duration) throws FailedBuildException {
        super(id);
        if (duration < MIN_DURATION || duration > MAX_DURATION) {
            throw new FailedBuildException(ERROR_DURATION_NEGATIVE.formatted(MIN_DURATION, MAX_DURATION));
        }

        this.duration = duration;
        this.countdown = duration;
        this.indicator = INITIAL_INDICATOR;
    }

    /**
     * With this method, the light is switched in such a way that the next street
     * has the right of way.
     */
    private void switchLights() {
        this.indicator++;
        if (this.indicator >= this.getIncomingStreets().size()) {
            this.indicator = 0;
        }
    }

    @Override
    public void update() {
        this.countdown--;
        if (this.countdown <= 0) {
            this.switchLights();
            this.countdown = this.duration;
        }
    }

    @Override
    public boolean hasRightOfWay(Street street) {
        return this.getIncomingStreets().get(this.indicator).equals(street);
    }

}
