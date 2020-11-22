package fr.unice.polytech.ps5.takenoko.et2.game;

import fr.unice.polytech.ps5.takenoko.et2.Color;
import fr.unice.polytech.ps5.takenoko.et2.Game;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.RandomBot;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GameTest
{

    @Test
    void gameConstructorEmptyPlotObjectiveDeck()
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

        var objectives = new ArrayList<PlotObjective>();

        assertThrows(IllegalArgumentException.class, () -> new Game(objectives, land));
    }

    @Test
    void gameContructorEmptyTileDeck() throws Exception
    {
        var land = new ArrayList<LandTile>();

        var objectives = List.of(
            new PlotObjective(
                2, List.of(Color.GREEN, Color.GREEN, Color.GREEN), List.of(0, 2)
            ),
            new PlotObjective(
                2, List.of(Color.GREEN, Color.GREEN, Color.GREEN), List.of(5, 0)
            ),
            new PlotObjective(
                2, List.of(Color.GREEN, Color.GREEN, Color.GREEN), List.of(5, 5)
            ),
            new PlotObjective(
                3, List.of(Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN), List.of(0, 2, 3)
            ),
            new PlotObjective(
                3, List.of(Color.GREEN, Color.GREEN, Color.YELLOW, Color.YELLOW), List.of(2, 3, 5)
            ),
            new PlotObjective(
                3, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(0, 2)
            ),
            new PlotObjective(
                3, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(5, 0)
            ),
            new PlotObjective(
                3, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(5, 5)
            ),
            new PlotObjective(
                4, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(0, 2, 3)
            ),
            new PlotObjective(
                4, List.of(Color.GREEN, Color.GREEN, Color.PINK, Color.PINK), List.of(2, 3, 5)
            ),
            new PlotObjective(
                4, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(0, 2)
            ),
            new PlotObjective(
                4, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(5, 0)
            ),
            new PlotObjective(
                4, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(5, 5)
            ),
            new PlotObjective(
                5, List.of(Color.PINK, Color.PINK, Color.PINK, Color.PINK), List.of(0, 2, 3)
            ),
            new PlotObjective(
                5, List.of(Color.PINK, Color.PINK, Color.YELLOW, Color.YELLOW), List.of(2, 3, 5)
            )
        );

        assertThrows(IllegalArgumentException.class, () -> new Game(objectives, land));
    }

    @Test
    void gameGameProcessing0Players() throws Exception
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
            ),
            new PlotObjective(
                2, List.of(Color.GREEN, Color.GREEN, Color.GREEN), List.of(5, 5)
            ),
            new PlotObjective(
                3, List.of(Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN), List.of(0, 2, 3)
            ),
            new PlotObjective(
                3, List.of(Color.GREEN, Color.GREEN, Color.YELLOW, Color.YELLOW), List.of(2, 3, 5)
            ),
            new PlotObjective(
                3, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(0, 2)
            ),
            new PlotObjective(
                3, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(5, 0)
            ),
            new PlotObjective(
                3, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(5, 5)
            ),
            new PlotObjective(
                4, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(0, 2, 3)
            ),
            new PlotObjective(
                4, List.of(Color.GREEN, Color.GREEN, Color.PINK, Color.PINK), List.of(2, 3, 5)
            ),
            new PlotObjective(
                4, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(0, 2)
            ),
            new PlotObjective(
                4, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(5, 0)
            ),
            new PlotObjective(
                4, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(5, 5)
            ),
            new PlotObjective(
                5, List.of(Color.PINK, Color.PINK, Color.PINK, Color.PINK), List.of(0, 2, 3)
            ),
            new PlotObjective(
                5, List.of(Color.PINK, Color.PINK, Color.YELLOW, Color.YELLOW), List.of(2, 3, 5)
            )
        );

        var game = new Game(objectives, land);

        assertThrows(IllegalArgumentException.class, game::gameProcessing);
    }

    @Test
    void gameGameProcessing1Players() throws Exception
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
            ),
            new PlotObjective(
                2, List.of(Color.GREEN, Color.GREEN, Color.GREEN), List.of(5, 5)
            ),
            new PlotObjective(
                3, List.of(Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN), List.of(0, 2, 3)
            ),
            new PlotObjective(
                3, List.of(Color.GREEN, Color.GREEN, Color.YELLOW, Color.YELLOW), List.of(2, 3, 5)
            ),
            new PlotObjective(
                3, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(0, 2)
            ),
            new PlotObjective(
                3, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(5, 0)
            ),
            new PlotObjective(
                3, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(5, 5)
            ),
            new PlotObjective(
                4, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(0, 2, 3)
            ),
            new PlotObjective(
                4, List.of(Color.GREEN, Color.GREEN, Color.PINK, Color.PINK), List.of(2, 3, 5)
            ),
            new PlotObjective(
                4, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(0, 2)
            ),
            new PlotObjective(
                4, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(5, 0)
            ),
            new PlotObjective(
                4, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(5, 5)
            ),
            new PlotObjective(
                5, List.of(Color.PINK, Color.PINK, Color.PINK, Color.PINK), List.of(0, 2, 3)
            ),
            new PlotObjective(
                5, List.of(Color.PINK, Color.PINK, Color.YELLOW, Color.YELLOW), List.of(2, 3, 5)
            )
        );

        var players = List.<DecisionMakerBuilder>of(
            RandomBot::new
        );

        var game = new Game(objectives, land);

        for (DecisionMakerBuilder player : players)
        {
            game.addPlayer(player);
        }

        assertThrows(IllegalArgumentException.class, game::gameProcessing);
    }

    @Test
    void gameAddPlayer5Players() throws Exception
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
            ),
            new PlotObjective(
                2, List.of(Color.GREEN, Color.GREEN, Color.GREEN), List.of(5, 5)
            ),
            new PlotObjective(
                3, List.of(Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN), List.of(0, 2, 3)
            ),
            new PlotObjective(
                3, List.of(Color.GREEN, Color.GREEN, Color.YELLOW, Color.YELLOW), List.of(2, 3, 5)
            ),
            new PlotObjective(
                3, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(0, 2)
            ),
            new PlotObjective(
                3, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(5, 0)
            ),
            new PlotObjective(
                3, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(5, 5)
            ),
            new PlotObjective(
                4, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(0, 2, 3)
            ),
            new PlotObjective(
                4, List.of(Color.GREEN, Color.GREEN, Color.PINK, Color.PINK), List.of(2, 3, 5)
            ),
            new PlotObjective(
                4, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(0, 2)
            ),
            new PlotObjective(
                4, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(5, 0)
            ),
            new PlotObjective(
                4, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(5, 5)
            ),
            new PlotObjective(
                5, List.of(Color.PINK, Color.PINK, Color.PINK, Color.PINK), List.of(0, 2, 3)
            ),
            new PlotObjective(
                5, List.of(Color.PINK, Color.PINK, Color.YELLOW, Color.YELLOW), List.of(2, 3, 5)
            )
        );

        var players = List.<DecisionMakerBuilder>of(
            RandomBot::new,
            RandomBot::new,
            RandomBot::new,
            RandomBot::new
        );

        var game = new Game(objectives, land);

        for (DecisionMakerBuilder player : players)
        {
            game.addPlayer(player);
        }

        assertThrows(IllegalAccessException.class, () -> game.addPlayer(RandomBot::new));
    }
}