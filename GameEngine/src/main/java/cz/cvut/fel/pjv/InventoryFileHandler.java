package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.Model.Inventory;
import cz.cvut.fel.pjv.Model.Item;
import cz.cvut.fel.pjv.Model.Sprite;
import cz.cvut.fel.pjv.Model.Weapon;
import javafx.scene.image.Image;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads and writes item IDs from/to file
 * Constructs inventory from readed file
 */
public class InventoryFileHandler {
    private static Logger LOGGER = Logger.getLogger(InventoryFileHandler.class.getName());

    /**
     * Creates inventory from file depends on required inventory type
     * @param isStart is start inventory required? In the opposite loads saved inventory
     * @return
     */
    public static Inventory inventoryInit(boolean isStart) {
        List<Item> itemList = new ArrayList<>();
        List<Integer> itemIDList = new ArrayList<>();
        Inventory inventory;

        Sprite healSprite = new Sprite(
                new Image("file:assets/healsprite.png"),
                24, 24,
                new ArrayList<>()
        );

        Sprite swordSprite = new Sprite(
                new Image("file:assets/swordsprite.png"),
                24, 24,
                new ArrayList<>()
        );

        Sprite greatSwordSprite = new Sprite(
                new Image("file:assets/greatswordsprite.png"),
                24, 24,
                new ArrayList<>()
        );

        if(isStart) {
            try {
                Scanner scanner = new Scanner(new FileReader("inventories/start.inv"));

                while (scanner.hasNextInt()) {
                    itemIDList.add(scanner.nextInt());
                }

                scanner.close();
            }
            catch (Exception e){
                System.out.println("reading error");
            }
        }
        else {
            try {
                Scanner scanner = new Scanner(new FileReader("inventories/save.inv"));

                while (scanner.hasNextInt()) {
                    itemIDList.add(scanner.nextInt());
                }

                scanner.close();
            }
            catch (Exception e){
                System.out.println("reading error");
            }
        }

        for (int i: itemIDList) {
            switch (i) {
                case 0:
                    itemList.add(new Weapon(0, swordSprite, "SWORD", 40, 35, 90, 30, false));
                    break;

                case 1:
                    itemList.add(new Item(1, healSprite, "HEAL"));
                    break;

                case 2:
                    itemList.add((new Weapon(2, greatSwordSprite, "GREAT SWORD",60, 30, 120, 30, false)));
                    break;
            }
        }

        inventory = new Inventory(itemList);

        return inventory;
    }

    /**
     * Writes character inventory to file
     * @param inventoryToSave
     */
    public static void saveInventroy(Inventory inventoryToSave) {
        List<Integer> itemIDList = new ArrayList<>();
        String out = "";

        LOGGER.log(Level.INFO, "SAVING INVENTORY...");

        try {
            FileWriter fileWriter = new FileWriter("inventories/save.inv");

            for(Item item: inventoryToSave.getGameItemList()) {
                out += item.getId() + " ";
            }

            fileWriter.write(out);
            fileWriter.close();

            LOGGER.log(Level.INFO, "INVENTORY SAVED!");
        }
        catch (Exception e) {
            LOGGER.log(Level.WARNING, "INVENTORY SAVING ERROR");
        }
    }
}
