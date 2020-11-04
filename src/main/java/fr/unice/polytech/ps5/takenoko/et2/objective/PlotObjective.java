package fr.unice.polytech.ps5.takenoko.et2.objective;

import fr.unice.polytech.ps5.takenoko.et2.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlotObjective extends Objective
{
    private PlotObjectivePath plotObjectivePath;
    private List<Color> listColors = new ArrayList<Color>();
    private List<TilePosition> listPaths = new ArrayList<>();

    public PlotObjective(int points)
    {
        super(points);
    }

    public boolean checkValidated(Board board)
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
                //
            }
        }
        return false;
    }

    public boolean setObjectivePath(List<Color> listC, List<TilePosition> listP)
    {
        if (listC.size() == 0 || listC.size() != (listP.size() + 1))
        {
            return false;
        }
        for (Color color : listC)
        {
            listColors.add(color);
        }
        for (TilePosition tp : listP)
        {
            listPaths.add(tp);
        }
        return true;
    }
}
