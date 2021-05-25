package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.Model.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Level controller class
 * Defines tiles image view (then gets from file handler) and enemy controller list
 */
public class LevelController {
    private cz.cvut.fel.pjv.Model.Level level;
    public List<ImageView> groundTiles;
    public List<EnemyController> enemyControllerList;

    private Logger LOGGER;

    public LevelController() {
        enemyControllerList = new ArrayList<>();

        LOGGER = Logger.getLogger(Level.class.getName());

    }

    public List<InteractiveLevelObject> getInteractiveLevelObjectList() {
        return level.getInteractiveLevelObjectList();
    }

    public List<SolidObject> getSolidObjectList() {
        return level.getSolidObjectList();
    }

    public List<EnemyController> getEnemyControllerList() {
        return enemyControllerList;
    }

    /**
     * Method of cooperation with View
     * @return array of panels created depends on enemy views (implicit in conrollers)
     */
    public List<StackPane> enemyPaneListConstructor() {
        List<StackPane> enemyPaneList = new ArrayList<>();
        StackPane enemyPane = new StackPane();

        for(EnemyController enemyController: enemyControllerList) {
            enemyPane = new StackPane(enemyController.getEnemyAnimation().getEnemyView());

            enemyPaneList.add(enemyPane);
        }

        return enemyPaneList;
    }

    /**
     * Method of cooperation with View
     * @return panel with views of each ground tile
     */
    public Pane groundPaneConstructor() {
        Pane groundPane = new Pane();
        groundPane.getChildren().addAll(groundTiles);

        return groundPane;
    }

    /**
     * Sets level, necessery at level choosing
     * @param level
     */
    public void setLevel(Level level) {
        this.level = level;

        for(Enemy enemy: level.getEnemyList()) {
            EnemyAnimation enemyAnimation = new EnemyAnimation(enemy.getSprite());

            enemyControllerList.add(new EnemyController(enemy, new EnemyAnimation(enemy.getSprite())));
        }

    }

    /**
     * Starts each enemy controller thread
     */
    public void enemyControllersStart() {
        for(EnemyController enemyController: enemyControllerList) {
            enemyController.start();
        }
    }

    /**
     * Kills each enemy controller thread
     */
    public void enemyControllersStop() {
        for(EnemyController enemyController: enemyControllerList) {
            enemyController.stopRequest();
            enemyController.interrupt();
        }
    }

    /**
     * Method of cooperation with View
     * @return an array of panels with interactive level objects
     */
    public List<Pane> interactiveLevelObjectPaneConstrucor() {
        List<InteractiveLevelObject> interactiveLevelObjectList = level.getInteractiveLevelObjectList();
        List<Pane> interactiveLevelObjectPaneList = new ArrayList<>();

        for (InteractiveLevelObject interactiveLevelObject: interactiveLevelObjectList) {
            ImageView iloSpriteView = new ImageView(interactiveLevelObject.getSprite().sprite);
            Pane interactiveLevelObjectPane = new Pane(iloSpriteView);
            interactiveLevelObjectPane.setTranslateX(interactiveLevelObject.getPosition().getPosX());
            interactiveLevelObjectPane.setTranslateY(interactiveLevelObject.getPosition().getPosY());

            interactiveLevelObjectPaneList.add(interactiveLevelObjectPane);
        }

        return interactiveLevelObjectPaneList;
    }

    /**
     * Method of cooperation with View
     * @return an array of panels with solid level objects
     */
    public List<Pane> solidObjectPaneConstructor() {
        List<SolidObject> solidObjectList = level.getSolidObjectList();
        List<Pane> solidObjectPaneList = new ArrayList<>();

        for (SolidObject solidObject: solidObjectList) {

            double xSide = solidObject.getxLength();
            double ySide = solidObject.getyLength();
            double xPos = solidObject.getTileNumb() % solidObject.getSprite().columnList.get(0);
            double yPos = solidObject.getTileNumb() / solidObject.getSprite().columnList.get(0);

            ImageView soSpriteView = new ImageView(solidObject.getSprite().sprite);
            soSpriteView.setViewport(new Rectangle2D(xPos * 32, yPos * 32, xSide, ySide));

            Pane solidObjectPane = new Pane(soSpriteView);

            solidObjectPane.setTranslateX(solidObject.getPosition().getPosX() - xSide/2);
            solidObjectPane.setTranslateY(solidObject.getPosition().getPosY() - ySide/2);


            solidObjectPaneList.add(solidObjectPane);
        }

        return solidObjectPaneList;
    }

}
