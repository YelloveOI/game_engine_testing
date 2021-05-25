package cz.cvut.fel.pjv.View;

import cz.cvut.fel.pjv.EnemyController;
import cz.cvut.fel.pjv.Main;
import cz.cvut.fel.pjv.Model.InteractiveLevelObject;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.util.List;

/**
 * Represents the game view
 * Takes info from controllers and displays
 */
public class GameView extends Scene {
    private ProgressBar healthBar;
    private Pane groundPane;
    private Pane characterPane;
    private HBox inventory;
    private AnimationTimer gameViewHandler;
    private List<StackPane> enemyPaneList;
    private List<Pane> solidObjectPaneList;
    private List<Pane> interactiveLevelObjectPaneList;
    private List<StackPane> itemPaneList;

    public GameView(int width, int height) {
        super(new Pane(), width, height);

        gameViewHandler = new AnimationTimer() {
            @Override
            public void handle(long l) {
                characterUpdate();
                enemiesUpdate();
                inventoryUpdate();
                interactiveLevelObjectUpdate();
            }
        };


        setOnKeyPressed(e -> {
            Main.characterController.keyPress(e.getCode());
        });

        setOnKeyReleased(e -> {
            Main.characterController.keyUnpress(e.getCode());
        });

    }

    public void gameViewInit() {
        groundPane = Main.levelController.groundPaneConstructor();
        characterPane = Main.characterController.characterPaneConstructor();
        enemyPaneList = Main.levelController.enemyPaneListConstructor();
        solidObjectPaneList = Main.levelController.solidObjectPaneConstructor();
        interactiveLevelObjectPaneList = Main.levelController.interactiveLevelObjectPaneConstrucor();
        itemPaneList = Main.characterController.itemPaneConstructor();

        healthBar = new ProgressBar();
        healthBar.setMaxHeight(10);
        healthBar.setMaxWidth(50);
        healthBar.setStyle("-fx-accent: red");

        characterPane.getChildren().add(healthBar);

        healthBar.setTranslateY(- 30);

        inventory = new HBox(10);
        inventory.setStyle(
                "-fx-background-color: rgba(16, 19, 16, 0.5);" +
                        "-fx-border-width: 2; -fx-border-color: linear-gradient(from 25% 25% to 100% 100%, #323232, #505050);" +
                        "-fx-effect: dropshadow(gaussian, darkslategray, 50, 0, 0, 0);"
        );
        inventory.setTranslateX(303);
        inventory.setTranslateY(10);

        for (Pane itemPane: itemPaneList) {
            inventory.getChildren().add(itemPane);
        }


        groundPane.getChildren().addAll(enemyPaneList);
        groundPane.getChildren().addAll(solidObjectPaneList);
        groundPane.getChildren().addAll(interactiveLevelObjectPaneList);
        groundPane.getChildren().add(characterPane);
        groundPane.getChildren().add(inventory);

        setRoot(groundPane);
    }

    public void highlightItemInHand(int index) {
        for (StackPane itemPane: itemPaneList) {
            itemPane.setStyle("");
        }
        itemPaneList.get(index).setStyle("-fx-border-width: 2; -fx-border-style: dashed; -fx-border-color: orange");
    }

    public   void inventoryUpdate() {
        inventory.getChildren().removeAll(itemPaneList);
        itemPaneList = Main.characterController.itemPaneConstructor();
        inventory.getChildren().addAll(itemPaneList);

        highlightItemInHand(Main.characterController.getItemInHandIndex());
    }

    public void stopHandle() {
        gameViewHandler.stop();
    }

    public void startHandle() {
        gameViewHandler.start();
    }


    private void characterUpdate() {
        double xPos = Main.characterController.getCharacterXPos();
        double yPos = Main.characterController.getCharacterYPos();

        characterPane.setTranslateX(xPos - 26);
        characterPane.setTranslateY(yPos - 26);

        healthBar.setProgress((double) Main.characterController.getCharacterHP()/100);

    }

    private void interactiveLevelObjectUpdate() {
        int interactiveLevelObjectIndex = -1;

        List<InteractiveLevelObject> interactiveLevelObjectList = Main.levelController.getInteractiveLevelObjectList();

        for (int i = 0; i < interactiveLevelObjectList.size(); i++) {
            if(interactiveLevelObjectList.get(i).isInteracted()) {
                interactiveLevelObjectIndex = i;
            }
        }

        if(interactiveLevelObjectIndex != -1) {
            InteractiveLevelObject ilo = Main.levelController.getInteractiveLevelObjectList().get(interactiveLevelObjectIndex);
            if(ilo.getId() == 1) {
                interactiveLevelObjectList.remove(interactiveLevelObjectIndex);
                groundPane.getChildren().remove(interactiveLevelObjectPaneList.get(interactiveLevelObjectIndex));
                interactiveLevelObjectPaneList.remove(interactiveLevelObjectIndex);
            }
            if(ilo.getId() == 2) {
                interactiveLevelObjectList.remove(interactiveLevelObjectIndex);
                groundPane.getChildren().remove(interactiveLevelObjectPaneList.get(interactiveLevelObjectIndex));
                interactiveLevelObjectPaneList.remove(interactiveLevelObjectIndex);
            }
        }
    }

    private void enemiesUpdate() {

        List<EnemyController> enemyControllerList = Main.levelController.getEnemyControllerList();

        for (int i = 0; i < enemyPaneList.size(); i++) {
            enemyPaneList.get(i).setTranslateX(enemyControllerList.get(i).getEnemyXPos() - 1.8 * enemyControllerList.get(i).getEnemySolidRadius());
            enemyPaneList.get(i).setTranslateY(enemyControllerList.get(i).getEnemyYPos() - 2 * enemyControllerList.get(i).getEnemySolidRadius());
        }
    }

}
