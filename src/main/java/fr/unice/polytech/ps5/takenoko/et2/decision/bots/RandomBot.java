package fr.unice.polytech.ps5.takenoko.et2.decision.bots;

import fr.unice.polytech.ps5.takenoko.et2.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import fr.unice.polytech.ps5.takenoko.et2.Weather;
import fr.unice.polytech.ps5.takenoko.et2.board.Edge;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTileImprovement;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.util.Pair;

import java.util.List;
import java.util.Random;

/**
 * This bot chooses randomly among valid choices every time.
 */
public class RandomBot extends DecisionMaker
{
    private static final Random RNG = new Random();

    public RandomBot(Player player)
    {
        super(player);
    }

    private static <T> T randomElement(List<T> list)
    {
        return list.get(RNG.nextInt(list.size()));
    }

    @Override
    public GameAction chooseAction(List<GameAction> base)
    {
        return randomElement(base);
    }

    @Override
    public Class<? extends Objective> chooseDeck(List<Class<? extends Objective>> available)
    {
        return randomElement(available);
    }

    @Override
    public Pair<LandTile, TilePosition> chooseTile(List<LandTile> drawnTiles, List<TilePosition> validPos)
    {
        return Pair.of(randomElement(drawnTiles), randomElement(validPos));
    }

    @Override
    public Objective chooseObjectiveToComplete(List<Objective> validObjectives)
    {
        return randomElement(validObjectives);
    }

    @Override
    public Edge chooseIrrigationPosition(List<Edge> irrigableEdges)
    {
        return randomElement(irrigableEdges);
    }

    @Override
    public TilePosition chooseGardenerTarget(List<TilePosition> valid)
    {
        return randomElement(valid);
    }

    @Override
    public Weather chooseWeather(List<Weather> weatherList)
    {
        return randomElement(weatherList);
    }

    @Override
    public LandTile chooseTileToAddBamboo(List<LandTile> listIrrigatedTiles)
    {
        return randomElement(listIrrigatedTiles);
    }

    @Override
    public LandTileImprovement chooseLandTileImprovement(List<LandTileImprovement> listLandTileImprovements)
    {
        return randomElement(listLandTileImprovements);
    }
}
