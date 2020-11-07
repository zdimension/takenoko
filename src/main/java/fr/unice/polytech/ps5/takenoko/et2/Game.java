package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game
{
    private static int numberActionsInTurn = 2;
    private Board board;
    private List<PlotObjective> plotObjectiveDeck;
    private List<LandTile> tileDeck;
    private ArrayList<Player> playerList;
    private boolean isFirstTurn;
    private boolean isLastTurn;

    public Game(HashMap<Player, DecisionMaker> playerMap)
    {
        playerList = new ArrayList<Player>();
        //initialiser les listes
        isFirstTurn = true;
        isLastTurn = false;
        board = new Board();
        plotObjectiveDeck = new ArrayList<PlotObjective>();
        tileDeck = new ArrayList<LandTile>();
    }

    void addPlayer(DecisionMakerBuilder builder)
    {
        this.playerList.add(new Player(this, builder));
    }

    public void gameProcessing()
    {
        if (playerList.size() < 2)
        {
            throw new IllegalArgumentException("Game started with less than 2 players");
        }
        //deroulement de la partie

        //une grande boucle tour

        //si le dernier joueur a joué et que c'etait le premier tour le champ passe à flase
        //si le dernier joueur a joué et que c'était le dernier tour, on quitte la boucle


        //on check qui gagne
    }

    private ArrayList<Player> whoWins()
    {
        int bestScore = 0;
        for (Player player : playerList)
        {
            if (bestScore <= player.countPoints())
            {
                bestScore = player.countPoints();
            }
        }
        ArrayList<Player> winners = new ArrayList<Player>();
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
}
