package cz.cvut.fel.pjv.View;

import cz.cvut.fel.pjv.Main;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements general view for application
 */
public class GeneralView  extends Application {
    /**
     * Scene enum
     */
    public enum sceneEnum {GAME, MENU};
    private MenuView menuView;
    private GameView gameView;
    private sceneEnum currentSceneName;
    private Stage mainStage;

    private Logger LOGGER;

    private final int sceneWidth = 896;
    private final int sceneHeight = 608;

    public GeneralView() {
        LOGGER = Logger.getLogger(GeneralView.class.getName());

        menuView = new MenuView(sceneWidth, sceneHeight);
        gameView = new GameView(sceneWidth, sceneHeight);

        currentSceneName = sceneEnum.MENU;
    }


    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        mainStage.setResizable(false);
        mainStage.setTitle("Legend");
        mainStage.setOnCloseRequest(e -> {
            e.consume();
            closeApp();
        });

        mainStage.setScene(menuView);
        mainStage.show();
    }

    /**
     * Updates inventory display
     */
    public void updadeInventory() {
        gameView.inventoryUpdate();
    }

    /**
     * Updates selected item (item in hand) display
     */
    public void highlightItemInHand(int index) {
        gameView.highlightItemInHand(index);
    }

    /**
     * Changing scene depending on the scene name
     * @param sceneName
     */
    public void changeScene(sceneEnum sceneName) {
        switch (sceneName) {
            case GAME:
                gameView.gameViewInit();
                mainStage.setScene(gameView);
                gameView.startHandle();
                Main.startControllers();


                LOGGER.log(Level.INFO, "SCENE CHANGES TO GAME");

                break;
            case MENU:
                mainStage.setScene(menuView);
                gameView.stopHandle();

                Main.stopControllers();

                LOGGER.log(Level.INFO, "SCENE CHANGES TO MENU");

                break;

        }
    }

    /**
     * Closing application processing
     */
    public void closeApp() {
        boolean areYouSure = ConfirmWindow.display("Closing confirmation", "Are you sure?", "Yes", "No");
        if (areYouSure) {
            Main.characterController.saveInventory();
            try {
                Main.stopControllers();
                mainStage.close();

                LOGGER.log(Level.INFO, "APPLICATION CLOSED");
                stop();
            }
            catch (Exception e) {
                LOGGER.log(Level.WARNING, "APLICATION CLOSING ERROR");
            }
        }
    }
}