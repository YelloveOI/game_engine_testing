package cz.cvut.fel.pjv.Model;

import java.util.logging.Logger;

/**
 * Represents character player controls
 */
public class Character {
    /**
     * Character's inventory
     */
    private Inventory inventory;
    /**
     * Character's health points
     */
    private int healthPoints;
    /**
     * Character's position
     */
    private Position position;
    /**
     * Character's speed
     */
    private double speed;
    /**
     * Character's solid radius, no collision point can arise in circle with center character's position and with radius solid radius
     */
    private double solidRadius;
    /**
     * Character's interactable radius, character can interact with object in this radius
     */
    private double interactableRadius;

    private static Logger LOGGER;

    /**
     * Constructs object Character with inventory inventory
     * @param inventory
     */
    public Character(Inventory inventory) {
        this.inventory = inventory;

        this.healthPoints = 100;
        this.position = new Position(350,225);
        this.speed = 80;
        this.solidRadius = 10;
        this.interactableRadius = 20;

        LOGGER = Logger.getLogger(Character.class.getName());
    }

    /**
     * Changes character's health point depends on value
     * @param value
     */
    public void changeHealthPoints(double value) {
        healthPoints += value;

        if(healthPoints > 100) healthPoints = 100;
        if(healthPoints < 0) healthPoints = 0;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public double getInteractableRadius() {
        return interactableRadius;
    }

    public double getSpeed() {
        return speed;
    }

    public double getSolidRadius() {
        return solidRadius;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public Position getPosition() {
        return position;
    }

}
