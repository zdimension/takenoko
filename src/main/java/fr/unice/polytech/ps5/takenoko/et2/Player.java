package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;

import java.util.ArrayList;
import java.util.List;

/**
 * The class representing the player.
 */
public class Player {
    private List<Objective> hand = new ArrayList<Objective>();
    private List<Objective> objectivesCompleted = new ArrayList<Objective>();
    private boolean hasTriggeredEmperor;

    /**
     * Constructor of the Player
     */
    public Player() {
        this.hasTriggeredEmperor = false;
    }

    /**
     * Count the total points of the player
     *
     * @return total points of completed objectifs
     */
    public int countPoint() {
        int totalPoint = 0;
        for (Objective objective : this.objectivesCompleted) {
            totalPoint += objective.getPoints();
        }
        if (hasTriggeredEmperor) {
            totalPoint += 2;
        }
        return totalPoint;
    }

    /**
     * Add one objective to the player hand
     *
     * @param objective a plot, gardener or panda objective
     */
    public void addObjective(Objective objective) {
        this.hand.add(objective);
    }

    /**
     * Add the first objectives to the player hand
     *
     * @param objectivesList  a list of Objective
     */
    public void addFirstObjectives(List<Objective> objectivesList) {
        for (Objective objective : objectivesList) {
            this.hand.add(objective);
        }
    }
}
