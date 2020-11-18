package fr.unice.polytech.ps5.takenoko.et2.game;

import fr.unice.polytech.ps5.takenoko.et2.Color;
import fr.unice.polytech.ps5.takenoko.et2.Game;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.RandomBot;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BambooTest
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
    void addBambooSectionToTileWithFirstIrrigationTest()
    {
        LandTile l1 = new LandTile(Color.GREEN);
        LandTile l2 = new LandTile(Color.YELLOW);
        LandTile l3 = new LandTile(Color.PINK);
        LandTile l4 = new LandTile(Color.PINK);
        LandTile l5 = new LandTile(Color.GREEN);
        LandTile l6 = new LandTile(Color.YELLOW);
        board.addTile(l1, new TilePosition( 0,  1), game.getBambooReserve());
        board.addTile(l2, new TilePosition( 1,  0), game.getBambooReserve());
        board.addTile(l3, new TilePosition( 1, -1), game.getBambooReserve());
        board.addTile(l4, new TilePosition( 0, -1), game.getBambooReserve());
        board.addTile(l5, new TilePosition(-1,  0), game.getBambooReserve());
        board.addTile(l6, new TilePosition(-1,  1), game.getBambooReserve());
        LandTile l7 = new LandTile(Color.GREEN);
        LandTile l8 = new LandTile(Color.YELLOW);
        LandTile l9 = new LandTile(Color.PINK);
        LandTile l10 = new LandTile(Color.PINK);
        LandTile l11 = new LandTile(Color.GREEN);
        LandTile l12 = new LandTile(Color.YELLOW);
        board.addTile( l7, new TilePosition( 1,  1), game.getBambooReserve());
        board.addTile( l8, new TilePosition( 2, -1), game.getBambooReserve());
        board.addTile( l9, new TilePosition( 1, -2), game.getBambooReserve());
        board.addTile(l10, new TilePosition(-1, -1), game.getBambooReserve());
        board.addTile(l11, new TilePosition(-2,  1), game.getBambooReserve());
        board.addTile(l12, new TilePosition(-1,  2), game.getBambooReserve());

        assertEquals(1, l1.getBambooSize());
        assertEquals(0, l7.getBambooSize());
        game.pickIrrigation(p);
        game.pickIrrigation(p);
        game.pickIrrigation(p);
        game.pickIrrigation(p);
        game.pickIrrigation(p);
        game.pickIrrigation(p);
        game.pickIrrigation(p);
        game.pickIrrigation(p);
        game.pickIrrigation(p);
        game.pickIrrigation(p);
        p.irrigateEdge(l1.getEdge(2));
        p.irrigateEdge(l1.getEdge(1));
        assertEquals(1, l1.getBambooSize());
        assertEquals(1, l7.getBambooSize());
        p.irrigateEdge(l2.getEdge(0));
        assertEquals(1, l7.getBambooSize());
        p.irrigateEdge(l2.getEdge(1));
        assertEquals(0, l8.getBambooSize());
        p.irrigateEdge(l8.getEdge(0));
        assertEquals(1, l8.getBambooSize());
        assertThrows(IllegalArgumentException.class, () -> game.addBambooSectionToTile(null));
    }
}