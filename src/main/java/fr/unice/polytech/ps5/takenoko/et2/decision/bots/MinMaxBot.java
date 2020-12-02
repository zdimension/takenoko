package fr.unice.polytech.ps5.takenoko.et2.decision.bots;

import fr.unice.polytech.ps5.takenoko.et2.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import fr.unice.polytech.ps5.takenoko.et2.Weather;
import fr.unice.polytech.ps5.takenoko.et2.board.*;
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

    private Edge maxEdge = null;

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
        if (base.contains(GameAction.PLACE_IRRIGATION) && getMaxEdgeChangePoints() > 0)
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
        var valid = player.getGame().getBoard().getValidEmptyPositions().collect(Collectors.toList());
        return drawnTiles.stream().flatMap(landTile ->
            validPos.stream().map(position ->
                Map.entry(evaluateAction(landTile, position, drawnTiles, valid, player.getGame().getBoard(), player.getHand(), depth, true),
                    Pair.of(landTile, position))
            )).max(Map.Entry.comparingByKey()).map(Map.Entry::getValue).orElse(Pair.of(drawnTiles.get(0), validPos.get(0)));
    }

    @Override
    public Objective chooseObjectiveToComplete(List<Objective> validObjectives)
    {
        return validObjectives.stream().max(Comparator.comparing(Objective::getPoints)).orElseThrow(NoSuchElementException::new);
    }

    private int getMaxEdgeChangePoints()
    {
        int maxPts = 0;
        for (Edge edge : player.getGame().findIrrigableEdges().collect(Collectors.toUnmodifiableList()))
        {
            edge.irrigated = true;
            for (Objective objective : player.getHand())
            {
                if (objective.checkValidated(getBoard(), player))
                {
                    maxEdge = edge;
                    if (objective.getPoints() > maxPts)
                    {
                        maxPts = objective.getPoints();
                    }
                }
            }
            edge.irrigated = false;
        }
        return maxPts;
    }

    @Override
    public Edge chooseIrrigationPosition(List<Edge> irrigableEdges)
    {
        if (maxEdge == null || !irrigableEdges.contains(maxEdge))
        {
            maxEdge = null;
            return irrigableEdges.get(0);
        }
        Edge edgeTmp = maxEdge;
        maxEdge = null;
        return edgeTmp;
    }

    @Override
    public TilePosition chooseGardenerTarget(List<TilePosition> valid)
    {
        return valid.get(0);
    }

    @Override
    public Weather chooseWeather(List<Weather> weatherList)
    {
        return Weather.SUN; //TODO
    }

    @Override
    public TilePosition choosePandaTarget(List<TilePosition> valid)
    {
        return valid.get(0);
    }

    @Override
    public LandTile chooseTileToAddBamboo(List<LandTile> listIrrigatedTiles)
    {
        return null; //TODO
    }

    @Override
    public LandTileImprovement chooseLandTileImprovement(List<LandTileImprovement> listLandTileImprovements)
    {
        return listLandTileImprovements.get(0); // TODO
    }

    @Override
    public Pair<LandTile, LandTileImprovement> chooseImprovementAndLandTile(List<LandTile> vacantLandTile, List<LandTileImprovement> availableImprovements)
    {
        return Pair.of(vacantLandTile.get(0), availableImprovements.get(0)); // TODO
    }

    private int evaluateAction(LandTile playedTile, TilePosition playedPos, List<LandTile> drawnTiles, List<TilePosition> ListValidsPositions, Board board, List<Objective> myObjectives, int n, boolean myTurn)
    {
        List<Objective> copyOfMyObjectives = new ArrayList<>(myObjectives);
        List<LandTile> copyOfDrawnTiles = new ArrayList<>(drawnTiles);
        for (int i = 0; i < copyOfDrawnTiles.size(); i++)
        {
            if (copyOfDrawnTiles.get(i) == playedTile)
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
                if (plotObjective.checkValidated(newBoard, player))
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
        scoreReturn -= newListValidsPositions
            .stream()
            .flatMapToInt(tilePosition ->
                copyOfDrawnTiles.stream()
                    .mapToInt(
                        landTile -> evaluateAction(landTile, tilePosition, copyOfDrawnTiles, newListValidsPositions, newBoard, copyOfMyObjectives, n - 1, !myTurn)
                    )).sum() * power;
        return scoreReturn;
    }

}
