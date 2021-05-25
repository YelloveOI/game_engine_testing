package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.Model.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileReader;
import java.util.*;

/**
 * Reads datas from file and creates level depends on it
 * Here are defined sprite lists of each type of object on the map
 * Reads object map from the file, constructs and locates objects
 * Reads ground tiles map from the file, constructs whole ground view
 */
public class LevelFileHandler {

    private static final int COLUMN = 29;
    private static final int ROW = 19;
    private static final int TILE_WIDTH = 32;

    private static Sprite healSprite = new Sprite(
            new Image("file:assets/healsprite.png"),
            24, 24,
            new ArrayList<>()
    );

    private static Sprite greatSwordSprite = new Sprite(
            new Image("file:assets/greatswordsprite.png"),
            24, 24,
            new ArrayList<>()
    );

    private static Sprite wallSprite = new Sprite(
            new Image("file:assets/wallsprite.png"),
            32, 32,
            new ArrayList<>(Arrays.asList(4))
    );

    private static Sprite objectSprite = new Sprite(
            new Image("file:assets/objectsprite.png"),
            32, 32,
            new ArrayList<>(Arrays.asList(11))
    );

    private static Sprite skeletonSprite = new Sprite(
            new Image("file:assets/enemysprite.png"),
            48, 48,
            new ArrayList<>(Arrays.asList(
                    6, 8, 8, 4, 6,
                    6, 8, 8, 4, 6,
                    6, 8, 8, 4, 6,
                    6, 8, 8, 4, 6
            ))
    );

