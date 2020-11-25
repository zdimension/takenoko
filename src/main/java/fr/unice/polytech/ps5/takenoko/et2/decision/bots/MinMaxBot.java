package fr.unice.polytech.ps5.takenoko.et2.decision.bots;

import fr.unice.polytech.ps5.takenoko.et2.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.Edge;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;
import fr.unice.polytech.ps5.takenoko.et2.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class MinMaxBot extends DecisionMaker
{
    /**
     * Depth of the min-max algorithm: the higher is the slower and the stronger
     */
    private final int depth;

    /**
     * Class constructor
     *
     * @param player The player for the Bot
     */
    public MinMaxBot(Player player, int depth)
    {
        super(player);
        this.depth = depth;
    }

    public static DecisionMakerBuilder getBuilder(int depth)
    {
        return p -> new MinMaxBot(p, depth);
    }

    @Override
    public GameAction chooseAction(List<GameAction> base)
    {
        if (base.contains(GameAction.COMPLETE_OBJECTIVE))
        {
            return GameAction.COMPLETE_OBJECTIVE;
        }
        if (base.contains(GameAction.PLACE_IRRIGATION))
        {
            return GameAction.PLACE_IRRIGATION;
        }
        if (base.contains(GameAction.DRAW_OBJECTIVE))
        {
            return GameAction.DRAW_OBJECTIVE;
        }
        if (base.contains(GameAction.PICK_IRRIGATION) && player.getNbIrrigationsInStock() < 3)
        {
            return GameAction.PICK_IRRIGATION;
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
                int actionEvaluated = evaluateAction(landTile, position, drawnTiles, player.getGame().getBoard().getValidEmptyPositions().collect(Collectors.toList()), player.getGame().getBoard(), player.getHand(), depth, true);
                if (actionEvaluated > maxPoints)
                {
                    maxPoints = actionEvaluated;
                    pairMaxPoints = Pair.of(landTile, position);
                }
            }
        }

        return Objects.requireNonNullElseGet(pairMaxPoints, () -> Pair.of(drawnTiles.get(0), validPos.get(0)));
    }

    @Override
    public Objective chooseObjectiveToComplete(List<Objective> validObjectives)
    {
        return validObjectives.stream().max(Comparator.comparing(Objective::getPoints)).orElseThrow(NoSuchElementException::new);
    }

    //private

    @Override
    public Edge chooseIrrigationPosition(List<Edge> irrigableEdges)
    {
        for (Edge edge : irrigableEdges)
        {
            edge.irrigated = true;
            for (Objective objective : player.getHand())
            {
                if (objective.checkValidated(getBoard()))
                {
                    edge.irrigated = false;
                    return edge;
                }
            }
            edge.irrigated = false;
        }
        return irrigableEdges.get(0);
    }

    @Override
    public TilePosition chooseGardenerTarget(List<TilePosition> valid)
    {
        return valid.get(0);
    }

    private int evaluateAction(LandTile playedTile, TilePosition playedPos, List<LandTile> drawnTiles, List<TilePosition> ListValidsPositions, Board board, List<Objective> myObjectives, int n, boolean myTurn)
    {
        List<Objective> copyOfMyObjectives = new ArrayList<>(myObjectives);
        List<LandTile> copyOfDrawnTiles = new ArrayList<>(drawnTiles);
        for (int i = 0; i < copyOfDrawnTiles.size(); i++)
        {
            if (copyOfDrawnTiles.get(i).equals(playedTile))
            {
                copyOfDrawnTiles.remove(i);
                break;
            }
        }
        long power = (int) Math.pow(100d, n);
        Board newBoard = (Board) board.clone();
        newBoard.addTile(playedTile, playedPos);
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
                scoreReturn -= evaluateAction(landTile, tilePosition, copyOfDrawnTiles, newListValidsPositions, newBoard, copyOfMyObjectives, n - 1, !myTurn) * power;
            }
        }
        return scoreReturn;
    }

}
