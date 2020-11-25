package fr.unice.polytech.ps5.takenoko.et2.board;

import fr.unice.polytech.ps5.takenoko.et2.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BambooTest
{
    Board board;
    LandTile l1;
    LandTile l2;
    LandTile l3;
    LandTile l4;
    LandTile l5;
    LandTile l6;
    LandTile l7;
    LandTile l8;
    LandTile l9;
    LandTile l10;
    LandTile l11;
    LandTile l12;

    @BeforeEach
    void init()
    {
        try
        {
            board = new Board();

            l1 = new LandTile(Color.GREEN);
            l2 = new LandTile(Color.YELLOW);
            l3 = new LandTile(Color.PINK);
            l4 = new LandTile(Color.PINK);
            l5 = new LandTile(Color.GREEN);
            l6 = new LandTile(Color.YELLOW);
            board.addTile(l1, new TilePosition(0, 1));
            board.addTile(l2, new TilePosition(1, 0));
            board.addTile(l3, new TilePosition(1, -1));
            board.addTile(l4, new TilePosition(0, -1));
            board.addTile(l5, new TilePosition(-1, 0));
            board.addTile(l6, new TilePosition(-1, 1));
            l7 = new LandTile(Color.GREEN);
            l8 = new LandTile(Color.YELLOW);
            l9 = new LandTile(Color.PINK);
            l10 = new LandTile(Color.PINK);
            l11 = new LandTile(Color.GREEN);
            l12 = new LandTile(Color.YELLOW);
            board.addTile(l7, new TilePosition(1, 1));
            board.addTile(l8, new TilePosition(2, -1));
            board.addTile(l9, new TilePosition(1, -2));
            board.addTile(l10, new TilePosition(-1, -1));
            board.addTile(l11, new TilePosition(-2, 1));
            board.addTile(l12, new TilePosition(-1, 2));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void growBambooSectionYellowTest()
    {
        for (int i = 0; i < 3; i++)
        {
            l2.growBambooSection();
        }

        assertEquals(4, l2.getBambooSize());

        l2.growBambooSection();
        assertEquals(4, l2.getBambooSize());

    }

    @Test
    void growBambooSectionFalseTest()
    {
        assertFalse(l10.growBambooSection());

        for (int i = 0; i < 3; i++)
        {
            l2.growBambooSection();
        }
        assertFalse(l2.growBambooSection());
    }
}