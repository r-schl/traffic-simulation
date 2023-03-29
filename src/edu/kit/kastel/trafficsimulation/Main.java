package edu.kit.kastel.trafficsimulation;

import edu.kit.kastel.trafficsimulation.io.InputSystem;
import edu.kit.kastel.trafficsimulation.simulation.Simulation;

/**
 * This is a traffic simulation program. It can read network configuration from
 * files and then simulate a number of ticks. It can be controlled via commands
 * on the command line.
 * 
 * @author ulqch
 * @version 1.0
 */
public final class Main {

    private Main() {

    }

    /**
     * This is a traffic simulation program. It can read network configuration from
     * files and then simulate a number of ticks. It can be controlled via commands
     * on the command line.
     * 
     * @param args Command line arguments, they do not have any effect on this
     *             program
     */
    public static void main(String[] args) {

        Simulation simulation = new Simulation();
        InputSystem inputSystem = new InputSystem();
        inputSystem.loop(simulation);
    }
}
