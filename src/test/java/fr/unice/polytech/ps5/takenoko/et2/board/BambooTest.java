package fr.unice.polytech.ps5.takenoko.et2.board;

import fr.unice.polytech.ps5.takenoko.et2.BambooSection;
import fr.unice.polytech.ps5.takenoko.et2.Color;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.RandomBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BambooTest
{
    Board board;
    List<BambooSection> bambooReserve;

    @BeforeEach
    void init()
    {
        bambooReserve = new ArrayList<BambooSection>();
        int i;
        for (i = 0; i < 30; i++)
        {
            bambooReserve.add(new BambooSection(Color.PINK));
            bambooReserve.add(new BambooSection(Color.GREEN));
            bambooReserve.add(new BambooSection(Color.YELLOW));
        }
        try
        {
            board = new Board();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void growBambooSectionYellowTest() throws Exception
    {
        //alors il va falloir instancier tout un plateau avec des irrigztions
        LandTile l1 = new LandTile(Color.GREEN);
        LandTile l2 = new LandTile(Color.YELLOW);
        LandTile l3 = new LandTile(Color.PINK);
        LandTile l4 = new LandTile(Color.PINK);
        LandTile l5 = new LandTile(Color.GREEN);
        LandTile l6 = new LandTile(Color.YELLOW);
        board.addTile(l1, new TilePosition(0, 1), bambooReserve);
        board.addTile(l2, new TilePosition(1, 0), bambooReserve);
        board.addTile(l3, new TilePosition(1, -1), bambooReserve);
        board.addTile(l4, new TilePosition(0, -1), bambooReserve);
        board.addTile(l5, new TilePosition(-1, 0), bambooReserve);
        board.addTile(l6, new TilePosition(-1, 1), bambooReserve);
        LandTile l7 = new LandTile(Color.GREEN);
        LandTile l8 = new LandTile(Color.YELLOW);
        LandTile l9 = new LandTile(Color.PINK);
        LandTile l10 = new LandTile(Color.PINK);
        LandTile l11 = new LandTile(Color.GREEN);
        LandTile l12 = new LandTile(Color.YELLOW);
        board.addTile(l7, new TilePosition(1, 1), bambooReserve);
        board.addTile(l8, new TilePosition(2, -1), bambooReserve);
        board.addTile(l9, new TilePosition(1, -2), bambooReserve);
        board.addTile(l10, new TilePosition(-1, -1), bambooReserve);
        board.addTile(l11, new TilePosition(-2, 1), bambooReserve);
        board.addTile(l12, new TilePosition(-1, 2), bambooReserve);

        for (int i = 0; i < 3; i++)
        {

            var res = this.bambooReserve.stream().filter(b -> b.getColor().equals(l2.getColor())).findAny();
            if (res.isEmpty())
            {
                return;
            }
            var bambooSection = res.get();
            l2.growBambooSection(bambooSection);
        }

        assertEquals(4, l2.getBambooSize());

        var res = this.bambooReserve.stream().filter(b -> b.getColor().equals(l2.getColor())).findAny();
        if (res.isEmpty())
        {
            return;
        }
        var bambooSection = res.get();
        l2.growBambooSection(bambooSection);
        assertEquals(4, l2.getBambooSize());



    }
}