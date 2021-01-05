package fr.unice.polytech.ps5.takenoko.et2.board;

import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LandTileImprovementTest
{
    private LandTile landTile;
    private LandTile landTileMock;

    @BeforeEach
    void init()
    {
        landTile = new LandTile(Color.PINK);
        landTileMock = spy(new LandTile(Color.YELLOW));
        when(landTileMock.isIrrigated()).thenReturn(true);
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

    @Test
    void testFertilizer()
    {
        when(landTileMock.canSetImprovement()).thenReturn(true);
        assertTrue(landTileMock.isIrrigated());
        assertTrue(landTileMock.setLandTileImprovement(LandTileImprovement.FERTILIZER));
        assertTrue(landTileMock.growBambooSection());
        assertEquals(landTileMock.getBambooSize(), 2);
    }

    @Test
    void testCanSetImprovement()
    {
        assertTrue(landTileMock.canSetImprovement());
        assertTrue(landTileMock.canGrowBamboo());
        assertTrue(landTileMock.growBambooSection());
        assertFalse(landTileMock.canSetImprovement());
        assertTrue(landTileMock.cutBambooSection());
        assertTrue(landTileMock.canSetImprovement());
        assertTrue(landTileMock.setLandTileImprovement(LandTileImprovement.FERTILIZER));
        assertFalse(landTileMock.canSetImprovement());
        assertFalse(landTileMock.setLandTileImprovement(LandTileImprovement.ENCLOSURE));
    }

    @Test
    void testsetWatershedOnIrrigatedNotGrows()
    {
        when(landTileMock.canSetImprovement()).thenReturn(true);
        assertTrue(landTileMock.isIrrigated());
        assertTrue(landTileMock.setLandTileImprovement(LandTileImprovement.WATERSHED));
        verify(landTileMock, never()).growBambooSection();
    }
}
