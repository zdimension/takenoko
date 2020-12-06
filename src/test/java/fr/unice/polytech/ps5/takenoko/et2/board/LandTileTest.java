package fr.unice.polytech.ps5.takenoko.et2.board;

import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class LandTileTest
{
    @Test
    void equalsTest()
    {
        LandTile l1 = new LandTile(Color.GREEN);
        LandTile l2 = new LandTile(Color.GREEN);
        LandTile l3 = new LandTile(Color.PINK);
        assertEquals(l2, l1);
        assertNotEquals(l3, l1);
    }
}