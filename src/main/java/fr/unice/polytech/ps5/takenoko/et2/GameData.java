package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class GameData
{
    /**
     * @return a list containing the 27 standard Takenoko land tiles (11 green, 9 yellow and 7 pink)
     */
    public static List<LandTile> getStandardLandTiles()
    {
        return getLandTiles(11, 9, 7);
    }

    public static List<LandTile> getLandTiles(int green, int yellow, int pink)
    {
        return Stream.of(
            Stream.generate(() -> new LandTile(Color.GREEN)).limit(green),
            Stream.generate(() -> new LandTile(Color.YELLOW)).limit(yellow),
            Stream.generate(() -> new LandTile(Color.PINK)).limit(pink)
        ).reduce(Stream::concat).get().collect(Collectors.toList());
    }

    /**
     * @return a list containing the 15 standard Takenoko plot objectives
     */
    public static ArrayList<PlotObjective> getStandardObjectives()
    {
        var objectives = new ArrayList<PlotObjective>();
        for (var x : Map.of(Color.GREEN, 2, Color.YELLOW, 3, Color.PINK, 4).entrySet())
        {
            var color = x.getKey();
            var score = x.getValue();
            objectives.add(new PlotObjective(score, Collections.nCopies(3, color), List.of(0, 2)));
            objectives.add(new PlotObjective(score, Collections.nCopies(3, color), List.of(0, 2)));
            objectives.add(new PlotObjective(score, Collections.nCopies(3, color), List.of(0, 2)));
            objectives.add(new PlotObjective(score + 1, Collections.nCopies(4, color), List.of(0, 2, 3)));
        }
        objectives.add(new PlotObjective(3, List.of(Color.GREEN, Color.GREEN, Color.YELLOW, Color.YELLOW), List.of(2, 3, 5)));
        objectives.add(new PlotObjective(4, List.of(Color.GREEN, Color.GREEN, Color.PINK, Color.PINK), List.of(2, 3, 5)));
        objectives.add(new PlotObjective(5, List.of(Color.PINK, Color.PINK, Color.YELLOW, Color.YELLOW), List.of(2, 3, 5)));
        return objectives;
    }

    /**
     * @return a Game instance containing standard Takenoko game items
     * @see GameData#getStandardLandTiles()
     * @see GameData#getStandardObjectives()
     */
    public static Game getStandardGame()
    {
        return new Game(getStandardObjectives(), getStandardLandTiles());
    }
}
