package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerException;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;

import java.util.*;
import java.util.stream.Collectors;

public class Game
{
    private static final int numberActionsInTurn = 2;
    private static final Map<Integer, Integer> objectiveThreshold = Map.of(
        2, 9,
        3, 8,
        4, 7
    );
    private Board board;
    private Map<Class<? extends Objective>, List<? extends Objective>> objectiveDecks = new HashMap<>();
    private List<LandTile> tileDeck;
    private ArrayList<Player> playerList;
    private boolean isFirstRound;
    private boolean emperorTriggered;

    /**
     * Game contructor
     *
     * @param plotObjectiveDeck
     * @param tileDeck
     */
    public Game(List<PlotObjective> plotObjectiveDeck, List<LandTile> tileDeck)
    {
        if (plotObjectiveDeck.size() == 0)
        {
            throw new IllegalArgumentException("PlotObjective deck is empty");
        }
        playerList = new ArrayList<Player>();
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
     */
    void addPlayer(DecisionMakerBuilder builder)
    {
        this.playerList.add(new Player(this, builder));
    }

    /**
     * Processes the game
     *
     * @throws DecisionMakerException
     */
    public void gameProcessing() throws Exception
    {
        if (playerList.size() < 2)
        {
            throw new IllegalArgumentException("Game started with less than 2 players");
        }
        if (this.tileDeck.size() == 0)
        {
            throw new IllegalArgumentException("Game started with empty tile deck");
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
                        base.remove(GameAction.DRAW_OBJECTIVE);
                }

                if (player.getHand().stream().noneMatch(o -> o.checkValidated(this)))
                    base.remove(GameAction.COMPLETE_OBJECTIVE);

                var action = dm.chooseAction(base);

                if (!base.contains(action))
                {
                    throwError(new DecisionMakerException("Invalid action"));
                }

                if (action == null)
                {
                    break;
                }

                switch (action)
                {
                    case DRAW_OBJECTIVE:
                        List<Class<? extends Objective>> valid =
                            objectiveDecks
                                .entrySet()
                                .stream()
                                .filter(e -> !e.getValue().isEmpty())
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toUnmodifiableList());
                        var chosen =
                            dm.chooseDeck(valid);
                        if (!valid.contains(chosen))
                        {
                            throwError(new IllegalArgumentException("Invalid deck chosen"));
                            continue;
                        }
                        player.addObjective(objectiveDecks.get(chosen).remove(0));
                        break;
                    case DRAW_TILE:
                        LandTile chosenTile = dm.chooseTile(Collections.unmodifiableList(tileDeck.subList(0, 3)));
                        var validPos = board.getTiles().keySet()
                            .stream()
                            .flatMap(t ->
                                board.getNeighboringPositions(t)
                                    .filter(p -> board.isValid(p)))
                            .collect(Collectors.toList());
                        TilePosition tilePosition = dm.chooseTilePosition(validPos, chosenTile);
                        if (!validPos.contains(tilePosition))
                        {
                            throwError(new IllegalArgumentException("Position of tile given is invalid"));
                            continue;
                        }
                        board.addTile(chosenTile, tilePosition);
                        tileDeck.remove(chosenTile);
                        break;
                    case COMPLETE_OBJECTIVE:
                        this.completeObjective(player);
                        break;
                    default:
                        throwError(new IllegalArgumentException("Value of chosenAction does not conform to available values"));
                        continue;
                }

                if (!action.isUnlimited())
                {
                    remaining--;
                }
            }

            System.out.printf("Player %d : %d pts%n", i, player.countPoints());

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
        ArrayList<Player> winners = whoWins();
        winners.forEach(w -> System.out.println(playerList.indexOf(w)));
    }

    /**
     * Return the list of winning players, based on their points and who triggered the emperor
     *
     * @return list of winning players, empty list if no one won
     */
    private ArrayList<Player> whoWins()
    {
        int bestScore = 0;
        ArrayList<Player> winners = new ArrayList<Player>();
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
     * Tries to complete desired objective of the player
     * the player triggers the emperor if he has enough objectives complete
     *
     * @param player to ask from what objective to complete
     */
    private void completeObjective(Player player) throws Exception
    {
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

    private void throwError(Exception exc) throws Exception
    {
        if (true)
        {
            System.err.printf("GAME ERROR: %s%n", exc.getMessage());
        }
        else
        {
            throw exc;
        }
    }

    //public getPlayerIndividualBoard(PLayer player)
}
