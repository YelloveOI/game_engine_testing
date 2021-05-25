package cz.cvut.fel.pjv.Model;

import java.util.List;

/**
 * Represents character's container for items
 */
public class Inventory {
    /**
     * List of items in inventory
     */
    private List<Item> gameItemList;

    /**
     * Constructs inventory object depends on params
     * @param gameItemList
     */
    public Inventory(List<Item> gameItemList) {
        this.gameItemList = gameItemList;
    }

    public List<Item> getGameItemList() {
        return gameItemList;
    }

    /**
     * Handles item taking, takes item
     * @param item
     * @return tru if there is space for item, false if inventory is full
     */
    public boolean putItem(Item item) {
        if(gameItemList.size() < 5) {
            gameItemList.add(item);

            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Removes item with index index from item list
     * @param index
     */
    public void removeFromInventory(int index) {
        gameItemList.remove(index);
    }

    public Item getItem(int i) {
        return gameItemList.get(i);
    }

    public Sprite getItemSprite(int i) {
        return gameItemList.get(i).getSprite();
    }

    public int getInventorySize() {
        return gameItemList.size() - 1;
    }
}
