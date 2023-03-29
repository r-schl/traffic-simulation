package edu.kit.kastel.trafficsimulation.simulation;

import edu.kit.kastel.trafficsimulation.exceptions.FailedBuildException;

/**
 * This class represents a car that can drive on a street.
 * 
 * @author ulqch
 * @version 1.0
 */
public class Car {

    private static final int INITIAL_VELOCITY = 0;
    private static final int INITIAL_DESIRED_DIRECTION = 0;
    private static final int INITIAL_MILEAGE = 0;
    private static final int MIN_ID = 0;
    private static final int MIN_TARGET_SPEED = 20;
    private static final int MAX_TARGET_SPEED = 40;
    private static final int MIN_ACCELERATION = 1;
    private static final int MAX_ACCELERATION = 10;

    private static final String ERROR_ID_INVALID = "Id must be higher than %d. ";
    private static final String ERROR_TARGET_SPEED_INVALID = "Target speed must be between %d and %d. ";
    private static final String ERROR_ACCELERATION_INVALID = "Acceleration must be between %d and %d. ";

    private static final String ERROR_DISTANCE_TOO_LARGE = "Cannot move this distance. ";

    private final int id;

    private final int acceleration;
    private int speed;
    private final int targetSpeed;

    private int nextDirection;

    private Street street;
    private int position;

    private int mileage;
    private int mileageLastTick;

    private boolean hasBeenUpdated;

    /**
     * This constructor creates a new car. It takes the cars id, the target speed
     * that this car should not exceed, the acceleration and the street on which
     * this car should drive.
     * 
     * @param id           ID of the car
     * @param targetSpeed  Target speed that this car should not exceed
     * @param acceleration Acceleration of this car
     * @param street       Street on which this car should drive
     * @throws FailedBuildException if the id, the target speed or the acceleration
     *                              is invalid or if the street is already full.
     */
    protected Car(int id, int targetSpeed, int acceleration, Street street) throws FailedBuildException {
        if (id < MIN_ID) {
            String message = ERROR_ID_INVALID.formatted(MIN_ID);
            throw new FailedBuildException(message);
        }
        if (targetSpeed < MIN_TARGET_SPEED || targetSpeed > MAX_TARGET_SPEED) {
            String message = ERROR_TARGET_SPEED_INVALID.formatted(MIN_TARGET_SPEED, MAX_TARGET_SPEED);
            throw new FailedBuildException(message);
        }
        if (acceleration < MIN_ACCELERATION || acceleration > MAX_ACCELERATION) {
            String message = ERROR_ACCELERATION_INVALID.formatted(MIN_ACCELERATION, MAX_ACCELERATION);
            throw new FailedBuildException(message);
        }

        this.id = id;
        this.speed = INITIAL_VELOCITY;
        this.targetSpeed = targetSpeed;
        this.acceleration = acceleration;
        this.nextDirection = INITIAL_DESIRED_DIRECTION;
        this.street = street;
        this.mileage = INITIAL_MILEAGE;
    }

    /**
     * This method returns the ID of this car.
     * 
     * @return ID of this car
     */
    public int getId() {
        return this.id;
    }

    /**
     * This method returns the direction in which this car wants to turn next time.
     * 
     * @return Direction in which this car wants to turn next time
     */
    public int getNextDirection() {
        return this.nextDirection;
    }

    /**
     * This method returns the street on which this car is driving.
     * 
     * @return The street on which this car is driving
     */
    public Street getStreet() {
        return this.street;
    }

    /**
     * This method returns the current speed of this car.
     * 
     * @return Current speed of this car
     */
    public int getSpeed() {
        return this.speed;
    }

    /**
     * This method returns the position of this vehicle on the street. If this
     * vehicle is at the beginning of the street its position is 0.
     * 
     * @return Position of this vehicle on the street
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * This method returns the distance (in meters) this car has left to drive
     * during the current tick.
     * 
     * @return Distance (in meters) this car has left to drive during the current
     *         tick.
     */
    public int getDistanceToDrive() {
        return this.speed - (this.mileage - this.mileageLastTick);
    }

    /**
     * This method retuns whether this car has been updated during this tick
     * 
     * @return True if this car has been updated during this tick
     */
    public boolean hasBeenUpdated() {
        return this.hasBeenUpdated;
    }

    /**
     * This method sets the position of this car on the street.
     * 
     * @param position The position this car should be at.
     */
    protected void setPosition(int position) {
        this.position = position;
    }

    /**
     * By calling this method it can be specified whether this car has been updated
     * already. This car will not be updated until this method gets called with true
     * as parameter.
     * 
     * @param value True if this car has been updated already
     */
    protected void setHasBeenUpdated(boolean value) {
        this.hasBeenUpdated = value;
    }

    /**
     * This method adapts the speed of this car based on the acceleration, the
     * target speed and the speed limit of the street. Thereby it also sets the
     * distance this car is allowed to drive during this tick.
     */
    protected void accelerate() {
        this.speed = this.speed + this.acceleration;
        if (this.speed > this.targetSpeed) {
            this.speed = this.targetSpeed;
        }
        if (this.speed > this.street.getSpeedLimit()) {
            this.speed = this.street.getSpeedLimit();
        }
        this.mileageLastTick = this.getMileage();
    }

    /**
     * This method sets the speed of this car to zero.
     */
    protected void stop() {
        this.speed = 0;
    }

    /**
     * This method returns the total distance driven by this vehicle since its
     * creation.
     * 
     * @return Total distance driven by this vehicle since its creation
     */
    public int getMileage() {
        return this.mileage;
    }

    /**
     * This method moves this car a given distance. If this distance exceeds the
     * distance this car is allowed to move during this tick, an runtime exception
     * is thrown.
     * 
     * @param distance Distance this car is to be moved
     */
    protected void drive(int distance) {
        if (distance > this.getDistanceToDrive()) {
            throw new IllegalArgumentException(ERROR_DISTANCE_TOO_LARGE);
        }

        this.position += distance;
        this.mileage += distance;
    }

    /**
     * This method resets this cars position by placing this car at the beginning of
     * the street.
     */
    protected void resetPosition() {
        this.position = 0;
    }

    /**
     * This method updates the direction this car wants to turn at the next
     * crossing.
     */
    protected void updateNextDirection() {
        this.nextDirection++;
        if (this.nextDirection >= Node.MAX_NUMBER_OUTGOING_STREETS) {
            this.nextDirection = 0;
        }
    }

    /**
     * This method sets the street this car currently drives on.
     * 
     * @param street The street this car should drive on
     */
    protected void setStreet(Street street) {
        this.street = street;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Car otherCar)) {
            return false;
        }
        return this.id == otherCar.id;
    }

}
