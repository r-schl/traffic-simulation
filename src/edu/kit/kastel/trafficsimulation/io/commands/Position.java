package edu.kit.kastel.trafficsimulation.io.commands;

import java.util.regex.Matcher;

import edu.kit.kastel.trafficsimulation.exceptions.ParseException;
import edu.kit.kastel.trafficsimulation.simulation.Car;
import edu.kit.kastel.trafficsimulation.simulation.Simulation;

/**
 * This class represents a command that returns a detailed description of where
 * a car is currently located.
 * 
 * @author ulqch
 * @version 1.0
 */
public class Position extends Command {

    private static final String REGEX = "position (\\d+)";
    private static final int ID_GROUP = 1;
    private static final String MESSAGE = "Car %d on street %d with speed %d and position %d";
    private static final String ERROR_NOT_INTEGER = "Id must be in integer range. ";

    /**
     * This constructor creates a new position command.
     */
    public Position() {
        super(REGEX);
    }

    @Override
    public String execute(Matcher matcher, Simulation simulation) {
        String string = matcher.group(ID_GROUP);
        try {
            int id = Integer.parseInt(string);
            Car car = simulation.getCar(id);
            return MESSAGE.formatted(car.getId(), car.getStreet().getId(), car.getSpeed(), car.getPosition());
        } catch (NumberFormatException numberFormatException) {
            return new ParseException(ERROR_NOT_INTEGER).getMessage();
        } catch (ParseException parseException) {
            return parseException.getMessage();
        }
    }

}
