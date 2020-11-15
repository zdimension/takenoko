package fr.unice.polytech.ps5.takenoko.et2.board;

import fr.unice.polytech.ps5.takenoko.et2.Color;
import fr.unice.polytech.ps5.takenoko.et2.Game;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.RandomBot;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IrrigationTest
{
    Game game;
    Player p;
    Board board;

    @BeforeEach
    void init()
    {
        try
        {
            var land = new ArrayList<LandTile>();
            for (var i = 0; i < 11; i++)
            {
                land.add(new LandTile(Color.GREEN));
            }
            for (var i = 0; i < 9; i++)
            {
                land.add(new LandTile(Color.YELLOW));
            }
            for (var i = 0; i < 7; i++)
            {
                land.add(new LandTile(Color.PINK));
            }
            var objectives = List.of(
                new PlotObjective(
                    2, List.of(Color.GREEN, Color.GREEN, Color.GREEN), List.of(0, 2)
                ),
                new PlotObjective(
                    2, List.of(Color.GREEN, Color.GREEN, Color.GREEN), List.of(5, 0)
                )
            );
            game = new Game(objectives, land);
            DecisionMakerBuilder dm = RandomBot::new;
            p = new Player(game, dm);
            game.addPlayer(dm);
            board = game.getBoard();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void checkIrrigationsWithPond()
    {
        LandTile l1 = new LandTile(Color.GREEN); // Tiles near the Pond should be irrigated
        LandTile l2 = new LandTile(Color.YELLOW);
        LandTile l3 = new LandTile(Color.PINK);
        LandTile l4 = new LandTile(Color.PINK);
        LandTile l5 = new LandTile(Color.GREEN);
        LandTile l6 = new LandTile(Color.YELLOW);
        assertTrue(board.addTile(l1, new TilePosition(0, 1)));
        assertTrue(board.addTile(l2, new TilePosition(1, 0)));
        assertTrue(board.addTile(l3, new TilePosition(1, -1)));
        assertTrue(board.addTile(l4, new TilePosition(0, -1)));
        assertTrue(board.addTile(l5, new TilePosition(-1, 0)));
        assertTrue(board.addTile(l6, new TilePosition(-1, 1)));
        assertTrue(l1.isIrrigated()); // Tiles far the Pond shouldn't be irrigated
        assertTrue(l2.isIrrigated());
        assertTrue(l3.isIrrigated());
        assertTrue(l4.isIrrigated());
        assertTrue(l5.isIrrigated());
        assertTrue(l6.isIrrigated());
        LandTile l7 = new LandTile(Color.GREEN);
        LandTile l8 = new LandTile(Color.YELLOW);
        LandTile l9 = new LandTile(Color.PINK);
        LandTile l10 = new LandTile(Color.PINK);
        LandTile l11 = new LandTile(Color.GREEN);
        LandTile l12 = new LandTile(Color.YELLOW);
        assertTrue(board.addTile(l7, new TilePosition(1, 1)));
        assertTrue(board.addTile(l8, new TilePosition(2, -1)));
        assertTrue(board.addTile(l9, new TilePosition(1, -2)));
        assertTrue(board.addTile(l10, new TilePosition(-1, -1)));
        assertTrue(board.addTile(l11, new TilePosition(-2, 1)));
        assertTrue(board.addTile(l12, new TilePosition(-1, 2)));
        assertFalse(l7.isIrrigated());
        assertFalse(l8.isIrrigated());
        assertFalse(l9.isIrrigated());
        assertFalse(l10.isIrrigated());
        assertFalse(l11.isIrrigated());
        assertFalse(l12.isIrrigated());

        // Check bamboos size
        /*assertEquals(0, l1.getBambooSize());
        assertEquals(0, l2.getBambooSize());
        assertEquals(0, l3.getBambooSize());
        assertEquals(0, l4.getBambooSize());
        assertEquals(0, l5.getBambooSize());
        assertEquals(0, l6.getBambooSize());*/
        assertEquals(0, l7.getBambooSize());
        assertEquals(0, l8.getBambooSize());
        assertEquals(0, l9.getBambooSize());
        assertEquals(0, l10.getBambooSize());
        assertEquals(0, l11.getBambooSize());
        assertEquals(0, l12.getBambooSize());
    }
}
