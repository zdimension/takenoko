package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerException;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game
{
    private static final int numberActionsInTurn = 2;
    private static final Map<Integer, Integer> objectiveThreshold = Map.of(
        2, 9,
        3, 8,
        4, 7
    );
    private static int minNumberOfPlayers = 2;
    private static int maxNumberOfPlayers = 4;
    private final Board board;
    private final Map<Class<? extends Objective>, List<? extends Objective>> objectiveDecks = new HashMap<>();
    private final List<LandTile> tileDeck;
    private final List<BambooSection> bambooReserve;
    private final ArrayList<Player> playerList;
    private final boolean isFirstRound;
    private final boolean emperorTriggered;
    private final Map<GameAction, Consumer<Player>> ACTION_MAP = Map.of(
        GameAction.DRAW_OBJECTIVE, this::drawObjective,
        GameAction.DRAW_TILE, this::drawAndAddTile,
        GameAction.COMPLETE_OBJECTIVE, this::completeObjective
    );
    private int nbIrrigationsInDeck = 20;

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
        this.bambooReserve = tileDeck
            .stream()
            .flatMap(t -> IntStream.range(0, 4).mapToObj(i -> new BambooSection(t.getColor())))
            .collect(Collectors.toList());
    }

    /**
     * Adds a player to the game
     *
     * @param builder of DecisionMaker (a bot supposedly)
     */
    public void addPlayer(DecisionMakerBuilder builder) throws IllegalAccessException
    {
        Objects.requireNonNull(builder, "builder must not be null");
        if (this.playerList.size() == maxNumberOfPlayers)
        {
            throw new IllegalAccessException("Game should not have more than " + maxNumberOfPlayers + " players");
        }
        this.playerList.add(new Player(this, builder));
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
            var player = playerList.get(i);
            if (player.isHasTriggeredEmperor())
            {
                break;
            }

            var dm = player.getDecisionMaker();

            int remaining = 2;
            var actions = Arrays.asList(GameAction.values());
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

                    if (tileDeck.size() < 3)
                    {
                        base.remove(GameAction.DRAW_TILE);
                    }
                }

                if (player.getHand().stream().noneMatch(o -> o.checkValidated(this)))
                {
                    base.remove(GameAction.COMPLETE_OBJECTIVE);
                }

                if (base.isEmpty())
                {
                    return Collections.emptyList();
                }

                var action = dm.chooseAction(base);

                if (!base.contains(action))
                {
                    throwError(new DecisionMakerException("Invalid action"));
                }

                if (action == null)
                {
                    break;
                }

                var handler = ACTION_MAP.getOrDefault(action, null);

                if (handler != null)
                {
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

    /**
     * Give an irrigation from the deck, to a Player normally (just remove 1 irrigation from the deck)
     *
     * @return true if there is enough irrigations, false otherwise
     */
    public boolean giveIrrigation()
    {
        if (nbIrrigationsInDeck > 0)
        {
            nbIrrigationsInDeck--;
            return true;
        }
        return false;
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
        LandTile chosenTile = dm.chooseTile(Collections.unmodifiableList(tileDeck.subList(0, 3)));
        var validPos =
            board
                .getValidEmptyPositions()
                .collect(Collectors.toList());
        TilePosition tilePosition = dm.chooseTilePosition(validPos, chosenTile);
        if (!validPos.contains(tilePosition))
        {
            throwError(new IllegalArgumentException("Position of tile given is invalid"));
            return;
        }
        board.addTile(chosenTile, tilePosition);
        tileDeck.remove(chosenTile);
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
        var valid = player
            .getHand()
            .stream()
            .filter(o -> o.checkValidated(this))
            .collect(Collectors.toList());
        Objective obj = player.getDecisionMaker().chooseObjectiveToComplete(valid);
        if (!valid.contains(obj))
        {
            return;
        }
        player.moveObjectiveToComplete(obj);

        if (player.countPoints() >= objectiveThreshold.get(playerList.size()))
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
            System.err.printf("GAME ERROR: %s%n", exc.getMessage());
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
    public void addBambooSectionToTile(LandTile tile) throws Exception
    {
        if (tile == null)
        {
            throw new IllegalArgumentException("tile must not be null");
        }

        var res = this.bambooReserve.stream().filter(b -> b.getColor().equals(tile.getColor())).findAny();

        if (res.isEmpty())
        {
            return;
        }

        var bambooSection = res.get();
        if (tile.growBambooSection(bambooSection))
        {
            bambooReserve.remove(bambooSection);
        }
    }

    //public getPlayerIndividualBoard(PLayer player)
}
