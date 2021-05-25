package cz.cvut.fel.pjv.Model;

/**
 * Represents weapons character can attack with
 */
public class Weapon extends Item{
    private final double attackValue;
    private final double attackRange;
    private final int attackAngle;
    private final double knockBack;
    private final boolean isVamp; // "vampire" weapon mechanics is TO DO

    public Weapon(int id, Sprite sprite, String itemType, double attackValue, double attackRange, int attackAngle, double knockBack, boolean isVamp) {
        super(id, sprite, itemType);
        this.attackValue = attackValue;
        this.attackRange = attackRange;
        this.attackAngle = attackAngle;
        this.knockBack = knockBack;
        this.isVamp = isVamp;
    }

    public double getAttackValue() {
        return attackValue;
    }

    public double getAttackRange() {
        return attackRange;
    }

    public int getAttackAngle() {
        return attackAngle;
    }

    public double getKnockBack() {
        return knockBack;
    }

    public boolean isVamp() {
        return isVamp;
    }
}
