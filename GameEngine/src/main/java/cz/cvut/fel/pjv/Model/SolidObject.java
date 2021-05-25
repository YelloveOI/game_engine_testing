package cz.cvut.fel.pjv.Model;

/**
 * Represents objects character can't go throught
 * Implements methods for works with solid objects
 */
public class SolidObject extends LevelObject {
    /**
     * Width of solid object
     */
    private double xLength;
    /**
     * Height of solid object
     */
    private double yLength;
    /**
     * Number of tile in sprite table
     */
    private int tileNumb;

    /**
     * Constructs solid object depends on params
     * @param sprite
     * @param loType
     * @param xLength
     * @param yLength
     */
    public SolidObject(Sprite sprite, String loType, double xLength, double yLength) {
        super(sprite, loType);
        this.tileNumb = 0;
        this.xLength = xLength;
        this.yLength = yLength;
    }

    /**
     * @return number of tile in sprite table solid object uses
     */
    public int getTileNumb() {
        return tileNumb;
    }

    /**
     * Because of sprite list can have tiles of different objects is necessery to explicitly indicate
     * the number of needed tile
     * @param tileNumb
     */
    public void setTileNumb(int tileNumb) {
        this.tileNumb = tileNumb;
    }

    public double getxLength() {
        return xLength;
    }

    public double getyLength() {
        return yLength;
    }
}
