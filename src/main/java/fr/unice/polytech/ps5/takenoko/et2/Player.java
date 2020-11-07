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

    /**
     * Add the first objectives to the player hand
     *
     * @param objectivesList a list of Objective
     */
    public void addFirstObjectives(List<Objective> objectivesList)
    {
        this.hand.addAll(objectivesList);
    }

    public List<Objective> getHand()
    {
        return Collections.unmodifiableList(hand);
    }

    public DecisionMaker getDecisionMaker()
    {
        return decisionMaker;
    }

    public Game getGame()
    {
        return game;
    }

    public boolean isHasTriggeredEmperor()
    {
        return hasTriggeredEmperor;
    }

    public void triggerEmperor()
    {
        this.hasTriggeredEmperor = true;
    }
}