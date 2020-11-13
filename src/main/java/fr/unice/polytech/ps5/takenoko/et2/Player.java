package fr.unice.polytech.ps5.takenoko.et2;

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
    private boolean hasTriggeredEmperor;

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