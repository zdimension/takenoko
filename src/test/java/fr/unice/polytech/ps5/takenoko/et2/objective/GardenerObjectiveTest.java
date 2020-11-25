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
        LandTile l1 = mock(LandTile.class);
        LandTile l2 = mock(LandTile.class);
        LandTile l3 = mock(LandTile.class);
        LandTile l4 = mock(LandTile.class);
        LandTile l5 = mock(LandTile.class);
        LandTile l6 = mock(LandTile.class);
        LandTile l7 = mock(LandTile.class);
        LandTile l8 = mock(LandTile.class);
        LandTile l9 = mock(LandTile.class);
        LandTile l10 = mock(LandTile.class);
        LandTile l11 = mock(LandTile.class);
        LandTile l12 = mock(LandTile.class);

        TilePosition tilePosition1 = mock(TilePosition.class);
        TilePosition tilePosition2 = mock(TilePosition.class);
        TilePosition tilePosition3 = mock(TilePosition.class);
        TilePosition tilePosition4 = mock(TilePosition.class);
        TilePosition tilePosition5 = mock(TilePosition.class);
        TilePosition tilePosition6 = mock(TilePosition.class);
        TilePosition tilePosition7 = mock(TilePosition.class);
        TilePosition tilePosition8 = mock(TilePosition.class);
        TilePosition tilePosition9 = mock(TilePosition.class);
        TilePosition tilePosition10 = mock(TilePosition.class);
        TilePosition tilePosition11 = mock(TilePosition.class);
        TilePosition tilePosition12 = mock(TilePosition.class);

        landTileList.put(tilePosition1,l1);
        landTileList.put(tilePosition2,l2);
        landTileList.put(tilePosition3,l3);
        landTileList.put(tilePosition4,l4);
        landTileList.put(tilePosition5,l5);
        landTileList.put(tilePosition6,l6);
        landTileList.put(tilePosition7,l7);
        landTileList.put(tilePosition8,l8);
        landTileList.put(tilePosition9,l9);
        landTileList.put(tilePosition10,l10);
        landTileList.put(tilePosition11,l11);
        landTileList.put(tilePosition12,l12);

        when(board.getTiles()).thenReturn(landTileList);
        when(l1.getColor()).thenReturn(Color.GREEN);
        when(l2.getColor()).thenReturn(Color.GREEN);
        when(l3.getColor()).thenReturn(Color.GREEN);
        when(l4.getColor()).thenReturn(Color.GREEN);
        when(l5.getColor()).thenReturn(Color.GREEN);
        when(l6.getColor()).thenReturn(Color.GREEN);
        when(l7.getColor()).thenReturn(Color.YELLOW);
        when(l8.getColor()).thenReturn(Color.YELLOW);
        when(l9.getColor()).thenReturn(Color.YELLOW);
        when(l10.getColor()).thenReturn(Color.YELLOW);
        when(l11.getColor()).thenReturn(Color.PINK);
        when(l12.getColor()).thenReturn(Color.PINK);

        when(l1.getBambooSize()).thenReturn(4);
        when(l2.getBambooSize()).thenReturn(3);
        when(l3.getBambooSize()).thenReturn(3);
        when(l4.getBambooSize()).thenReturn(3);
        when(l5.getBambooSize()).thenReturn(3);
        when(l6.getBambooSize()).thenReturn(3);
        when(l7.getBambooSize()).thenReturn(2);
        when(l8.getBambooSize()).thenReturn(3);
        when(l9.getBambooSize()).thenReturn(3);
        when(l10.getBambooSize()).thenReturn(2);
        when(l11.getBambooSize()).thenReturn(3);
        when(l12.getBambooSize()).thenReturn(3);
    }

    @Test
    void oneStackWithFourBambooSectionsTest() {
        GardenerObjective greenGardenerObjective = new GardenerObjective(5, Color.GREEN , 1, 4);
        GardenerObjective yellowGardenerObjective = new GardenerObjective(6, Color.YELLOW , 1, 4);

        assertTrue(greenGardenerObjective.checkValidated(board));
        assertEquals(5, greenGardenerObjective.getPoints());

        assertFalse(yellowGardenerObjective.checkValidated(board));
        assertEquals(6, yellowGardenerObjective.getPoints());
    }

    @Test
    void threeStacksOfBambooTest() {
        GardenerObjective greenGardenerObjective = new GardenerObjective(8, Color.GREEN , 4, 3);
        GardenerObjective yellowGardenerObjective = new GardenerObjective(7, Color.YELLOW , 3, 3);
        GardenerObjective pinkGardenerObjective = new GardenerObjective(6, Color.PINK, 2,3);

        assertTrue(greenGardenerObjective.checkValidated(board));
        assertEquals(8, greenGardenerObjective.getPoints());

        assertFalse(yellowGardenerObjective.checkValidated(board));
        assertEquals(7, yellowGardenerObjective.getPoints());

        assertTrue(pinkGardenerObjective.checkValidated(board));
        assertEquals(6, pinkGardenerObjective.getPoints());
    }
}