package fr.unice.polytech.ps5.takenoko.et2.objective;

import fr.unice.polytech.ps5.takenoko.et2.Color;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlotObjectiveTest
{
    Board board;

    @BeforeEach
    void initBoard()
    {
        board = new Board();
    }

    @Test
    void ObjectiveWithOnePlotTest()
    {
        board.addTile(new LandTile(Color.GREEN), new TilePosition(0, 1));
        try
        {
            PlotObjective plotObjective = new PlotObjective(5, List.of(Color.GREEN), Collections.emptyList());
            PlotObjective plotObjective2 = new PlotObjective(5, List.of(Color.PINK), Collections.emptyList());
            assertTrue(plotObjective.checkValidated(board));
            assertFalse(plotObjective2.checkValidated(board));
            assertEquals(5, plotObjective.getPoints());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(); // Test false
        }
    }

    @Test
    void ObjectiveWithTwoPlotsTest1()
    {
        board.addTile(new LandTile(Color.YELLOW), new TilePosition(0, 1));
        board.addTile(new LandTile(Color.GREEN), new TilePosition(1, 0));
        try
        {
            for (int i = 0; i < 6; i++)
            {
                assertTrue(
                    new PlotObjective(5, List.of(Color.GREEN, Color.YELLOW), List.of(i))
                        .checkValidated(board));

                assertTrue(
                    new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN), List.of(i))
                        .checkValidated(board));

                assertFalse(
                    new PlotObjective(5, List.of(Color.GREEN, Color.PINK), List.of(i))
                        .checkValidated(board));

                assertFalse(
                    new PlotObjective(5, List.of(Color.YELLOW, Color.PINK), List.of(i))
                        .checkValidated(board));
            }

            PlotObjective plotObjective7Loop = new PlotObjective(5, List.of(Color.GREEN, Color.YELLOW, Color.GREEN, Color.YELLOW, Color.GREEN, Color.YELLOW, Color.GREEN, Color.YELLOW), List.of(0, 3, 0, 3, 0, 3, 0));
            assertTrue(plotObjective7Loop.checkValidated(board)); // ヽ༼ ಠ益ಠ ༽ﾉ
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(); // Test false
        }
    }

    @Test
    void ObjectiveWithTwoPlotsTest2()
    {
        board.addTile(new LandTile(Color.YELLOW), new TilePosition(0, 1));
        board.addTile(new LandTile(Color.GREEN), new TilePosition(0, -1));
        try
        {
            for (int i = 0; i < 6; i++)
            {
                assertFalse(
                    new PlotObjective(5, List.of(Color.GREEN, Color.YELLOW), List.of(i))
                        .checkValidated(board));
            }

            PlotObjective plotObjective10 = new PlotObjective(5, List.of(Color.GREEN), Collections.emptyList());
            PlotObjective plotObjective11 = new PlotObjective(5, List.of(Color.YELLOW), Collections.emptyList());
            PlotObjective plotObjective12 = new PlotObjective(5, List.of(Color.PINK), Collections.emptyList());
            assertTrue(plotObjective10.checkValidated(board));
            assertTrue(plotObjective11.checkValidated(board));
            assertFalse(plotObjective12.checkValidated(board));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(); // Test false
        }
    }

    @Test
    void ObjectiveWithThreePlotsTest1()
    {
        board.addTile(new LandTile(Color.YELLOW), new TilePosition(0, 1));
        board.addTile(new LandTile(Color.GREEN), new TilePosition(1, 0));
        board.addTile(new LandTile(Color.YELLOW), new TilePosition(1, 1));
        try
        {
            PlotObjective plotObjective1 = new PlotObjective(5, List.of(Color.GREEN, Color.YELLOW), List.of(0));
            PlotObjective plotObjective2 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN), List.of(4));
            PlotObjective plotObjective3 = new PlotObjective(5, List.of(Color.GREEN, Color.PINK), List.of(0));
            assertTrue(plotObjective1.checkValidated(board));
            assertTrue(plotObjective2.checkValidated(board));
            assertFalse(plotObjective3.checkValidated(board));

            PlotObjective plotObjective11 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.YELLOW), List.of(2, 0));
            PlotObjective plotObjective12 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.YELLOW), List.of(1, 3));
            PlotObjective plotObjective13 = new PlotObjective(5, List.of(Color.GREEN, Color.YELLOW, Color.YELLOW), List.of(5, 1));
            PlotObjective plotObjective14 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.YELLOW), List.of(1, 6)); // Impossible path
            PlotObjective plotObjective15 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.YELLOW), List.of(1, 5)); // Possible path
            assertTrue(plotObjective11.checkValidated(board));
            assertTrue(plotObjective12.checkValidated(board));
            assertTrue(plotObjective13.checkValidated(board));
            assertFalse(plotObjective14.checkValidated(board));
            assertTrue(plotObjective15.checkValidated(board));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(); // Test false
        }
    }

    @Test
    void ObjectiveWithThreePlotsTest2()
    {
        board.addTile(new LandTile(Color.YELLOW), new TilePosition(0, 1));
        board.addTile(new LandTile(Color.GREEN), new TilePosition(1, 0));
        board.addTile(new LandTile(Color.PINK), new TilePosition(1, -1));
        try
        {
            PlotObjective plotObjective1 = new PlotObjective(5, List.of(Color.GREEN, Color.YELLOW), List.of(0));
            PlotObjective plotObjective2 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN), List.of(4));
            PlotObjective plotObjective3 = new PlotObjective(5, List.of(Color.YELLOW, Color.PINK), List.of(0));
            assertTrue(plotObjective1.checkValidated(board));
            assertTrue(plotObjective2.checkValidated(board));
            assertFalse(plotObjective3.checkValidated(board));

            PlotObjective plotObjective11 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.YELLOW), List.of(2, 5));
            PlotObjective plotObjective12 = new PlotObjective(5, List.of(Color.GREEN, Color.PINK, Color.GREEN), List.of(1, 4));
            assertTrue(plotObjective11.checkValidated(board));
            assertTrue(plotObjective12.checkValidated(board));

            PlotObjective plotObjective21 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.PINK), List.of(3, 4));
            PlotObjective plotObjective22 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.PINK), List.of(0, 1));
            PlotObjective plotObjective23 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.PINK), List.of(4, 3));
            PlotObjective plotObjective24 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.PINK), List.of(1, 0));
            PlotObjective plotObjective25 = new PlotObjective(5, List.of(Color.PINK, Color.GREEN, Color.YELLOW), List.of(4, 3));
            PlotObjective plotObjective26 = new PlotObjective(5, List.of(Color.PINK, Color.GREEN, Color.YELLOW), List.of(1, 0));
            PlotObjective plotObjective27 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.PINK), List.of(1, 1));
            assertTrue(plotObjective21.checkValidated(board));
            assertTrue(plotObjective22.checkValidated(board));
            assertFalse(plotObjective23.checkValidated(board));
            assertFalse(plotObjective24.checkValidated(board));
            assertTrue(plotObjective25.checkValidated(board));
            assertTrue(plotObjective26.checkValidated(board));
            assertFalse(plotObjective27.checkValidated(board));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(); // Test false
        }
    }

    @Test
    void ObjectiveWithFourPlotsTest1()
    {
        board.addTile(new LandTile(Color.YELLOW), new TilePosition(-1, 0));
        board.addTile(new LandTile(Color.GREEN), new TilePosition(-1, 1));
        board.addTile(new LandTile(Color.PINK), new TilePosition(-2, 1));
        board.addTile(new LandTile(Color.GREEN), new TilePosition(-2, 2));
        try
        {
            PlotObjective plotObjective1 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.PINK, Color.GREEN), List.of(0, 4, 0));
            PlotObjective plotObjective1b = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.PINK, Color.GREEN), List.of(3, 1, 3));
            PlotObjective plotObjective2 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.PINK), List.of(0, 4));
            PlotObjective plotObjective3 = new PlotObjective(5, List.of(Color.GREEN, Color.PINK, Color.YELLOW, Color.GREEN), List.of(3, 2, 0));
            PlotObjective plotObjective4 = new PlotObjective(5, List.of(Color.GREEN, Color.GREEN, Color.GREEN, Color.PINK, Color.YELLOW, Color.PINK), List.of(2, 5, 3, 2, 5));
            PlotObjective plotObjective5 = new PlotObjective(5, List.of(Color.GREEN, Color.GREEN, Color.PINK, Color.YELLOW), List.of(5, 3, 3));
            PlotObjective plotObjective5b = new PlotObjective(5, List.of(Color.GREEN, Color.GREEN, Color.PINK, Color.YELLOW), List.of(0, 4, 4));
            assertTrue(plotObjective1.checkValidated(board));
            assertTrue(plotObjective1b.checkValidated(board));
            assertTrue(plotObjective2.checkValidated(board));
            assertTrue(plotObjective3.checkValidated(board));
            assertTrue(plotObjective4.checkValidated(board));
            assertFalse(plotObjective5.checkValidated(board));
            assertFalse(plotObjective5b.checkValidated(board));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(); // Test false
        }
    }

    @Test
    void realObjectivePlotTest() // https://startyourmeeples.com/2019/05/26/which-are-the-best-takenoko-objective-cards/
    {
        assertTrue(board.addTile(new LandTile(Color.GREEN), new TilePosition(0, 1))); // Take care of the order^^
        assertTrue(board.addTile(new LandTile(Color.PINK), new TilePosition(1, 0)));
        assertTrue(board.addTile(new LandTile(Color.YELLOW), new TilePosition(1, -1)));
        assertTrue(board.addTile(new LandTile(Color.YELLOW), new TilePosition(0, -1)));
        assertTrue(board.addTile(new LandTile(Color.YELLOW), new TilePosition(-1, 0)));
        assertTrue(board.addTile(new LandTile(Color.GREEN), new TilePosition(-1, 1)));

        assertTrue(board.addTile(new LandTile(Color.GREEN), new TilePosition(-2, 1)));
        assertTrue(board.addTile(new LandTile(Color.YELLOW), new TilePosition(-2, 2)));
        assertTrue(board.addTile(new LandTile(Color.GREEN), new TilePosition(-2, 0)));
        assertTrue(board.addTile(new LandTile(Color.YELLOW), new TilePosition(-1, -1)));

        assertTrue(board.addTile(new LandTile(Color.PINK), new TilePosition(1, 1)));
        assertTrue(board.addTile(new LandTile(Color.PINK), new TilePosition(2, 0)));
        assertTrue(board.addTile(new LandTile(Color.GREEN), new TilePosition(2, 1)));
        assertTrue(board.addTile(new LandTile(Color.PINK), new TilePosition(2, -1)));
        assertTrue(board.addTile(new LandTile(Color.PINK), new TilePosition(2, -2)));
        try
        {
            PlotObjective plotObjective1 = new PlotObjective(2, List.of(Color.GREEN, Color.GREEN, Color.GREEN), List.of(0, 1));
            assertTrue(plotObjective1.checkValidated(board));
            assertEquals(2, plotObjective1.getPoints());
            PlotObjective plotObjective2 = new PlotObjective(4, List.of(Color.PINK, Color.GREEN, Color.GREEN, Color.PINK), List.of(0, 2, 0));
            assertFalse(plotObjective2.checkValidated(board));
            assertEquals(4, plotObjective2.getPoints());
            PlotObjective plotObjective3 = new PlotObjective(2, List.of(Color.GREEN, Color.GREEN, Color.GREEN), List.of(1, 3));
            assertFalse(plotObjective3.checkValidated(board));
            assertEquals(2, plotObjective3.getPoints());
            PlotObjective plotObjective4 = new PlotObjective(5, List.of(Color.PINK, Color.PINK, Color.PINK, Color.PINK), List.of(0, 1, 3));
            assertTrue(plotObjective4.checkValidated(board));
            assertEquals(5, plotObjective4.getPoints());
            PlotObjective plotObjective5 = new PlotObjective(2, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(4, 4));
            assertTrue(plotObjective5.checkValidated(board));
            assertEquals(2, plotObjective5.getPoints());
            PlotObjective plotObjective6 = new PlotObjective(5, List.of(Color.YELLOW, Color.PINK, Color.YELLOW, Color.PINK), List.of(3, 4, 0));
            assertFalse(plotObjective6.checkValidated(board));
            assertEquals(5, plotObjective6.getPoints());
            PlotObjective plotObjective7 = new PlotObjective(3, List.of(Color.YELLOW, Color.GREEN, Color.YELLOW, Color.GREEN), List.of(1, 3, 4));
            assertFalse(plotObjective7.checkValidated(board));
            assertEquals(3, plotObjective7.getPoints());
            PlotObjective plotObjective8 = new PlotObjective(4, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(2, 4, 5));
            assertFalse(plotObjective8.checkValidated(board));
            assertEquals(4, plotObjective8.getPoints());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(); // Test false
        }
    }
}