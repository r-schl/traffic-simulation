package edu.kit.kastel.trafficsimulation.io;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.kit.kastel.trafficsimulation.exceptions.FailedBuildException;
import edu.kit.kastel.trafficsimulation.exceptions.ParseException;
import edu.kit.kastel.trafficsimulation.simulation.Network;
import edu.kit.kastel.trafficsimulation.simulation.NetworkBuilder;

/**
 * This class provides functionality to build a traffic network from strings
 * that contain the necessary information.
 * 
 * @author ulqch
 * @version 1.0
 */
public class NetworkParser {

    private static final String NODE_REGEX = "(\\d+):(\\d+)t";
    private static final int NODE_ID_GROUP = 1;
    private static final int NODE_DURATION_GROUP = 2;

    private static final String STREET_REGEX = "(\\d+)-->(\\d+):(\\d+)m,(\\d+)x,(\\d+)max";
    private static final int STREET_START_NODE_GROUP = 1;
    private static final int STREET_END_NODE_GROUP = 2;
    private static final int STREET_LENGTH_GROUP = 3;
    private static final int STREET_NUMBER_LANES_GROUP = 4;
    private static final int STREET_SPEED_LIMIT_GROUP = 5;

    private static final String CAR_REGEX = "(\\d+),(\\d+),(\\d+),(\\d+)";
    private static final int CAR_ID_GROUP = 1;
    private static final int CAR_STREET_GROUP = 2;
    private static final int CAR_VELOCITY_GROUP = 3;
    private static final int CAR_ACCELERATION_GROUP = 4;

    private static final String ERROR_NO_PATTERN_MATCH = "Input at line %d did not match pattern '%s'.";
    private static final String ERROR_NOT_INTEGER = "Number at line %d was not within the integer range. ";

    private final NetworkBuilder networkBuilder;

    /**
     * This constructor creates a new network parser. It starts with an empty
     * network.
     */
    public NetworkParser() {
        this.networkBuilder = new NetworkBuilder();
    }

    /**
     * This method parses node, street and cars information from given string
     * lists. Then it returns a valid network with that given configuration. 
     * 
     * @param nodes   List of strings containing information about the nodes of the
     *                network. Strings must match {@value #NODE_REGEX} pattern.
     * @param streets List of strings containing information about the streets of
     *                the network. String must match {@value #STREET_REGEX} pattern.
     * @param cars    List of strings containing information about the cars of the
     *                network. Strings must match {@value #CAR_REGEX} pattern.
     * @return A valid network with the given configuration
     * @throws ParseException       if some string could not be processed
     * @throws FailedBuildException if the information provided results in an
     *                              invalid network
     */
    public Network parse(List<String> nodes, List<String> streets, List<String> cars)
            throws ParseException, FailedBuildException {
        this.parseNodes(nodes);
        this.parseStreets(streets);
        this.parseCars(cars);
        return this.networkBuilder.build();
    }

    private void parseNodes(List<String> strings) throws ParseException, FailedBuildException {
        Pattern pattern = Pattern.compile(NODE_REGEX);
        for (int i = 0; i < strings.size(); i++) {
            String string = strings.get(i);
            Matcher matcher = pattern.matcher(string);
            if (!matcher.matches()) {
                String message = ERROR_NO_PATTERN_MATCH.formatted(i, pattern.pattern());
                throw new ParseException(message);
            }
            try {
                int id = Integer.parseInt(matcher.group(NODE_ID_GROUP));
                int duration = Integer.parseInt(matcher.group(NODE_DURATION_GROUP));
                this.networkBuilder.addNode(id, duration);
            } catch (NumberFormatException numberFormatException) {
                String message = ERROR_NOT_INTEGER.formatted(i);
                throw new ParseException(message);
            }
        }
    }

    private void parseStreets(List<String> strings) throws ParseException, FailedBuildException {
        Pattern pattern = Pattern.compile(STREET_REGEX);
        for (int i = 0; i < strings.size(); i++) {
            String string = strings.get(i);
            Matcher matcher = pattern.matcher(string);
            if (!matcher.matches()) {
                String message = ERROR_NO_PATTERN_MATCH.formatted(i, pattern.pattern());
                throw new ParseException(message);
            }
            try {
                int startNodeId = Integer.parseInt(matcher.group(STREET_START_NODE_GROUP));
                int endNodeId = Integer.parseInt(matcher.group(STREET_END_NODE_GROUP));
                int length = Integer.parseInt(matcher.group(STREET_LENGTH_GROUP));
                int numberOfLanes = Integer.parseInt(matcher.group(STREET_NUMBER_LANES_GROUP));
                int speedLimit = Integer.parseInt(matcher.group(STREET_SPEED_LIMIT_GROUP));
                networkBuilder.connect(startNodeId, endNodeId, length, numberOfLanes, speedLimit);
            } catch (NumberFormatException numberFormatException) {
                String message = ERROR_NOT_INTEGER.formatted(i);
                throw new ParseException(message);
            }
        }
    }

    private void parseCars(List<String> strings) throws ParseException, FailedBuildException {
        Pattern pattern = Pattern.compile(CAR_REGEX);
        for (int i = 0; i < strings.size(); i++) {
            String string = strings.get(i);
            Matcher matcher = pattern.matcher(string);
            if (!matcher.matches()) {
                String message = ERROR_NO_PATTERN_MATCH.formatted(i, pattern.pattern());
                throw new ParseException(message);
            }
            try {
                int id = Integer.parseInt(matcher.group(CAR_ID_GROUP));
                int streetId = Integer.parseInt(matcher.group(CAR_STREET_GROUP));
                int velocity = Integer.parseInt(matcher.group(CAR_VELOCITY_GROUP));
                int acceleration = Integer.parseInt(matcher.group(CAR_ACCELERATION_GROUP));
                networkBuilder.putCar(id, streetId, velocity, acceleration);
            } catch (NumberFormatException numberFormatException) {
                String message = ERROR_NOT_INTEGER.formatted(i);
                throw new ParseException(message);
            }
        }

    }

}
