package cz.cvut.fel.pjv.Model;

/**
 * Represents pairs of float values
 * Implementation of point position ands math methods with vectors
 */
public class Position {
    private double posX;
    private double posY;

    /**
     * Constructs position object depends on params
     * @param posX
     * @param posY
     */
    public Position(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * Creates Position vector representation from current point to another point endPoint
     * @param endPoint
     * @return vector representation
     */
    public Position createVector(Position endPoint) {
        return new Position(endPoint.posX - posX, endPoint.posY - posY);
    }

    /**
     * Creates unite Position vector representation from current point to another point endPoint
     * @param endPoint
     * @return unite vector representation
     */
    public Position createUniteVector(Position endPoint) {
        double range = getRangeTo(endPoint);
        if(range < 1) {
            return new Position(0,0);
        }
        else
        {
            return new Position((endPoint.posX - posX)/range, (endPoint.posY - posY)/range);
        }
    }

    /**
     * Calculates a projection of current Position vector representation on vector vector
     * @param vector
     * @return projection double representation
     */
    public double getProjectionOn(Position vector) {
        double projection = getVectorScalarProd(vector) / vector.getVectorMod();

        return projection;
    }

    /**
     * Calculates range from current point to another point endPoint
     * @param endPoint
     * @return double range representation
     */
    public double getRangeTo(Position endPoint) {
        double lx = posX - endPoint.posX;
        double ly = posY - endPoint.posY;
        double range = Math.sqrt(lx*lx + ly*ly);

        return range;
    }

    /**
     * Multiplies current Position vector representation by factor
     * @param factor
     */
    public void multiplyVector(double factor) {
        this.posX *= factor;
        this.posY *= factor;
    }

    /**
     * Divides current Position vector representation by divider divider
     * @param divider
     */
    public void divideVector(double divider) {
        if(divider != 0) {
            this.posX /= divider;
            this.posY /= divider;
        }
    }

    /**
     * Applies vector vector to current point
     * @param vector
     */
    public void applyVector(Position vector) {
        this.posX += vector.posX;
        this.posY += vector.posY;
    }

    /**
     * Calculates angle between curent Position vector representation and another vector vector
     * @param vector
     * @return angle in degrees
     */
    public double getVectorsAngle(Position vector) {
        if(getVectorMod() == 0 || vector.getVectorMod() == 0) {
            return 0;
        }
        else {
            double angle = getVectorScalarProd(vector) / (getVectorMod()*vector.getVectorMod());
            angle = Math.acos(angle);

            return Math.toDegrees(angle);
        }
    }

    /**
     * Calculates scalar products of current Position vector representation and another vector vector
     * @param vector
     * @return double scalar product representation
     */
    public double getVectorScalarProd(Position vector) {
        double skalarProduct = posX*vector.posX + posY*vector.posY;

        return skalarProduct;
    }

    /**
     * Calculates |current Position vector representation|
     * @return double vector length representation
     */
    public double getVectorMod() {
        double vectorMod = Math.sqrt(posX*posX + posY*posY);

        return vectorMod;
    }

    /**
     * Constructs another point same as current point
     * @return copy of current point
     */
    public Position copy() {
        return new Position(posX, posY);
    }

    @Override
    public String toString() {
        return "Position{" +
                "posX=" + posX +
                ", posY=" + posY +
                '}';
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

}
