package cz.cvut.fel.pjv.View;

import cz.cvut.fel.pjv.InventoryFileHandler;
import cz.cvut.fel.pjv.Main;
import cz.cvut.fel.pjv.LevelFileHandler;
import cz.cvut.fel.pjv.Model.Character;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Implements main game menu view
 */
public class MenuView extends Scene {
    private final VBox menuLayout;
    private final VBox chooseLayout;

    public MenuView(int width, int height) {
        super(new Pane(), width, height);

        menuLayout = new VBox(20);
        menuLayout.setAlignment(Pos.CENTER);

        chooseLayout = new VBox(20);
        chooseLayout.setAlignment(Pos.CENTER);

        this.setRoot(menuLayout);

        Button levelBtn = new Button("Level");
        levelBtn.getStyleClass().add("custom-button");
        levelBtn.setPrefWidth(150);
        levelBtn.setPrefHeight(30);

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("custom-button");
        backBtn.setPrefWidth(150);
        backBtn.setPrefHeight(30);

        Button anotherLevelBtn = new Button("Another level");
        anotherLevelBtn.getStyleClass().add("custom-button");
        anotherLevelBtn.setPrefWidth(150);
        anotherLevelBtn.setPrefHeight(30);

        Button chooseBtn = new Button("Choose level");
        chooseBtn.getStyleClass().add("custom-button");
        chooseBtn.setPrefWidth(150);
        chooseBtn.setPrefHeight(30);

        Button exitBtn = new Button("Exit");
        exitBtn.getStyleClass().add("custom-button");
        exitBtn.setPrefWidth(150);
        exitBtn.setPrefHeight(30);

        Button cont = new Button("Continue");
        cont.getStyleClass().add("custom-button");
        cont.setMaxWidth(150);
        cont.setMaxHeight(30);

        // adding handlers
        levelBtn.setOnAction(e -> {
            Main.loadLevel(1);
            Main.characterController.loadCharacter(InventoryFileHandler.inventoryInit(true));
            Main.generalView.changeScene(GeneralView.sceneEnum.GAME);
        });

        anotherLevelBtn.setOnAction(e -> {
            Main.loadLevel(2);
            Main.characterController.loadCharacter(InventoryFileHandler.inventoryInit(true));
            Main.generalView.changeScene(GeneralView.sceneEnum.GAME);
        });

        cont.setOnAction(e -> {
            Main.loadLevel(1);
            Main.characterController.loadCharacter(InventoryFileHandler.inventoryInit(false));
            Main.generalView.changeScene(GeneralView.sceneEnum.GAME);
        });

        backBtn.setOnAction(e -> {
            this.setRoot(menuLayout);
        });

        chooseBtn.setOnAction(e -> {
            this.setRoot(chooseLayout);
        });

        exitBtn.setOnAction(e -> {
            Main.generalView.closeApp();
        });

        chooseLayout.getChildren().addAll(levelBtn, anotherLevelBtn, cont, backBtn);
        chooseLayout.getStyleClass().add("custom-background");
        menuLayout.getChildren().addAll(chooseBtn, exitBtn);
        menuLayout.getStyleClass().add("custom-background");

        getStylesheets().add("file:menustyle.css");
    }
}
