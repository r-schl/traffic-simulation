package edu.kit.kastel.trafficsimulation.simulation;

import java.io.IOException;
import java.util.List;

import edu.kit.kastel.trafficsimulation.exceptions.FailedBuildException;
import edu.kit.kastel.trafficsimulation.exceptions.ParseException;
import edu.kit.kastel.trafficsimulation.io.NetworkParser;
import edu.kit.kastel.trafficsimulation.io.SimulationFileLoader;

/**
 * This class represents a simulation. It provides functionality to load a new
 * network from a file, to simulate a number of ticks and to get information
 * about the current state of the network.
 * 
 * @author ulqch
 * @version 1.0
 */
public class Simulation {

    private static final String ERROR_NO_NETWORK = "Please load a network first. ";
    private static final String ERROR_PATH_NOT_VALID = "Path '%s' is not valid. ";

    private Network network;

    /**
     * This constructor creates a new simulation. There is no network loaded yet.
     */
    public Simulation() {
        this.network = null;
    }

    /**
     * This method loads a new network from a file to this simulation.
     * 
     * @param path Path to a files that contain network information
     * @throws ParseException       if the information in the files could not be
     *                              interpreted correctly
     * @throws FailedBuildException if the network described by the given files
     *                              resulted in an invalid network
     */
    public void load(String path) throws ParseException, FailedBuildException {
        try {
            SimulationFileLoader simulationFileLoader = new SimulationFileLoader(path);
            List<String> stringsNodes = simulationFileLoader.loadCrossings();
            List<String> stringsStreets = simulationFileLoader.loadStreets();
            List<String> stringsCars = simulationFileLoader.loadCars();
            NetworkParser parser = new NetworkParser();
            this.network = parser.parse(stringsNodes, stringsStreets, stringsCars);
        } catch (IOException ioException) {
            String message = ERROR_PATH_NOT_VALID.formatted(path);
            throw new ParseException(message);
        }
    }

    /**
     * This method returns a car of the network.
     * 
     * @param id ID of the car that should be returned
     * @return A Car of the network with the given id
     * @throws ParseException if there is no network loaded or there is not car with
     *                        the specified ID.
     */
    public Car getCar(int id) throws ParseException {
        if (this.network == null) {
            throw new ParseException(ERROR_NO_NETWORK);
        }
        return this.network.getCar(id);
    }

    /**
     * this method lets a number of ticks elapse in the network.
     * 
     * @param ticks Number of ticks to elapse in the network
     * @throws ParseException if there is no network loaded
     */
    public void simulate(int ticks) throws ParseException {
        if (this.network == null) {
            throw new ParseException(ERROR_NO_NETWORK);
        }
        this.network.simulate(ticks);
    }

}
