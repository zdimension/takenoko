package fr.unice.polytech.ps5.takenoko.et2.objective;

import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
            assertTrue(plotObjective.checkValidated(board, null));
            assertFalse(plotObjective2.checkValidated(board, null));
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
                        .checkValidated(board, null));

                assertTrue(
                    new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN), List.of(i))
                        .checkValidated(board, null));

                assertFalse(
                    new PlotObjective(5, List.of(Color.GREEN, Color.PINK), List.of(i))
                        .checkValidated(board, null));

                assertFalse(
                    new PlotObjective(5, List.of(Color.YELLOW, Color.PINK), List.of(i))
                        .checkValidated(board, null));
            }

            PlotObjective plotObjective7Loop = new PlotObjective(5, List.of(Color.GREEN, Color.YELLOW, Color.GREEN, Color.YELLOW, Color.GREEN, Color.YELLOW, Color.GREEN, Color.YELLOW), List.of(0, 3, 0, 3, 0, 3, 0));
            assertTrue(plotObjective7Loop.checkValidated(board, null)); // ヽ༼ ಠ益ಠ ༽ﾉ
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
                        .checkValidated(board, null));
            }

            PlotObjective plotObjective10 = new PlotObjective(5, List.of(Color.GREEN), Collections.emptyList());
            PlotObjective plotObjective11 = new PlotObjective(5, List.of(Color.YELLOW), Collections.emptyList());
            PlotObjective plotObjective12 = new PlotObjective(5, List.of(Color.PINK), Collections.emptyList());
            assertTrue(plotObjective10.checkValidated(board, null));
            assertTrue(plotObjective11.checkValidated(board, null));
            assertFalse(plotObjective12.checkValidated(board, null));
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
        LandTile l1 = new LandTile(Color.YELLOW), l2 = new LandTile(Color.GREEN), l3 = new LandTile(Color.YELLOW);
        board.addTile(l1, new TilePosition(0, 1));
        board.addTile(l2, new TilePosition(1, 0));
        board.addTile(l3, new TilePosition(1, 1));
        board.getEdgeBetweenTwoTiles(l1, l2).addIrrigation();
        board.getEdgeBetweenTwoTiles(l2, l3).addIrrigation();
        try
        {
            PlotObjective plotObjective1 = new PlotObjective(5, List.of(Color.GREEN, Color.YELLOW), List.of(0));
            PlotObjective plotObjective2 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN), List.of(4));
            PlotObjective plotObjective3 = new PlotObjective(5, List.of(Color.GREEN, Color.PINK), List.of(0));
            assertTrue(plotObjective1.checkValidated(board, null));
            assertTrue(plotObjective2.checkValidated(board, null));
            assertFalse(plotObjective3.checkValidated(board, null));

            PlotObjective plotObjective11 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.YELLOW), List.of(2, 0));
            PlotObjective plotObjective12 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.YELLOW), List.of(1, 3));
            PlotObjective plotObjective13 = new PlotObjective(5, List.of(Color.GREEN, Color.YELLOW, Color.YELLOW), List.of(5, 1));
            PlotObjective plotObjective14 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.YELLOW), List.of(1, 6)); // Impossible path
            PlotObjective plotObjective15 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.YELLOW), List.of(1, 5)); // Possible path
            assertTrue(plotObjective11.checkValidated(board, null));
            assertTrue(plotObjective12.checkValidated(board, null));
            assertTrue(plotObjective13.checkValidated(board, null));
            assertFalse(plotObjective14.checkValidated(board, null));
            assertTrue(plotObjective15.checkValidated(board, null));
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
            assertTrue(plotObjective1.checkValidated(board, null));
            assertTrue(plotObjective2.checkValidated(board, null));
            assertFalse(plotObjective3.checkValidated(board, null));

            PlotObjective plotObjective11 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.YELLOW), List.of(2, 5));
            PlotObjective plotObjective12 = new PlotObjective(5, List.of(Color.GREEN, Color.PINK, Color.GREEN), List.of(1, 4));
            assertTrue(plotObjective11.checkValidated(board, null));
            assertTrue(plotObjective12.checkValidated(board, null));

            PlotObjective plotObjective21 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.PINK), List.of(3, 4));
            PlotObjective plotObjective22 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.PINK), List.of(0, 1));
            PlotObjective plotObjective23 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.PINK), List.of(4, 3));
            PlotObjective plotObjective24 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.PINK), List.of(1, 0));
            PlotObjective plotObjective25 = new PlotObjective(5, List.of(Color.PINK, Color.GREEN, Color.YELLOW), List.of(4, 3));
            PlotObjective plotObjective26 = new PlotObjective(5, List.of(Color.PINK, Color.GREEN, Color.YELLOW), List.of(1, 0));
            PlotObjective plotObjective27 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.PINK), List.of(1, 1));
            assertTrue(plotObjective21.checkValidated(board, null));
            assertTrue(plotObjective22.checkValidated(board, null));
            assertFalse(plotObjective23.checkValidated(board, null));
            assertFalse(plotObjective24.checkValidated(board, null));
            assertTrue(plotObjective25.checkValidated(board, null));
            assertTrue(plotObjective26.checkValidated(board, null));
            assertFalse(plotObjective27.checkValidated(board, null));
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
        LandTile l1 = new LandTile(Color.YELLOW), l2 = new LandTile(Color.GREEN), l3 = new LandTile(Color.PINK), l4 = new LandTile(Color.GREEN);
        board.addTile(l1, new TilePosition(-1, 0));
        board.addTile(l2, new TilePosition(-1, 1));
        board.addTile(l3, new TilePosition(-2, 1));
        board.addTile(l4, new TilePosition(-2, 2));
        board.getEdgeBetweenTwoTiles(l1, l2).addIrrigation();
        board.getEdgeBetweenTwoTiles(l2, l3).addIrrigation();
        board.getEdgeBetweenTwoTiles(l3, l4).addIrrigation();
        try
        {
            PlotObjective plotObjective1 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.PINK, Color.GREEN), List.of(0, 4, 0));
            PlotObjective plotObjective1b = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.PINK, Color.GREEN), List.of(3, 1, 3));
            PlotObjective plotObjective2 = new PlotObjective(5, List.of(Color.YELLOW, Color.GREEN, Color.PINK), List.of(0, 4));
            PlotObjective plotObjective3 = new PlotObjective(5, List.of(Color.GREEN, Color.PINK, Color.YELLOW, Color.GREEN), List.of(3, 2, 0));
            PlotObjective plotObjective4 = new PlotObjective(5, List.of(Color.GREEN, Color.GREEN, Color.GREEN, Color.PINK, Color.YELLOW, Color.PINK), List.of(2, 5, 3, 2, 5));
            PlotObjective plotObjective5 = new PlotObjective(5, List.of(Color.GREEN, Color.GREEN, Color.PINK, Color.YELLOW), List.of(5, 3, 3));
            PlotObjective plotObjective5b = new PlotObjective(5, List.of(Color.GREEN, Color.GREEN, Color.PINK, Color.YELLOW), List.of(0, 4, 4));
            assertTrue(plotObjective1.checkValidated(board, null));
            assertTrue(plotObjective1b.checkValidated(board, null));
            assertTrue(plotObjective2.checkValidated(board, null));
            assertTrue(plotObjective3.checkValidated(board, null));
            assertTrue(plotObjective4.checkValidated(board, null));
            assertFalse(plotObjective5.checkValidated(board, null));
            assertFalse(plotObjective5b.checkValidated(board, null));
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
        LandTile l1 = new LandTile(Color.GREEN), l2 = new LandTile(Color.PINK), l3 = new LandTile(Color.YELLOW), l4 = new LandTile(Color.YELLOW), l5 = new LandTile(Color.YELLOW), l6 = new LandTile(Color.GREEN);
        assertTrue(board.addTile(l1, new TilePosition(0, 1))); // Take care of the order^^
        assertTrue(board.addTile(l2, new TilePosition(1, 0)));
        assertTrue(board.addTile(l3, new TilePosition(1, -1)));
        assertTrue(board.addTile(l4, new TilePosition(0, -1)));
        assertTrue(board.addTile(l5, new TilePosition(-1, 0)));
        assertTrue(board.addTile(l6, new TilePosition(-1, 1)));
        board.getEdgeBetweenTwoTiles(l1, l2).addIrrigation();
        board.getEdgeBetweenTwoTiles(l2, l3).addIrrigation();
        board.getEdgeBetweenTwoTiles(l3, l4).addIrrigation();
        board.getEdgeBetweenTwoTiles(l4, l5).addIrrigation();
        board.getEdgeBetweenTwoTiles(l5, l6).addIrrigation();
        board.getEdgeBetweenTwoTiles(l6, l1).addIrrigation();

        LandTile l7 = new LandTile(Color.GREEN), l8 = new LandTile(Color.YELLOW), l9 = new LandTile(Color.GREEN), l10 = new LandTile(Color.YELLOW);
        assertTrue(board.addTile(l7, new TilePosition(-2, 1)));
        assertTrue(board.addTile(l8, new TilePosition(-2, 2)));
        assertTrue(board.addTile(l9, new TilePosition(-2, 0)));
        assertTrue(board.addTile(l10, new TilePosition(-1, -1)));
        board.getEdgeBetweenTwoTiles(l5, l10).addIrrigation();
        board.getEdgeBetweenTwoTiles(l5, l9).addIrrigation();
        board.getEdgeBetweenTwoTiles(l5, l7).addIrrigation();
        board.getEdgeBetweenTwoTiles(l6, l7).addIrrigation();
        board.getEdgeBetweenTwoTiles(l7, l8).addIrrigation();

        LandTile l11 = new LandTile(Color.PINK), l12 = new LandTile(Color.PINK), l13 = new LandTile(Color.GREEN), l14 = new LandTile(Color.PINK), l15 = new LandTile(Color.PINK);
        assertTrue(board.addTile(l11, new TilePosition(1, 1)));
        assertTrue(board.addTile(l12, new TilePosition(2, 0)));
        assertTrue(board.addTile(l13, new TilePosition(2, 1)));
        assertTrue(board.addTile(l14, new TilePosition(2, -1)));
        assertTrue(board.addTile(l15, new TilePosition(2, -2)));
        board.getEdgeBetweenTwoTiles(l2, l11).addIrrigation();
        board.getEdgeBetweenTwoTiles(l11, l12).addIrrigation();
        board.getEdgeBetweenTwoTiles(l11, l13).addIrrigation();
        board.getEdgeBetweenTwoTiles(l2, l12).addIrrigation();
        board.getEdgeBetweenTwoTiles(l2, l14).addIrrigation();
        board.getEdgeBetweenTwoTiles(l3, l14).addIrrigation();
        board.getEdgeBetweenTwoTiles(l3, l15).addIrrigation();
        try
        {
            PlotObjective plotObjective1 = new PlotObjective(2, List.of(Color.GREEN, Color.GREEN, Color.GREEN), List.of(0, 1));
            assertTrue(plotObjective1.checkValidated(board, null));
            assertEquals(2, plotObjective1.getPoints());
            PlotObjective plotObjective2 = new PlotObjective(4, List.of(Color.PINK, Color.GREEN, Color.GREEN, Color.PINK), List.of(0, 2, 0));
            assertFalse(plotObjective2.checkValidated(board, null));
            assertEquals(4, plotObjective2.getPoints());
            PlotObjective plotObjective3 = new PlotObjective(2, List.of(Color.GREEN, Color.GREEN, Color.GREEN), List.of(1, 3));
            assertFalse(plotObjective3.checkValidated(board, null));
            assertEquals(2, plotObjective3.getPoints());
            PlotObjective plotObjective4 = new PlotObjective(5, List.of(Color.PINK, Color.PINK, Color.PINK, Color.PINK), List.of(0, 1, 3));
            assertTrue(plotObjective4.checkValidated(board, null));
            assertEquals(5, plotObjective4.getPoints());
            PlotObjective plotObjective5 = new PlotObjective(2, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(4, 4));
            assertTrue(plotObjective5.checkValidated(board, null));
            assertEquals(2, plotObjective5.getPoints());
            PlotObjective plotObjective6 = new PlotObjective(5, List.of(Color.YELLOW, Color.PINK, Color.YELLOW, Color.PINK), List.of(3, 4, 0));
            assertFalse(plotObjective6.checkValidated(board, null));
            assertEquals(5, plotObjective6.getPoints());
            PlotObjective plotObjective7 = new PlotObjective(3, List.of(Color.YELLOW, Color.GREEN, Color.YELLOW, Color.GREEN), List.of(1, 3, 4));
            assertFalse(plotObjective7.checkValidated(board, null));
            assertEquals(3, plotObjective7.getPoints());
            PlotObjective plotObjective8 = new PlotObjective(4, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(2, 4, 5));
            assertFalse(plotObjective8.checkValidated(board, null));
            assertEquals(4, plotObjective8.getPoints());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(); // Test false
        }
    }
}