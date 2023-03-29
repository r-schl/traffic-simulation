package edu.kit.kastel.trafficsimulation.io.commands;

import java.util.regex.Matcher;

import edu.kit.kastel.trafficsimulation.exceptions.ParseException;
import edu.kit.kastel.trafficsimulation.simulation.Simulation;

/**
 * This class represents a command that updates a network such that a specified
 * number of ticks passed.
 * 
 * @author ulqch
 * @version 1.0
 */
public class Simulate extends Command {

    private static final String REGEX = "simulate (\\d+)";
    private static final String ERROR_NOT_INTEGER = "Number of ticks must be in integer range. ";
    private static final String MESSAGE = "READY";

    /**
     * This constructor creates a new simulate command.
     */
    public Simulate() {
        super(REGEX);
    }

    @Override
    public String execute(Matcher matcher, Simulation simulation) {
        String string = matcher.group(1);
        try {
            int ticks = Integer.parseInt(string);
            simulation.simulate(ticks);
        } catch (NumberFormatException numberFormatException) {
            return new ParseException(ERROR_NOT_INTEGER).getMessage();
        } catch (ParseException parseException) {
            return parseException.getMessage();
        }
        return MESSAGE;
    }

}
