package cz.cvut.fel.pjv.Model;

import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * Composes necessery fields for works with sprites
 */
public class Sprite {
    public final Image sprite;
    public final int width;
    public final int height;
    // sprite list (table of frames) can has different quantity of frames on different rows
    // so columnList has form "columnList[0] has quantity of frames of 0 row of sprite list"
    public final ArrayList<Integer> columnList;

    public Sprite(Image sprite, int width, int height, ArrayList<Integer> columnList) {
        this.sprite = sprite;
        this.width = width;
        this.height = height;
        this.columnList = columnList;
    }
}
