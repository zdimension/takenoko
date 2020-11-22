package fr.unice.polytech.ps5.takenoko.et2.board;

import fr.unice.polytech.ps5.takenoko.et2.BambooSection;
import fr.unice.polytech.ps5.takenoko.et2.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BambooTest
{
    Board board;
    List<BambooSection> bambooReserve;
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
        bambooReserve = new ArrayList<>();
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

            l1 = new LandTile(Color.GREEN);
            l2 = new LandTile(Color.YELLOW);
            l3 = new LandTile(Color.PINK);
            l4 = new LandTile(Color.PINK);
            l5 = new LandTile(Color.GREEN);
            l6 = new LandTile(Color.YELLOW);
            board.addTile(l1, new TilePosition(0, 1), bambooReserve);
            board.addTile(l2, new TilePosition(1, 0), bambooReserve);
            board.addTile(l3, new TilePosition(1, -1), bambooReserve);
            board.addTile(l4, new TilePosition(0, -1), bambooReserve);
            board.addTile(l5, new TilePosition(-1, 0), bambooReserve);
            board.addTile(l6, new TilePosition(-1, 1), bambooReserve);
            l7 = new LandTile(Color.GREEN);
            l8 = new LandTile(Color.YELLOW);
            l9 = new LandTile(Color.PINK);
            l10 = new LandTile(Color.PINK);
            l11 = new LandTile(Color.GREEN);
            l12 = new LandTile(Color.YELLOW);
            board.addTile(l7, new TilePosition(1, 1), bambooReserve);
            board.addTile(l8, new TilePosition(2, -1), bambooReserve);
            board.addTile(l9, new TilePosition(1, -2), bambooReserve);
            board.addTile(l10, new TilePosition(-1, -1), bambooReserve);
            board.addTile(l11, new TilePosition(-2, 1), bambooReserve);
            board.addTile(l12, new TilePosition(-1, 2), bambooReserve);
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

    @Test
    void growBambooSectionFalseTest()
    {
        var res = this.bambooReserve.stream().filter(b -> b.getColor().equals(l10.getColor())).findAny();
        if (res.isEmpty())
        {
            return;
        }
        var bambooSection = res.get();
        assertFalse(l10.growBambooSection(bambooSection));

        for (int i = 0; i < 3; i++)
        {

            var res1 = this.bambooReserve.stream().filter(b -> b.getColor().equals(l2.getColor())).findAny();
            if (res1.isEmpty())
            {
                return;
            }
            var bambooSection1 = res1.get();
            l2.growBambooSection(bambooSection1);
        }
        var res2 = this.bambooReserve.stream().filter(b -> b.getColor().equals(l2.getColor())).findAny();
        if (res2.isEmpty())
        {
            return;
        }
        var bambooSection2 = res2.get();
        assertFalse(l2.growBambooSection(bambooSection2));
    }

    @Test
    void growBambooSectionThrowsException()
    {
        var res = this.bambooReserve.stream().filter(b -> b.getColor().equals(Color.GREEN)).findAny();
        if (res.isEmpty())
        {
            return;
        }
        var bambooSection = res.get();
        assertThrows(IllegalArgumentException.class, () -> l2.growBambooSection(bambooSection));

        assertThrows(NullPointerException.class, () -> l2.growBambooSection(null));
    }

    @Test
    void bambooReserveDecrementsWhenPlaceTileNextToPondTest()
    {
        var actualBambooStock = bambooReserve.stream().collect(Collectors.groupingBy(BambooSection::getColor));
        assertEquals(28, actualBambooStock.get(Color.GREEN).size());
        assertEquals(28, actualBambooStock.get(Color.PINK).size());
        assertEquals(28, actualBambooStock.get(Color.YELLOW).size());
    }
}