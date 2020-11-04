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
        for (Map.Entry<TilePosition, Tile> entry : listTiles.entrySet()) // For each Tile
        {
            if (!(entry.getValue() instanceof LandTile)) // If it's the pond
            {
                continue; // useless to evaluate
            }
            LandTile landTile = (LandTile) entry.getValue();
            if (landTile.getColor() == listColors.get(0)) // If we have found a Tile with the right color
            { // this is a match^^
                if (checkValidatedSpecificRotationFromOneTile(landTile))
                { // We can check if all the path is ok
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkValidatedSpecificRotationFromOneTile(LandTile landTile)
    {
        int i = 0;
        for (Integer nextEdgePosition : listPaths) // for each neighbour
        {
            Tile landTileNeighbour = landTile.getEdge(nextEdgePosition).getTile(0); // looking for the neightbour
            if (landTileNeighbour == landTile)
            {
                landTileNeighbour = landTile.getEdge(nextEdgePosition).getTile(1);
            }
            if (landTileNeighbour instanceof PondTile) // If the neighbour is the pond
            {
                return false;
            }
            if (((LandTile) landTileNeighbour).getColor() != listColors.get(i))
            {
                return false;
            }
            i++;
        }
        return true;
    }
}
