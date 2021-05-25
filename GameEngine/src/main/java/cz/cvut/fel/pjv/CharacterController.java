package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.Model.*;
import cz.cvut.fel.pjv.Model.Character;
import cz.cvut.fel.pjv.View.GeneralView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Character manager, adjusts it's state
 * Calculate necessery values to define current character state
 * Computations go in individual thread
 */
public class CharacterController extends Thread {
    private Character character;
    private CharacterAnimation characterAnimation;
    private volatile boolean stop = false; // variable to outer thread manages
    private Map<KeyCode, Boolean> keyCodeMap; // map of keys for handle keyboard events
    private List<Position> collisionPointList;

    public Position directionVector;
    public String simpleDir;
    public double speed;

    public boolean isHitable; // true if enemies can hit the charactre
    public boolean isArmed; // true if character has weapon in hand
    public boolean isDied;
    public boolean isAnimationLocked;
    public boolean acts; // true if character presses action key SPACE
    public boolean attacks; // true if character attacks

    // to handle with multple key press
    private boolean altFlag;
    private boolean controlFlag;
    private boolean spaceFlag;

    public int itemInHandIndex;
    public Item itemInHand;

    private final Position UP = new Position(0, -1);
    private final Position DOWN = new Position(0, 1);
    private final Position RIGHT = new Position(1, 0);
    private final Position LEFT = new Position(-1, 0);

    private final Position UPRIGHT = new Position(Math.sqrt(2)/2, -Math.sqrt(2)/2);
    private final Position UPLEFT = new Position(-Math.sqrt(2)/2, -Math.sqrt(2)/2);
    private final Position DOWNRIGHT = new Position(Math.sqrt(2)/2, Math.sqrt(2)/2);
    private final Position DOWNLEFT = new Position(-Math.sqrt(2)/2, Math.sqrt(2)/2);

    private static Logger LOGGER;

    /**
     * Character constructor
     */
    public CharacterController(CharacterAnimation characterAnimation) {
        this.characterAnimation = characterAnimation;
        simpleDir = "RIGHT";

        keyCodeMap = new HashMap<KeyCode, Boolean>();
        keyCodeMap.put(KeyCode.UP, false);
        keyCodeMap.put(KeyCode.DOWN, false);
        keyCodeMap.put(KeyCode.RIGHT, false);
        keyCodeMap.put(KeyCode.LEFT, false);
        keyCodeMap.put(KeyCode.SPACE, false);
        keyCodeMap.put(KeyCode.CONTROL, false);
        keyCodeMap.put(KeyCode.ALT, false);

        isHitable = true;
        isDied = false;
        isAnimationLocked = false;
        acts = false;
        attacks = false;

        controlFlag = true;
        altFlag = true;
        spaceFlag = true;

        itemInHandIndex = 0;
        directionVector = new Position(0, 0);

        LOGGER = Logger.getLogger(CharacterController.class.getName());
    }

    /**
     * Usual thread method implementation
     */
    public void run() {
        characterAnimation.start();

        while (!stop) {
            // thread "speed limit", feature to intelligent computations with character speed
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(!isDied) {
                // sets animation of staying depends on direction
                if(!isAnimationLocked) {
                    if(simpleDir == "UP") {
                        characterAnimation.setRow(19);
                    }
                    if(simpleDir == "DOWN") {
                        characterAnimation.setRow(4);
                    }
                    if(simpleDir == "RIGHT" || simpleDir == "UPRIGHT" || simpleDir == "DOWNRIGHT") {
                        characterAnimation.setRow(14);
                    }
                    if(simpleDir == "LEFT" || simpleDir == "UPLEFT" || simpleDir == "DOWNLEFT") {
                        characterAnimation.setRow(9);
                    }
                }

                // Convert Position direction to string
                setDirection();

                if(acts && spaceFlag) {
                    if(isArmed && !attacks) {
                        attack();
                    }
                    else {
                        // ID 1 for heal
                        if(itemInHand.getId() == 1) {
                            interactWithItem();
                        }
                    }
                }
                else {
                    // if any move key pressed and |direction vector| != 0
                    if(directionVector.getVectorMod() != 0 && pressedMoveKeysQuantity() != 0) {
                        move();
                    }
                }

                if(keyCodeMap.get(KeyCode.ALT) && altFlag) {
                    changeItemInHand();
                }

                if(keyCodeMap.get(KeyCode.CONTROL) && controlFlag) {
                    interactWithObject();
                }
            }
            else
            {
                // last iteration then character dies
                characterAnimation.lastIteration();
            }
        }

        characterAnimation.stop();
    }

