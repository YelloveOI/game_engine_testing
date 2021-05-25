package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.Model.Inventory;
import cz.cvut.fel.pjv.Model.Item;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InventoryFileHandlerTest {

    @Test
    void saveInventroy() {
        // Arrange
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        Item item3 = mock(Item.class);
        Item item4 = mock(Item.class);
        Item item5 = mock(Item.class);

        when(item1.getId()).thenReturn(1);
        when(item2.getId()).thenReturn(2);
        when(item3.getId()).thenReturn(3);
        when(item4.getId()).thenReturn(4);
        when(item5.getId()).thenReturn(5);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        itemList.add(item4);
        itemList.add(item5);

        Inventory inventory = new Inventory(itemList);

        int[] expectedArr = {1, 2, 3, 4, 5};
        int[] currentArr = new int[5];

        // Act
        InventoryFileHandler.saveInventroy(inventory);

        // Assert
        try {
            Scanner scanner = new Scanner(new FileReader("inventories/save.inv"));
            int i = 0;

            while (scanner.hasNext()) {
                currentArr[i] = scanner.nextInt();
                i++;
            }

            scanner.close();

            assertArrayEquals(expectedArr, currentArr);
        }
        catch (Exception e){
            System.out.println("reading error");
        }
    }
}