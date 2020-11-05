package fr.unice.polytech.ps5.takenoko.et2;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
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
}