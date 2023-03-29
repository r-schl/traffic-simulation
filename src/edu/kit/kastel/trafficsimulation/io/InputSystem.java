package edu.kit.kastel.trafficsimulation.io;

import java.util.Scanner;
import java.util.regex.Matcher;

import edu.kit.kastel.trafficsimulation.exceptions.ParseException;
import edu.kit.kastel.trafficsimulation.io.commands.Command;
import edu.kit.kastel.trafficsimulation.io.commands.Load;
import edu.kit.kastel.trafficsimulation.io.commands.Position;
import edu.kit.kastel.trafficsimulation.io.commands.Simulate;
import edu.kit.kastel.trafficsimulation.simulation.Simulation;

/**
 * This class deals with user inputs and redirects it to the corresponding
 * commands.
 * 
 * @author ulqch
 * @version 1.0
 */
public class InputSystem {

    private static final String ERROR_COMMAND_NOT_FOUND = "Command not found. ";
    private static final String QUIT_COMMAND = "quit";

    private final Command[] commands = {
        new Load(),
        new Position(),
        new Simulate()
    };

    /**
     * This method repeatedly scans user input from the command line and executes
     * the corresponding commands on the simulation object given as a parameter.
     * This loop ends if the user enters {@value #QUIT_COMMAND}.
     * 
     * @param simulation Simulation to be affected by the user's input
     */
    public void loop(Simulation simulation) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals(QUIT_COMMAND)) {

            boolean commandFound = false;
            for (Command command : commands) {
                Matcher matcher = command.getRegExPattern().matcher(input);
                if (matcher.matches()) {
                    commandFound = true;
                    String result = command.execute(matcher, simulation);
                    if (result != null) {
                        System.out.println(result);
                    }
                }
            }

            if (!commandFound) {
                Exception exception = new ParseException(ERROR_COMMAND_NOT_FOUND);
                System.out.println(exception.getMessage());
            }

            input = scanner.nextLine();
        }

        scanner.close();
    }

}
