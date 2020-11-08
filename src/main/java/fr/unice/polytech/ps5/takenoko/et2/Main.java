package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.RandomBot;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;

import java.util.ArrayList;
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
                2, List.of(Color.GREEN, Color.GREEN, Color.GREEN), List.of(0, 2)
            ),
            new PlotObjective(
                3, List.of(Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN), List.of(0, 2, 3)
            ),
            new PlotObjective(
                5, List.of(Color.PINK, Color.PINK, Color.YELLOW, Color.YELLOW), List.of(2, 3, 5)
            ),
            new PlotObjective(
                4, List.of(Color.GREEN, Color.GREEN, Color.PINK, Color.PINK), List.of(2, 3, 5)
            ),
            new PlotObjective(
                3, List.of(Color.GREEN, Color.GREEN, Color.YELLOW, Color.YELLOW), List.of(2, 3, 5)
            ),
            new PlotObjective(
                3, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(5, 5)
            ),
            new PlotObjective(
                4, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(0, 2, 3)
            ),
            new PlotObjective(
                3, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(5, 0)
            ),
            new PlotObjective(
                2, List.of(Color.GREEN, Color.GREEN, Color.GREEN), List.of(5, 5)
            ),
            new PlotObjective(
                2, List.of(Color.GREEN, Color.GREEN, Color.GREEN), List.of(5, 0)
            ),
            new PlotObjective(
                4, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(5, 0)
            ),
            new PlotObjective(
                4, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(0, 2)
            ),
            new PlotObjective(
                5, List.of(Color.PINK, Color.PINK, Color.PINK, Color.PINK), List.of(0, 2, 3)
            ),
            new PlotObjective(
                4, List.of(Color.PINK, Color.PINK, Color.PINK), List.of(5, 5)
            ),
            new PlotObjective(
                3, List.of(Color.YELLOW, Color.YELLOW, Color.YELLOW), List.of(0, 2)
            )
        )), land);
        //game.addPlayer(DecisionMakerDebugger::new);
        game.addPlayer(RandomBot::new);
        game.addPlayer(RandomBot::new);
        game.gameProcessing();
    }
}
