package fr.unice.polytech.ps5.takenoko.et2.board;

import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest
{
    @Test
    void edgeTest()
    {
        var t1 = new LandTile(Color.GREEN);
        var t2 = new LandTile(Color.PINK);

        var e = new Edge(t1);
        assertTrue(e.setTile(t2));
        assertFalse(e.setTile(t2));
        Tile et1 = e.getTile(0);
        Tile et2 = e.getTile(1);
        if (!(et1 instanceof LandTile))
        {
            fail();
        }
        if (!(et2 instanceof LandTile))
        {
            fail();
        }
        assertEquals(Color.GREEN, ((LandTile) et1).getColor());
        assertEquals(Color.PINK, ((LandTile) et2).getColor());
        assertEquals(et1, t1);
        assertEquals(et2, t2);
    }
}