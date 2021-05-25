package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.View.GeneralView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application main class
 * Static prefixes grant acces from any program point
 */
public class Main extends Application{
    public static CharacterController characterController;
    public static LevelController levelController;
    public static GeneralView generalView;
    private static Logger LOGGER;

    /**
     * Main start point
     * @param args
     */
    public static void main(String[] args) {
        LOGGER = Logger.getLogger(Main.class.getName());
        characterController = new CharacterController(new CharacterAnimation());
        levelController = new LevelController();
        generalView = new GeneralView();

        launch();
    }

    /**
     * JavaFX required method, GUI start point
     * @param stage JavaFX default
     * @throws Exception JavaFX default
     */
    @Override
    public void start(Stage stage) throws Exception {
        generalView.start(stage);
        LOGGER.log(Level.INFO, "GENERAL VIEW STARTS");
    }

    /**
     * Load level depends on level number
     * @param levelNumber
     */
    public static void loadLevel(int levelNumber) {
        levelController.setLevel(LevelFileHandler.levelInit(levelNumber));
        levelController.groundTiles = LevelFileHandler.groundTilesInit(levelNumber);
    }

    /**
     * Launch inner controller's threads
     */
    public static void startControllers() {
        levelController.enemyControllersStart();
        LOGGER.log(Level.INFO, "LEVEL CONTROLLER STARTS");

        characterController.start();
        LOGGER.log(Level.INFO, "CHARACTER CONTROLLER STARTS");
    }

    /**
     * Kills inner controller's threads
     */
    public static void stopControllers() {
        levelController.enemyControllersStop();
        LOGGER.log(Level.INFO, "LEVEL CONTROLLER STOPS");

        characterController.stopRequest();
        characterController.interrupt();
        LOGGER.log(Level.INFO, "CHARACTER CONTROLLER STOPS");
    }

    /**
     * Was added by an issue with closing application, clearly closing program
     */
    @Override
    public void stop(){
        System.exit(0);
    }

}