    /**
     * Level constructor depending on the read file
     * @param number of level to create
     * @return level object
     */
    public static Level levelInit(int number) {
        List<Enemy> enemyList = new ArrayList<>();
        List<SolidObject> solidObjectList = new ArrayList<>();
        List<InteractiveLevelObject> interactiveLevelObjectList = new ArrayList<>();
        String objectMap = "";

        /**
         * Map reading depending on the level number and recording to string
         */
        switch (number) {
            case 1:
                try {
                    Scanner scanner = new Scanner(new FileReader("levels/level1object.map"));
                    StringBuilder sb = new StringBuilder();

                    while (scanner.hasNext()) {
                        sb.append(scanner.next());
                    }
                    scanner.close();

                    objectMap = sb.toString();
                }
                catch (Exception e){
                    System.out.println("reading error");
                }

                break;

            case 2: {
                try {
                    Scanner scanner = new Scanner(new FileReader("levels/level2object.map"));
                    StringBuilder sb = new StringBuilder();

                    while (scanner.hasNext()) {
                        sb.append(scanner.next());
                    }
                    scanner.close();

                    objectMap = sb.toString();
                }
                catch (Exception e){
                    System.out.println("reading error");
                }

                break;
            }


            default: return null;
        }

        /**
         * Recorded string processing
         * Run loop to read each symbol from string
         * Constructs level objects depends on string symbol and symbol serial number
         */
        for (int i = 0; i < objectMap.length(); i++) {
            int column = i % COLUMN;
            int row = i / COLUMN;
            Enemy enemy;
            SolidObject solidObject;
            InteractiveLevelObject interactiveLevelObject;

            switch (objectMap.charAt(i)) {
                case 'S':
                    enemy = new Enemy(
                            skeletonSprite, "SKELETON",
                            20, 50, 100, 18, 15
                    );
                    enemy.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    enemyList.add(enemy);
                    break;

                case 'H':
                    interactiveLevelObject = new InteractiveLevelObject(
                            healSprite, "HEAL", 1, new Item(1, healSprite, "HEAL")
                    );
                    interactiveLevelObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    interactiveLevelObjectList.add(interactiveLevelObject);
                    break;

                case 'E':
                    interactiveLevelObject = new InteractiveLevelObject(
                            greatSwordSprite, "GREAT SWORD", 2, new Weapon(2, greatSwordSprite, "GREATSWORD", 60, 30, 120, 30, false)
                    );
                    interactiveLevelObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    interactiveLevelObjectList.add(interactiveLevelObject);
                    break;

                case '-':
                    solidObject = new SolidObject(wallSprite, "WALL", 32,32);
                    solidObject.setTileNumb(1);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case '|':
                    solidObject = new SolidObject(wallSprite,"WALL", 32,32);
                    solidObject.setTileNumb(8);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case '@':
                    solidObject = new SolidObject(wallSprite,"WALL", 32,32);
                    solidObject.setTileNumb(4);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case '#':
                    solidObject = new SolidObject(wallSprite, "WALL", 32,32);
                    solidObject.setTileNumb(6);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case '%':
                    solidObject = new SolidObject(wallSprite, "WALL",32,32);
                    solidObject.setTileNumb(12);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case '$':
                    solidObject = new SolidObject(wallSprite, "WALL", 32,32);
                    solidObject.setTileNumb(14);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case '}':
                    solidObject = new SolidObject(wallSprite, "WALL", 32,32);
                    solidObject.setTileNumb(16);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case 'i':
                    solidObject = new SolidObject(wallSprite, "WALL", 32,32);
                    solidObject.setTileNumb(17);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case '!':
                    solidObject = new SolidObject(wallSprite, "WALL", 32,32);
                    solidObject.setTileNumb(18);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case '{':
                    solidObject = new SolidObject(wallSprite, "WALL", 32,32);
                    solidObject.setTileNumb(19);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case '^':
                    solidObject = new SolidObject(wallSprite, "WALL", 32,32);
                    solidObject.setTileNumb(7);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case 'v':
                    solidObject = new SolidObject(wallSprite, "WALL", 32,32);
                    solidObject.setTileNumb(15);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case '>':
                    solidObject = new SolidObject(wallSprite, "WALL", 32,32);
                    solidObject.setTileNumb(2);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case '<':
                    solidObject = new SolidObject(wallSprite, "WALL", 32,32);
                    solidObject.setTileNumb(0);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case 'a':
                    solidObject = new SolidObject(objectSprite, "TREE", 32,32);
                    solidObject.setTileNumb(20);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case 'b':
                    solidObject = new SolidObject(objectSprite, "TREE", 32,32);
                    solidObject.setTileNumb(21);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case 'c':
                    solidObject = new SolidObject(objectSprite, "TREE", 32,32);
                    solidObject.setTileNumb(31);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case 'd':
                    solidObject = new SolidObject(objectSprite, "TREE", 32,32);
                    solidObject.setTileNumb(32);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case 'o':
                    solidObject = new SolidObject(objectSprite, "STONE", 32,32);
                    solidObject.setTileNumb(6);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case '/':
                    solidObject = new SolidObject(objectSprite, "BUSH", 32,32);
                    solidObject.setTileNumb(29);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case 'q':
                    solidObject = new SolidObject(objectSprite, "BIG STONE", 32,32);
                    solidObject.setTileNumb(9);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case 'Q':
                    solidObject = new SolidObject(objectSprite, "BIG STONE", 32,32);
                    solidObject.setTileNumb(10);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case 'G':
                    solidObject = new SolidObject(objectSprite, "GRAVE", 32,32);
                    solidObject.setTileNumb(0);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case '1':
                    solidObject = new SolidObject(objectSprite, "HOUSE", 32,32);
                    solidObject.setTileNumb(4);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case '2':
                    solidObject = new SolidObject(objectSprite, "HOUSE", 32,32);
                    solidObject.setTileNumb(5);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case '3':
                    solidObject = new SolidObject(objectSprite, "HOUSE", 32,32);
                    solidObject.setTileNumb(15);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;

                case '4':
                    solidObject = new SolidObject(objectSprite, "HOUSE", 32,32);
                    solidObject.setTileNumb(16);
                    solidObject.setPosition(new Position(column * TILE_WIDTH, row * TILE_WIDTH));
                    solidObjectList.add(solidObject);
                    break;
            }
        }

        return new Level(enemyList, solidObjectList, interactiveLevelObjectList);
    }

