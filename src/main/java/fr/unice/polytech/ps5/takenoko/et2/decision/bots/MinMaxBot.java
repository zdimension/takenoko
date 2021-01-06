package fr.unice.polytech.ps5.takenoko.et2.decision.bots;

import fr.unice.polytech.ps5.takenoko.et2.GameData;
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
    private int globalMax;
    private Map<GameAction, Object> actionsChosen = new HashMap<>();
    private GameAction lastActionChosen = null;

    /**
     * Class constructor
     *
     * @param player The player for the Bot
     */
    private MinMaxBot(Player player, int depth)
    {
        super(player);
        this.depth = depth;
        for (GameAction gameAction : GameAction.values())
        {
            actionsChosen.put(gameAction, null);
        }
    }

    public static DecisionMakerBuilder getBuilder(int depth)
    {
        return p -> new MinMaxBot(p, depth);
    }

    @Override
    public GameAction chooseAction(List<GameAction> base)
    {
        lastActionChosen = null;
        if (base.contains(GameAction.COMPLETE_OBJECTIVE))
        {
            return GameAction.COMPLETE_OBJECTIVE;
        }

        if (base.contains(GameAction.DRAW_OBJECTIVE))
        {
            return GameAction.DRAW_OBJECTIVE;
        }

        GameAction bestAction = null;
        int maxPtsAction = 0;
        List<GameAction> gameActionList = new ArrayList<>(Arrays.asList(GameAction.MOVE_GARDENER, GameAction.MOVE_PANDA, GameAction.PLACE_IRRIGATION, GameAction.DRAW_TILE, GameAction.PLACE_IMPROVEMENT));
        for (GameAction gameAction : gameActionList)
        {
            if (base.contains(gameAction))
            {
                int pts = getPointsForAction(gameAction);
                if (pts > maxPtsAction)
                {
                    maxPtsAction = pts;
                    bestAction = gameAction;
                }
            }
        }
        if (bestAction != null)
        {
            lastActionChosen = bestAction;
            return bestAction;
        }

        if (base.contains(GameAction.PICK_IRRIGATION)/* && player.getNbIrrigationsInStock() < depth*/)
        {
            return GameAction.PICK_IRRIGATION;
        }
        return randomElement(base);// TODO (improve ?)
    }

    private int getPointsForAction(GameAction action)
    {
        switch (action)
        {
            case MOVE_GARDENER:
                List<TilePosition> positionsGardener = player.getGame().getValidGardenerTargets().collect(Collectors.toUnmodifiableList());
                if (positionsGardener.size() < 1)
                {
                    return 0;
                }
                actionsChosen.replace(GameAction.MOVE_GARDENER, chooseGardenerTarget(positionsGardener));
                return globalMax;
            case MOVE_PANDA:
                List<TilePosition> positionsPanda = player.getGame().getValidPandaTargets().collect(Collectors.toUnmodifiableList());
                if (positionsPanda.size() < 1)
                {
                    return 0;
                }
                actionsChosen.replace(GameAction.MOVE_PANDA, choosePandaTarget(positionsPanda, false));
                return globalMax;
            case PLACE_IRRIGATION:
                List<Edge> positionsIrrigation = player.getGame().findIrrigableEdges().collect(Collectors.toUnmodifiableList());
                if (positionsIrrigation.size() < 1)
                {
                    return 0;
                }
                actionsChosen.replace(GameAction.PLACE_IRRIGATION, chooseIrrigationPosition(positionsIrrigation));
                return globalMax;
            case DRAW_TILE:
                GameData gameData = player.getGame().getGameData();
                var validTiles = Collections.unmodifiableList(gameData.tileDeck.subList(0, Math.min(gameData.tileDeck.size(), 3)));
                var validPos = getBoard().getValidEmptyPositions().collect(Collectors.toUnmodifiableList());
                if (validTiles.size() < 1 || validPos.size() < 1)
                {
                    return 0;
                }
                actionsChosen.replace(GameAction.DRAW_TILE, chooseTile(validTiles, validPos));
                return globalMax;
            case PLACE_IMPROVEMENT:
                var vacantLandTile = getBoard().getLandTilesWithoutImprovement().collect(Collectors.toUnmodifiableList());
                var availableImprovements = player.getChipReserve();
                if (vacantLandTile.size() < 1 || availableImprovements.size() < 1)
                {
                    return 0;
                }
                actionsChosen.replace(GameAction.PLACE_IMPROVEMENT, chooseImprovementAndLandTile(vacantLandTile, availableImprovements));
                return globalMax;
        }
        return 0;
    }

    @Override
    public Class<? extends Objective> chooseDeck(List<Class<? extends Objective>> available)
    {
        if (available.contains(PandaObjective.class) /*&& getRandom().nextInt(6) > 0*/)
        {
            return PandaObjective.class;
        }
        if (available.contains(PlotObjective.class) && (getBoard().getLandTiles().size() > 10 || getRandom().nextInt(6) == 0))
        {
            return PlotObjective.class;
        }
        if (available.contains(GardenerObjective.class) && getBoard().getLandTiles().stream().filter(l -> (l.getBambooSize() > 0)).count() > 5)
        {
            return GardenerObjective.class;
        }
        return randomElement(available); // return Random if can't choose
    }

    @Override
    public Pair<LandTile, TilePosition> chooseTile(List<LandTile> drawnTiles, List<TilePosition> validPos)
    {
        if (lastActionChosen == GameAction.DRAW_TILE)
        {
            Object a = actionsChosen.get(GameAction.DRAW_TILE);
            if (a != null)
            {
                Pair<LandTile, TilePosition> action = (Pair<LandTile, TilePosition>) a;
                if (drawnTiles.contains(action.first) && validPos.contains(action.second))
                {
                    return action;
                }
            }
        }

        var valid = player.getGame().getBoard().getValidEmptyPositions().collect(Collectors.toList());
        var returns = drawnTiles.stream().flatMap(landTile -> validPos.stream().map(position -> Map.entry(evaluatePlotAction(landTile, position, drawnTiles, valid, player.getGame().getBoard(), player.getHand(), depth, true), Pair.of(landTile, position)))).max(Map.Entry.comparingByKey());
        globalMax = returns.get().getKey();
        return returns.map(Map.Entry::getValue).orElse(Pair.of(drawnTiles.get(0), validPos.get(0)));
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
        if (lastActionChosen == GameAction.PLACE_IRRIGATION)
        {
            Object a = actionsChosen.get(GameAction.PLACE_IRRIGATION);
            if (a != null)
            {
                Edge action = (Edge) a;
                if (irrigableEdges.contains(action))
                {
                    return action;
                }
            }
        }

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
            if (numEdge < 0)
            {
                continue;
            }
            TilePosition tilePosition1 = realTile1.getPosition().get();
            if (tilePosition1 == null)
            {
                throw new IllegalArgumentException("getPosition(): pb");
            }
            Tile newTile1 = b.findTile(tilePosition1);
            if (newTile1 == null)
            {
                continue;
                //throw new IllegalArgumentException("findTile: pb");
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
        globalMax = max;
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
        if (valid.size() < 1)
        {
            throw new IllegalArgumentException("There are no valid position for gardener");
        }
        if (lastActionChosen == GameAction.MOVE_GARDENER)
        {
            Object a = actionsChosen.get(GameAction.MOVE_GARDENER);
            if (a != null)
            {
                TilePosition action = (TilePosition) a;
                if (valid.contains(action))
                {
                    return action;
                }
            }
        }

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
        globalMax = maxPts;
        if (bestPosition == null)
        {
            return randomElement(valid);
        }
        return bestPosition;
    }

    @Override
    public Weather chooseWeather(List<Weather> weatherList)
    {
        List<GameAction> gameActionList = new ArrayList<>();
        if (weatherList.contains(Weather.RAIN))
        {
            gameActionList.add(GameAction.MOVE_GARDENER);
        }
        if (weatherList.contains(Weather.STORM))
        {
            gameActionList.add(GameAction.MOVE_PANDA);
        }
        if (weatherList.contains(Weather.CLOUDS))
        {
            gameActionList.add(GameAction.PLACE_IMPROVEMENT);
        }
        int maxPtsAction = 0;
        GameAction bestAction = null;
        for (GameAction gameAction : gameActionList)
        {
            int pts = getPointsForAction(gameAction);
            if (pts > maxPtsAction)
            {
                maxPtsAction = pts;
                bestAction = gameAction;
            }
        }
        if (bestAction != null)
        {
            switch (bestAction)
            {
                case MOVE_GARDENER:
                    return Weather.RAIN;
                case MOVE_PANDA:
                    return Weather.STORM;
                case PLACE_IMPROVEMENT:
                    return Weather.CLOUDS;
            }
        }
        List<Weather> randomChoose = new ArrayList<>();
        if (weatherList.contains(Weather.SUN))
        {
            randomChoose.add(Weather.SUN);
        }
        if (weatherList.contains(Weather.WIND))
        {
            randomChoose.add(Weather.WIND);
        }
        if (!randomChoose.isEmpty())
        {
            return randomElement(randomChoose);
        }
        return randomElement(weatherList);
    }

    @Override
    public TilePosition choosePandaTarget(List<TilePosition> valid, boolean isStorm) // TODO : not recursive right now
    {
        if (lastActionChosen == GameAction.MOVE_PANDA)
        {
            Object a = actionsChosen.get(GameAction.MOVE_PANDA);
            if (a != null)
            {
                TilePosition action = (TilePosition) a;
                if (valid.contains(action))
                {
                    return action;
                }
            }
        }

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
        globalMax = max;
        if (bestPosition != null)
        {
            return bestPosition;
        }
        //return null; // Panda doesn't move
        return player.getGame().getPandaPosition();
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
        if (lastActionChosen == GameAction.PLACE_IMPROVEMENT)
        {
            Object a = actionsChosen.get(GameAction.PLACE_IMPROVEMENT);
            if (a != null)
            {
                Pair<LandTile, LandTileImprovement> action = (Pair<LandTile, LandTileImprovement>) a;
                if (vacantLandTile.contains(action.first) && availableImprovements.contains(action.second))
                {
                    return action;
                }
            }
        }

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
        globalMax = max;
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
