package fr.unice.polytech.ps5.takenoko.et2.board;

import fr.unice.polytech.ps5.takenoko.et2.Color;
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

        assertEquals("[Edge, 1=[Land tile, Green], 2=[Land tile, Pink]]", e.toString());
    }
}