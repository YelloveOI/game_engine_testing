package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.Model.Inventory;
import cz.cvut.fel.pjv.Model.Item;
import cz.cvut.fel.pjv.Model.Position;
import cz.cvut.fel.pjv.Model.Weapon;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CharacterControllerTest {
    private CharacterController characterController;
    private CharacterAnimation characterAnimation;
    private Inventory inventory;

    @BeforeEach
    public void setup() {
        characterAnimation = mock(CharacterAnimation.class);
        inventory = mock(Inventory.class);
        characterController = new CharacterController(characterAnimation);
        characterController.loadCharacter(inventory);
    }

    @Test
    public void move_directionVectorUP_movesUp() {
        // Arrange
        characterController.directionVector = mock(Position.class);

        when(characterController.directionVector.copy()).thenReturn(new Position(0, -1));

        double expectedX = characterController.getCharacterXPos();
        double expectedY = characterController.getCharacterYPos() - characterController.speed/100;

        // Act
        characterController.move();

        // Assert
        assertEquals(expectedX, characterController.getCharacterXPos());
        assertEquals(expectedY, characterController.getCharacterYPos());
    }

    @Test
    public void changeItemInHand_2items_2ndItemInHand() {
        // Arrange
        inventory = mock(Inventory.class);
        Item item = mock(Item.class);
        Weapon weapon = mock(Weapon.class);

        when(item.getId()).thenReturn(1);
        when(weapon.getId()).thenReturn(0);
        when(inventory.getInventorySize()).thenReturn(1);
        when(inventory.getItem(0)).thenReturn(item);
        when(inventory.getItem(1)).thenReturn(weapon);

        characterController.loadCharacter(inventory);

        // Act
        characterController.changeItemInHand();

        // Assert
        assertEquals(0, characterController.itemInHand.getId());
        assertEquals(1, characterController.itemInHandIndex);
    }

    @Test
    public void interactWithItem_2items_2ndItemInHand() {
        // Arrange
        inventory = mock(Inventory.class);
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);

        when(item1.getId()).thenReturn(1);
        when(item2.getId()).thenReturn(0);
        when(inventory.getItem(0)).thenReturn(item1);
        when(inventory.getItem(1)).thenReturn(item2);
        when(inventory.getInventorySize()).thenReturn(1);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                when(inventory.getItem(0)).thenReturn(item2);
                return null;
            }
        }).when(inventory).removeFromInventory(0);

        characterController.loadCharacter(inventory);

        // Act
        characterController.interactWithItem();

        // Assert
        assertEquals(0, characterController.itemInHand.getId());
        assertEquals(0, characterController.itemInHandIndex);
    }

    @Test
    public void processTest1() {
        // setDirection, move, characterGetsHit, interactWithItem

        //Arrange
        inventory = mock(Inventory.class);
        Item item1 = mock(Item.class);
        when(item1.getId()).thenReturn(1);
        when(inventory.getItem(0)).thenReturn(item1);
        characterController.loadCharacter(inventory);
        // Act
        characterController.keyPress(KeyCode.UP);
        characterController.setDirection();
        // Assert
        assertEquals("UP", characterController.simpleDir);

        // Arrange
        double curX = characterController.getCharacterXPos();
        double curY = characterController.getCharacterYPos();
        // Act
        characterController.move();
        assertTrue(characterController.getCharacterYPos() < curY);
        assertTrue(characterController.getCharacterXPos() == curX);

        // Arrange
        double curHP = characterController.getCharacterHP();
        // Act
        characterController.characterGetsHit(50);
        // Assert
        assertEquals(curHP - 50, characterController.getCharacterHP());

        // Arrange
        curHP = characterController.getCharacterHP();
        // Act
        characterController.interactWithItem();
        //Assert
        assertTrue(characterController.getCharacterHP() > curHP);

    }

    @Test
    public void processTest2() {
        // changeItemInHand, attack, characterGetsHit => die

        // Arrange
        inventory = mock(Inventory.class);
        Item item1 = mock(Item.class);
        Weapon weapon = mock(Weapon.class);
        when(item1.getId()).thenReturn(1);
        when(weapon.getId()).thenReturn(0);
        when(inventory.getItem(0)).thenReturn(item1);
        when(inventory.getItem(1)).thenReturn(weapon);
        when(inventory.getInventorySize()).thenReturn(1);
        characterController.loadCharacter(inventory);
        // Act
        characterController.changeItemInHand();
        // Assert
        assertEquals(0, characterController.itemInHand.getId());

        // Act
        characterController.attack();
        // Assert
        assertTrue(characterController.attacks);

        // Act
        characterController.characterGetsHit(110);
        // Assert
        assertTrue(characterController.isDied);
    }

    @Test
    public void processTest3() {
        // changeItemInHand, act, changeItemInHand x3, interactWithItem

        //Assert
        inventory = mock(Inventory.class);
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        Item item3 = mock(Item.class);
        Item item4 = mock(Item.class);
        when(item1.getId()).thenReturn(1);
        when(item2.getId()).thenReturn(2);
        when(item3.getId()).thenReturn(2);
        when(item4.getId()).thenReturn(2);
        when(inventory.getItem(0)).thenReturn(item1);
        when(inventory.getItem(1)).thenReturn(item2);
        when(inventory.getItem(2)).thenReturn(item3);
        when(inventory.getItem(3)).thenReturn(item4);
        when(inventory.getInventorySize()).thenReturn(3);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                when(inventory.getItem(0)).thenReturn(item2);
                return null;
            }
        }).when(inventory).removeFromInventory(0);
        characterController.loadCharacter(inventory);

        // Act
        characterController.changeItemInHand();
        //Assert
        assertEquals(2, characterController.itemInHand.getId());
        assertEquals(1, characterController.itemInHandIndex);

        // Act
        characterController.act();
        //Assert
        assertTrue(characterController.acts);

        // Act
        characterController.changeItemInHand();
        characterController.changeItemInHand();
        characterController.changeItemInHand();
        // Assert
        assertEquals(1, characterController.itemInHand.getId());
        assertEquals(0, characterController.itemInHandIndex);

        // Act
        characterController.interactWithItem();
        // Assert
        assertEquals(2, characterController.itemInHand.getId());
        assertEquals(0, characterController.itemInHandIndex);
    }

    @Test
    public void processTest4() {
        // keyPress, setDirection, move, keyUnpress, setDirection, move

        // Arrange
        double curX = characterController.getCharacterXPos();
        double curY = characterController.getCharacterYPos();
        // Act
        characterController.keyPress(KeyCode.LEFT);
        characterController.setDirection();
        // Assert
        assertEquals("LEFT", characterController.simpleDir);

        // Act
        characterController.move();
        // Assert
        assertTrue(curX > characterController.getCharacterYPos());

        // Act
        characterController.keyUnpress(KeyCode.LEFT);
        characterController.setDirection();
        // Assert
        assertEquals("", characterController.simpleDir);
    }
}