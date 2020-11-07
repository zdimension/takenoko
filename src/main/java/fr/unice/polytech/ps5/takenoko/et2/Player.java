package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class representing the player.
 */
public class Player
{
    private final List<Objective> hand = new ArrayList<Objective>();
    private final List<Objective> objectivesCompleted = new ArrayList<Objective>();
    private boolean hasTriggeredEmperor;
    private final Game game;
    private final DecisionMaker decisionMaker;

    /**
     * Constructor of the Player
     */
    public Player(Game game, DecisionMakerBuilder builder)
    {
        this.game = game;
        this.decisionMaker = builder.build(this);
        this.hasTriggeredEmperor = false;
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
        this.hand.add(objective);
    }

    public void moveObjectiveToComplete(Objective objective)
    {
        if(!hand.contains(objective)){
            throw new IllegalArgumentException("Hand of player does not contain  given objective");
        }
        objectivesCompleted.add(objective);
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


}