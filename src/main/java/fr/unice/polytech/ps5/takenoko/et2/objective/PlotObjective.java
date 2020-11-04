package fr.unice.polytech.ps5.takenoko.et2.objective;

import fr.unice.polytech.ps5.takenoko.et2.Board;
import fr.unice.polytech.ps5.takenoko.et2.Color;
import fr.unice.polytech.ps5.takenoko.et2.TilePosition;

import java.util.ArrayList;
import java.util.List;

public class PlotObjective extends Objective
{
    private PlotObjectivePath plotObjectivePath;

    public PlotObjective(int points)
    {
        super(points);
    }

    public boolean isValidated(Board board)
    {
        return false;
    }

    public void setPlotObjectivePath(PlotObjectivePath plotObjectivePath)
    {
        this.plotObjectivePath = plotObjectivePath;
    }

    public class PlotObjectivePath
    {
        private List<Color> listColors = new ArrayList<Color>();
        private List<TilePosition> listPaths = new ArrayList<>();

        public PlotObjectivePath(List<Color> listC, List<TilePosition> listP) throws Exception
        {
            if (listC.size() == 0)
            {
                throw new Exception("At least 1 color");
            }
            for (Color color : listC)
            {
                listColors.add(color);
            }
            for (TilePosition tp : listP)
            {
                listPaths.add(tp);
            }
        }
    }
}
