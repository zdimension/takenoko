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
import fr.unice.polytech.ps5.takenoko.et2.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class MinMaxBot extends DecisionMaker
{
    /**
     * Depth of the min-max algorithm: the higher is the slower and the stronger
     */
    static final int DEPTH = 2;

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
        if (available.contains(PlotObjective.class))
        {
            return PlotObjective.class;
        }
        return available.get(0);
    }

    @Override
    public Pair<LandTile, TilePosition> chooseTile(List<LandTile> drawnTiles, List<TilePosition> validPos)
    {
        Pair<LandTile, TilePosition> pairMaxPoints = null;
        int maxPoints = 0;
        for (LandTile landTile : drawnTiles)
        {
            for (TilePosition position : validPos)
            {
                /*Board b2 = (Board) player.getGame().getBoard().clone();
                List<BambooSection> listBambooReserv = cloneBambooReserv(player.getGame().getBambooReserve());
                b2.addTile((LandTile) landTile.clone(), position, listBambooReserv);
                for (Objective objective : player.getHand())
                {
                    if (objective instanceof PlotObjective)
                    {
                        PlotObjective plotObjective = (PlotObjective) objective;
                        if (plotObjective.checkValidated(b2))
                        {
                            if (plotObjective.getPoints() > maxPoints)
                            {
                                maxPoints = plotObjective.getPoints();
                                pairMaxPoints = Pair.of(landTile, position);
                            }
                        }
                    }
                }*/
                int actionEvaluated = evaluteAction(landTile, position, drawnTiles, player.getGame().getBoard().getValidEmptyPositions().collect(Collectors.toList()), player.getGame().getBoard(), player.getHand(), DEPTH, true);
                if (actionEvaluated > maxPoints)
                {
                    maxPoints = actionEvaluated;
                    pairMaxPoints = Pair.of(landTile, position);
                }
            }
        }
        if (pairMaxPoints != null)
        {
            return pairMaxPoints;
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

    private int evaluteAction(LandTile playedTile, TilePosition playedPos, List<LandTile> drawnTiles, List<TilePosition> ListValidsPositions, Board board, List<Objective> myObjectives, int n, boolean myTurn)
    {
        List<Objective> copyOfMyObjectives = new ArrayList<>();
        copyOfMyObjectives.addAll(myObjectives);
        List<LandTile> copyOfDrawnTiles = new ArrayList<>();
        copyOfDrawnTiles.addAll(drawnTiles);
        for (int i = 0; i < copyOfDrawnTiles.size(); i++)
        {
            if (copyOfDrawnTiles.get(i).equals(playedTile))
            {
                copyOfDrawnTiles.remove(i);
                break;
            }
        }
        long power = (int) Math.pow(Double.valueOf(100), Double.valueOf(n));
        Board newBoard = (Board) board.clone();
        newBoard.addTile(playedTile, playedPos, cloneBambooReserv(player.getGame().getBambooReserve()));
        int scoreReturn = 0;
        if (myTurn)
        {
            int max = 0;
            int maxI = -1;
            for (int i = 0; i < copyOfMyObjectives.size(); i++)
            {
                if (!(copyOfMyObjectives.get(i) instanceof PlotObjective))
                {
                    continue;
                }
                PlotObjective plotObjective = (PlotObjective) copyOfMyObjectives.get(i);
                if (plotObjective.checkValidated(newBoard))
                {
                    if (max < plotObjective.getPoints())
                    {
                        max = plotObjective.getPoints();
                        maxI = i;
                    }
                }
            }
            if (maxI != -1)
            {
                copyOfMyObjectives.remove(maxI);
            }
            scoreReturn += max * power;
        }
        if (n == 0)
        {
            return scoreReturn;
        }
        power = power / 100;
        List<TilePosition> newListValidsPositions = new ArrayList<>();
        for (TilePosition tilePosition : ListValidsPositions)
        {
            if (tilePosition.equals(playedPos))
            {
                continue;
            }
            newListValidsPositions.add(tilePosition);
        }
        for (TilePosition tilePosition : newListValidsPositions)
        {
            for (LandTile landTile : copyOfDrawnTiles)
            {
                scoreReturn -= evaluteAction(landTile, tilePosition, copyOfDrawnTiles, newListValidsPositions, newBoard, copyOfMyObjectives, n - 1, !myTurn) * power;
            }
        }
        return scoreReturn;
    }

}
