package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;

import java.util.*;

public class Game
{
    private static int numberActionsInTurn = 2;
    private Board board;
    private Map<Class<? extends Objective>, List<? extends Objective>> objectiveDecks = new HashMap<>();

    private List<LandTile> tileDeck;
    private ArrayList<Player> playerList;
    private boolean isFirstRound;
    private static int numberObjectivesToWin = 9;
    private boolean emperorTriggered;

    public Game(List<PlotObjective> plotObjectiveDeck, List<LandTile> tileDeck)
    {
        if (plotObjectiveDeck.size() == 0)
        {
            throw new IllegalArgumentException("PlotObjective deck is empty");
        }
        playerList = new ArrayList<Player>();
        isFirstRound = true;
        board = new Board();
        this.objectiveDecks.put(PlotObjective.class, plotObjectiveDeck);
        this.tileDeck = tileDeck;
        this.emperorTriggered = false;
    }

    void addPlayer(DecisionMakerBuilder builder)
    {
        this.playerList.add(new Player(this, builder));
    }

    public void gameProcessing() throws DecisionMakerException
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


        int numberPlayers = playerList.size() - 1;
        int i = 0;

        do
        {
            var player = playerList.get(i);
            if (player.isHasTriggeredEmperor())
            {
                break;
            }

            var dm = player.getDecisionMaker();

            GameAction chosenAction = dm.chooseAction();

            for (int j = numberActionsInTurn; j > 0; )
            {
                switch (chosenAction)
                {
                    case DRAW_OBJECTIVE:
                        if (player.getHand().size() >= 5)
                        {
                            continue;
                        }

                        var clazz = dm.chooseDeck();
                        if (clazz == null)
                        {
                            continue;
                        }
                        player.addObjective(objectiveDecks.get(clazz).get(0));
                        objectiveDecks.get(clazz).remove(0); //TODO can a deck be empty?
                        break;

                    case DRAW_TILE:
                        LandTile chosenTile = dm.chooseTile(Collections.unmodifiableList(tileDeck.subList(0, 3)));
                        TilePosition tilePosition = dm.chooseTilePosition(chosenTile);
                        if (!board.isValid(tilePosition))
                        {
                            continue;
                        }
                        board.addTile(chosenTile, tilePosition);
                        tileDeck.remove(chosenTile);
                        break;

                    case COMPLETE_OBJECTIVE:
                        this.completeObjective(player);
                        break;
                    default:
                        throw new DecisionMakerException("Value of chosenAction does not conform to available values");
                }
                if (!chosenAction.isUnlimited())
                {
                    j--;
                }
            }

            while (dm.anyExtraAction())
            {
                GameAction chosenExtraAction = dm.chooseExtraAction();
                switch (chosenExtraAction)
                {
                    case COMPLETE_OBJECTIVE:
                        this.completeObjective(player);
                        break;
                    default:
                        throw new DecisionMakerException("Value of chosenExtraAction does not conform to available values");
                }
            }


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
        winners.forEach(w -> System.out.println(w));
    }

    /**
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
        if (bestScore < Game.numberObjectivesToWin)
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


    public Board getBoard()
    {
        return board;
    }

    private void completeObjective(Player player)
    {
        //on regarde si l'objectif est complet
        //si oui on le retire de la main pour mettre dans la liste des objectifs finis
        //on calcule le nb de points
        //TODO


        if (player.countPoints() >= numberObjectivesToWin)
        {
            if (emperorTriggered)
            {
                return;
            }
            player.triggerEmperor();
        }
    }

    //public getPlayerIndividualBoard(PLayer player)
}