    /**
     * Calling saveInventory from InventoryFileHandler with current inventory as param
     */
    public void saveInventory() {
        InventoryFileHandler.saveInventroy(character.getInventory());
    }

    /**
     * Creates character with inventory
     * @param inventory
     */
    public void loadCharacter(Inventory inventory) {
        character = new Character(inventory);
        this.speed = character.getSpeed();
        this.itemInHand = inventory.getItem(itemInHandIndex);
        if(itemInHand instanceof Weapon) {
            isArmed = true;
        }
    }

    /**
     * Method manages character's state when it takes damage
     * @param hitValue
     */
    public void characterGetsHit(double hitValue) {
        isHitable = false;

        // makes character unhittable
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isHitable = true;
            }
        }, 1500);

        // sets animation of getting hit depends on direction
        if(simpleDir == "UP") {
            characterAnimation.setRow(18);
        }
        if(simpleDir == "DOWN") {
            characterAnimation.setRow(3);
        }
        if(simpleDir == "RIGHT" || simpleDir == "UPRIGHT" || simpleDir == "DOWNRIGHT") {
            characterAnimation.setRow(13);
        }
        if(simpleDir == "LEFT" || simpleDir == "UPLEFT" || simpleDir == "DOWNLEFT") {
            characterAnimation.setRow(8);
        }

        character.changeHealthPoints(-hitValue);

        if(character.getHealthPoints() <= 0) {
            isDied = true;

            // sets animation of dying depends on direction
            if(simpleDir == "UP") {
                characterAnimation.setRow(17);
            }
            if(simpleDir == "DOWN") {
                characterAnimation.setRow(2);
            }
            if(simpleDir == "RIGHT" || simpleDir == "UPRIGHT" || simpleDir == "DOWNRIGHT") {
                characterAnimation.setRow(12);
            }
            if(simpleDir == "LEFT" || simpleDir == "UPLEFT" || simpleDir == "DOWNLEFT") {
                characterAnimation.setRow(7);
            }

        }

        animationLock(1000);
    }

    public boolean isHitable() {
        return isHitable;
    }

    /**
     * Method to outer thread calculations stop
     */
    public void stopRequest() {
        stop = true;
    }

    public int getItemInHandIndex() {
        return itemInHandIndex;
    }

    /**
     * Method of cooperation with View
     * @return panels with sprites of inventory items
     */
    public List<StackPane> itemPaneConstructor() {
        List<StackPane> itemSpriteList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Rectangle slotSprite = new Rectangle(40, 40, Color.GRAY);

            StackPane slot = new StackPane(slotSprite);

            slot.setMinWidth(46);
            slot.setMinHeight(46);
            itemSpriteList.add(slot);
        }

        for (int i = 0; i < character.getInventory().getInventorySize() + 1; i++) {
            ImageView slotSprite = new ImageView(character.getInventory().getItemSprite(i).sprite);

            StackPane slot = new StackPane(slotSprite);

            slot.setMinWidth(46);
            slot.setMinHeight(46);
            itemSpriteList.set(i, slot);
        }

        itemSpriteList.get(0).setStyle("-fx-border-width: 2; -fx-border-style: dashed; -fx-border-color: orange;");

        return itemSpriteList;
    }

    /**
     * Method of cooperation with View
     * @return panel with character view
     */
    public StackPane characterPaneConstructor() {
        StackPane characterPane = new StackPane(characterAnimation.getCharacterView());

        return characterPane;
    }

    /**
     * KeyMap manager method, sets a key true depends on key
     * @param key
     */
    public void keyPress(KeyCode key) {
        switch (key) {
            case UP:
                keyCodeMap.replace(KeyCode.UP, true);
                break;

            case DOWN:
                keyCodeMap.replace(KeyCode.DOWN, true);
                break;

            case RIGHT:
                keyCodeMap.replace(KeyCode.RIGHT, true);
                break;

            case LEFT:
                keyCodeMap.replace(KeyCode.LEFT, true);
                break;

            case SPACE:
                keyCodeMap.replace(KeyCode.SPACE, true);
                // start acting, mostly for changing character's state when he uses an item
                if(!acts) {
                    act();
                }
                break;

            case CONTROL:
                keyCodeMap.replace(KeyCode.CONTROL, true);
                break;

            case ALT:
                keyCodeMap.replace(KeyCode.ALT, true);
                break;
        }
    }

    public Position getCharacterPosition() {
        return character.getPosition();
    }

    public boolean isCharacterDied() {
        return isDied;
    }

    public double getCharacterXPos() {
        return character.getPosition().getPosX();
    }

    public double getCharacterYPos() {
        return character.getPosition().getPosY();
    }

    public double getCharacterSolidRadius() {
        return character.getSolidRadius();
    }

    public int getCharacterHP() {
        return character.getHealthPoints();
    }

    /**
     * Manage character's state when it gets heal
     * @param healValue
     */
    public void heal(int healValue) {
        character.changeHealthPoints(healValue);

        LOGGER.log(Level.INFO, "CHARACTER HEALED");
    }

    /**
     * KeyMap manager method, sets a key false depends on key
     * Also manages keys flags to prevent multiple presses
     * @param key
     */
    public void keyUnpress(KeyCode key)  {
        switch (key) {
            case UP:
                keyCodeMap.replace(KeyCode.UP, false);
                break;

            case DOWN:
                keyCodeMap.replace(KeyCode.DOWN, false);
                break;

            case RIGHT:
                keyCodeMap.replace(KeyCode.RIGHT, false);
                break;

            case LEFT:
                keyCodeMap.replace(KeyCode.LEFT, false);
                break;

            case SPACE:
                keyCodeMap.replace(KeyCode.SPACE, false);
                spaceFlag = true;
                break;

            case CONTROL:
                keyCodeMap.replace(KeyCode.CONTROL, false);
                controlFlag = true;
                break;

            case ALT:
                keyCodeMap.replace(KeyCode.ALT, false);
                altFlag = true;
                break;
        }
    }

    /**
     * Manage character's state when he switches items in inventory
     */
    public void changeItemInHand() {
        altFlag = false;
        itemInHandIndex++;
        // index of item in hand can't be more than (quantity of items in inventory - 1)
        itemInHandIndex %= character.getInventory().getInventorySize() + 1;
        itemInHand = character.getInventory().getItem(itemInHandIndex);

        if(itemInHand instanceof Weapon) {
            isArmed = true;
        }
        else {
            isArmed = false;
        }
    }


    /**
     * Locks animation to play current animation for a time depends on duration
     * @param duration
     */
    public void animationLock(int duration) {
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
     * Converting string direction from direction vector
     */
    public void setDirection() {
        simpleDir = "";
        if (keyCodeMap.get(KeyCode.UP)) {
            directionVector = UP.copy();
            simpleDir = "UP";
        }
        if (keyCodeMap.get(KeyCode.DOWN)) {
            directionVector = DOWN.copy();
            simpleDir = "DOWN";
        }
        if (keyCodeMap.get(KeyCode.RIGHT)) {
            directionVector = RIGHT.copy();
            simpleDir = "RIGHT";
        }
        if (keyCodeMap.get(KeyCode.LEFT)) {
            directionVector = LEFT.copy();
            simpleDir = "LEFT";
        }

        if(keyCodeMap.get(KeyCode.UP) && keyCodeMap.get(KeyCode.RIGHT)) {
            directionVector = UPRIGHT.copy();
            simpleDir = "UPRIGHT";
        }
        if(keyCodeMap.get(KeyCode.UP) && keyCodeMap.get(KeyCode.LEFT)) {
            directionVector = UPLEFT.copy();
            simpleDir = "UPLEFT";
        }
        if(keyCodeMap.get(KeyCode.DOWN) && keyCodeMap.get(KeyCode.RIGHT)) {
            directionVector = DOWNRIGHT.copy();
            simpleDir = "DOWNRIGHT";
        }
        if(keyCodeMap.get(KeyCode.DOWN) && keyCodeMap.get(KeyCode.LEFT)) {
            directionVector = DOWNLEFT.copy();
            simpleDir = "DOWNLEFT";
        }
    }

    /**
     * Manages character's position
     */
    public void move() {
        Position moveVector = directionVector.copy();
        moveVector.multiplyVector(speed/100);

        // sets walk's animation depends on direction
        if(!isAnimationLocked) {
            if(simpleDir == "UP") {
                characterAnimation.setRow(15);
            }
            if(simpleDir == "DOWN") {
                characterAnimation.setRow(0);
            }
            if(simpleDir == "RIGHT" || simpleDir == "UPRIGHT" || simpleDir == "DOWNRIGHT") {
                characterAnimation.setRow(10);
            }
            if(simpleDir == "LEFT" || simpleDir == "UPLEFT" || simpleDir == "DOWNLEFT") {
                characterAnimation.setRow(5);
            }
        }

        // change character's position
        character.getPosition().applyVector(moveVector);

        try {
            collisionPointList = getCollisionPointList(Main.levelController.getSolidObjectList());

            // collision processing
            // if character reaches wall in process position handles by "flow" vector
            // flow vector if the force vector - vector of moving to wall
            // vector of moving to wall calculates by applying projection of force vector on
            // vector of moving straight to the wall
            if(collisionPointList.size() != 0) {
                for(Position colisionPoint: collisionPointList) {
                    Position collisionVector = character.getPosition().createVector(colisionPoint);
                    Position flowVector = colisionPoint.createUniteVector(character.getPosition());
                    flowVector.multiplyVector(directionVector.getProjectionOn(collisionVector));
                    character.getPosition().applyVector(flowVector);
                }
            }
        }
        catch (Exception e) {

        }

    }

    /**
     * Manages character's state when it attacks
     */
    public void attack() {
        Position enemyVector;

        // resetting animation to prevent starting with frame in the middle
        characterAnimation.stop();
        characterAnimation.start();

        // managing the character attack influence
        speed /= 1.5;
        attacks = true;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                speed *= 1.5;
                attacks = false;
            }
        }, 700);

        // sets animation of attack depends on direction
        if(simpleDir == "UP") {
            characterAnimation.setRow(16);
        }
        if(simpleDir == "DOWN") {
            characterAnimation.setRow(1);
        }
        if(simpleDir == "RIGHT" || simpleDir == "UPRIGHT" || simpleDir == "DOWNRIGHT") {
            characterAnimation.setRow(11);
        }
        if(simpleDir == "LEFT" || simpleDir == "UPLEFT" || simpleDir == "DOWNLEFT") {
            characterAnimation.setRow(6);
        }

        try {
            List<EnemyController> enemyControllerList = Main.levelController.getEnemyControllerList();

            // checking if character hits an enemy
            for (EnemyController enemyController: enemyControllerList) {
                if(!enemyController.isDied()) {
                    // calculating range to enemy and comparing with character weapon's attack range (solid radiuses are considered)
                    // calculating angle between direction vector and vector from character's position to enemy's position
                    // comparing with (character weapon's in hand attack angle value)/2
                    Position attackVector = directionVector.copy();
                    attackVector.multiplyVector(((Weapon) itemInHand).getAttackRange() + character.getSolidRadius() + enemyController.getEnemySolidRadius());
                    enemyVector = character.getPosition().createVector(enemyController.getEnemyPosition());
                    if(
                            character.getPosition().getRangeTo(enemyController.getEnemyPosition()) <= attackVector.getVectorMod() &&
                                    attackVector.getVectorsAngle(enemyVector) <= (double) ((Weapon) itemInHand).getAttackAngle()/2
                    ) {
                        enemyController.enemyGetsHit(((Weapon) itemInHand).getAttackValue());
                        LOGGER.log(Level.INFO, "CHARACTER HITS " + enemyController.getEnemyType());

                        double knockbackValue = ((Weapon) itemInHand).getKnockBack();

                        Position forceUnitVector = character.getPosition().createUniteVector(enemyController.getEnemyPosition());
                        forceUnitVector.multiplyVector(knockbackValue);

                        enemyController.force(forceUnitVector, 50);
                    }
                }

                animationLock(700);
            }
        }
        catch (Exception e) {

        }
    }

    /**
     * @return points of all collisions with character
     */
    public List<Position> getCollisionPointList(List<SolidObject> solidObjectList) {
        List<Position> collisionPoints = new ArrayList<>();

        for (SolidObject so: solidObjectList) {
            Position colisionPoint = so.getPosition().copy();
            Position centerVector = so.getPosition().createVector(character.getPosition());

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

            // verify, if |vector from character's position to collision point| < character's solid radius
            if(colisionPoint.getRangeTo(character.getPosition()) <= character.getSolidRadius()) {
                collisionPoints.add(colisionPoint);
            }
        }

        return collisionPoints;
    }

    /**
     * Makes character status "in action" and offs ot after 200 ms
     */
    public void act() {
        acts = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                acts = false;
                spaceFlag = false;
            }
        }, 200);
    }

    /**
     * @return quantity of pressed moving keys
     */
    public int pressedMoveKeysQuantity() {
        int quantity = 0;
        if(keyCodeMap.get(KeyCode.UP)) quantity++;
        if(keyCodeMap.get(KeyCode.DOWN)) quantity++;
        if(keyCodeMap.get(KeyCode.RIGHT)) quantity++;
        if(keyCodeMap.get(KeyCode.LEFT)) quantity++;

        return quantity;
    }

    public boolean isPressed(KeyCode key) {
        return keyCodeMap.getOrDefault(key, false);
    }

    /**
     * Manages character's state when he uses an item
     */
    public void interactWithItem() {
        // heal if item ID is 1 (ID 1 is for heal items)
        if(itemInHand.getId() == 1) {
            character.getInventory().removeFromInventory(itemInHandIndex);
            heal(45);
        }

        // shifting item in hand
        if(itemInHandIndex != 0) {
            itemInHandIndex --;
        }

        itemInHand = character.getInventory().getItem(itemInHandIndex);

        if(itemInHand instanceof Weapon) {
            isArmed = true;
        }
        else {
            isArmed = false;
        }

        spaceFlag = false;
    }

    /**
     * Manages character's and object's state when he uses the interactive object
     */
    public void interactWithObject() {
        List<InteractiveLevelObject> interactiveLevelObjectList = Main.levelController.getInteractiveLevelObjectList();

        for (InteractiveLevelObject ilo: interactiveLevelObjectList) {
            // verify if character can reach the interactive object
            if(ilo.getPosition().getRangeTo(character.getPosition()) <= character.getInteractableRadius() + ilo.getInteractableRadius()) {
                // managing of interaction with object
                if(!character.getInventory().putItem(ilo.getItem())) {
                    character.getInventory().removeFromInventory(itemInHandIndex);
                    character.getInventory().putItem(ilo.getItem());
                }
                ilo.setInteracted(true);

                controlFlag = false;
            }
        }
    }

}
