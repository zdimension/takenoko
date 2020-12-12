package fr.unice.polytech.ps5.takenoko.et2.decision.bots;

import fr.unice.polytech.ps5.takenoko.et2.board.Edge;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTileImprovement;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.commandline.Bot;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.enums.Weather;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Player;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.util.Pair;

import java.util.List;

/**
 * This bot chooses randomly among valid choices every time.
 */
@Bot(key = "random")
public class RandomBot extends DecisionMaker
{
    public RandomBot(Player player)
    {
        super(player);
    }

    public static DecisionMakerBuilder getBuilder()
    {
        return RandomBot::new;
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
    public TilePosition choosePandaTarget(List<TilePosition> valid)
    {
        return randomElement(valid);
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

    @Override
    public Pair<LandTile, LandTileImprovement> chooseImprovementAndLandTile(List<LandTile> vacantLandTile, List<LandTileImprovement> availableImprovements)
    {
        return Pair.of(randomElement(vacantLandTile), randomElement(availableImprovements));
    }
}
