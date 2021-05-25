package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.Model.Sprite;
import cz.cvut.fel.pjv.SpriteAnimation;
import javafx.animation.Animation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Implementation of sprite animation for character
 * Provides functions for character's animation managing
 */
public class CharacterAnimation {
    private final Sprite characterSprite; // character sprite list
    private final ImageView characterView; // view of the frame on sprite lis
    private final SpriteAnimation characterSpriteAnimation; // "view changing" processor

    public CharacterAnimation() {
        this.characterSprite = new Sprite(
                new Image("file:assets/charactersprite.png"),
                48, 48,
                new ArrayList<Integer>(Arrays.asList(
                        8, 6, 5, 4, 5,
                        8, 6, 5, 4, 5,
                        8, 6, 5, 4, 5,
                        8, 6, 5, 4, 5
                )
        ));

        this.characterView = new ImageView(characterSprite.sprite);

        this.characterSpriteAnimation = new SpriteAnimation(
                characterView, Duration.millis(700), 672,
                characterSprite.width, characterSprite.height, characterSprite.columnList
        );
        characterSpriteAnimation.setCycleCount(Animation.INDEFINITE);
    }

    public void lastIteration() {
        characterSpriteAnimation.isLastIteration = true;
    }

    public ImageView getCharacterView() {
        return characterView;
    }

    public void start() {
        characterSpriteAnimation.play();
    }

    public void stop() {
        characterSpriteAnimation.stop();
    }

    // sets offsetY depends on frame width and row
    public void setRow(int row) {
        characterSpriteAnimation.setOffsetY(row*48);
    }
}
