package fr.unice.polytech.ps5.takenoko.et2.objective;

import fr.unice.polytech.ps5.takenoko.et2.Color;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.Tile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GardenerObjectiveTest
{
    Board board;

    @BeforeEach
    void init()
    {
        board = mock(Board.class);
        Map<TilePosition, Tile> landTileList = new HashMap<>();
        LandTile[] tileList = new LandTile[12];

        for (int i = 0; i < 12; i++)
        {
            LandTile mockLandTile = mock(LandTile.class);
            TilePosition mockTilePosition = mock(TilePosition.class);
            tileList[i] = mockLandTile;
            landTileList.put(mockTilePosition, mockLandTile);
        }

        when(board.getTiles()).thenReturn(landTileList);
        when(tileList[0].getColor()).thenReturn(Color.GREEN);
        when(tileList[1].getColor()).thenReturn(Color.GREEN);
        when(tileList[2].getColor()).thenReturn(Color.GREEN);
        when(tileList[3].getColor()).thenReturn(Color.GREEN);
        when(tileList[4].getColor()).thenReturn(Color.GREEN);
        when(tileList[5].getColor()).thenReturn(Color.GREEN);
        when(tileList[6].getColor()).thenReturn(Color.YELLOW);
        when(tileList[7].getColor()).thenReturn(Color.YELLOW);
        when(tileList[8].getColor()).thenReturn(Color.YELLOW);
        when(tileList[9].getColor()).thenReturn(Color.YELLOW);
        when(tileList[10].getColor()).thenReturn(Color.PINK);
        when(tileList[11].getColor()).thenReturn(Color.PINK);

        when(tileList[0].getBambooSize()).thenReturn(4);
        when(tileList[1].getBambooSize()).thenReturn(3);
        when(tileList[2].getBambooSize()).thenReturn(3);
        when(tileList[3].getBambooSize()).thenReturn(3);
        when(tileList[4].getBambooSize()).thenReturn(3);
        when(tileList[5].getBambooSize()).thenReturn(3);
        when(tileList[6].getBambooSize()).thenReturn(2);
        when(tileList[7].getBambooSize()).thenReturn(3);
        when(tileList[8].getBambooSize()).thenReturn(3);
        when(tileList[9].getBambooSize()).thenReturn(2);
        when(tileList[10].getBambooSize()).thenReturn(3);
        when(tileList[11].getBambooSize()).thenReturn(3);
    }

    @Test
    void oneStackWithFourBambooSectionsTest()
    {
        GardenerObjective greenGardenerObjective = new GardenerObjective(5, Color.GREEN, 1, 4);
        GardenerObjective yellowGardenerObjective = new GardenerObjective(6, Color.YELLOW, 1, 4);

        assertTrue(greenGardenerObjective.checkValidated(board, null));
        assertEquals(5, greenGardenerObjective.getPoints());

        assertFalse(yellowGardenerObjective.checkValidated(board, null));
        assertEquals(6, yellowGardenerObjective.getPoints());
    }

    @Test
    void threeStacksOfBambooTest()
    {
        GardenerObjective greenGardenerObjective = new GardenerObjective(8, Color.GREEN, 4, 3);
        GardenerObjective yellowGardenerObjective = new GardenerObjective(7, Color.YELLOW, 3, 3);
        GardenerObjective pinkGardenerObjective = new GardenerObjective(6, Color.PINK, 2, 3);

        assertTrue(greenGardenerObjective.checkValidated(board, null));
        assertEquals(8, greenGardenerObjective.getPoints());

        assertFalse(yellowGardenerObjective.checkValidated(board, null));
        assertEquals(7, yellowGardenerObjective.getPoints());

        assertTrue(pinkGardenerObjective.checkValidated(board, null));
        assertEquals(6, pinkGardenerObjective.getPoints());
    }
}