    /**
     * Creates an array of image view and locates it depends on read symbyl an it's serial number
     * @param number of level to create
     * @return array of image views
     */
    public static List<ImageView> groundTilesInit(int number) {
        List<ImageView> imageViewList = new ArrayList<>();
        Map<String, ImageView> imageViewTypeMap = new HashMap<>();
        Image tileSprite = new Image("file:assets/tilesprite.png");
        String groundMap = "";

        /**
         * Map reading depending on the level number and recording to string
         */
        switch (number) {
            case 1:
                try {
                    Scanner scanner = new Scanner(new FileReader("levels/level1ground.map"));
                    StringBuilder sb = new StringBuilder();

                    while (scanner.hasNext()) {
                        sb.append(scanner.next());
                    }
                    scanner.close();

                    groundMap = sb.toString();
                }
                catch (Exception e){
                    System.out.println("reading error");
                }

                break;

            case 2:
                try {
                    Scanner scanner = new Scanner(new FileReader("levels/level2ground.map"));
                    StringBuilder sb = new StringBuilder();

                    while (scanner.hasNext()) {
                        sb.append(scanner.next());
                    }
                    scanner.close();

                    groundMap = sb.toString();
                }
                catch (Exception e){
                    System.out.println("reading error");
                }

                break;
        }

        /**
         * Recorded string processing
         * Run loop to read each symbol from string
         * Constructs image view of each tile on the map depends on string symbol and symbol serial number
         */
        for (int i = 0 ; i < groundMap.length(); i++) {
            int column = i % 28;
            int row = i / 28;
            ImageView tileView;

            switch (groundMap.charAt(i)) {
                case '*':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH * 2, TILE_WIDTH*4, TILE_WIDTH, TILE_WIDTH));
                    break;

                case '+':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH, TILE_WIDTH, TILE_WIDTH, TILE_WIDTH));
                    break;

                case '-':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH, TILE_WIDTH * 3, TILE_WIDTH, TILE_WIDTH));
                    break;

                case '|':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH * 3, TILE_WIDTH, TILE_WIDTH, TILE_WIDTH));
                    break;

                case '<':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(0, TILE_WIDTH * 3, TILE_WIDTH, TILE_WIDTH));
                    break;

                case '>':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH * 2, TILE_WIDTH * 3, TILE_WIDTH, TILE_WIDTH));
                    break;

                case 'T':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(0, TILE_WIDTH * 4, TILE_WIDTH, TILE_WIDTH));
                    break;

                case 'v':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH * 3, TILE_WIDTH * 2, TILE_WIDTH, TILE_WIDTH));
                    break;

                case 'i':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH * 3, TILE_WIDTH * 6, TILE_WIDTH, TILE_WIDTH));
                    break;

                case 'F':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH * 2, TILE_WIDTH * 6, TILE_WIDTH, TILE_WIDTH));
                    break;

                case 'J':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH * 3, TILE_WIDTH * 7, TILE_WIDTH, TILE_WIDTH));
                    break;

                case 'X':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH * 3, TILE_WIDTH * 4, TILE_WIDTH, TILE_WIDTH));
                    break;

                case '^':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH, 0, TILE_WIDTH, TILE_WIDTH));
                    break;

                case '_':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH, TILE_WIDTH * 2, TILE_WIDTH, TILE_WIDTH));
                    break;

                case ')':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH * 2, TILE_WIDTH, TILE_WIDTH, TILE_WIDTH));
                    break;

                case '(':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(0, TILE_WIDTH, TILE_WIDTH, TILE_WIDTH));
                    break;

                case '#':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH * 2, 0, TILE_WIDTH, TILE_WIDTH));
                    break;

                case '%':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(0, TILE_WIDTH * 2, TILE_WIDTH, TILE_WIDTH));
                    break;

                case '@':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(0, 0, TILE_WIDTH, TILE_WIDTH));
                    break;

                case '$':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH * 2, TILE_WIDTH * 2, TILE_WIDTH, TILE_WIDTH));
                    break;

                case ';':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH * 2, TILE_WIDTH * 5, TILE_WIDTH, TILE_WIDTH));
                    break;

                case ']':
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH, TILE_WIDTH * 6, TILE_WIDTH, TILE_WIDTH));
                    break;

                default:
                    tileView = new ImageView(tileSprite);
                    tileView.setViewport(new Rectangle2D(TILE_WIDTH * 2, TILE_WIDTH * 4, TILE_WIDTH, TILE_WIDTH));
                    break;
            }

            tileView.setX(column * TILE_WIDTH);
            tileView.setY(row * TILE_WIDTH);

            imageViewList.add(tileView);
        }

        return  imageViewList;
    }

}
