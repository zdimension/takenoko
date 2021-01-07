package fr.unice.polytech.ps5.takenoko.et2.decision.bots;

import fr.unice.polytech.ps5.takenoko.et2.GameData;
import fr.unice.polytech.ps5.takenoko.et2.board.*;
import fr.unice.polytech.ps5.takenoko.et2.commandline.annotations.Bot;
import fr.unice.polytech.ps5.takenoko.et2.commandline.annotations.BotParameter;
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
public class MinMaxBot extends RandomBot
{
    private final Map<GameAction, Object> actionsChosen = new HashMap<>(); // Store temporarily the result of choose[...]. Filled by chooseAction() and used by choose[...]

    /**
     * Depth of the min-max algorithm: the higher is the slower and the stronger
     */
    @BotParameter(lowerBound = 0)
    private final int depth;
    private int globalMax; // Max for all functions. Global and temp field
    private GameAction lastActionChosen = null; // Action choosen by chooseAction

    /**
     * Class constructor
     *
     * @param player The player for the Bot
     */
    private MinMaxBot(Player player, int depth)
    {
        super(player);
        this.depth = depth;
        for (GameAction gameAction : GameAction.values()) // For all GameAction, fill by null choice
        {
            actionsChosen.put(gameAction, null);
        }
    }

    public static DecisionMakerBuilder getBuilder(int depth)
    {
        return p -> new MinMaxBot(p, depth);
    }

    /**
     * Min-max bot depth getter
     *
     * @return the depth of min-max algorithm
     */
    public int getDepth()
    {
        return depth;
    }

    void setLastActionChosen(GameAction action)
    {
        this.lastActionChosen = action;
    }

    @Override
    public GameAction chooseAction(List<GameAction> base)
    {
        lastActionChosen = null;
        if (base.contains(GameAction.COMPLETE_OBJECTIVE)) // If possible, complete objective first
        {
            return GameAction.COMPLETE_OBJECTIVE;
        }

        if (base.contains(GameAction.DRAW_OBJECTIVE)) // If possible, pick an objective
        {
            return GameAction.DRAW_OBJECTIVE;
        }

        GameAction bestAction = null; // Check if action can give us points
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

        if (base.contains(GameAction.PICK_IRRIGATION)) // If no action is possible, pick an irrigation
        {
            return GameAction.PICK_IRRIGATION;
        }
        return randomElement(base); // Random if can't choice
    }

    private int getPointsForAction(GameAction action) // Check which action gives to us the max of points
    {
        switch (action)
        {
            case MOVE_GARDENER:
                List<TilePosition> positionsGardener = player.getGame().getValidGardenerTargets().collect(Collectors.toUnmodifiableList());
                if (positionsGardener.isEmpty())
                {
                    return 0;
                }
                actionsChosen.replace(GameAction.MOVE_GARDENER, chooseGardenerTarget(positionsGardener));
                return globalMax;
            case MOVE_PANDA:
                List<TilePosition> positionsPanda = player.getGame().getValidPandaTargets().collect(Collectors.toUnmodifiableList());
                if (positionsPanda.isEmpty())
                {
                    return 0;
                }
                actionsChosen.replace(GameAction.MOVE_PANDA, choosePandaTarget(positionsPanda, false));
                return globalMax;
            case PLACE_IRRIGATION:
                List<Edge> positionsIrrigation = player.getGame().findIrrigableEdges().collect(Collectors.toUnmodifiableList());
                if (positionsIrrigation.isEmpty())
                {
                    return 0;
                }
                actionsChosen.replace(GameAction.PLACE_IRRIGATION, chooseIrrigationPosition(positionsIrrigation));
                return globalMax;
            case DRAW_TILE:
                GameData gameData = player.getGame().getGameData();
                var validTiles = Collections.unmodifiableList(gameData.tileDeck.subList(0, Math.min(gameData.tileDeck.size(), 3)));
                var validPos = getBoard().getValidEmptyPositions().collect(Collectors.toUnmodifiableList());
                if (validTiles.isEmpty() || validPos.isEmpty())
                {
                    return 0;
                }
                actionsChosen.replace(GameAction.DRAW_TILE, chooseTile(validTiles, validPos));
                return globalMax;
            case PLACE_IMPROVEMENT:
                var vacantLandTile = getBoard().getLandTilesWithoutImprovement().collect(Collectors.toUnmodifiableList());
                var availableImprovements = player.getChipReserve();
                if (vacantLandTile.isEmpty() || availableImprovements.isEmpty())
                {
                    return 0;
                }
                actionsChosen.replace(GameAction.PLACE_IMPROVEMENT, chooseImprovementAndLandTile(vacantLandTile, availableImprovements));
                return globalMax;
        }
        return 0;
    }

    @Override
    public Class<? extends Objective> chooseDeck(List<Class<? extends Objective>> available) // Better Objective. Depends of the game configuration
    {
        if (available.contains(PandaObjective.class) && (getBoard().getLandTiles().stream().filter(l -> l.getBambooSize() > 0).count() > 2 || getBoard().getLandTiles().size() < 10))
        {
            return PandaObjective.class;
        }
        if (available.contains(PlotObjective.class) && randomBoolean())
        {
            return PlotObjective.class;
        }
        if (available.contains(GardenerObjective.class) && getBoard().getLandTiles().size() > 5)
        {
            return GardenerObjective.class;
        }
        return randomElement(available); // return Random if can't choose
    }

