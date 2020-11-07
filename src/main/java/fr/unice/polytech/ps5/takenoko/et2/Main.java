package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.Tile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;

import java.util.Map;

public class Main
{

    static int nPoss = 0;

    static void checkPossRec(Board board, int n)
    {
        if (n == 0)
        {
            nPoss++;
            return;
        }
        for (int i = -30; i < 30; i++)
        {
            for (int j = -30; j < 30; j++)
            {
                Board boardBis = new Board();
                for (Map.Entry<TilePosition, Tile> entry : board.getTiles().entrySet()) // For each Tile
                {
                    boardBis.addTile(new LandTile(Color.GREEN), entry.getKey());
                }
                checkPossRec(boardBis, n - 1);
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
        checkPossRec(board, 1);
        System.out.println(nPoss);
    }

}
