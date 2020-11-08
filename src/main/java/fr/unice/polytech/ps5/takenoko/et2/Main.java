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

    static int nPoss = 0;

    static void checkPossRec(Board board, int n, int minX, int maxX, int minY, int maxY)
    {
        if (n == 0)
        {
            nPoss++;
            return;
        }
        for (int i = minX; i <= maxX; i++)
        {
            for (int j = minY; j <= maxY; j++)
            {
                Board boardBis = new Board();
                for (Map.Entry<TilePosition, Tile> entry : board.getTiles().entrySet()) // For each Tile
                {
                    boardBis.addTile(new LandTile(Color.GREEN), entry.getKey());
                }
                if (boardBis.addTile(new LandTile(Color.GREEN), new TilePosition(i, j)))
                {
                    checkPossRec(boardBis, n - 1, ((i == minX) ? (minX - 1) : minX), ((i == maxX) ? (maxX + 1) : maxX), ((j == minY) ? (minY - 1) : minY), ((j == maxY) ? (maxY + 1) : maxY));
                }
            }
        }
    }
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
        Board board = new Board();
        int n = 8;
        checkPossRec(board, n, -1, 1, -1, 1);
        System.out.println(nPoss);
        System.out.println(nPoss * Math.pow((double) 3, (double) n));
    }
}
