package fr.unice.polytech.ps5.takenoko.et2.decision.bots;

import fr.unice.polytech.ps5.takenoko.et2.board.*;
import fr.unice.polytech.ps5.takenoko.et2.commandline.Bot;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import fr.unice.polytech.ps5.takenoko.et2.enums.Weather;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Player;
import fr.unice.polytech.ps5.takenoko.et2.objective.GardenerObjective;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PandaObjective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;
import fr.unice.polytech.ps5.takenoko.et2.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

@Bot(key = "minmax")
public class MinMaxBot extends DecisionMaker
{
    public int getDepth()
    {
        return depth;
    }

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
    private MinMaxBot(Player player, int depth)
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

        if (base.contains(GameAction.DRAW_OBJECTIVE) && player.getHand().size() < 10)
        {
            return GameAction.DRAW_OBJECTIVE;
        }

        Map<GameAction, Integer> actionsWithPlayerObjectives = new HashMap<>();
        if (base.contains(GameAction.MOVE_GARDENER))
        {
            actionsWithPlayerObjectives.put(GameAction.MOVE_GARDENER, (int) player.getHand().stream().filter(GardenerObjective.class::isInstance).count());
        }
        if (base.contains(GameAction.MOVE_PANDA))
        {
            actionsWithPlayerObjectives.put(GameAction.MOVE_PANDA, (int) player.getHand().stream().filter(PandaObjective.class::isInstance).count());
        }
        if (base.contains(GameAction.PLACE_IRRIGATION))
        {
            actionsWithPlayerObjectives.put(GameAction.PLACE_IRRIGATION, (int) player.getHand().stream().filter(PlotObjective.class::isInstance).count());
        }
        Map.Entry<GameAction, Integer> bestActionGardenerPandaIrrigation = null;
        for (Map.Entry<GameAction, Integer> entry : actionsWithPlayerObjectives.entrySet())
        {
            if (bestActionGardenerPandaIrrigation == null || entry.getValue() > bestActionGardenerPandaIrrigation.getValue())
            {
                bestActionGardenerPandaIrrigation = entry;
            }
        }
        if (bestActionGardenerPandaIrrigation != null && bestActionGardenerPandaIrrigation.getValue() > 3) /// TODO : check if objectives are possible
        {
            return bestActionGardenerPandaIrrigation.getKey();
        }

        if (base.contains(GameAction.PICK_IRRIGATION) && player.getNbIrrigationsInStock() < depth)
        {
            return GameAction.PICK_IRRIGATION;
        }
        if (base.contains(GameAction.PLACE_IRRIGATION) && (getMaxEdgeChangePoints() > 0 || (float) getBoard().getBambooableTiles().size() / (float) getBoard().getTiles().size() < 0.75))
        {
            return GameAction.PLACE_IRRIGATION;
        }
        if (base.contains(GameAction.DRAW_TILE) && player.getGame().getRandom().nextInt() % 2 == 0) // TODO : check objectives
        {
            return GameAction.DRAW_TILE;
        }

