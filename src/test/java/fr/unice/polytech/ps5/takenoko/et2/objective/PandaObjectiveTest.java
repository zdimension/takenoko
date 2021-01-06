package fr.unice.polytech.ps5.takenoko.et2.objective;

import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Player;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PandaObjectiveTest
{
    private Board mockBoard;
    private Player mockPlayer;
    private final Map<Color, Integer> playerBambooReserve = new HashMap<>();

    @BeforeEach
    void init()
    {
        mockBoard = mock(Board.class);
        mockPlayer = mock(Player.class);

        playerBambooReserve.put(Color.GREEN,2);
        playerBambooReserve.put(Color.YELLOW,1);
        playerBambooReserve.put(Color.PINK,3);

        when(mockPlayer.getBambooSectionReserve()).thenReturn(playerBambooReserve);
    }


    @Test
    void checkValidatedWhenPlayerReserveContains2BambooSectionsOfTheObjectiveTest()
    {
        Map<Color, Integer> objectiveBambooSections1 = Map.of(Color.GREEN,2);
        Map<Color, Integer> objectiveBambooSections2 = Map.of(Color.YELLOW,2);

        PandaObjective greenPandaObjective = new PandaObjective(3, objectiveBambooSections1);
        PandaObjective yellowPandaObjective = new PandaObjective(4, objectiveBambooSections2);

        assertTrue(greenPandaObjective.checkValidated(mockBoard, mockPlayer));
        assertEquals(3, greenPandaObjective.getPoints());

        assertFalse(yellowPandaObjective.checkValidated(mockBoard, mockPlayer));
        assertEquals(4, yellowPandaObjective.getPoints());

        verify(mockPlayer, times(2)).getBambooSectionReserve();

        verifyNoMoreInteractions(mockBoard);
        verifyNoMoreInteractions(mockPlayer);
    }

    @Test
    void checkValidatedWith3BambooSectionsObjectiveTest()
    {
        Map<Color, Integer> objectiveBambooSections1 = Map.of(Color.GREEN,1, Color.YELLOW, 1, Color.PINK, 1);
        Map<Color, Integer> objectiveBambooSections2 = Map.of(Color.GREEN,1, Color.YELLOW, 2, Color.PINK, 1);

        PandaObjective greenYellowPinkPandaObjective1 = new PandaObjective(5, objectiveBambooSections1);
        PandaObjective greenYellowPinkPandaObjective2 = new PandaObjective(6, objectiveBambooSections2);

        assertTrue(greenYellowPinkPandaObjective1.checkValidated(mockBoard, mockPlayer));
        assertEquals(5, greenYellowPinkPandaObjective1.getPoints());

        assertFalse(greenYellowPinkPandaObjective2.checkValidated(mockBoard, mockPlayer));
        assertEquals(6, greenYellowPinkPandaObjective2.getPoints());

        verify(mockPlayer, times(2)).getBambooSectionReserve();

        verifyNoMoreInteractions(mockBoard);
        verifyNoMoreInteractions(mockPlayer);
    }

    @Test
    void checkValidatedNotNullArgumentTest()
    {
        Map<Color, Integer> objectiveBambooSections1 = Map.of(Color.YELLOW, 2);
        PandaObjective yellowPandaObjective = new PandaObjective(5, objectiveBambooSections1);

        assertThrows(NullPointerException.class, () -> yellowPandaObjective.checkValidated(null, mockPlayer));
        assertThrows(NullPointerException.class, () -> yellowPandaObjective.checkValidated(mockBoard, null));
        assertThrows(NullPointerException.class, () -> yellowPandaObjective.checkValidated(null, null));

    }
}