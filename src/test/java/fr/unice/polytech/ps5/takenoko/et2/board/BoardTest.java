package fr.unice.polytech.ps5.takenoko.et2.board;

import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest
{
    @Test
    void addTile()
    {
        var b = new Board();

        var tile = new LandTile(Color.GREEN);
        assertTrue(b.addTile(tile, new TilePosition(0, 1)));

        // duplicate
        assertFalse(b.addTile(new LandTile(Color.GREEN), new TilePosition(0, 1)));

        // only touches one tile
        assertFalse(b.addTile(new LandTile(Color.GREEN), new TilePosition(0, 2)));

        assertSame(b.getCenter().getEdge(0), tile.getEdge(3));
        assertEquals(tile, b.getCenter().getEdge(0).getTile(1));
        assertEquals(b.getCenter(), tile.getEdge(3).getTile(0));

        var tile2 = new LandTile(Color.YELLOW);
        assertTrue(b.addTile(tile2, new TilePosition(1, 0)));

        assertSame(b.getCenter().getEdge(1), tile2.getEdge(4));
        assertEquals(tile2, b.getCenter().getEdge(1).getTile(1));
        assertEquals(b.getCenter(), tile2.getEdge(4).getTile(0));

        assertSame(tile.getEdge(2), tile2.getEdge(5));
        assertEquals(tile2, tile.getEdge(2).getTile(1));
        assertEquals(tile, tile2.getEdge(5).getTile(0));

        var tile3 = new LandTile(Color.PINK);

        assertFalse(b.addTile(tile3, new TilePosition(-1, 2)));
        assertFalse(b.addTile(tile3, new TilePosition(2, -1)));
        assertTrue(b.addTile(tile3, new TilePosition(1, 1)));

        var map = new HashMap<TilePosition, Tile>();

        // check that the linked storage is coherent with the absolute storage
        addTileInfo(map, TilePosition.ZERO, b.getCenter());

        assertEquals(map, b.getTiles());
    }

    private void addTileInfo(Map<TilePosition, Tile> map, TilePosition pos, Tile t)
    {
        map.put(pos, t);

        for (var i = 0; i < 6; i++)
        {
            var neighbor = t.getEdge(i).getOther(t);
            var np = pos.add(Board.edgeNumbers.get(i));

            if (neighbor != null && !map.containsKey(np))
            {
                addTileInfo(map, np, neighbor);
            }
        }
    }

    @Test
    void testGetBambooableTiles()
    {
        Board board = new Board();
        LandTile l1 = new LandTile(Color.GREEN);
        LandTile l2 = new LandTile(Color.YELLOW);
        LandTile l3 = new LandTile(Color.PINK);
        LandTile l4 = new LandTile(Color.PINK);
        LandTile l5 = new LandTile(Color.GREEN);
        LandTile l6 = new LandTile(Color.YELLOW);
        LandTile l7 = new LandTile(Color.YELLOW);
        LandTile l8 = new LandTile(Color.YELLOW);



        board.addTile(l1, new TilePosition(0, 1));
        board.addTile(l2, new TilePosition(1, 0));
        board.addTile(l3, new TilePosition(1, -1));
        board.addTile(l4, new TilePosition(1, 1));
        board.addTile(l5, new TilePosition(2, -1));
        board.addTile(l6, new TilePosition(2, 0));
        board.addTile(l7, new TilePosition(2, 1));
        board.addTile(l8, new TilePosition(0, 2));

        l1.getEdge(2).addIrrigation();
        l1.getEdge(1).addIrrigation();
        l1.getEdge(0).addIrrigation();
        l1.cutBambooSection();
        l2.getEdge(0).addIrrigation();
        l5.setLandTileImprovement(LandTileImprovement.WATERSHED);
        l7.setLandTileImprovement(LandTileImprovement.WATERSHED);
        l7.cutBambooSection();
        l4.growBambooSection();
        l4.growBambooSection();
        l4.growBambooSection();
        l4.growBambooSection();
        l3.growBambooSection();
        l3.growBambooSection();
        l3.growBambooSection();
        l5.growBambooSection();
        l5.growBambooSection();
        l5.growBambooSection();
        l8.growBambooSection();
        l8.growBambooSection();

        var expectedResult = new HashSet<LandTile>();
        expectedResult.add(l1);
        expectedResult.add(l2);
        expectedResult.add(l7);
        expectedResult.add(l8);

        assertEquals(expectedResult, new HashSet<>(board.getBambooableTiles()));
    }
}