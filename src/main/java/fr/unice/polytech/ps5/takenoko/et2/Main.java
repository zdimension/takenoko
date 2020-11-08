package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.Tile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;

import java.util.Map;

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

    //TODO code game
    public static String hello()
    {
        return "Hello World!";
    }

    public static void main(String... args)
    {
        System.out.println(hello());
        Board board = new Board();
        int n = 8;
        checkPossRec(board, n, -1, 1, -1, 1);
        System.out.println(nPoss);
        System.out.println(nPoss * Math.pow((double) 3, (double) n));
    }

}
