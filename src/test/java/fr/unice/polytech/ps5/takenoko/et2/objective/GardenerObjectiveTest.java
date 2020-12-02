package fr.unice.polytech.ps5.takenoko.et2.objective;

import fr.unice.polytech.ps5.takenoko.et2.Color;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GardenerObjectiveTest
{
    Board mockBoard;
    Player mockPlayer;
    List<LandTile> tileList;

    @BeforeEach
    void init()
    {
        mockBoard = mock(Board.class);
        mockPlayer = mock(Player.class);
        tileList = new ArrayList<>();

        for (int i = 0; i < 12; i++)
        {
            LandTile mockLandTile = mock(LandTile.class);
            tileList.add(mockLandTile);
        }

        Set<LandTile> landTileList = new HashSet<>(tileList);

        when(mockBoard.getLandTiles()).thenReturn(landTileList);

        when(tileList.get(0).getColor()).thenReturn(Color.GREEN);
        when(tileList.get(1).getColor()).thenReturn(Color.GREEN);
        when(tileList.get(2).getColor()).thenReturn(Color.GREEN);
        when(tileList.get(3).getColor()).thenReturn(Color.GREEN);
        when(tileList.get(4).getColor()).thenReturn(Color.GREEN);
        when(tileList.get(5).getColor()).thenReturn(Color.GREEN);
        when(tileList.get(6).getColor()).thenReturn(Color.YELLOW);
        when(tileList.get(7).getColor()).thenReturn(Color.YELLOW);
        when(tileList.get(8).getColor()).thenReturn(Color.YELLOW);
        when(tileList.get(9).getColor()).thenReturn(Color.YELLOW);
        when(tileList.get(10).getColor()).thenReturn(Color.PINK);
        when(tileList.get(11).getColor()).thenReturn(Color.PINK);

        when(tileList.get(0).getBambooSize()).thenReturn(4);
        when(tileList.get(1).getBambooSize()).thenReturn(3);
        when(tileList.get(2).getBambooSize()).thenReturn(3);
        when(tileList.get(3).getBambooSize()).thenReturn(3);
        when(tileList.get(4).getBambooSize()).thenReturn(3);
        when(tileList.get(5).getBambooSize()).thenReturn(3);
        when(tileList.get(6).getBambooSize()).thenReturn(2);
        when(tileList.get(7).getBambooSize()).thenReturn(3);
        when(tileList.get(8).getBambooSize()).thenReturn(3);
        when(tileList.get(9).getBambooSize()).thenReturn(2);
        when(tileList.get(10).getBambooSize()).thenReturn(3);
        when(tileList.get(11).getBambooSize()).thenReturn(3);

           }

    @Test
    void oneStackWithFourBambooSectionsTest()
    {
        GardenerObjective greenGardenerObjective = new GardenerObjective(5, Color.GREEN, 1, 4);
        GardenerObjective yellowGardenerObjective = new GardenerObjective(6, Color.YELLOW, 1, 4);

        assertTrue(greenGardenerObjective.checkValidated(mockBoard, mockPlayer));
        assertEquals(5, greenGardenerObjective.getPoints());

        assertFalse(yellowGardenerObjective.checkValidated(mockBoard, mockPlayer));
        assertEquals(6, yellowGardenerObjective.getPoints());

        verify(mockBoard, times(2)).getLandTiles();
        for(int i=0; i<12; i++)
        {
            verify(tileList.get(i), times(2)).getColor();
            verify(tileList.get(i),times(2)).getBambooSize();
        }

        verifyNoMoreInteractions(mockBoard);
        for(int i=0; i<12; i++)
        {
            verifyNoMoreInteractions(tileList.get(i));
        }

    }

    @Test
    void threeStacksOfBambooTest()
    {
        GardenerObjective greenGardenerObjective = new GardenerObjective(8, Color.GREEN, 4, 3);
        GardenerObjective yellowGardenerObjective = new GardenerObjective(7, Color.YELLOW, 3, 3);
        GardenerObjective pinkGardenerObjective = new GardenerObjective(6, Color.PINK, 2, 3);

        assertTrue(greenGardenerObjective.checkValidated(mockBoard, mockPlayer));
        assertEquals(8, greenGardenerObjective.getPoints());

        assertFalse(yellowGardenerObjective.checkValidated(mockBoard, mockPlayer));
        assertEquals(7, yellowGardenerObjective.getPoints());

        assertTrue(pinkGardenerObjective.checkValidated(mockBoard, mockPlayer));
        assertEquals(6, pinkGardenerObjective.getPoints());

        verify(mockBoard, times(3)).getLandTiles();
        for(int i=0; i<12; i++)
        {
            verify(tileList.get(i), times(3)).getColor();
            verify(tileList.get(i),times(3)).getBambooSize();
        }

        verifyNoMoreInteractions(mockBoard);
        for(int i=0; i<12; i++)
        {
            verifyNoMoreInteractions(tileList.get(i));
        }
    }
}