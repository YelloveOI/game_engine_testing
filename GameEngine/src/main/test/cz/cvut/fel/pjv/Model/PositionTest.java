package cz.cvut.fel.pjv.Model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

/**
 * Parametrized test of Position class
 */
public class PositionTest {
    private static Position position;

    @ParameterizedTest
    @MethodSource("createVectorDataProvider")
    public void createVector(Position expectedVector, Position p1, Position p2) {
        double expectedX = expectedVector.getPosX();
        double expectedY = expectedVector.getPosY();
        double X;
        double Y;

        Position resultVector = p1.createVector(p2);
        X = resultVector.getPosX();
        Y = resultVector.getPosY();

        assertEquals(expectedX, X);
        assertEquals(expectedY, Y);
    }

    static Stream<Arguments> createVectorDataProvider() {
        return Stream.of(
                Arguments.arguments(new Position(0, 0), new Position(0, 0), new Position(0, 0)),
                Arguments.arguments(new Position(12, 13), new Position(-12, -13), new Position(0, 0)),
                Arguments.arguments(new Position(87.6, 13.2), new Position(0, -13.2), new Position(87.6, 0)),
                Arguments.arguments(new Position(21, 1.2), new Position(-21, 0), new Position(0, 1.2)),
                Arguments.arguments(new Position(-321, 0), new Position(321, 0), new Position(0, 0)),
                Arguments.arguments(new Position(282.6, 29.2313), new Position(-278, 0.9), new Position(4.6, 30.1313))
        );
    }

    @ParameterizedTest
    @MethodSource("createUniteVectorDataProvider")
    public void createUniteVector(Position expectedVector, Position p1, Position p2) {
        double expectedX = expectedVector.getPosX();
        double expectedY = expectedVector.getPosY();
        double X;
        double Y;

        Position resultVector = p1.createUniteVector(p2);
        X = resultVector.getPosX();
        Y = resultVector.getPosY();

        assertEquals(expectedX, X);
        assertEquals(expectedY, Y);
    }

    static Stream<Arguments> createUniteVectorDataProvider() {
        return Stream.of(
                Arguments.arguments(new Position(0, 0), new Position(0, 0), new Position(0, 0)),
                Arguments.arguments(new Position(0.6782801027330658, 0.7348034446274879), new Position(-12, -13), new Position(0, 0)),
                Arguments.arguments(new Position(0.9888367737587507, 0.14900280152529122), new Position(0, -13.2), new Position(87.6, 0)),
                Arguments.arguments(new Position(0.9983713344239766, 0.057049790538512946), new Position(-21, 0), new Position(0, 1.2)),
                Arguments.arguments(new Position(-1, 0), new Position(321, 0), new Position(0, 0)),
                Arguments.arguments(new Position(0.9946929403884799, 0.10288806705016904), new Position(-278, 0.9), new Position(4.6, 30.1313))
        );
    }

    @ParameterizedTest
    @MethodSource("multipleVectorDataProvider")
    public void multiplyVector(Position expectedVector, Position v1, double factor) {
        double expectedX = expectedVector.getPosX();
        double expectedY = expectedVector.getPosY();
        double X;
        double Y;

        v1.multiplyVector(factor);
        X = v1.getPosX();
        Y = v1.getPosY();

        assertEquals(expectedX, X);
        assertEquals(expectedY, Y);
    }

    static Stream<Arguments> multipleVectorDataProvider() {
        return Stream.of(
                Arguments.arguments(new Position(0, 0), new Position(0, 0), 0),
                Arguments.arguments(new Position(5, 0), new Position(2.5, 0), 2),
                Arguments.arguments(new Position(120, 50), new Position(2.4, 1), 50),
                Arguments.arguments(new Position(0, -.0), new Position(55, -7), 0),
                Arguments.arguments(new Position(-50.264, 12.566), new Position(-16, 4), 3.1415),
                Arguments.arguments(new Position(-2, 5), new Position(2, -5), -1)
        );
    }

    @ParameterizedTest
    @MethodSource("getVectorsAngleDataProvider")
    public void getVectorsAngle(double expectedAngle, Position v1, Position v2) {
        double angle;

        angle = v1.getVectorsAngle(v2);

        assertEquals(expectedAngle, angle);
    }

    static Stream<Arguments> getVectorsAngleDataProvider() {
        return Stream.of(
                Arguments.arguments(0, new Position(0, 0), new Position(0, 0)),
                Arguments.arguments(136.56770367713028, new Position(58, 23), new Position(-5, 2)),
                Arguments.arguments(22.61986494804042, new Position(2.4, 1), new Position(50, 0)),
                Arguments.arguments(2.573359463176981, new Position(0, 55), new Position(-4, 89)),
                Arguments.arguments(87.31575127447422, new Position(5.116, 0.2), new Position(3.1415, 404)),
                Arguments.arguments(104.17233770013196, new Position(5, 100), new Position(-5, -1))
        );
    }

