package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.board.*;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerException;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game
{
    private static final Logger LOGGER = Logger.getLogger(Game.class.getSimpleName());

    private static final int numberActionsInTurn = 2;
    private static final Map<Integer, Integer> objectiveThreshold = Map.of(
        2, 9,
        3, 8,
        4, 7
    );
    private static final int minNumberOfPlayers = 2;
    private static final int maxNumberOfPlayers = 4;
    private final Board board;
    private final Map<Class<? extends Objective>, List<? extends Objective>> objectiveDecks = new HashMap<>();
    private final List<LandTile> tileDeck;
    private final ArrayList<Player> playerList;
    private final boolean isFirstRound;
    private final boolean emperorTriggered;
    private TilePosition gardenerPosition = TilePosition.ZERO;
    private int nbIrrigationsInDeck = 20;
    private final Map<GameAction, Consumer<Player>> ACTION_MAP = Map.of(
        GameAction.DRAW_OBJECTIVE, this::drawObjective,
        GameAction.DRAW_TILE, this::drawAndAddTile,
        GameAction.COMPLETE_OBJECTIVE, this::completeObjective,
        GameAction.PICK_IRRIGATION, this::pickIrrigation,
        GameAction.PLACE_IRRIGATION, this::placeIrrigation,
        GameAction.MOVE_GARDENER, this::moveGardener
    );

    /**
     * Game contructor
     *
     * @param plotObjectiveDeck
     * @param tileDeck
     */
    public Game(List<PlotObjective> plotObjectiveDeck, List<LandTile> tileDeck)
    {
        Objects.requireNonNull(plotObjectiveDeck, "plotObjectiveDeck must not be null");
        Objects.requireNonNull(tileDeck, "tileDeck must not be null");

        if (plotObjectiveDeck.size() == 0)
        {
            throw new IllegalArgumentException("PlotObjective deck is empty");
        }
        if (tileDeck.size() == 0)
        {
            throw new IllegalArgumentException("Game started with empty tile deck");
        }

        playerList = new ArrayList<>();
        isFirstRound = true;
        board = new Board();
        this.objectiveDecks.put(PlotObjective.class, new ArrayList<>(plotObjectiveDeck));
        this.tileDeck = new ArrayList<>(tileDeck);
        this.emperorTriggered = false;
    }

    /**
     * Adds a player to the game
     *
     * @param builder of DecisionMaker (a bot supposedly)
     * @return the created Player instance
     */
    public Player addPlayer(DecisionMakerBuilder builder) throws IllegalAccessException
    {
        Objects.requireNonNull(builder, "builder must not be null");
        if (this.playerList.size() == maxNumberOfPlayers)
        {
            throw new IllegalAccessException("Game should not have more than " + maxNumberOfPlayers + " players");
        }
        var p = new Player(this, builder);
        this.playerList.add(p);
        return p;
    }

    /**
     * Processes the game
     *
     * @return List of indexes of winners
     * @throws IllegalArgumentException
     * @throws DecisionMakerException
     */
    public List<Integer> gameProcessing() throws Exception
    {
        if (playerList.size() < minNumberOfPlayers)
        {
            throw new IllegalArgumentException("Game started with less than " + minNumberOfPlayers + " players");
        }

        objectiveDecks.values().forEach(Collections::shuffle);
        Collections.shuffle(tileDeck);

        for (Player player1 : playerList)
        {
            for (var deck : objectiveDecks.values())
            {
                player1.addObjective(deck.remove(0));
            }
        }

        int numberPlayers = playerList.size();
        int i = 0;

        do
        {
            LOGGER.log(Level.FINE, "Turn of player {0}'", i);
            var player = playerList.get(i);
            if (player.isHasTriggeredEmperor())
            {
                break;
            }

            var dm = player.getDecisionMaker();

            int remaining = numberActionsInTurn;
            var actions = new ArrayList<>(Arrays.asList(GameAction.values()));
            var unlimited = actions
                .stream()
                .filter(GameAction::isUnlimited).collect(Collectors.toCollection(ArrayList::new));
            unlimited.add(null); // player can choose "null" after the required 2 actions are performed
            while (true)
            {
                List<GameAction> base;
                if (remaining == 0)
                {
                    base = unlimited;
                }
                else
                {
                    base = new ArrayList<>(actions);

                    if (player.getHand().size() == 5 || objectiveDecks.values().stream().allMatch(List::isEmpty))
                    {
                        base.remove(GameAction.DRAW_OBJECTIVE);
                    }

                    if (tileDeck.isEmpty())
                    {
                        base.remove(GameAction.DRAW_TILE);
                    }
                }

                if (findCompletableObjectives(player).findAny().isEmpty())
                {
                    base.remove(GameAction.COMPLETE_OBJECTIVE);
                }

                if (getValidGardenerTargets().findAny().isEmpty())
                {
                    base.remove(GameAction.MOVE_GARDENER);
                }

                if (player.getNbIrrigationsInStock() <= 0 || findIrrigableEdges().findAny().isEmpty())
                {
                    base.remove(GameAction.PLACE_IRRIGATION);
                }

                if (nbIrrigationsInDeck <= 0)
                {
                    base.remove(GameAction.PICK_IRRIGATION);
                }

                if (base.isEmpty())
                {
                    LOGGER.log(Level.WARNING, "Dead-end game");
                    return Collections.emptyList();
                }

                var action = dm.chooseAction(base);

                if (!base.contains(action))
                {
                    throwError(new DecisionMakerException("Invalid action"));
                }

                LOGGER.log(Level.FINE, "Action chosen: {0}", action == null ? "<end turn>" : action.toString());

                if (action == null)
                {
                    break;
                }

                var handler = ACTION_MAP.getOrDefault(action, null);

                if (handler != null)
                {
                    actions.remove(action); // player has to choose two different actions
                    handler.accept(player);
                }
                else
                {
                    throwError(new IllegalArgumentException("Value of chosenAction does not conform to available values"));
                    continue;
                }

                if (!action.isUnlimited())
                {
                    remaining--;
                }
            }

            //System.out.printf("Player %d : %d pts%n", i, player.countPoints());

            if (i == numberPlayers - 1)
            {
                i = 0;
            }
            else
            {
                i++;
                //if(isFirstRound){
                //    isFirstRound = false;
                //}
            }
        }
        while (true);
        return whoWins().stream().map(playerList::indexOf).collect(Collectors.toList());
    }

    /**
     * Return the list of winning players, based on their points and who triggered the emperor
     *
     * @return list of winning players, empty list if no one won
     */
    private ArrayList<Player> whoWins()
    {
        int bestScore = 0;
        ArrayList<Player> winners = new ArrayList<>();
        for (Player player : playerList)
        {
            if (bestScore <= player.countPoints())
            {
                bestScore = player.countPoints();
            }
        }
        if (bestScore < objectiveThreshold.get(playerList.size()))
        {
            return winners;
        }

        for (Player player : playerList)
        {
            if (bestScore == player.countPoints())
            {
                winners.add(player);
            }
        }
        return winners;
    }


    /**
     * @return board
     */
    public Board getBoard()
    {
        return board;
    }

    private void drawObjective(Player player)
    {
        List<Class<? extends Objective>> valid =
            objectiveDecks
                .entrySet()
                .stream()
                .filter(e -> !e.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .collect(Collectors.toUnmodifiableList());
        var chosen = player.getDecisionMaker().chooseDeck(valid);
        if (!valid.contains(chosen))
        {
            throwError(new IllegalArgumentException("Invalid deck chosen"));
            return;
        }
        player.addObjective(objectiveDecks.get(chosen).remove(0));
    }

    private void drawAndAddTile(Player p)
    {
        DecisionMaker dm = p.getDecisionMaker();
        var validTiles = Collections.unmodifiableList(tileDeck.subList(0, Math.min(tileDeck.size(), 3)));
        var validPos =
            board
                .getValidEmptyPositions()
                .collect(Collectors.toList());
        var chosenTile = dm.chooseTile(validTiles, validPos);
        if (!validTiles.contains(chosenTile.first))
        {
            throwError(new IllegalArgumentException("Invalid tile chosen"));
            return;
        }
        if (!validPos.contains(chosenTile.second))
        {
            throwError(new IllegalArgumentException("Position of tile given is invalid"));
            return;
        }
        board.addTile(chosenTile.first, chosenTile.second);
        tileDeck.remove(chosenTile.first);
    }

    private Stream<Objective> findCompletableObjectives(Player player)
    {
        return player
            .getHand()
            .stream()
            .filter(o -> o.checkValidated(this));
    }

    /**
     * Tries to complete desired objective of the player
     * the player triggers the emperor if he has enough objectives complete
     *
     * @param player to ask from what objective to complete
     */
    private void completeObjective(Player player)
    {
        Objects.requireNonNull(player, "player must not be null");
        // the collection is always populated because gameProcessing checks for non-emptiness
        var valid = findCompletableObjectives(player).collect(Collectors.toList());
        Objective obj = player.getDecisionMaker().chooseObjectiveToComplete(valid);
        if (!valid.contains(obj))
        {
            return;
        }
        player.moveObjectiveToComplete(obj);

        if (player.getNumberObjectivesCompleted()>= objectiveThreshold.get(playerList.size()))
        {
            if (emperorTriggered)
            {
                return;
            }
            player.triggerEmperor();
        }
    }

    private <T extends Exception> void throwError(T exc) throws T
    {
        Objects.requireNonNull(exc, "exc must not be null");
        if (true)
        {
            LOGGER.log(Level.SEVERE, "GAME ERROR: {0}", exc.getMessage());
        }
        else
        {
            throw exc;
        }
    }

    /**
     * Adds a BambooSection to the given tile, if no bamboo is left in bambooReserve or if tile is at max of bambooSection capacity, does nothing
     *
     * @param tile to give BambooSection to
     * @throws Exception
     */
    public void addBambooSectionToTile(LandTile tile)
    {
        Objects.requireNonNull(tile, "tile must not be null");

        tile.growBambooSection();
    }

    /**
     * Give an irrigation from the deck, to a Player normally (just remove 1 irrigation from the deck)
     *
     * @param p to give the irrigation
     */
    public void pickIrrigation(Player p)
    {
        nbIrrigationsInDeck--;
        p.pickIrrigation();
    }

    private Stream<Edge> findIrrigableEdges()
    {
        return board
            .getTiles()
            .values()
            .stream()
            .filter(tile -> tile instanceof LandTile)
            .map(tile -> (LandTile) tile)
            .flatMap(Tile::getEdges)
            .distinct()
            .filter(Edge::canBeIrrigated);
    }

    public void placeIrrigation(Player p)
    {
        DecisionMaker dm = p.getDecisionMaker();
        var valid = findIrrigableEdges().collect(Collectors.toUnmodifiableList());
        Edge chosenEdge = dm.chooseIrrigationPosition(valid);
        if (!valid.contains(chosenEdge))
        {
            return;
        }
        p.irrigateEdge(chosenEdge);
    }

    private Stream<TilePosition> getValidGardenerTargets()
    {
        return board.getTiles().keySet().stream()
            .filter(pos ->
            {
                // prevent the player from moving the gardener to the position it already occupies
                if (pos.equals(gardenerPosition))
                    return false;

                var basis = pos.sub(gardenerPosition).getBasis();

                // prevent from moving to a position not in a straight line from the current position
                if (basis == null)
                    return false;

                // check that the line is full, i.e. there are no "holes"
                for (var initial = gardenerPosition; initial != pos; initial = initial.add(basis))
                {
                    if (!board.getTiles().containsKey(initial))
                        return false;
                }

                return true;
            });
    }

    private void moveGardener(Player player)
    {
        var valid = getValidGardenerTargets().collect(Collectors.toList());
        TilePosition chosenPos = player
            .getDecisionMaker()
            .chooseGardenerTarget(valid);
        if (!valid.contains(chosenPos))
        {
            return;
        }

        gardenerPosition = chosenPos;

        var landing = board.getTiles().get(chosenPos);

        if (!(landing instanceof LandTile))
            return;

        var cast = (LandTile)landing;

        for (var pos : board.getNeighboringPositions(chosenPos).collect(Collectors.toList()))
        {
            var tile = board.getTiles().getOrDefault(pos, null);
            if (tile instanceof LandTile)
            {
                var land = (LandTile)tile;
                if (land.getColor() == cast.getColor())
                {
                    try
                    {
                        addBambooSectionToTile((LandTile) tile);
                    }
                    catch (Exception e)
                    {
                        // critical error, indicates corruption of game state
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private Weather rollWeatherDice()
    {
        var diceResult = Weather.values()[diceRoller.nextInt(6)];
        if (diceResult.equals(Weather.CLOUDS) && chipReserve.size() == 0)
        {
            return Weather.QUESTION_MARK;
        }
        return diceResult;
    }

    //public getPlayerIndividualBoard(PLayer player)
}
