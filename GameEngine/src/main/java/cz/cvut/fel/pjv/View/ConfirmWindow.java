package cz.cvut.fel.pjv.View;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Simple confirmation windows
 */
public class ConfirmWindow {

    static boolean answer = false;

    public static boolean display(String title, String message, String btn1, String btn2) {
        Stage alertStage = new Stage();
        alertStage.initModality(Modality.APPLICATION_MODAL);
        alertStage.setTitle(title);
        alertStage.setWidth(200);
        alertStage.setHeight(150);
        alertStage.setResizable(false);

        Label messageLable = new Label(message);

        Button yesButton = new Button(btn1);
        yesButton.setOnAction(e -> {
            answer = true;
            alertStage.close();
        });

        Button noButton = new Button(btn2);
        noButton.setOnAction(e -> {
            answer = false;
            alertStage.close();
        });

        HBox buttonLayout = new HBox(yesButton, noButton);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setSpacing(20);
        VBox layout = new VBox(messageLable, buttonLayout);
        layout.setSpacing(10);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        alertStage.setScene(scene);
        alertStage.showAndWait();

        return answer;
    }
}
