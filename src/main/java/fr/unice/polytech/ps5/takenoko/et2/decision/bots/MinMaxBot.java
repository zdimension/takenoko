package fr.unice.polytech.ps5.takenoko.et2.decision.bots;

import fr.unice.polytech.ps5.takenoko.et2.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import fr.unice.polytech.ps5.takenoko.et2.board.Edge;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;

import java.util.List;

public class MinMaxBot extends DecisionMaker
{
    /**
     * Class constructor
     *
     * @param player
     */
    public MinMaxBot(Player player)
    {
        super(player);
        Player p2 = (Player) player.clone();
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
            if (available.contains((Class<? extends Objective>) Class.forName("fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective")))
            {
                return (Class<? extends Objective>) Class.forName("fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective");
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
        return drawnTiles.get(0);
    }

    @Override
    public TilePosition chooseTilePosition(List<TilePosition> validPos, LandTile tile)
    {
        return validPos.get(0);
    }

    @Override
    public Objective chooseObjectiveToComplete(List<Objective> validObjectives)
    {
        return validObjectives.get(0);
    }

    @Override
    public Edge chooseIrrigationPosition(List<Edge> irrigableEdges)
    {
        return irrigableEdges.get(0);
    }
}
