package fr.unice.polytech.ps5.takenoko.et2.gameplay;

import fr.unice.polytech.ps5.takenoko.et2.GameData;
import fr.unice.polytech.ps5.takenoko.et2.board.*;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerException;
import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import fr.unice.polytech.ps5.takenoko.et2.enums.Weather;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PandaObjective;
import fr.unice.polytech.ps5.takenoko.et2.util.Pair;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Core of the Takenoko game upon what happens, links classes to give it sense. Asks DecisionMaker
 * to act rather than letting DecisionMaker act on its own to avoid any unwanted interruption,
 * redundancy, forbidden action like the gamemaster in an RPG or 'loup-garou de Thiercelieux' game
 */
public class Game
{
    //TODO document
    private static final Logger LOGGER = Logger.getLogger(Game.class.getSimpleName());
    //TODO document
    private static final int MAX_TURNS = 200;
    /**
     * Number of actions a DecisionMaker is allowed to performed during their turn, in the
     * DecisionMaker phase.
     */
    private static final int numberActionsInTurn = 2;
    /**
     * Defines how many objectives need to be completed for the emperor to be triggered, depending
     * on the number of players.
     */
    private static final Map<Integer, Integer> objectiveThreshold = Map.of(
        2, 9,
        3, 8,
        4, 7
    );
    /**
     * Minimum number of players in the game.
     */
    private static final int minNumberOfPlayers = 2;
    /**
     * Maximum number of players in the game.
     */
    private static final int maxNumberOfPlayers = 4;
    /**
     * Board of the game, on which DecisionMaker will place LandTiles and irrigations.
     */
    private final Board board;
    /**
     * Decks and other game elements.
     */
    final GameData gameData;
    /**
     * All players in the game.
     */
    private final ArrayList<Player> playerList;
    /**
     * Set to true before the first round, set to false at the end of it. The only usage of this
     * field is to unable Weather functionalities during the first round.
     */
    boolean isFirstRound;
    /**
     * Position of the gardener. It starts on the PondTile.
     */
    private TilePosition gardenerPosition = TilePosition.ZERO;
    /**
     * Position of the panda. It starts on the PondTile.
     */
    private TilePosition pandaPosition = TilePosition.ZERO;
    /**
     * Association of Weather (with directAction = true) and functions.
     */
    private final Map<Weather, Consumer<Player>> WEATHER_MAP = Map.of(
        Weather.RAIN, this::rainAction,
        Weather.STORM, this::stormAction,
        Weather.CLOUDS, this::cloudsAction
    );
    /**
     * Number of irrigations avaiable for the DecisionMaker to pick.
     */
    private int nbIrrigationsInDeck = 20;
    /**
     * Association of GameAction and functions.
     */
    private final Map<GameAction, Consumer<Player>> ACTION_MAP = Map.of(
        GameAction.DRAW_OBJECTIVE, this::drawObjective,
        GameAction.DRAW_TILE, this::drawAndAddTile,
        GameAction.COMPLETE_OBJECTIVE, this::completeObjective,
        GameAction.PICK_IRRIGATION, this::pickIrrigation,
        GameAction.PLACE_IRRIGATION, this::placeIrrigation,
        GameAction.MOVE_GARDENER, this::moveGardener,
        GameAction.MOVE_PANDA, this::movePanda,
        GameAction.PLACE_IMPROVEMENT, this::placeImprovement
    );

    /**
     * Seedable random of the game
     */
    private final Random random;

    public Game()
    {
        this(new GameData());
    }

    /**
     * Game contructor
     *
     * @param gameData
     */
    public Game(GameData gameData)
    {
        this(gameData, new Random());
    }

    public Game(Random rng)
    {
        this(new GameData(), rng);
    }

    public Game(GameData gameData, Random rng){
        Objects.requireNonNull(gameData, "gameData must not be null");

        playerList = new ArrayList<>();
        isFirstRound = true;
        board = new Board();
        this.gameData = gameData;
        this.random = rng;
    }

