package edu.kit.kastel.trafficsimulation.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.kit.kastel.trafficsimulation.exceptions.FailedBuildException;

/**
 * This class represents a street that connect two nodes. Cars can drive on
 * a street.
 * 
 * @author ulqch
 * @version 1.0
 */
public class Street implements Entity {

    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 10000;
    private static final int MIN_SPEED_LIMIT = 5;
    private static final int MAX_SPEED_LIMIT = 40;
    private static final int MIN_NUMBER_LANES = 1;
    private static final int MAX_NUMBER_LANES = 2;

    private static final int SAFE_DISTANCE = 10;

    private static final String ERROR_LENGTH_INVALID = "Length of street must be between %d and %d. ";
    private static final String ERROR_SPEED_LIMIT_INVALID = "Speed limit of street must be between %d and %d. ";
    private static final String ERROR_ILLEGAL_NUMBER_LANES = "Number of lanes must be between %d and %d. ";
    // Runtime exceptions
    private static final String ERROR_STREET_FULL = "Street is full. ";
    private static final String ERROR_CAR_DOES_NOT_EXIST = "Car does not exist. ";
    private static final String ERROR_NO_CAR_AT_END = "There is no car at the end of this street. ";
    private static final String ERROR_NOT_ALLOWED_TO_TURN = "Car at the end of the street is not allowed to turn. ";
    private static final String ERROR_CAR_CANNOT_OVERTAKE = "Car cannot overtake. ";

    private final int id;

    private final int length;
    private final int speedLimit;
    private final int numberOfLanes;

    private final Node endNode;

    private final List<Car> cars;

    /**
     * This constructor creates a new street. It takes an ID that is used to
     * identify this entity, the end node of this street, the length, the number of
     * lanes and the speed limit. The number of lanes determine whether a car is
     * allowed to overtake another car.
     * 
     * @param id            ID that is used to identify this entity
     * @param endNode       End node of this street
     * @param length        Length of this street
     * @param numberOfLanes Number of lanes that determine whether a car is allowed
     *                      to overtake another car on this street
     * @param speedLimit    Speed limit of this street
     * @throws FailedBuildException if ID, length, number of lanes or speed limit
     *                              were not valid
     */
    public Street(int id, Node endNode, int length, int numberOfLanes, int speedLimit)
            throws FailedBuildException {
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            String message = ERROR_LENGTH_INVALID.formatted(MIN_LENGTH, MAX_LENGTH);
            throw new FailedBuildException(message);
        }
        if (speedLimit < MIN_SPEED_LIMIT || speedLimit > MAX_SPEED_LIMIT) {
            String message = ERROR_SPEED_LIMIT_INVALID.formatted(MIN_SPEED_LIMIT, MAX_SPEED_LIMIT);
            throw new FailedBuildException(message);
        }
        if (numberOfLanes < MIN_NUMBER_LANES || numberOfLanes > MAX_NUMBER_LANES) {
            String message = ERROR_ILLEGAL_NUMBER_LANES.formatted(MIN_NUMBER_LANES, MAX_NUMBER_LANES);
            throw new FailedBuildException(message);
        }

