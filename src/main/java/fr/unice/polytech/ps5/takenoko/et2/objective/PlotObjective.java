package fr.unice.polytech.ps5.takenoko.et2.objective;

import fr.unice.polytech.ps5.takenoko.et2.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlotObjective extends Objective
{
    private final List<Color> listColors = new ArrayList<>();
    private final List<Integer> listPaths = new ArrayList<>();

    public PlotObjective(List<Color> listC, List<Integer> listP, int points) throws Exception
    {
        super(points);
        if (listC.size() == 0 || listC.size() != (listP.size() + 1))
        {
            throw new Exception("Invalid Objective");
        }
        listColors.addAll(listC);
        listPaths.addAll(listP);
    }

    public boolean checkValidated(Board board)
    {
        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < listPaths.size(); j++)
            {
                listPaths.set(j, (listPaths.get(j) + i) % 6);
            }
            if (checkValidatedSpecificRotation(board))
            {
                return true;
            }
        }
        return false;
    }

    private boolean checkValidatedSpecificRotation(Board board)
    {
        Map<TilePosition, Tile> listTiles = board.getTiles();
        for (Map.Entry<TilePosition, Tile> entry : listTiles.entrySet())
        {
            if (!(entry.getValue() instanceof LandTile))
            {
                continue;
            }
            LandTile landTile = (LandTile) entry.getValue();
            if (landTile.getColor() == listColors.get(0))
            { // this is a match^^
                int i = 0;
                for (Integer nextEdgePosition : listPaths)
                {
                    Tile landTile2 = landTile.getEdge(nextEdgePosition).getTile(0);
                    if (landTile2 instanceof PondTile)
                    {
                        return false;
                    }
                    if (landTile2 == landTile)
                    {
                        landTile2 = landTile.getEdge(nextEdgePosition).getTile(1);
                        if (landTile2 instanceof PondTile)
                        {
                            return false;
                        }
                        landTile = (LandTile) landTile2;
                        if (landTile.getColor() != listColors.get(i))
                        {
                            return false;
                        }
                    }
                    i++;
                }
            }
        }
        return true;
    }
}
