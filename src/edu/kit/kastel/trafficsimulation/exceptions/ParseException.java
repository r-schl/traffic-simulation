package edu.kit.kastel.trafficsimulation.exceptions;

/**
 * This class represents an exception that gets thrown if user input could not
 * be interpreted correctly.
 * 
 * @author ulqch
 * @version 1.0
 */
public class ParseException extends GeneralSimulationException {

    /**
     * This constructor creates a new ParseException. It takes a message that
     * should contain exact information about why this exception was thrown.
     * 
     * @param message A message that should contain exact information about why this
     *                exception was thrown
     */
    public ParseException(String message) {
        super(message);
    }

}
