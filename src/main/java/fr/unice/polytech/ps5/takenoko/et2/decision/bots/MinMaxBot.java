package fr.unice.polytech.ps5.takenoko.et2.decision.bots;

import fr.unice.polytech.ps5.takenoko.et2.BambooSection;
import fr.unice.polytech.ps5.takenoko.et2.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.Edge;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;
import fr.unice.polytech.ps5.takenoko.et2.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

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
        if (base.contains(GameAction.DRAW_OBJECTIVE)/* && player.getHand().size() < 6*/)
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
    public Pair<LandTile, TilePosition> chooseTile(List<LandTile> drawnTiles, List<TilePosition> validPos)
    {
        for (LandTile landTile : drawnTiles)
        {
            for (TilePosition position : validPos)
            {
                Board b2 = (Board) player.getGame().getBoard().clone();
                List<BambooSection> listBambooReserv = new ArrayList<>();
                for (BambooSection bambooSection : player.getGame().getBambooReserve())
                {
                    listBambooReserv.add(new BambooSection(bambooSection.getColor()));
                }
                b2.addTile((LandTile) landTile.clone(), position, listBambooReserv);
                for (Objective objective : player.getHand())
                {
                    if (objective instanceof PlotObjective)
                    {
                        PlotObjective plotObjective = (PlotObjective) objective;
                        if (plotObjective.checkValidated(b2))
                        {
                            return Pair.of(landTile, position);
                        }
                    }
                }
            }
        }
        return Pair.of(drawnTiles.get(0), validPos.get(0));
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
