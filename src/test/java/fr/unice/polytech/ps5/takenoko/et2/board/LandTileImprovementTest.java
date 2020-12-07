package fr.unice.polytech.ps5.takenoko.et2.board;

import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LandTileImprovementTest
{
    private LandTile landTile;

    @BeforeEach
    void init()
    {
        landTile = new LandTile(Color.PINK);
    }

    @Test
    void testConst()
    {
        landTile = new LandTile(Color.PINK, LandTileImprovement.ENCLOSURE);
        assertEquals(landTile.getLandTileImprovement(), LandTileImprovement.ENCLOSURE);
    }

    @Test
    void testNull()
    {
        assertNull(landTile.getLandTileImprovement());
    }

    @Test
    void testIrrigated()
    {
        assertFalse(landTile.isIrrigated());
        landTile.setLandTileImprovement(LandTileImprovement.WATERSHED);
        assertTrue(landTile.isIrrigated());
    }
}
