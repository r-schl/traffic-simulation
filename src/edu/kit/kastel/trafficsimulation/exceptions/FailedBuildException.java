package edu.kit.kastel.trafficsimulation.exceptions;

/**
 * This class represents an exception that gets thrown if the building of a
 * network resulted in an invalid network.
 * 
 * @author ulqch
 * @version 1.0
 */
public class FailedBuildException extends GeneralSimulationException {

    /**
     * This constructor creates a new FailedBuildException. It takes a message that
     * should contain exact information about why this exception was thrown.
     * 
     * @param message A message that should contain exact information about why this
     *                exception was thrown
     */
    public FailedBuildException(String message) {
        super(message);
    }

}
