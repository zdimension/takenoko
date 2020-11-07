package fr.unice.polytech.ps5.takenoko.et2.objective;

import fr.unice.polytech.ps5.takenoko.et2.*;
import fr.unice.polytech.ps5.takenoko.et2.board.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The class representing the objectives with a specific plots scheme
 */
public class PlotObjective extends Objective
{
    private final List<Color> listColors = new ArrayList<>();
    private final List<Integer> listPaths = new ArrayList<>();

    /**
     * Constructor, build a PlotObjective with a list of colors and a list of directions
     *
     * @param listC  a list of colors eg [PINK, PINK, YELLOW, GREEN, GREEN]
     * @param listP  A list of directions eg [0, 5, 4, 2]
     * @param points Points of this objective
     * @throws Exception if listC.size() == 0 or listC.size() != listP.size() + 1
     */
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

    @Override
    public boolean checkValidated(Game game)
    {
        return checkValidated(game.getBoard());
    }

    /**
     * Check if the objective is validated with the given board
     *
     * @param board The board to check
     * @return true if the Objective is validated in the Game, false otherwise
     */
    public boolean checkValidated(Board board)
    {
        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < listPaths.size(); j++)
            {
                listPaths.set(j, (listPaths.get(j) + 1) % 6);
            }
            System.out.println();
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

    private boolean checkValidatedSpecificRotationFromOneTile(LandTile currentLandTile)
    {
        int i = 1;
        for (Integer nextEdgePosition : listPaths) // for each neighbour
        {
            Tile landTileNeighbour = currentLandTile.getEdge(nextEdgePosition).getTile(0); // looking for the neightbour
            if (landTileNeighbour == currentLandTile)
            {
                landTileNeighbour = currentLandTile.getEdge(nextEdgePosition).getTile(1);
            }
            if (landTileNeighbour instanceof PondTile || landTileNeighbour == null) // If the neighbour is the pond or doesn't exist
            {
                return false;
            }
            if (((LandTile) landTileNeighbour).getColor() != listColors.get(i))
            {
                return false;
            }
            i++;
            currentLandTile = (LandTile) landTileNeighbour;
        }
        return true;
    }
}
