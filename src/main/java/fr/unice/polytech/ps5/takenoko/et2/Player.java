package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.board.Edge;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTileImprovement;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The class representing the player.
 */
public class Player
{
    private final List<Objective> hand = new ArrayList<>();
    private final List<Objective> objectivesCompleted = new ArrayList<>();
    private final Game game;
    private final DecisionMaker decisionMaker;
    private final List<LandTileImprovement> chipReserve = new ArrayList<>();
    private boolean hasTriggeredEmperor;
    private int nbIrrigationsInStock = 0;
    private final List<BambooSection> bambooSectionReserve= new ArrayList<>();

    /**
     * Constructor of the Player
     */
    public Player(Game game, DecisionMakerBuilder builder)
    {
        Objects.requireNonNull(builder, "builder must not be null");
        this.game = Objects.requireNonNull(game, "game must not be null");
        this.decisionMaker = builder.build(this);
        this.hasTriggeredEmperor = false;
    }

    /**
     * @return the number of objectives the player has completed during the game
     */
    public int completedObjectivesCount()
    {
        return this.objectivesCompleted.size();
    }

    /**
     * Count the total points of the player
     *
     * @return total points of completed objectifs
     */
    public int countPoints()
    {
        int totalPoint = 0;
        for (Objective objective : this.objectivesCompleted)
        {
            totalPoint += objective.getPoints();
        }
        if (hasTriggeredEmperor)
        {
            totalPoint += 2;
        }
        return totalPoint;
    }

    /**
     * Add one objective to the player hand
     *
     * @param objective a plot, gardener or panda objective
     */
    public void addObjective(Objective objective)
    {
        this.hand.add(Objects.requireNonNull(objective, "objective must not be null"));
    }

    public void moveObjectiveToComplete(Objective objective)
    {
        Objects.requireNonNull(objective, "objective must not be null");
        if (!hand.contains(objective))
        {
            throw new IllegalArgumentException("Hand of player does not contain given objective");
        }
        objectivesCompleted.add(objective);
        //System.out.println(completedObjectivesCount());
        hand.remove(objective);
    }

    /**
     * Get read-only Hand of objectives
     *
     * @return List of objectives
     */
    public List<Objective> getHand()
    {
        return Collections.unmodifiableList(hand);
    }

    /**
     * @return associated DecisionMaker
     */
    public DecisionMaker getDecisionMaker()
    {
        return decisionMaker;
    }

    /**
     * @return associated Game
     */
    public Game getGame()
    {
        return game;
    }

    /**
     * @return if player has triggered the emperor
     */
    public boolean isHasTriggeredEmperor()
    {
        return hasTriggeredEmperor;
    }

    /**
     * Triggers the emperor
     * sets hasTriggeredEmperor to true
     */
    public void triggerEmperor()
    {
        this.hasTriggeredEmperor = true;
    }

    /**
     * Pick an irrigation from the game. Remove one irrigation in the game's deck and add it to the player's stock
     */
    public void pickIrrigation()
    {
        nbIrrigationsInStock++;
    }

    public int getNbIrrigationsInStock()
    {
        return nbIrrigationsInStock;
    }

    public boolean irrigateEdge(Edge edge)
    {
        if (nbIrrigationsInStock <= 0)
        {
            return false;
        }
        if (edge.isIrrigated())
        {
            return false;
        }
        LandTile[] tilesToGrowBamboo = edge.addIrrigation();
        nbIrrigationsInStock--;
        if (tilesToGrowBamboo == null)
        {
            return true;
        }
        for (LandTile landTile : tilesToGrowBamboo)
        {
            try
            {
                game.addBambooSectionToTile(landTile);
            }
            catch (Exception e)
            {
                //Do nothing
            }
        }
        return true;
    }

    /**
     * Add a LandTileImprovement to the player's reserve (called after a cloud)
     *
     * @param landTileImprovement The chip to add
     */
    public void addChip(LandTileImprovement landTileImprovement)
    {
        chipReserve.add(landTileImprovement);
    }

    /**
     * Get a List containing the player's LandTileImprovements
     *
     * @return the List
     */
    public List<LandTileImprovement> getChipReserve()
    {
        return chipReserve;
    }


    /**
     * Add a BambooSection to the player's reserve (called after moving the panda)
     *
     * @param bambooSection the BambooSection to add
     */
    public void addBambooSection(BambooSection bambooSection)
    {
        bambooSectionReserve.add(bambooSection);
    }

    /**
     * Get a List containing the player's BambooSection
     *
     * @return the List
     */
    public List<BambooSection> getBambooSectionReserve() { return bambooSectionReserve; }


    /**
     * @return a String describing the player
     */
    @Override
    public String toString()
    {
        StringBuilder playerString = new StringBuilder("Game : " + game.toString() +
            "\nDecisionMaker : " + decisionMaker.toString() +
            "Triggered Emperor : " + hasTriggeredEmperor +
            "Ojectives in Hand : ");
        for (Objective objective : hand)
        {
            playerString.append(objective.toString()).append("\n");
        }
        playerString.append("Objectives completed :");
        for (Objective objective : objectivesCompleted)
        {
            playerString.append(objective.toString()).append("\n");
        }
        return playerString.toString();
    }
}