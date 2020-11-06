package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
            PlotObjective plotObjective = new PlotObjective(new ArrayList<>(Collections.singletonList(Color.GREEN)), new ArrayList<>(Collections.emptyList()), 5);
            PlotObjective plotObjective2 = new PlotObjective(new ArrayList<>(Collections.singletonList(Color.PINK)), new ArrayList<>(Collections.emptyList()), 5);
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
            PlotObjective plotObjective1 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.YELLOW)), new ArrayList<>(Collections.singletonList(0)), 5);
            PlotObjective plotObjective2 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.YELLOW)), new ArrayList<>(Collections.singletonList(1)), 5);
            PlotObjective plotObjective3 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.YELLOW)), new ArrayList<>(Collections.singletonList(2)), 5);
            PlotObjective plotObjective4 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.YELLOW)), new ArrayList<>(Collections.singletonList(3)), 5);
            PlotObjective plotObjective5 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.YELLOW)), new ArrayList<>(Collections.singletonList(4)), 5);
            PlotObjective plotObjective6 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.YELLOW)), new ArrayList<>(Collections.singletonList(5)), 5);
            assertTrue(plotObjective1.checkValidated(board));
            assertTrue(plotObjective2.checkValidated(board));
            assertTrue(plotObjective3.checkValidated(board));
            assertTrue(plotObjective4.checkValidated(board));
            assertTrue(plotObjective5.checkValidated(board));
            assertTrue(plotObjective6.checkValidated(board));
            plotObjective1 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), new ArrayList<>(Collections.singletonList(0)), 5);
            plotObjective2 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), new ArrayList<>(Collections.singletonList(1)), 5);
            plotObjective3 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), new ArrayList<>(Collections.singletonList(2)), 5);
            plotObjective4 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), new ArrayList<>(Collections.singletonList(3)), 5);
            plotObjective5 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), new ArrayList<>(Collections.singletonList(4)), 5);
            plotObjective6 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), new ArrayList<>(Collections.singletonList(5)), 5);
            assertTrue(plotObjective1.checkValidated(board));
            assertTrue(plotObjective2.checkValidated(board));
            assertTrue(plotObjective3.checkValidated(board));
            assertTrue(plotObjective4.checkValidated(board));
            assertTrue(plotObjective5.checkValidated(board));
            assertTrue(plotObjective6.checkValidated(board));


            plotObjective1 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.PINK)), new ArrayList<>(Collections.singletonList(0)), 5);
            plotObjective2 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.PINK)), new ArrayList<>(Collections.singletonList(1)), 5);
            plotObjective3 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.PINK)), new ArrayList<>(Collections.singletonList(2)), 5);
            plotObjective4 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.PINK)), new ArrayList<>(Collections.singletonList(3)), 5);
            plotObjective5 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.PINK)), new ArrayList<>(Collections.singletonList(4)), 5);
            plotObjective6 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.PINK)), new ArrayList<>(Collections.singletonList(5)), 5);
            assertFalse(plotObjective1.checkValidated(board));
            assertFalse(plotObjective2.checkValidated(board));
            assertFalse(plotObjective3.checkValidated(board));
            assertFalse(plotObjective4.checkValidated(board));
            assertFalse(plotObjective5.checkValidated(board));
            assertFalse(plotObjective6.checkValidated(board));
            plotObjective1 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.PINK)), new ArrayList<>(Collections.singletonList(0)), 5);
            plotObjective2 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.PINK)), new ArrayList<>(Collections.singletonList(1)), 5);
            plotObjective3 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.PINK)), new ArrayList<>(Collections.singletonList(2)), 5);
            plotObjective4 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.PINK)), new ArrayList<>(Collections.singletonList(3)), 5);
            plotObjective5 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.PINK)), new ArrayList<>(Collections.singletonList(4)), 5);
            plotObjective6 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.PINK)), new ArrayList<>(Collections.singletonList(5)), 5);
            assertFalse(plotObjective1.checkValidated(board));
            assertFalse(plotObjective2.checkValidated(board));
            assertFalse(plotObjective3.checkValidated(board));
            assertFalse(plotObjective4.checkValidated(board));
            assertFalse(plotObjective5.checkValidated(board));
            assertFalse(plotObjective6.checkValidated(board));

            PlotObjective plotObjective7Loop = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.YELLOW, Color.GREEN, Color.YELLOW, Color.GREEN, Color.YELLOW, Color.GREEN, Color.YELLOW)), new ArrayList<>(Arrays.asList(0, 3, 0, 3, 0, 3, 0)), 5);
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
            PlotObjective plotObjective1 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.YELLOW)), new ArrayList<>(Collections.singletonList(0)), 5);
            PlotObjective plotObjective2 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.YELLOW)), new ArrayList<>(Collections.singletonList(1)), 5);
            PlotObjective plotObjective3 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.YELLOW)), new ArrayList<>(Collections.singletonList(2)), 5);
            PlotObjective plotObjective4 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.YELLOW)), new ArrayList<>(Collections.singletonList(3)), 5);
            PlotObjective plotObjective5 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.YELLOW)), new ArrayList<>(Collections.singletonList(4)), 5);
            PlotObjective plotObjective6 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.YELLOW)), new ArrayList<>(Collections.singletonList(5)), 5);
            assertFalse(plotObjective1.checkValidated(board));
            assertFalse(plotObjective2.checkValidated(board));
            assertFalse(plotObjective3.checkValidated(board));
            assertFalse(plotObjective4.checkValidated(board));
            assertFalse(plotObjective5.checkValidated(board));
            assertFalse(plotObjective6.checkValidated(board));
            PlotObjective plotObjective10 = new PlotObjective(new ArrayList<>(Collections.singletonList(Color.GREEN)), new ArrayList<>(Collections.emptyList()), 5);
            PlotObjective plotObjective11 = new PlotObjective(new ArrayList<>(Collections.singletonList(Color.YELLOW)), new ArrayList<>(Collections.emptyList()), 5);
            PlotObjective plotObjective12 = new PlotObjective(new ArrayList<>(Collections.singletonList(Color.PINK)), new ArrayList<>(Collections.emptyList()), 5);
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
            PlotObjective plotObjective1 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.YELLOW)), new ArrayList<>(Collections.singletonList(0)), 5);
            PlotObjective plotObjective2 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), new ArrayList<>(Collections.singletonList(4)), 5);
            PlotObjective plotObjective3 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.PINK)), new ArrayList<>(Collections.singletonList(0)), 5);
            assertTrue(plotObjective1.checkValidated(board));
            assertTrue(plotObjective2.checkValidated(board));
            assertFalse(plotObjective3.checkValidated(board));

            PlotObjective plotObjective11 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.YELLOW)), new ArrayList<>(Arrays.asList(2, 0)), 5);
            PlotObjective plotObjective12 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.YELLOW)), new ArrayList<>(Arrays.asList(1, 3)), 5);
            PlotObjective plotObjective13 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.YELLOW, Color.YELLOW)), new ArrayList<>(Arrays.asList(5, 1)), 5);
            PlotObjective plotObjective14 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.YELLOW)), new ArrayList<>(Arrays.asList(1, 6)), 5); // Impossible path
            PlotObjective plotObjective15 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.YELLOW)), new ArrayList<>(Arrays.asList(1, 5)), 5); // Possible path
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
            PlotObjective plotObjective1 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.YELLOW)), new ArrayList<>(Collections.singletonList(0)), 5);
            PlotObjective plotObjective2 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), new ArrayList<>(Collections.singletonList(4)), 5);
            PlotObjective plotObjective3 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.PINK)), new ArrayList<>(Collections.singletonList(0)), 5);
            assertTrue(plotObjective1.checkValidated(board));
            assertTrue(plotObjective2.checkValidated(board));
            assertFalse(plotObjective3.checkValidated(board));

            PlotObjective plotObjective11 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.YELLOW)), new ArrayList<>(Arrays.asList(2, 5)), 5);
            PlotObjective plotObjective12 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.PINK, Color.GREEN)), new ArrayList<>(Arrays.asList(1, 4)), 5);
            assertTrue(plotObjective11.checkValidated(board));
            assertTrue(plotObjective12.checkValidated(board));

            PlotObjective plotObjective21 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.PINK)), new ArrayList<>(Arrays.asList(3, 4)), 5);
            PlotObjective plotObjective22 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.PINK)), new ArrayList<>(Arrays.asList(0, 1)), 5);
            PlotObjective plotObjective23 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.PINK)), new ArrayList<>(Arrays.asList(4, 3)), 5);
            PlotObjective plotObjective24 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.PINK)), new ArrayList<>(Arrays.asList(1, 0)), 5);
            PlotObjective plotObjective25 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.PINK, Color.GREEN, Color.YELLOW)), new ArrayList<>(Arrays.asList(4, 3)), 5);
            PlotObjective plotObjective26 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.PINK, Color.GREEN, Color.YELLOW)), new ArrayList<>(Arrays.asList(1, 0)), 5);
            PlotObjective plotObjective27 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.PINK)), new ArrayList<>(Arrays.asList(1, 1)), 5);
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
            PlotObjective plotObjective1 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.PINK, Color.GREEN)), new ArrayList<>(Arrays.asList(0, 4, 0)), 5);
            PlotObjective plotObjective1b = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.PINK, Color.GREEN)), new ArrayList<>(Arrays.asList(3, 1, 3)), 5);
            PlotObjective plotObjective2 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.PINK)), new ArrayList<>(Arrays.asList(0, 4)), 5);
            PlotObjective plotObjective3 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.PINK, Color.YELLOW, Color.GREEN)), new ArrayList<>(Arrays.asList(3, 2, 0)), 5);
            PlotObjective plotObjective4 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.GREEN, Color.GREEN, Color.PINK, Color.YELLOW, Color.PINK)), new ArrayList<>(Arrays.asList(2, 5, 3, 2, 5)), 5);
            PlotObjective plotObjective5 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.GREEN, Color.PINK, Color.YELLOW)), new ArrayList<>(Arrays.asList(5, 3, 3)), 5);
            PlotObjective plotObjective5b = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.GREEN, Color.PINK, Color.YELLOW)), new ArrayList<>(Arrays.asList(0, 4, 4)), 5);
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
            PlotObjective plotObjective1 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.GREEN, Color.GREEN)), new ArrayList<>(Arrays.asList(0, 1)), 2);
            assertTrue(plotObjective1.checkValidated(board));
            assertEquals(2, plotObjective1.getPoints());
            PlotObjective plotObjective2 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.PINK, Color.GREEN, Color.GREEN, Color.PINK)), new ArrayList<>(Arrays.asList(0, 2, 0)), 4);
            assertFalse(plotObjective2.checkValidated(board));
            assertEquals(4, plotObjective2.getPoints());
            PlotObjective plotObjective3 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.GREEN, Color.GREEN, Color.GREEN)), new ArrayList<>(Arrays.asList(1, 3)), 2);
            assertFalse(plotObjective3.checkValidated(board));
            assertEquals(2, plotObjective3.getPoints());
            PlotObjective plotObjective4 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.PINK, Color.PINK, Color.PINK, Color.PINK)), new ArrayList<>(Arrays.asList(0, 1, 3)), 5);
            assertTrue(plotObjective4.checkValidated(board));
            assertEquals(5, plotObjective4.getPoints());
            PlotObjective plotObjective5 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.PINK, Color.PINK, Color.PINK)), new ArrayList<>(Arrays.asList(4, 4)), 2);
            assertTrue(plotObjective5.checkValidated(board));
            assertEquals(2, plotObjective5.getPoints());
            PlotObjective plotObjective6 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.PINK, Color.YELLOW, Color.PINK)), new ArrayList<>(Arrays.asList(3, 4, 0)), 5);
            assertFalse(plotObjective6.checkValidated(board));
            assertEquals(5, plotObjective6.getPoints());
            PlotObjective plotObjective7 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.YELLOW, Color.GREEN)), new ArrayList<>(Arrays.asList(1, 3, 4)), 3);
            assertFalse(plotObjective7.checkValidated(board));
            assertEquals(3, plotObjective7.getPoints());
            PlotObjective plotObjective8 = new PlotObjective(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW)), new ArrayList<>(Arrays.asList(2, 4, 5)), 4);
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