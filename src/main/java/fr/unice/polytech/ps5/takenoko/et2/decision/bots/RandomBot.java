package fr.unice.polytech.ps5.takenoko.et2.decision.bots;

import fr.unice.polytech.ps5.takenoko.et2.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

    private static <T> T randomElement(T[] arr)
    {
        return arr[RNG.nextInt(arr.length)];
    }

    @Override
    public GameAction chooseAction(List<GameAction> base)
    {
        return randomElement(base);
    }

    @Override
    public Class<? extends Objective> chooseDeck()
    {
        return randomElement(List.of(PlotObjective.class));
    }

    @Override
    public LandTile chooseTile(List<LandTile> drawnTiles)
    {
        return randomElement(drawnTiles);
    }

    @Override
    public TilePosition chooseTilePosition(List<TilePosition> validPos, LandTile tile)
    {
        return randomElement(validPos);
    }

    @Override
    public Objective chooseObjectiveToComplete(List<Objective> validObjectives)
    {
        return randomElement(validObjectives);
    }
}
