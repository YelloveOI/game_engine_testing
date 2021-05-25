package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.Model.Enemy;
import cz.cvut.fel.pjv.Model.Position;
import cz.cvut.fel.pjv.Model.SolidObject;
import javafx.geometry.Pos;

import java.util.*;
import java.util.logging.Logger;

/**
 * Enemy manager, adjusts it's state
 * Calculate necessery values to define current enemy state
 * Computations go in individual thread
 */
public class EnemyController extends Thread {
    private Enemy enemy;
    private EnemyAnimation enemyAnimation;
    private volatile boolean stop = false; // variable to outer thread manages
    private List<Position> collisionPointList;

    private Position targetPoint;
    private Position directionVector;
    private Position nestPoint;
    private double nestAreaRadius;
    private String simpleDir;
    private double speed;

    private boolean isDied;
    private boolean isAgred; // true if chasing the character
    private boolean isStunned; // true if can't act
    private boolean isAnimationLocked;

    private final Position UPRIGHT = new Position(Math.sqrt(2)/2, -Math.sqrt(2)/2);
    private final Position UPLEFT = new Position(-Math.sqrt(2)/2, -Math.sqrt(2)/2);
    private final Position DOWNRIGHT = new Position(Math.sqrt(2)/2, Math.sqrt(2)/2);
    private final Position DOWNLEFT = new Position(-Math.sqrt(2)/2, Math.sqrt(2)/2);

    Logger LOGGER;

    public EnemyController(Enemy enemy, EnemyAnimation enemyAnimation) {
        this.enemy = enemy;
        this.targetPoint = enemy.getPosition().copy();
        this.enemyAnimation = enemyAnimation;
        this.nestPoint = enemy.getPosition().copy(); // area of "hanging around" of the enemy, enemy can't go further by itself
        this.speed = enemy.getSpeed();
        this.simpleDir = "";

        this.isDied = false;
        this.isAgred = false;
        this.isStunned = false;
        this.isAnimationLocked = false;

        this.nestAreaRadius = 30;

        LOGGER = Logger.getLogger(EnemyController.class.getName());
    }

    /**
     * Usual thread method implementation
     */
    public void run() {
        enemyAnimation.setRow(4); // animation start offset, defines first row of sprite list to play animation
        enemyAnimation.start();

        while (!stop) {

            // thread "speed limit", feature to intelligent computation with enemy speed
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(!isDied) {
                // sets animation of staying depends on direction
                if(!isAnimationLocked) {
                    if(simpleDir == "UP") {
                        enemyAnimation.setRow(19);
                    }
                    if(simpleDir == "DOWN") {
                        enemyAnimation.setRow(4);
                    }
                    if(simpleDir == "RIGHT") {
                        enemyAnimation.setRow(14);
                    }
                    if(simpleDir == "LEFT") {
                        enemyAnimation.setRow(4);
                    }
                }

                if(!isStunned) {
                    // if enemy sees the character and character is alive
                    if(seePoint(Main.characterController.getCharacterPosition())&& !Main.characterController.isCharacterDied()) {
                        isAgred = true;
                        nestPoint = enemy.getPosition().copy();
                        targetPoint = Main.characterController.getCharacterPosition().copy();

                        // if enemy can hit character
                        if(enemy.getPosition().getRangeTo(Main.characterController.getCharacterPosition()) < enemy.getAttackRange() && Main.characterController.isHitable()) {
                            attack();
                            Main.characterController.characterGetsHit(enemy.getHitValue());
                        }
                    }
                    else {
                        isAgred = false;
                    }

                    directionVector = enemy.getPosition().createUniteVector(targetPoint);
                    simpleDir = setDirection(directionVector);

                    if(directionVector.getVectorMod() != 0) {
                        move(!isAgred);
                    }
                    else {
                        hangAround();
                    }
                }

            }
            else {
                // last iteration then enemy dies
                enemyAnimation.lastIteration();
            }

        }
        enemyAnimation.stop();
    }

    // each enemy direction vector defines in a group of "simple directions"
    // it because of 4 directional sprites
    // calculates range from direction vector to each pair of marginal points - UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT
    // if range sum > curve length => direction vector is between they
    public String setDirection(Position directionVector) {
        String sDir = "";
        
        if(directionVector.getRangeTo(UPLEFT) + directionVector.getRangeTo(UPRIGHT) < 1.57) sDir = "UP";
        if(directionVector.getRangeTo(DOWNLEFT) + directionVector.getRangeTo(DOWNRIGHT) < 1.57) sDir = "DOWN";
        if(directionVector.getRangeTo(UPLEFT) + directionVector.getRangeTo(DOWNLEFT) < 1.57) sDir = "LEFT";
        if(directionVector.getRangeTo(UPRIGHT) + directionVector.getRangeTo(DOWNRIGHT) < 1.57) sDir = "RIGHT";
        
        return sDir;
    }