        if (base.contains(GameAction.PLACE_IMPROVEMENT) && player.getGame().getRandom().nextInt() % 2 == 0)
        {
            return GameAction.PLACE_IMPROVEMENT;
        }
        return randomElement(base);// TODO (improve ?)
    }

    @Override
    public Class<? extends Objective> chooseDeck(List<Class<? extends Objective>> available)
    {
        if (available.contains(PlotObjective.class) && getBoard().getLandTiles().size() > 20)
        {
            return PlotObjective.class;
        }
        if (available.contains(GardenerObjective.class) && getBoard().getLandTiles().stream().filter(l -> (l.getBambooSize() > 0)).count() > 10)
        {
            return GardenerObjective.class;
        }
        if (available.contains(PandaObjective.class) && player.getBambooSum() > 5)
        {
            return PandaObjective.class;
        }
        return randomElement(available); // return Random if can't choose
    }

    @Override
    public Pair<LandTile, TilePosition> chooseTile(List<LandTile> drawnTiles, List<TilePosition> validPos)
    {
        var valid = player.getGame().getBoard().getValidEmptyPositions().collect(Collectors.toList());
        return drawnTiles.stream().flatMap(landTile ->
            validPos.stream().map(position ->
                Map.entry(evaluatePlotAction(landTile, position, drawnTiles, valid, player.getGame().getBoard(), player.getHand(), depth, true),
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
            try(var ignored = edge.setTemporaryIrrigationState(true))
            {
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
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return maxPts;
    }

    @Override
    public Edge chooseIrrigationPosition(List<Edge> irrigableEdges) /// TODO: must be improved
    {
        Board b = (Board) getBoard().clone();
        List<GardenerObjective> listGardenerObjectives = player.getHand().stream().filter(GardenerObjective.class::isInstance).map(o -> (GardenerObjective) o).collect(Collectors.toList());
        List<PlotObjective> listPlotObjectives = player.getHand().stream().filter(PlotObjective.class::isInstance).map(o -> (PlotObjective) o).collect(Collectors.toList());
        Edge bestEdge = null;
        int max = 0;
        for (Edge edge : irrigableEdges)
        {
            Tile realTile1 = edge.getTile(0);
            if (realTile1 == null)
            {
                throw new IllegalArgumentException("Get Tile from Edge: pb");
            }
            int numEdge = edge.getPositionFromTile(realTile1);
            TilePosition tilePosition1 = realTile1.getPosition().get();
            if (tilePosition1 == null)
            {
                throw new IllegalArgumentException("getPosition(): pb");
            }
            Tile newTile1 = b.findTile(tilePosition1);
            if (newTile1 == null)
            {
                throw new IllegalArgumentException("findTile: pb");
            }
            Edge newEdge = newTile1.getEdge(numEdge);
            if (newEdge == null)
            {
                throw new IllegalArgumentException("getEdge: pb");
            }
            /*if(!newEdge.canBeIrrigated())
            {
                throw new IllegalArgumentException("newEdge.canBeIrrigated(): pb: "+newEdge.irrigated);
            }*/
            int valueEdge = evaluateIrrigationPosition(newEdge, b, listGardenerObjectives, listPlotObjectives);
            if (valueEdge > max)
            {
                max = valueEdge;
                bestEdge = edge;
            }
        }
        if (bestEdge != null)
        {
            return bestEdge;
        }
        return randomElement(irrigableEdges);
    }

    private int evaluateIrrigationPosition(Edge edge, Board b, List<GardenerObjective> listGardenerObjectives, List<PlotObjective> listPlotObjectives)
    {
        if (!edge.canBeIrrigated())
        {
            return 0;
        }
        LandTile[] landTilesIrrigated = edge.addIrrigation();
        for (LandTile landTile : landTilesIrrigated)
        {
            if (landTile != null)
            {
                landTile.growBambooSection();
            }
        }
        int max = 0;
        for (GardenerObjective gardenerObjective : listGardenerObjectives)
        {
            if (gardenerObjective.checkValidated(b) && gardenerObjective.getPoints() > max)
            {
                max = gardenerObjective.getPoints();
            }
        }
        for (PlotObjective plotObjective : listPlotObjectives)
        {
            if (plotObjective.checkValidated(b) && plotObjective.getPoints() > max)
            {
                max = plotObjective.getPoints();
            }
        }
        return max;
    }

    @Override
    public TilePosition chooseGardenerTarget(List<TilePosition> valid)
    {
        int maxPts = 0;
        TilePosition bestPosition = null;
        for (TilePosition tilePosition : valid)
        {
            Board b = (Board) getBoard().clone();
            Tile tile = b.findTile(tilePosition);
            if (!(tile instanceof LandTile))
            {
                continue;
            }
            LandTile landTile = (LandTile) tile;
            landTile.growBambooSection();
            for (Objective objective : player.getHand())
            {
                if (!(objective instanceof GardenerObjective))
                {
                    continue;
                }
                GardenerObjective gardenerObjective = (GardenerObjective) objective;
                if (gardenerObjective.checkValidated(b, player))
                {
                    if (gardenerObjective.getPoints() > maxPts)
                    {
                        bestPosition = tilePosition;
                        maxPts = gardenerObjective.getPoints();
                    }
                }
            }
        }
        if (bestPosition == null)
        {
            return randomElement(valid);
        }
        return bestPosition;
    }

    @Override
    public Weather chooseWeather(List<Weather> weatherList)
    {
        return randomElement(weatherList); //TODO
    }

    @Override
    public TilePosition choosePandaTarget(List<TilePosition> valid, boolean isStorm) // TODO : not recursive right now
    {
        List<PandaObjective> listPandaObjectives = player.getHand().stream().filter(PandaObjective.class::isInstance).map(o -> (PandaObjective) o).collect(Collectors.toList());
        Board b = (Board) getBoard().clone();
        HashMap<Color, Integer> playerReserve = new HashMap<Color, Integer>(player.getBambooSectionReserve());
        TilePosition bestPosition = null;
        int max = 0;
        for (TilePosition tilePosition : valid)
        {
            if (tilePosition == null)
            {
                continue;
            }
            int valuePos = evaluatePandaPosition(tilePosition, b, listPandaObjectives, playerReserve);
            if (valuePos > max)
            {
                max = valuePos;
                bestPosition = tilePosition;
            }
        }
        if (bestPosition != null)
        {
            return bestPosition;
        }
        return randomElement(valid); // null ?
    }

    private int evaluatePandaPosition(TilePosition tilePosition, Board b, List<PandaObjective> listPandaObjectives, HashMap<Color, Integer> playerReserve)
    {
        Tile tile = b.findTile(tilePosition);
        if (!(tile instanceof LandTile))
        {
            return 0;
        }
        LandTile landTile = (LandTile) tile;
        if (landTile.getBambooSize() == 0 || landTile.getLandTileImprovement() == LandTileImprovement.ENCLOSURE)
        {
            return 0;
        }
        playerReserve.replace(landTile.getColor(), playerReserve.get(landTile.getColor()) + 1);
        int max = 0;
        for (PandaObjective pandaObjective : listPandaObjectives)
        {
            if (pandaObjective.checkValidated(playerReserve) && pandaObjective.getPoints() > max)
            {
                max = pandaObjective.getPoints();
            }
        }
        return max;
    }

    @Override
    public LandTile chooseTileToAddBamboo(List<LandTile> listIrrigatedTiles)  // TODO : not recursive right now
    {
        List<GardenerObjective> listGardenerObjectives = player.getHand().stream().filter(GardenerObjective.class::isInstance).map(o -> (GardenerObjective) o).collect(Collectors.toList());
        Board b = (Board) getBoard().clone();
        LandTile bestLandTile = null;
        int max = 0;
        for (LandTile landTile : listIrrigatedTiles)
        {
            Optional<TilePosition> tilePosition = landTile.getPosition();
            if (tilePosition.isEmpty())
            {
                continue;
            }
            Tile newTile = b.findTile(tilePosition.get());
            if (!(newTile instanceof LandTile))
            {
                continue;
            }
            LandTile newLandTile = (LandTile) newTile;
            if (!newLandTile.canGrowBamboo())
            {
                continue;
            }
            int valueL = evaluateGardenerPosition(listGardenerObjectives, b, newLandTile);
            if (valueL > max)
            {
                max = valueL;
                bestLandTile = landTile;
            }
        }
        if (bestLandTile != null)
        {
            return bestLandTile;
        }
        return randomElement(listIrrigatedTiles);
    }

    private int evaluateGardenerPosition(List<GardenerObjective> listGardenerObjectives, Board b, LandTile landTile)
    {
        landTile.growBambooSection();
        int max = 0;
        for (GardenerObjective gardenerObjective : listGardenerObjectives)
        {
            if (gardenerObjective.checkValidated(b) && gardenerObjective.getPoints() > max)
            {
                max = gardenerObjective.getPoints();
            }
        }
        return max;
    }

    @Override
    public LandTileImprovement chooseLandTileImprovement(List<LandTileImprovement> listLandTileImprovements) // TODO: not recursive right now
    {
        List<LandTileImprovement> newListImprovements = new ArrayList<>();
        for (LandTileImprovement landTileImprovement : listLandTileImprovements)
        {
            if (!newListImprovements.contains(landTileImprovement))
            {
                newListImprovements.add(landTileImprovement);
            }
        }
        List<Objective> listObjectives = player.getHand();
        int max = 0;
        LandTileImprovement bestImprovement = null;
        for (LandTileImprovement landTileImprovement : newListImprovements)
        {
            Board b = (Board) getBoard().clone();
            int v = evaluateImprovement(landTileImprovement, b, listObjectives);
            if (v > max)
            {
                max = v;
                bestImprovement = landTileImprovement;
            }
        }
        if (bestImprovement == null)
        {
            return randomElement(listLandTileImprovements);
        }
        return bestImprovement;
    }

    @Override
    public Pair<LandTile, LandTileImprovement> chooseImprovementAndLandTile(List<LandTile> vacantLandTile, List<LandTileImprovement> availableImprovements) // TODO: not recursive right now
    {
        int max = 0;
        LandTile bestLandTile = null;
        LandTileImprovement bestImprovement = null;
        List<Objective> listObjectives = player.getHand();
        for (LandTile landTile : vacantLandTile)
        {
            TilePosition position = landTile.getPosition().get();
            if (position == null)
            {
                continue;
            }
            for (LandTileImprovement landTileImprovement : availableImprovements)
            {
                Board b = (Board) getBoard().clone();
                int v = evaluateImprovementAndLandTile(landTileImprovement, position, b, listObjectives);
                if (v > max)
                {
                    bestImprovement = landTileImprovement;
                    bestLandTile = landTile;
                    max = v;
                }
            }
        }
        if (bestImprovement == null || bestLandTile == null)
        {
            return Pair.of(randomElement(vacantLandTile), randomElement(availableImprovements));
        }
        return Pair.of(bestLandTile, bestImprovement);
    }

    private int evaluateImprovement(LandTileImprovement improvement, Board b, List<Objective> listObjectives)
    {
        int max = 0;
        for (Map.Entry<TilePosition, Tile> entry : b.getTiles().entrySet())
        {
            if (entry.getValue() instanceof LandTile)
            {
                LandTile landTile = (LandTile) entry.getValue();
                if (landTile.getLandTileImprovement() == null && landTile.getBambooSize() == 0)
                {
                    int v = evaluateImprovementAndLandTile(improvement, entry.getKey(), b, listObjectives);
                    if (v > max)
                    {
                        max = v;
                    }
                }
            }
        }
        return max;
    }

    private int evaluateImprovementAndLandTile(LandTileImprovement improvement, TilePosition tilePosition, Board b, List<Objective> listObjectives)
    {
        if (tilePosition == null)
        {
            return 0;
        }
        Tile tile = b.findTile(tilePosition);
        if (!(tile instanceof LandTile))
        {
            return 0;
        }
        LandTile landTile = (LandTile) tile;
        if (landTile.getBambooSize() != 0 || landTile.getLandTileImprovement() != null)
        {
            return 0;
        }
        landTile.setLandTileImprovement(improvement);
        int max = 0;
        for (Objective objective : listObjectives)
        {
            if (objective.checkValidated(b, player) && objective.getPoints() > max)
            {
                max = objective.getPoints();
            }
        }
        return max;
    }

    private int evaluatePlotAction(LandTile playedTile, TilePosition playedPos, List<LandTile> drawnTiles, List<TilePosition> ListValidsPositions, Board board, List<Objective> myObjectives, int n, boolean myTurn)
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
                        landTile -> evaluatePlotAction(landTile, tilePosition, copyOfDrawnTiles, newListValidsPositions, newBoard, copyOfMyObjectives, n - 1, !myTurn)
                    )).sum() * power;
        return scoreReturn;
    }

}
