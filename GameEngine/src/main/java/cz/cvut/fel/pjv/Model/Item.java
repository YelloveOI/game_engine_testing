package cz.cvut.fel.pjv.Model;

/**
 * Represents items character can use
 * Each type of item has an ID and string ID representation
 */
public class Item {
    /**
     * Item's sprite
     */
    private Sprite sprite;
    /**
     * Item's identificator
     */
    private int id;
    /**
     * String representation of id
     */
    private final String itemType;

    /**
     * Constructs item object depends on params
     * @param id
     * @param sprite
     * @param itemType
     */
    public Item(int id, Sprite sprite, String itemType) {
        this.sprite = sprite;
        this.itemType = itemType;
        this.id = id;
    }

    public String getItemType() {
        return itemType;
    }

    public int getId() {
        return id;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
