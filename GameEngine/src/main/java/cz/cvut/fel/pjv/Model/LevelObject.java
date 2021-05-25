package cz.cvut.fel.pjv.Model;

/**
 * Represents display able objects which influence game play
 */
public class LevelObject {
    /**
     * Level object sprite
     */
    private Sprite sprite;
    /**
     * Level object position
     */
    protected Position position;
    /**
     * Level object type
     */
    public final String loType;

    /**
     * Constructs level object depends on params
     * @param sprite
     * @param loType
     */
    public LevelObject(Sprite sprite, String loType) {
        this.loType = loType;
        this.sprite = sprite;
        this.position = new Position(10, 10);
    }

    /**
     * @return string with type of level object
     */
    public String getLoType() {
        return loType;
    }

    /**
     * Sets level object position depends on position
     * @param position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Position getPosition() {
        return position;
    }
}
