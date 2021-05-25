package cz.cvut.fel.pjv.Model;

import java.util.logging.Logger;

/**
 * Represents enemies which character confronts
 */
public class Enemy extends SolidObject {
    /**
     * Enemy's health points
     */
    private int healthPoints;
    /**
     * Enemy's attack power
     */
    private int hitValue;
    /**
     * Enemy's speed
     */
    private double speed;
    /**
     * Enemy's foresight
     */
    private double foresight;
    /**
     * Enemy's attack range, if character position point in circle with center enemy position and with radius attack range, enemy can hit character
     */
    private double attackRange;
    /**
     * Enemy's solid radius, no collision point can arise in circle with center character's position and with radius solid radius
     */
    private double solidRadius;

    private Logger LOGGER;

    /**
     * Constructs Enemy object depends on params
     * @param sprite
     * @param enemyType
     * @param hitValue
     * @param speed
     * @param healthPoints
     * @param attackRange
     * @param solidRadius
     */
    public Enemy(
            Sprite sprite, String enemyType,
            int hitValue, double speed, int healthPoints,
            double attackRange, double solidRadius) {
        super(sprite, enemyType, 24, 24); //to change
        this.healthPoints = healthPoints;
        this.hitValue = hitValue;
        this.speed = speed;
        this.attackRange = attackRange;
        this.solidRadius = solidRadius;
        this.foresight = 70;

        LOGGER = Logger.getLogger(Enemy.class.getName());
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public double getSpeed() {
        return speed;
    }

    /**
     * Changes enemy's health point depends on value
     * @param value
     */
    public void changeHealthPoints(double value) {
        healthPoints += value;
    }

    public double getForesight() {
        return foresight;
    }

    public double getSolidRadius() {
        return solidRadius;
    }

    public int getHitValue() {
        return hitValue;
    }

    public double getAttackRange() {
        return attackRange;
    }

}
