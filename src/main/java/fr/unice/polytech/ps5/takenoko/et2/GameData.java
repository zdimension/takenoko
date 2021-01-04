package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTileImprovement;
import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import fr.unice.polytech.ps5.takenoko.et2.objective.GardenerObjective;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PandaObjective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class GameData
{
    /**
     * Deck of LandTile avaiable and unplaced.
     */
    public final List<LandTile> tileDeck;
    /**
     * All three decks of objectives.
     */
    public final Map<Class<? extends Objective>, List<? extends Objective>> objectiveDecks;
    /**
     * LandTileImprovement stock.
     */
    public final List<LandTileImprovement> chipReserve;

    public GameData()
    {
        this(getStandardLandTiles(), getStandardObjectives(), getStandardImprovements());
    }

    private GameData(List<LandTile> tileDeck, Map<Class<? extends Objective>, List<? extends Objective>> objectiveDecks, List<LandTileImprovement> chipReserve)
    {
        this.tileDeck = tileDeck;
        this.objectiveDecks = objectiveDecks;
        this.chipReserve = chipReserve;
    }

    /**
     * @return a list containing the 27 standard Takenoko land tiles (11 green, 9 yellow and 7 pink)
     */
    public static List<LandTile> getStandardLandTiles()
    {
        return Stream.of(
            Stream.generate(() -> new LandTile(Color.GREEN)).limit(6),
            Stream.generate(() -> new LandTile(Color.GREEN, LandTileImprovement.WATERSHED)).limit(2),
            Stream.generate(() -> new LandTile(Color.GREEN, LandTileImprovement.ENCLOSURE)).limit(2),
            Stream.generate(() -> new LandTile(Color.GREEN, LandTileImprovement.FERTILIZER)).limit(1),

            Stream.generate(() -> new LandTile(Color.YELLOW)).limit(6),
            Stream.generate(() -> new LandTile(Color.YELLOW, LandTileImprovement.WATERSHED)).limit(1),
            Stream.generate(() -> new LandTile(Color.YELLOW, LandTileImprovement.ENCLOSURE)).limit(1),
            Stream.generate(() -> new LandTile(Color.YELLOW, LandTileImprovement.FERTILIZER)).limit(1),

            Stream.generate(() -> new LandTile(Color.PINK)).limit(4),
            Stream.generate(() -> new LandTile(Color.PINK, LandTileImprovement.WATERSHED)).limit(1),
            Stream.generate(() -> new LandTile(Color.PINK, LandTileImprovement.ENCLOSURE)).limit(1),
            Stream.generate(() -> new LandTile(Color.PINK, LandTileImprovement.FERTILIZER)).limit(1)
        ).reduce(Stream::concat).get().collect(Collectors.toList());
    }

    /**
     * @return a list containing the 9 standard improvement chips (3 of each kind)
     */
    public static List<LandTileImprovement> getStandardImprovements()
    {
        return Stream.of(
            Collections.nCopies(3, LandTileImprovement.WATERSHED),
            Collections.nCopies(3, LandTileImprovement.FERTILIZER),
            Collections.nCopies(3, LandTileImprovement.ENCLOSURE)
        ).flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * @return a list containing the 15 standard Takenoko plot objectives
     */
    private static ArrayList<PlotObjective> getStandardPlotObjectives()
    {
        var objectives = new ArrayList<PlotObjective>();
        for (var x : Map.of(Color.GREEN, 2, Color.YELLOW, 3, Color.PINK, 4).entrySet())
        {
            var color = x.getKey();
            var score = x.getValue();
            objectives.add(new PlotObjective(score, Collections.nCopies(3, color), List.of(0, 2)));
            objectives.add(new PlotObjective(score, Collections.nCopies(3, color), List.of(5, 0)));
            objectives.add(new PlotObjective(score, Collections.nCopies(3, color), List.of(5, 5)));
            objectives.add(new PlotObjective(score + 1, Collections.nCopies(4, color), List.of(0, 2, 3)));
        }
        objectives.add(new PlotObjective(3, List.of(Color.GREEN, Color.GREEN, Color.YELLOW, Color.YELLOW), List.of(2, 3, 5)));
        objectives.add(new PlotObjective(4, List.of(Color.GREEN, Color.GREEN, Color.PINK, Color.PINK), List.of(2, 3, 5)));
        objectives.add(new PlotObjective(5, List.of(Color.PINK, Color.PINK, Color.YELLOW, Color.YELLOW), List.of(2, 3, 5)));
        return objectives;
    }

    private static ArrayList<PandaObjective> getStandardPandaObjectives()
    {
        var res = new ArrayList<PandaObjective>();
        for (var i = 0; i < 5; i++)
            res.add(new PandaObjective(3, Map.of(Color.GREEN, 2)));
        for (var i = 0; i < 4; i++)
            res.add(new PandaObjective(4, Map.of(Color.YELLOW, 2)));
        for (var i = 0; i < 3; i++)
            res.add(new PandaObjective(5, Map.of(Color.PINK, 2)));
        for (var i = 0; i < 3; i++)
            res.add(new PandaObjective(6, Map.of(Color.GREEN, 1, Color.YELLOW, 1, Color.PINK, 1)));
        return res;
    }

    private static ArrayList<GardenerObjective> getStandardGardenerObjectives()
    {
        var res = new ArrayList<GardenerObjective>();

        for (var x : Map.of(Color.GREEN, 3, Color.YELLOW, 4, Color.PINK, 5).entrySet())
        {
            var col = x.getKey();
            var score = x.getValue();
            res.add(new GardenerObjective(score, col, 1, 4, LandTileImprovement.FERTILIZER));
            res.add(new GardenerObjective(score + 1, col, 1, 4, LandTileImprovement.WATERSHED));
            res.add(new GardenerObjective(score + 1, col, 1, 4, LandTileImprovement.ENCLOSURE));
            res.add(new GardenerObjective(score + 2, col, 1, 4));
            res.add(new GardenerObjective(11 - score, col, 7 - score, 3));
        }

        return res;
    }

    public static Map<Class<? extends Objective>, List<? extends Objective>> getStandardObjectives()
    {
        return Map.of(
            PlotObjective.class, getStandardPlotObjectives(),
            PandaObjective.class, getStandardPandaObjectives(),
            GardenerObjective.class, getStandardGardenerObjectives()
        );
    }
}