    @Override
    public Pair<LandTile, TilePosition> chooseTile(List<LandTile> drawnTiles, List<TilePosition> validPos) // Where can we place a Tile ?
    {
        if (lastActionChosen == GameAction.DRAW_TILE) // Already found by chooseAction ?
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

        var valid = new ArrayList<>(validPos);
        var returns =
            drawnTiles.stream().flatMap(
                landTile -> validPos.stream().map(
                    position -> Map.entry(
                        evaluatePlotAction(landTile, position, drawnTiles, valid, player.getGame().getBoard(), player.getHand(), depth, true),
                        Pair.of(landTile, position)
                    )
                )
            ).max(Map.Entry.comparingByKey());
        globalMax = returns.get().getKey();
        return returns.map(Map.Entry::getValue).orElse(Pair.of(randomElement(drawnTiles), randomElement(validPos)));
    }

    @Override
    public Objective chooseObjectiveToComplete(List<Objective> validObjectives) // Max points
    {
        return validObjectives.stream().max(Comparator.comparing(Objective::getPoints)).orElseThrow(NoSuchElementException::new);
    }

    private int getMaxEdgeChangePoints()
    {
        int maxPts = 0;
        for (Edge edge : player.getGame().findIrrigableEdges().collect(Collectors.toUnmodifiableList()))
        {
            try (var ignored = edge.setTemporaryIrrigationState(true))
            {
                for (Objective objective : player.getHand())
                {
                    if (objective.checkValidated(getBoard(), player))
                    {
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
        if (lastActionChosen == GameAction.PLACE_IRRIGATION) // If already found by chooseAction
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

        Board b = (Board) getBoard().clone(); // Min-max, with objectives
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
            var tilePosition1 = realTile1.getPosition();
            if (tilePosition1.isEmpty())
            {
                throw new IllegalArgumentException("getPosition(): pb");
            }
            Tile newTile1 = b.findTile(tilePosition1.get());
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

    private int evaluateIrrigationPosition(Edge edge, Board b, List<GardenerObjective> listGardenerObjectives, List<PlotObjective> listPlotObjectives) // Min-max application
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
    public TilePosition chooseGardenerTarget(List<TilePosition> valid) // Get gardener target, by min-max
    {
        if (valid.size() < 1)
        {
            throw new IllegalArgumentException("There are no valid position for gardener");
        }
        if (lastActionChosen == GameAction.MOVE_GARDENER) // If already found by chooseAction
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
    public Weather chooseWeather(List<Weather> weatherList) // Choose weather by actions permitted By them.
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
        for (GameAction gameAction : gameActionList) // Best weather depends of the future actions
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
        if (lastActionChosen == GameAction.MOVE_PANDA) // If already found by chooseAction
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

        // Use min-max
        List<PandaObjective> listPandaObjectives = player.getHand().stream().filter(PandaObjective.class::isInstance).map(o -> (PandaObjective) o).collect(Collectors.toList());
        Board b = (Board) getBoard().clone();
        HashMap<Color, Integer> playerReserve = new HashMap<>(player.getBambooSectionReserve());
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

    private int evaluatePandaPosition(TilePosition tilePosition, Board b, List<PandaObjective> listPandaObjectives, HashMap<Color, Integer> playerReserve) // Min-max application
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
        if (lastActionChosen == GameAction.PLACE_IMPROVEMENT) // If already found by chooseAction
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
            var position = landTile.getPosition();
            if (position.isEmpty())
            {
                continue;
            }
            for (LandTileImprovement landTileImprovement : availableImprovements)
            {
                Board b = (Board) getBoard().clone();
                int v = evaluateImprovementAndLandTile(landTileImprovement, position.get(), b, listObjectives);
                if (v > max)
                {
                    bestImprovement = landTileImprovement;
                    bestLandTile = landTile;
                    max = v;
                }
            }
        }
        globalMax = max;
        if (bestLandTile == null || bestImprovement == null)
        {
            return Pair.of(randomElement(vacantLandTile), randomElement(availableImprovements));
        }
        return Pair.of(bestLandTile, bestImprovement);
    }

    private int evaluateImprovement(LandTileImprovement improvement, Board b, List<Objective> listObjectives) // For each position, calls evaluateImprovementAndLandTile()
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

    private int evaluateImprovementAndLandTile(LandTileImprovement improvement, TilePosition tilePosition, Board b, List<Objective> listObjectives) // Check if improvement in specific Land Tile could yet points
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

    private int evaluatePlotAction(LandTile playedTile, TilePosition playedPos, List<LandTile> drawnTiles, List<TilePosition> ListValidsPositions, Board board, List<Objective> myObjectives, int n, boolean myTurn) // Evaluate position for new plot, by points given by PlotObjective (min-max)
    {
        List<Objective> copyOfMyObjectives = new ArrayList<>(myObjectives);
        List<LandTile> copyOfDrawnTiles = new ArrayList<>(drawnTiles);
        copyOfDrawnTiles.remove(playedTile);
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
        List<TilePosition> newListValidsPositions = depth == 1
            ? ListValidsPositions
            : new ArrayList<>(ListValidsPositions);
        newListValidsPositions.remove(playedPos);
        scoreReturn -= newListValidsPositions
            .stream()
            .flatMapToInt(tilePosition ->
                copyOfDrawnTiles.stream()
                    .mapToInt(
                        landTile -> evaluatePlotAction(landTile, tilePosition, copyOfDrawnTiles, newListValidsPositions, newBoard, copyOfMyObjectives, n - 1, !myTurn)
                    )).sum() * power / 100;
        return scoreReturn;
    }

}