    /**
     * "Forcing" enemy to vector by steps with period 5 ms
     * @param forceVector
     * @param duration total duration of forcing
     */
    public void force(Position forceVector, int duration) {
        if(!isDied) {
            Timer forceTimer = new Timer();
            double factor = 2;

            forceTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // divides force vector by 2 each step and apllies it to enemy position
                    forceVector.divideVector(factor);
                    enemy.getPosition().applyVector(forceVector);

                    // if enemy reaches wall in process position handles by "flow" vector
                    // flow vector if the force vector - vector of moving to wall
                    // vector of moving to wall calculates by applying projection of force vector on
                    // vector of moving straight to the wall
                    if(collisionPointList.size() != 0) {
                        for(Position colisionPoint: collisionPointList) {
                            Position collisionVector = enemy.getPosition().createVector(colisionPoint);
                            Position flowVector = colisionPoint.createUniteVector(enemy.getPosition());
                            flowVector.multiplyVector(forceVector.getProjectionOn(collisionVector));
                            enemy.getPosition().applyVector(flowVector);
                        }
                        if(collisionPointList.size() != 0) {
                            forceVector.multiplyVector(-1);
                            enemy.getPosition().applyVector(forceVector);
                        }
                        if(!isAgred) {
                            targetPoint = enemy.getPosition();
                        }
                    }
                }
            }, 0, 5);

            try {
                sleep(duration);
                forceTimer.cancel();
            }
            catch (Exception e) {
                forceTimer.cancel();
            }
        }
    }

    /**
     * @return type of the enemy ("SKELETON" in this case)
     */
    public String getEnemyType() {
        return enemy.getLoType();
    }

    public boolean isDied() {
        return isDied;
    }

    public double getEnemySolidRadius() {
        return enemy.getSolidRadius();
    }

    public Position getEnemyPosition() {
        return enemy.getPosition();
    }

    public EnemyAnimation getEnemyAnimation() {
        return enemyAnimation;
    }

    /**
     * Method to outer thread calculations stop
     */
    public void stopRequest() {
        stop = true;
    }

    /**
     * Calculates range to a point and compares it's value to enemy's foresight
     * @param point
     * @return true if sees, false if not sees
     */
    public boolean seePoint(Position point) {
        double range = enemy.getPosition().getRangeTo(point);

        if(enemy.getForesight() >= range) {
            return true;
        }
        else {
            return false;
        }
    }

    public double getEnemyXPos() {
        return enemy.getPosition().getPosX();
    }

    public double getEnemyYPos() {
        return enemy.getPosition().getPosY();
    }

    /**
     * Method manages enemy's state when it takes damage
     * @param hitValue
     */
    public void enemyGetsHit(double hitValue) {
        speed /= 2;

        // slows enemy on 500 ms
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                speed *= 2;
            }
        }, 500);

        // sets damage getting animation depends in enemy's direction
        if(simpleDir == "UP") {
            enemyAnimation.setRow(18);
        }
        if(simpleDir == "DOWN") {
            enemyAnimation.setRow(3);
        }
        if(simpleDir == "RIGHT") {
            enemyAnimation.setRow(13);
        }
        if(simpleDir == "LEFT") {
            enemyAnimation.setRow(8);
        }

        enemy.changeHealthPoints(-hitValue);

        if(enemy.getHealthPoints() <= 0) {
            isDied = true;

            // sets dying animation depends on enemy's direction
            if(simpleDir == "UP") {
                enemyAnimation.setRow(12);
            }
            if(simpleDir == "DOWN") {
                enemyAnimation.setRow(2);
            }
            if(simpleDir == "RIGHT") {
                enemyAnimation.setRow(12);
            }
            if(simpleDir == "LEFT") {
                enemyAnimation.setRow(7);
            }
        }

        animationLock(1000); // locks animation to play whole animation
    }

    /**
     * Locks animation to play current animation for a time depends on duration
     * @param duration
     */
    private void animationLock(int duration) {
        isAnimationLocked = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isAnimationLocked = false;
            }
        }, duration);
    }

    /**
     * Manages enemy's state when it gets stun
     * @param duration
     */
    private void getStun(int duration) {
        isStunned = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isStunned = false;
            }
        }, duration);
    }

    /**
     * Method sets a random target point to enemy with chance 1%
     * Target point must be in nest area
     * Simple AI :)
     */
    private void hangAround() {
        Random random = new Random();
        int chance = random.nextInt(100);
        if(chance == 1) {
            double factor = random.nextDouble();
            boolean sign = random.nextBoolean();
            double x,y;

            if(sign) {
                x = nestAreaRadius*factor;
            }
            else {
                x = -nestAreaRadius*factor;
            }

            sign = random.nextBoolean();
            factor = random.nextDouble();

            if(sign) {
                y = Math.sqrt(nestAreaRadius*nestAreaRadius - x*x) * factor;
            }
            else {
                y = -Math.sqrt(nestAreaRadius*nestAreaRadius - x*x) * factor;
            }

            if(x < nestAreaRadius/3) {
                x += nestAreaRadius/3;
            }
            if(y < nestAreaRadius/3) {
                y += nestAreaRadius/3;
            }

            x += nestPoint.getPosX();
            y += nestPoint.getPosY();

            if(x > 700) {
                x = 700;
            }
            if(x < 0) {
                x = 0;
            }

            if(y > 450) {
                y = 450;
            }
            if(y < 0) {
                y = 0;
            }

            targetPoint = new Position(x, y);
        }

    }

    /**
     * Manages enemy's position
     * @param running, if not running speed /= 2, walking speed
     */
    private void move(boolean running) {
        Position moveVector = directionVector.copy();

        if(!running) {
            moveVector.multiplyVector(speed/100);
        }
        else {
            moveVector.multiplyVector(speed/200);
        }

        // sets walk's animation depends on direction
        if(!isAnimationLocked) {
            if(simpleDir == "UP") {
                enemyAnimation.setRow(15);
            }
            if(simpleDir == "DOWN") {
                enemyAnimation.setRow(0);
            }
            if(simpleDir == "RIGHT") {
                enemyAnimation.setRow(10);
            }
            if(simpleDir == "LEFT") {
                enemyAnimation.setRow(5);
            }
        }

        // change enemy's position
        enemy.getPosition().applyVector(moveVector);

        collisionPointList = getCollisionPoints(enemy, Main.levelController.getSolidObjectList());

        // collision processing
        // if enemy reaches wall in process position handles by "flow" vector
        // flow vector if the force vector - vector of moving to wall
        // vector of moving to wall calculates by applying projection of force vector on
        // vector of moving straight to the wall
        if(collisionPointList.size() != 0) {
            for(Position colisionPoint: collisionPointList) {
                Position collisionVector = enemy.getPosition().createVector(colisionPoint);
                Position flowVector = colisionPoint.createUniteVector(enemy.getPosition());
                flowVector.multiplyVector(directionVector.getProjectionOn(collisionVector));
                enemy.getPosition().applyVector(flowVector);
            }
            if(!isAgred) {
                targetPoint = enemy.getPosition();
            }
        }
    }

    /**
     * Manages enemy's state when it attacks
     */
    private void attack() {
        // sets animation of attack depends on direction
        if(simpleDir == "UP") {
            enemyAnimation.setRow(16);
        }
        if(simpleDir == "DOWN") {
            enemyAnimation.setRow(1);
        }
        if(simpleDir == "RIGHT") {
            enemyAnimation.setRow(11);
        }
        if(simpleDir == "LEFT") {
            enemyAnimation.setRow(6);
        }

        animationLock(1000);
    }

    /**
     * @return points of all collisions with enemy
     */
    private List<Position> getCollisionPoints(Enemy enemy, List<SolidObject> levelSolidObjectList) {
        List<Position> collisionPoints = new ArrayList<>();

        for (SolidObject so: levelSolidObjectList) {
            Position colisionPoint = so.getPosition().copy();
            Position centerVector = so.getPosition().createVector(enemy.getPosition());

            // creating assumed collision point (they are on the edges of solid object)
            if(Math.abs(centerVector.getPosX()) > so.getxLength()/2) {
                if(centerVector.getPosX() > so.getxLength()/2) {
                    colisionPoint.applyVector(new Position(so.getxLength()/2, 0));
                }
                else {
                    colisionPoint.applyVector(new Position(-so.getxLength()/2, 0));
                }
            }
            else {
                colisionPoint.applyVector(new Position(centerVector.getPosX(), 0));
            }

            if(Math.abs(centerVector.getPosY()) > so.getyLength()/2) {
                if(centerVector.getPosY() > so.getyLength()/2) {
                    colisionPoint.applyVector(new Position(0, so.getyLength()/2));
                }
                else {
                    colisionPoint.applyVector(new Position(0, -so.getyLength()/2));
                }
            }
            else {
                colisionPoint.applyVector(new Position(0, centerVector.getPosY()));
            }

            if(colisionPoint.getRangeTo(enemy.getPosition()) <= enemy.getSolidRadius()) {
                collisionPoints.add(colisionPoint);
            }
        }

        // verify, if |vector from enemy's position to collision point| < enemy's solid radius
        if(
                enemy.getPosition().createVector(Main.characterController.getCharacterPosition()).getVectorMod() <
                enemy.getSolidRadius()
        ) {
            Position characterCollisionPoint = getEnemyPosition().createUniteVector(Main.characterController.getCharacterPosition());
            characterCollisionPoint.multiplyVector(enemy.getSolidRadius());

            collisionPoints.add(characterCollisionPoint);
        }

        return collisionPoints;
    }

}
