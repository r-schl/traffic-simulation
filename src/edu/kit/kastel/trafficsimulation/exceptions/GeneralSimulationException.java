package edu.kit.kastel.trafficsimulation.exceptions;

/**
 * This class represents an exception that gets thrown while creating a
 * simulation or intacting with one.
 * 
 * @author ulqch
 * @version 1.0
 */
public class GeneralSimulationException extends Exception {

    private static final String ERROR_PREFIX = "Error: ";

    /**
     * This constructor creates a new GeneralSimulationException. It takes a message
     * that should contain exact information about why this exception was thrown.
     * 
     * @param message A message that should contain exact information about why this
     *                exception was thrown
     */
    public GeneralSimulationException(String message) {
        super(ERROR_PREFIX + message);
    }

}
