package edu.kit.kastel.trafficsimulation.io.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.kit.kastel.trafficsimulation.simulation.Simulation;

/**
 * This class represents a command a user can execute via the command line. It
 * performs action on the simulation.
 * 
 * @author ulqch
 * @version 1.0
 */
public abstract class Command {

    private final Pattern pattern;

    /**
     * This constructor creates a new command. It takes a regEx that defines what
     * user input should cause that command to run.
     * 
     * @param regEx RegEx that defines what user input should cause that command to
     *              run.
     */
    public Command(String regEx) {
        this.pattern = Pattern.compile(regEx);
    }

    /**
     * This method returns the RegEx pattern on which to run this command.
     * 
     * @return RegEx pattern on which to run this command.
     */
    public Pattern getRegExPattern() {
        return this.pattern;
    }

    /**
     * This method accepts a structured user input (matcher) containing the command
     * arguments, and a simulation object on which to run this command. It executes
     * the purpose of this command by calling methods of the simulation object. Then
     * it returns a string result.
     * 
     * @param matcher    Strutured user input (matcher) containing the command
     *                   arguments
     * @param simulation simulation object on which to run this command
     * @return The message containing information about the result of this command
     */
    public abstract String execute(Matcher matcher, Simulation simulation);

}
