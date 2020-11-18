package fr.unice.polytech.ps5.takenoko.et2.decision.bots;

import fr.unice.polytech.ps5.takenoko.et2.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.Edge;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class MinMaxBot extends DecisionMaker
{
    /**
     * Class constructor
     *
     * @param player The player for the Bot
     */
    public MinMaxBot(Player player)
    {
        super(player);
    }

    @Override
    public GameAction chooseAction(List<GameAction> base)
    {
        if (base.contains(GameAction.COMPLETE_OBJECTIVE))
        {
            return GameAction.COMPLETE_OBJECTIVE;
        }
        if (base.contains(GameAction.DRAW_OBJECTIVE) && player.getHand().size() < 6)
        {
            return GameAction.DRAW_OBJECTIVE;
        }
        if (base.contains(GameAction.DRAW_TILE))
        {
            return GameAction.DRAW_TILE;
        }
        return base.get(0);
    }

    @Override
    public Class<? extends Objective> chooseDeck(List<Class<? extends Objective>> available)
    {
        try
        {
            @SuppressWarnings("unchecked")
            Class<? extends Objective> objectiveClass = (Class<? extends Objective>) Class.forName("fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective");
            if (available.contains(objectiveClass))
            {
                return objectiveClass;
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return available.get(0);
    }

    @Override
    public LandTile chooseTile(List<LandTile> drawnTiles)
    {
        for (LandTile landTile : drawnTiles)
        {
            List<TilePosition> validPositionsForTile = player.getGame().getBoard().getValidEmptyPositions().collect(Collectors.toList());
            for (TilePosition position : validPositionsForTile)
            {
                Board b2 = (Board) player.getGame().getBoard().clone();
                b2.addTile(landTile, position);
                for (Objective objective : player.getHand())
                {
                    if (objective instanceof PlotObjective)
                    {
                        PlotObjective plotObjective = (PlotObjective) objective;
                        if (plotObjective.checkValidated(b2))
                        {
                            return landTile;
                        }
                    }
                }
            }
        }
        return drawnTiles.get(0);
    }

    @Override
    public TilePosition chooseTilePosition(List<TilePosition> validPos, LandTile tile)
    {
        for (TilePosition position : validPos)
        {
            Board b2 = (Board) player.getGame().getBoard().clone();
            b2.addTile(tile, position);
            for (Objective objective : player.getHand())
            {
                if (objective instanceof PlotObjective)
                {
                    PlotObjective plotObjective = (PlotObjective) objective;
                    if (plotObjective.checkValidated(b2))
                    {
                        return position;
                    }
                }
            }
        }
        return validPos.get(0);
    }

    @Override
    public Objective chooseObjectiveToComplete(List<Objective> validObjectives)
    {
        return validObjectives.stream().max(Comparator.comparing(Objective::getPoints)).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Edge chooseIrrigationPosition(List<Edge> irrigableEdges)
    {
        return irrigableEdges.get(0);
    }

    @Override
    public TilePosition chooseGardenerTarget(List<TilePosition> valid)
    {
        return valid.get(0);
    }
}
