package cz.cvut.fel.pjv.Model;

import java.util.List;

/**
 * Represents different surrounds in which character turn up
 */
public class Level {
    /**
     * List of enemies on the level
     */
    private List<Enemy> enemyList;
    /**
     * List of solid objects on the level
     */
    private List<SolidObject> solidObjectList;
    /**
     * List of interactive objects on the level
     */
    private List<InteractiveLevelObject> interactiveLevelObjectList;

    /**
     * Constructs level object depends on params
     * @param enemyList
     * @param levelSolidObjectList
     * @param interactiveLevelObjectList
     */
    public Level(
            List<Enemy> enemyList,
            List<SolidObject> levelSolidObjectList,
            List<InteractiveLevelObject> interactiveLevelObjectList
    ) {
        this.enemyList = enemyList;
        this.solidObjectList = levelSolidObjectList;
        this.interactiveLevelObjectList = interactiveLevelObjectList;

    }

    public List<InteractiveLevelObject> getInteractiveLevelObjectList() {
        return interactiveLevelObjectList;
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }

    public List<SolidObject> getSolidObjectList() {
        return solidObjectList;
    }
}