    @ParameterizedTest
    @MethodSource("getVectorScalarProdDataProvider")
    public void getVectorScalarProd(double expectedScalarProd, Position v1, Position v2) {
        double scalarProd;

        scalarProd = v1.getVectorScalarProd(v2);

        assertEquals(expectedScalarProd, scalarProd);
    }

    static Stream<Arguments> getVectorScalarProdDataProvider() {
        return Stream.of(
                Arguments.arguments(0, new Position(0, 0), new Position(0, 0)),
                Arguments.arguments(36.599639999999994, new Position(2.4, 1.56666), new Position(-20, 54)),
                Arguments.arguments(801, new Position(0, 89), new Position(-124, 9)),
                Arguments.arguments(7.839986391399999, new Position(5.116, 0.2), new Position(1.14151415, 10)),
                Arguments.arguments(-125, new Position(5, 100), new Position(-5, -1))
        );
    }

    @ParameterizedTest
    @MethodSource("copyDataProvider")
    public void copy(Position expectedPoint, Position p1) {
        Position result;
        double expectedX = expectedPoint.getPosX();
        double expectedY = expectedPoint.getPosY();
        double X;
        double Y;

        result = p1.copy();
        X = result.getPosX();
        Y = result.getPosY();

        assertEquals(expectedX, X);
        assertEquals(expectedY, Y);
    }

    static Stream<Arguments> copyDataProvider() {
        return Stream.of(
                Arguments.arguments(new Position(0, 0), new Position(0, 0)),
                Arguments.arguments(new Position(-2, 0), new Position(-2, 0)),
                Arguments.arguments(new Position(13.4, -2), new Position(13.4, -2)),
                Arguments.arguments(new Position(0.009, 1.1212), new Position(0.009, 1.1212)),
                Arguments.arguments(new Position(84, -120.1), new Position(84, -120.1)),
                Arguments.arguments(new Position(11, 1.111), new Position(11, 1.111))
        );
    }

    @ParameterizedTest
    @MethodSource("getVectorModDataProvider")
    public void getVectorMod(double expectedMod, Position v1) {
        double mod;

        mod = v1.getVectorMod();

        assertEquals(expectedMod, mod);
    }

    static Stream<Arguments> getVectorModDataProvider() {
        return Stream.of(
                Arguments.arguments(0, new Position(0, 0)),
                Arguments.arguments(4.790884815522077, new Position(3.234, 3.53466)),
                Arguments.arguments(323, new Position(0, 323)),
                Arguments.arguments(51.123, new Position(51.123, 0)),
                Arguments.arguments(106.0143386528445, new Position(-35.2, 100))
        );
    }

    @ParameterizedTest
    @MethodSource("applyVectorDataProvider")
    public void applyVector(Position expectedPoint, Position p1, Position v1) {
        double expectedX = expectedPoint.getPosX();
        double expectedY = expectedPoint.getPosY();
        double X;
        double Y;

        p1.applyVector(v1);
        X = p1.getPosX();
        Y = p1.getPosY();

        assertEquals(expectedX, X);
        assertEquals(expectedY, Y);
    }

    static Stream<Arguments> applyVectorDataProvider() {
        return Stream.of(
                Arguments.arguments(new Position(0, 0), new Position(0, 0), new Position(0, 0)),
                Arguments.arguments(new Position(-22, 0), new Position(-22, 0), new Position(0, 0)),
                Arguments.arguments(new Position(13.4, -2), new Position(0, 0), new Position(13.4, -2)),
                Arguments.arguments(new Position(117.2, 240.212), new Position(76.2, 12.212), new Position(41, 228)),
                Arguments.arguments(new Position(59.1415, 411.8585), new Position(3.1415, -3.1415), new Position(56, 415)),
                Arguments.arguments(new Position(-3.7799999999999994, -516.22), new Position(11.22, -511.22), new Position(-15, -5))
        );
    }

    @ParameterizedTest
    @MethodSource("divideVectorDataProvider")
    public void divideVector(Position expectedVector, Position v1, double factor) {
        double expectedX = expectedVector.getPosX();
        double expectedY = expectedVector.getPosY();
        double X;
        double Y;

        v1.divideVector(factor);
        X = v1.getPosX();
        Y = v1.getPosY();

        assertEquals(expectedX, X);
        assertEquals(expectedY, Y);
    }

    static Stream<Arguments> divideVectorDataProvider() {
        return Stream.of(
                Arguments.arguments(new Position(0, 0), new Position(0, 0), 0),
                Arguments.arguments(new Position(1.25, 0), new Position(2.5, 0), 2),
                Arguments.arguments(new Position(0.048, 0.02), new Position(2.4, 1), 50),
                Arguments.arguments(new Position(55.0, -7.0), new Position(55, -7), 0),
                Arguments.arguments(new Position(-5.093108387712876, 1.273277096928219), new Position(-16, 4), 3.1415),
                Arguments.arguments(new Position(-2, 5), new Position(2, -5), -1)
        );
    }
}
