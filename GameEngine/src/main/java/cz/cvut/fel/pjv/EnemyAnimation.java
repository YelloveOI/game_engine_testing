package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.Model.Sprite;
import javafx.animation.Animation;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Implementation of sprite animation for enemy
 * Provides functions for enemy's animation managing
 */
public class EnemyAnimation {
    private final Sprite enemySprite;
    private final ImageView enemyView;
    private final SpriteAnimation enemySpriteAnimation;

    public EnemyAnimation(Sprite sprite) {
        this.enemySprite = sprite;
        this.enemyView = new ImageView(sprite.sprite);
        this.enemySpriteAnimation = new SpriteAnimation(
                enemyView, Duration.millis(700), 0,
                enemySprite.width, enemySprite.height, enemySprite.columnList
        );
        enemySpriteAnimation.setCycleCount(Animation.INDEFINITE);
    }

    public void lastIteration() {
        enemySpriteAnimation.isLastIteration = true;
    }

    public ImageView getEnemyView() {
        return enemyView;
    }

    public void start() {
        enemySpriteAnimation.play();
    }

    public void stop() {
        enemySpriteAnimation.stop();
    }

    // sets offsetY depends on frame width and row
    public void setRow(int row) {
        enemySpriteAnimation.setOffsetY(row*48);
    }

}