    public Random getRandom()
    {
        return random;
    }

    /**
     * @param stream a stream of items
     * @return whether the stream contains at least one item
     */
    private static <T> boolean someAvailable(Stream<T> stream)
    {
        return stream.findAny().isPresent();
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
     * {@code ignoreLimitReached} defaults to {@code false}
     *
     * @see Game#gameProcessing(boolean)
     */
    public List<Integer> gameProcessing() throws Exception
    {
        return this.gameProcessing(false);
    }

    /**
     * Processes the game, needs players to be added before via addPlayer (between 2 and 4 players).
     * Starts the game, process each turn in two phases :
     * - Weather phase : weather is picked randomly and prompts DecisionMaker to act when needed.
     * - DecisionMaker phase : prompts DecisionMaker to perform actions.
     * Ends the game one round after a player triggered the Emperor by completing a certain amount
     * of objectives.
     *
     * @return List of indexes of winners
     */
    public List<Integer> gameProcessing(boolean ignoreThreshold) throws Exception
    {
        if (playerList.size() < minNumberOfPlayers)
        {
            throw new IllegalArgumentException("Game started with less than " + minNumberOfPlayers + " players");
        }

        gameData.objectiveDecks.values().forEach(Collections::shuffle);
        Collections.shuffle(gameData.tileDeck);

        for (Player player : playerList)
        {
            for (var deck : gameData.objectiveDecks.values())
            {
                player.addObjective(deck.remove(0));
            }
        }

        int numberPlayers = playerList.size();
        int i = 0;
        int turn = 0;
        while (turn < MAX_TURNS)
        {
            LOGGER.log(Level.FINE, "Turn of player {0}'", i);
            var player = playerList.get(i);
            if (player.isHasTriggeredEmperor())
            {
                break;
            }

            if (!processTurn(player)) // deadlock
            {
                if (ignoreThreshold)
                {
                    break;
                }
                else
                {
                    return null;
                }
            }

            if (i == numberPlayers - 1)
            {
                i = 0;
            }
            else
            {
                i++;
            }
            turn++;

            if(isFirstRound && turn == numberPlayers -1)
            {
                isFirstRound = false;
            }
        }

        if (turn == MAX_TURNS)
        {
            LOGGER.log(Level.WARNING, "Max turn count reached ({0})", turn);
            if (!ignoreThreshold)
            {
                return Collections.emptyList();
            }
        }

        return whoWins().stream().map(playerList::indexOf).collect(Collectors.toList());
    }

    /**
     * Processes the current turn
     *
     * @param player the current player
     * @return true if the turn was completed, false if a deadlock happened
     * @throws DecisionMakerException if the player makes invalid choices
     */
    boolean processTurn(Player player) throws DecisionMakerException
    {
        var dm = player.getDecisionMaker();
        int remaining = numberActionsInTurn;

        Weather turnWeather = null;

        if (!isFirstRound)
        {
            turnWeather = rollWeatherDice();
            if (turnWeather == Weather.QUESTION_MARK)
            {
                turnWeather = chooseWeather(player);
            }
            if (turnWeather.isDirectAction())
            {
                var handler = WEATHER_MAP.getOrDefault(turnWeather, null);

                if (handler == null)
                {
                    throw new IllegalStateException();
                }

                handler.accept(player);
            }
            else
            {
                if (turnWeather == Weather.SUN)
                {
                    remaining++;
                }
            }
        }

        var actions = new ArrayList<>(Arrays.asList(GameAction.values()));
        var unlimited = new ArrayList<>(GameAction.getUnlimitedActions());
        unlimited.add(null); // player can choose "null" after the required 2 actions are performed
        while (true)
        {
            List<GameAction> base;
            if (remaining == 0)
            {
                base = new ArrayList<>(unlimited);
            }
            else
            {
                base = new ArrayList<>(actions);
            }

            base.removeIf(action ->
                action != null &&  // keep the "end turn" action
                    !isActionAvailable(action, player));

            if (base.isEmpty())
            {
                LOGGER.log(Level.WARNING, "No available actions.");
                return false;
            }

            LOGGER.log(Level.FINE, "Available actions: {0}", base.stream().map(o -> o == null ? "null" : o.toString()).collect(Collectors.joining(", ")));

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
                if (turnWeather != Weather.WIND)
                {
                    actions.remove(action); // player has to choose two different actions
                }
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
        return true;
    }

    /**
     * @param act    a game action
     * @param player the current player
     * @return whether the specified action can be performed given the current game state
     */
    private boolean isActionAvailable(GameAction act, Player player)
    {
        Objects.requireNonNull(act, "act must not be null");

        switch (act)
        {
            case PLACE_IMPROVEMENT:
                return someAvailable(board.getLandTilesWithoutImprovement())
                    && !player.getChipReserve().isEmpty();

            case COMPLETE_OBJECTIVE:
                return someAvailable(findCompletableObjectives(player));

            case MOVE_GARDENER:
                return someAvailable(getValidGardenerTargets());

            case MOVE_PANDA:
                return someAvailable(getValidPandaTargets());

            case PLACE_IRRIGATION:
                return player.getNbIrrigationsInStock() > 0 && someAvailable(findIrrigableEdges());

            case PICK_IRRIGATION:
                return nbIrrigationsInDeck > 0;

            case DRAW_OBJECTIVE:
                return !player.isHandFull() && gameData.objectiveDecks.values().stream().anyMatch(l -> !l.isEmpty());

            case DRAW_TILE:
                return !gameData.tileDeck.isEmpty();

            default:
                throw new IllegalArgumentException("Invalid action provided");
        }
    }

    /**
     * Return the list of winning players, based on their points and who triggered the emperor
     *
     * @return list of winning players, empty list if no one won
     */
    private List<Player> whoWins()
    {
        return playerList
            .stream().collect(Collectors.groupingBy(p -> Pair.of(p.countPoints(), p.countPointsPanda()))).entrySet()
            .stream().max(Map.Entry.comparingByKey(Pair.getComparator())).orElseThrow().getValue();
    }

    /**
     * @return board
     */
    public Board getBoard()
    {
        return board;
    }

    /**
     * Prompts DecisionMaker to choose an objective deck. An objective is drawn from this deck and
     * put in the player's hand.
     *
     * @param player the current player
     */
    private void drawObjective(Player player)
    {
        List<Class<? extends Objective>> valid =
            gameData.objectiveDecks
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
        player.addObjective(gameData.objectiveDecks.get(chosen).remove(0));
    }

    /**
     * Prompts DecisionMaker to choose one LandTile from the three on top of the deck and a
     * position on the board to put it.
     *
     * @param p the current player
     */
    private void drawAndAddTile(Player p)
    {
        DecisionMaker dm = p.getDecisionMaker();
        var validTiles = Collections.unmodifiableList(gameData.tileDeck.subList(0, Math.min(gameData.tileDeck.size(), 3)));
        var validPos =
            board
                .getValidEmptyPositions()
                .collect(Collectors.toUnmodifiableList());
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
        gameData.tileDeck.remove(chosenTile.first);
    }

    /**
     * Finds the objectives a player can complete from the objectives in their hand.
     *
     * @param player the current player
     * @return a stream of the objectives that can be complete by the payer
     */
    private Stream<Objective> findCompletableObjectives(Player player)
    {
        return player
            .getHand()
            .stream()
            .filter(o -> o.checkValidated(getBoard(), player));
    }

    /**
     * Prompts DecisionMaker to return an objective to complete, then tries to complete it.
     * The player triggers the emperor if they have enough objectives complete.
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
        if (obj instanceof PandaObjective){
            player.removeBambooSection(((PandaObjective) obj).getBambooSectionList());
        }
        LOGGER.log(Level.INFO, "Player validated objective, N=" + player.completedObjectivesCount());

        if ((player.completedObjectivesCount() >= objectiveThreshold.get(playerList.size())) && !hasSomeoneTriggeredTheEmperor())
        {
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
     */
    public void addBambooSectionToTile(LandTile tile)
    {
        Objects.requireNonNull(tile, "tile must not be null");

        tile.growBambooSection();
    }

    /**
     * Removes a BambooSection to the given tile, if tile has no bambooSection, does nothing
     *
     * @param tile to remove BambooSection to
     */
    private void removeBambooSectionToTile(LandTile tile)
    {
        Objects.requireNonNull(tile, "tile must not be null");

        tile.cutBambooSection();
    }

    /**
     * Gives a BambooSection to a Player
     *
     * @param p     Player to give the bambooSection
     * @param color Color of the bamboo section to give to the player
     */
    private void getBambooSection(Player p, Color color)
    {
        Objects.requireNonNull(p, "player must not be null");
        Objects.requireNonNull(color, "color of the bamboo section must not be null");

        p.addBambooSection(color);
    }

    /**
     * Gives an irrigation from the deck, to a Player normally (just remove 1 irrigation from the deck)
     *
     * @param p to give the irrigation
     */
    public void pickIrrigation(Player p)
    {
        nbIrrigationsInDeck--;
        p.pickIrrigation();
    }

    /**
     * @return all the irrigable edges of the board. An edge is irrigable if it has a common vertex
     * with an irrigated edge. All edges around the Pond are irrigated. An irrigated edge is not
     * irrigable.
     */
    public Stream<Edge> findIrrigableEdges()
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

    /**
     * Prompts DecisionMaker to choose one edge on which to put an irrigation from all irrigable
     * edges.
     *
     * @param p to choose one edge
     */
    private void placeIrrigation(Player p)
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
        return getValidTargets(gardenerPosition);
    }

    private void moveGardener(Player player)
    {
        var valid = getValidGardenerTargets().collect(Collectors.toUnmodifiableList());
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
        {
            return;
        }

        var cast = (LandTile) landing;

        for (var pos : board.getNeighboringPositions(chosenPos).collect(Collectors.toList()))
        {
            var tile = board.getTiles().getOrDefault(pos, null);
            if (tile instanceof LandTile)
            {
                var land = (LandTile) tile;
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

    /**
     * Picks a random weather condition.
     * If no chip is avaiable in the reserve, returns QUESTION_MARK weather so DecisionMaker can
     * choose another weather condition.
     *
     * @return random weather
     */
    private Weather rollWeatherDice()
    {
        var diceResult = Weather.values()[random.nextInt(6)];
        if (diceResult == Weather.CLOUDS && gameData.chipReserve.isEmpty())
        {
            return Weather.QUESTION_MARK;
        }
        return diceResult;
    }

    /**
     * WHen it rains, prompts DecisionMaker to choose a tile to grow bamboo on.
     *
     * @param player to choose a tile
     */
    private void rainAction(Player player)
    {
        var listIrrigatedTiles = board.getBambooableTiles();
        if (listIrrigatedTiles.isEmpty())
        {
            return;
        }
        var tile = player.getDecisionMaker().chooseTileToAddBamboo(listIrrigatedTiles);
        if (listIrrigatedTiles.contains(tile))
        {
            tile.growBambooSection();
        }
    }

    private void stormAction(Player player)
    {
        if (someAvailable(getValidPandaTargets()))
        {
            movePanda(player, true);
        }
    }

    private void cloudsAction(Player player)
    {
        var chosen = player.getDecisionMaker().chooseLandTileImprovement(new ArrayList<>(gameData.chipReserve));
        if (!gameData.chipReserve.contains(chosen))
        {
            throw new IllegalArgumentException("Chosen LandTileImprovement is invalid");
        }
        player.addChip(chosen);
        gameData.chipReserve.remove(chosen);
    }

    /**
     * Asks DecisionMaker what weather to choose from a legal list of weathers
     *
     * @param player to act
     * @return weather the player choose
     */
    Weather chooseWeather(Player player)
    {
        var weatherList = new ArrayList<>(Arrays.asList(Weather.values()));
        weatherList.remove(Weather.QUESTION_MARK);
        if (gameData.chipReserve.isEmpty())
        {
            weatherList.remove(Weather.CLOUDS);
        }
        var weatherChosen = player.getDecisionMaker().chooseWeather(Collections.unmodifiableList(weatherList));
        if (!weatherList.contains(weatherChosen))
        {
            throw new IllegalArgumentException("Chosen weather is invalid");
        }
        return weatherChosen;
    }

    private Stream<TilePosition> getValidPandaTargets()
    {
        return getValidTargets(pandaPosition);
    }

    /**
     * @param piecePosition starting position of the piece
     * @return a stream of valid target positions for the piece
     */
    private Stream<TilePosition> getValidTargets(TilePosition piecePosition)
    {
        return board.getTiles().keySet().stream()
            .filter(pos ->
            {
                // prevent the player from moving the piece to the position it already occupies
                if (pos.equals(piecePosition))
                {
                    return false;
                }

                var basis = pos.sub(piecePosition).getBasis();

                // prevent from moving to a position not in a straight line from the current position
                if (basis == null)
                {
                    return false;
                }

                // check that the line is full, i.e. there are no "holes"
                for (var initial = piecePosition; !initial.equals(pos); initial = initial.add(basis))
                {
                    if (!board.getTiles().containsKey(initial))
                    {
                        return false;
                    }
                }

                return true;
            });
    }

    private void movePanda(Player player)
    {
        movePanda(player, false);
    }

    private void movePanda(Player player, boolean anyPosition)
    {
        var valid =
            anyPosition
                ? List.copyOf(board.getTiles().keySet())
                : getValidPandaTargets().collect(Collectors.toUnmodifiableList());
        TilePosition chosenPos = player
            .getDecisionMaker()
            .choosePandaTarget(valid);
        if (!valid.contains(chosenPos))
        {
            return;
        }

        pandaPosition = chosenPos;

        var landing = board.getTiles().get(chosenPos);

        if (!(landing instanceof LandTile))
        {
            return;
        }

        var cast = (LandTile) landing;

        try
        {
            if (cast.getLandTileImprovement() != LandTileImprovement.ENCLOSURE)
            {
                removeBambooSectionToTile(cast);
                getBambooSection(player, cast.getColor());
            }

        }
        catch (Exception e)
        {
            // critical error, indicates corruption of game state
            throw new RuntimeException(e);
        }
    }

    private void placeImprovement(Player player)
    {
        var vacantLandTile = board.getLandTilesWithoutImprovement().collect(Collectors.toUnmodifiableList());
        var availableImprovements = player.getChipReserve();
        var chosenTileNImprovement = player.getDecisionMaker().chooseImprovementAndLandTile(vacantLandTile, availableImprovements);

        if (!vacantLandTile.contains(chosenTileNImprovement.first) ||
            !availableImprovements.contains(chosenTileNImprovement.second))
        {
            throw new IllegalArgumentException("Chosen LandTile or Improvement is invalid");
        }
        chosenTileNImprovement.first.setLandTileImprovement(chosenTileNImprovement.second);
        player.getChipReserve().remove(chosenTileNImprovement.second);
    }

    /**
     * Get all Players in the Game
     *
     * @return A stream of all Game's Players
     */
    public Stream<Player> getPlayers()
    {
        return playerList.stream();
    }

    private boolean hasSomeoneTriggeredTheEmperor()
    {
        return playerList.stream().anyMatch(Player::isHasTriggeredEmperor);
    }

    //public getPlayerIndividualBoard(PLayer player)
}