        this.id = id;
        this.endNode = endNode;
        this.length = length;
        this.numberOfLanes = numberOfLanes;
        this.speedLimit = speedLimit;
        this.cars = new ArrayList<>();
    }

    /**
     * This method returns the ID of this street.
     * 
     * @return ID of this street
     */
    public int getId() {
        return this.id;
    }

    /**
     * This method returns the speed limit of this street.
     * 
     * @return Speed limit of this street.
     */
    public int getSpeedLimit() {
        return this.speedLimit;
    }

    /**
     * This method checks whether this street allows a car to overtake another car.
     * 
     * @return True if overtaking is allowed
     */
    private boolean allowsOvertaking() {
        return this.numberOfLanes > 1;
    }

    /**
     * This method lets a car enter this street. It places the car at the beginning
     * of this street and then lets it advance the distance this car has left to
     * drive during the current tick.
     * 
     * @param car Car that should enter this street.
     */
    private void enter(Car car) {
        if (this.isFull()) {
            throw new IllegalStateException(ERROR_STREET_FULL);
        }

        car.setStreet(this);
        car.resetPosition();
        this.cars.add(0, car);
        this.advance(0, false, false);
    }

    /**
     * This method places a new car with the given configuration as far back on the
     * street as possible while maintaining the safe distance.
     * 
     * @param id           The ID of the car
     * @param targetSpeed  Target speed that the car should not exceed
     * @param acceleration Acceleration of the car
     * @throws FailedBuildException if the id, the target speed or the acceleration
     *                              is invalid or if the street is already full
     * @return The new car that got created and placed on this street
     */
    public Car putNewCar(int id, int targetSpeed, int acceleration) throws FailedBuildException {
        if (this.isFull()) {
            String message = ERROR_STREET_FULL.formatted(this.id);
            throw new FailedBuildException(message);
        }
        Car car = new Car(id, targetSpeed, acceleration, this);
        this.cars.add(0, car);
        car.setPosition(this.getDistanceAheadOf(0));
        return car;
    }

    private Car getCarAt(int index) {
        if (index >= this.cars.size() || index < 0) {
            throw new IllegalArgumentException(ERROR_CAR_DOES_NOT_EXIST);
        } else {
            return this.cars.get(index);
        }
    }

    /**
     * This method returns the distance a car at a given index can drive while
     * maintaining the safe distance.
     * 
     * @param index Index of the car which distance ahead should be calculated
     * @return the distance the car at the given index can drive while maintaining
     *         the safe distance
     */
    private int getDistanceAheadOf(int index) {
        if (index + 1 >= this.cars.size()) {
            return this.length - this.getCarAt(index).getPosition();
        } else {
            return this.getCarAt(index + 1).getPosition() - this.getCarAt(index).getPosition() - SAFE_DISTANCE;
        }
    }

    /**
     * This method lets a car at a given index drive until it reaches the end of the
     * street or another car. Then, depending on the given arguments, the car either
     * tries to turn onto another street or to overtake the car infront or stays
     * behind the obstacle.
     * 
     * @param index           Index of the car that should advance
     * @param allowTurn       True if the car should be able to turn onto another
     *                        street at the end of the street
     * @param allowOvertaking True if the car should be able to overtake one car
     *                        that is infront of it. 
     */
    private void advance(int index, boolean allowTurn, boolean allowOvertaking) {
        Car car = this.getCarAt(index);
        car.drive(Math.min(car.getDistanceToDrive(), this.getDistanceAheadOf(index)));

        if (car.getDistanceToDrive() > 0) {
            if (allowTurn && car.getPosition() == this.length) {
                if (this.endNode.getStreetToTurn(car) != null) {

                    this.letLastCarTurn();
                }
            } else if (allowOvertaking
                    && this.allowsOvertaking()
                    && car.getDistanceToDrive() >= 2 * SAFE_DISTANCE
                    && this.getDistanceAheadOf(index + 1) >= SAFE_DISTANCE) {

                this.overtakeCarAhead(index);
            }
        }
    }

    /**
     * This method lets the last car on the street turn at the end node onto another
     * street. Requirements are that the car must be at the end of the street and it
     * must be allowed to turn (has right of way and fits into the next street). If
     * these requirements are not met, a runtime exception is thrown.
     */
    private void letLastCarTurn() {
        Car lastCar = this.getCarAt(this.cars.size() - 1);
        if (lastCar.getPosition() != this.length) {
            throw new IllegalStateException(ERROR_NO_CAR_AT_END);
        }
        if (this.endNode.getStreetToTurn(lastCar) == null) {
            throw new IllegalStateException(ERROR_NOT_ALLOWED_TO_TURN);
        }

        Street nextStreet = this.endNode.getStreetToTurn(lastCar);
        this.cars.remove(this.cars.size() - 1);
        nextStreet.enter(lastCar);
        lastCar.updateNextDirection();
    }

    /**
     * This method lets a car at a given index overtake the car ahead. If it is
     * not able to do so while maintaining the safe distance, a runtime exception is
     * thrown.
     * 
     * @param index Index of the car that should overtake the car ahead
     */
    private void overtakeCarAhead(int index) {
        Car car = this.getCarAt(index);
        if (!this.allowsOvertaking()
                || car.getDistanceToDrive() < this.getDistanceAheadOf(index) + 2 * SAFE_DISTANCE
                || this.getDistanceAheadOf(index + 1) < SAFE_DISTANCE) {
            throw new IllegalStateException(ERROR_CAR_CANNOT_OVERTAKE);
        }

        car.drive(this.getDistanceAheadOf(index) + 2 * SAFE_DISTANCE);
        Collections.swap(this.cars, index, index + 1);
        this.advance(index + 1, false, false);
    }

    /**
     * This method checks wether there are any cars on this street.
     * 
     * @return True if there are no cars on this street
     */
    private boolean isEmpty() {
        return this.cars.isEmpty();
    }

    /**
     * This method checks whether this street is full (meaning there is no space for
     * another car to enter).
     * 
     * @return True if this street is full and there is no space for another car to
     *         enter
     */
    public boolean isFull() {
        return !this.isEmpty() && this.getCarAt(0).getPosition() < SAFE_DISTANCE;
    }

    @Override
    public void update() {
        for (int i = this.cars.size() - 1; i >= 0; i--) {
            Car car = this.getCarAt(i);
            if (car.hasBeenUpdated())
                continue;

            int mileage = car.getMileage();
            car.accelerate();
            this.advance(i, true, true);

            if (mileage == car.getMileage()) {
                car.stop();
            }

            car.setHasBeenUpdated(true);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Street otherStreet)) {
            return false;
        }
        return this.id == otherStreet.getId();
    }

}
