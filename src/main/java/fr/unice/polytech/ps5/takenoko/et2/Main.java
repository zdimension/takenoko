package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.Tile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;

import java.util.Map;

import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerDebugger;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.RandomBot;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main
{
    public static void main(String... args) throws Exception
    {
        var land = new ArrayList<LandTile>();
        for (var i = 0; i < 11; i++)
            land.add(new LandTile(Color.GREEN));
        for (var i = 0; i < 9; i++)
            land.add(new LandTile(Color.YELLOW));
        for (var i = 0; i < 7; i++)
            land.add(new LandTile(Color.PINK));
        var game = new Game(new ArrayList<>(List.of(
            new PlotObjective(
                List.of(Color.GREEN, Color.GREEN, Color.GREEN),
                List.of(0, 2),
                2
            ),
            new PlotObjective(
                List.of(Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN),
                List.of(0, 2, 3),
                3
            ),
            new PlotObjective(
                List.of(Color.PINK, Color.PINK, Color.YELLOW, Color.YELLOW),
                List.of(2, 3, 5),
                5
            ),
            new PlotObjective(
                List.of(Color.GREEN, Color.GREEN, Color.PINK, Color.PINK),
                List.of(2, 3, 5),
                4
            ),
            new PlotObjective(
                List.of(Color.GREEN, Color.GREEN, Color.YELLOW, Color.YELLOW),
                List.of(2, 3, 5),
                3
            ),
            new PlotObjective(
                List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW),
                List.of(5, 5),
                3
            ),
            new PlotObjective(
                List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW),
                List.of(0, 2, 3),
                4
            ),
            new PlotObjective(
                List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW),
                List.of(5, 0),
                3
            ),
            new PlotObjective(
                List.of(Color.GREEN, Color.GREEN, Color.GREEN),
                List.of(5, 5),
                2
            ),
            new PlotObjective(
                List.of(Color.GREEN, Color.GREEN, Color.GREEN),
                List.of(5, 0),
                2
            ),
            new PlotObjective(
                List.of(Color.PINK, Color.PINK, Color.PINK),
                List.of(5, 0),
                4
            ),
            new PlotObjective(
                List.of(Color.PINK, Color.PINK, Color.PINK),
                List.of(0, 2),
                4
            ),
            new PlotObjective(
                List.of(Color.PINK, Color.PINK, Color.PINK, Color.PINK),
                List.of(0, 2, 3),
                5
            ),
            new PlotObjective(
                List.of(Color.PINK, Color.PINK, Color.PINK),
                List.of(5, 5),
                4
            ),
            new PlotObjective(
                List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW),
                List.of(0, 2),
                3
            )
        )), land);
        //game.addPlayer(DecisionMakerDebugger::new);
        game.addPlayer(RandomBot::new);
        game.addPlayer(RandomBot::new);
        game.gameProcessing();
    }
}
