package edu.kit.kastel.trafficsimulation.io.commands;

import java.util.regex.Matcher;

import edu.kit.kastel.trafficsimulation.exceptions.FailedBuildException;
import edu.kit.kastel.trafficsimulation.exceptions.ParseException;
import edu.kit.kastel.trafficsimulation.simulation.Simulation;

/**
 * This class represents a command that loads a network from a given file.
 * 
 * @author ulqch
 * @version 1.0
 */
public class Load extends Command {

    private static final String REGEX = "load (.+)";
    private static final int PATH_GROUP = 1;
    private static final String MESSAGE = "READY";

    /**
     * This constructor creates a new load command. 
     */
    public Load() {
        super(REGEX);
    }

    @Override
    public String execute(Matcher matcher, Simulation simulation) {
        String path = matcher.group(PATH_GROUP);
        try {
            simulation.load(path);
        } catch (ParseException | FailedBuildException exception) {
            return exception.getMessage();
        }
        return MESSAGE;
    }

}
