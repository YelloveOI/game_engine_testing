package cz.cvut.fel.pjv.Model;

/**
 * Represents objects character can interact with
 */
public class InteractiveLevelObject extends LevelObject {
    /**
     * Interactive level object identificator, must be same as inner Item ID
     */
    private int id;
    /**
     * Shows if character has used the interactive object
     */
    private boolean isInteracted;
    /**
     * Character can interact with interactive object if range between character and the interactive object less than sum of their interactive radiuses
     */
    private double interactableRadius;
    /**
     * Item behind interactive object
     */
    private Item item;

    public InteractiveLevelObject(Sprite sprite, String loType, int id, Item item) {
        super(sprite, loType);
        this.id = id;
        this.isInteracted = false;
        this.interactableRadius = 5;
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public double getInteractableRadius() {
        return interactableRadius;
    }

    public int getId() {
        return id;
    }

    /**
     * Change isInteracted depends on boo
     * @param boo
     */
    public void setInteracted(boolean boo) {
        isInteracted = boo;
    }

    public boolean isInteracted() {
        return isInteracted;
    }
}
