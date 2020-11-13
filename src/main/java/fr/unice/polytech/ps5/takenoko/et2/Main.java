package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
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
        var freq = new int[players.size()];
        var N = 10000;
        for (var i = 0; i < N; i++)
        {
            if (i % 1000 == 0)
            {
                System.out.println(i);
            }
            var game = new Game(objectives, land);
            for (DecisionMakerBuilder player : players)
            {
                game.addPlayer(player);
            }
            var res = game.gameProcessing();
            for (Integer re : res)
            {
                freq[re]++;
            }
        }
        System.out.println(Arrays.toString(Arrays.stream(freq).asDoubleStream().map(d -> d / N).toArray()));
    }
}
