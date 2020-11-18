package fr.unice.polytech.ps5.takenoko.et2.objective;

import fr.unice.polytech.ps5.takenoko.et2.Color;
import fr.unice.polytech.ps5.takenoko.et2.Game;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;

import java.util.Objects;

/**
 * The class representing the gardener objective
 */
public class GardenerObjective extends Objective {
    private final Color color;
    private final int numberOfBambooStack;
    private final int numberOfBambooSection;

    /**
     * Construtor of a Gardener Objective
     *
     * @param points Points of the Objective
     * @param color Color of the bamboo's stacks and sections
     * @param stack number of stacks of bamboo
     * @param section number of bamboo section on each stack
     */
    public GardenerObjective(int points, Color color, int stack, int section) {
        super(points);
        this.color = Objects.requireNonNull(color, "color must not be null");
        this.numberOfBambooStack = stack;
        this.numberOfBambooSection = section;
    }

    @Override
    public boolean checkValidated(Game game)
    {
        return checkValidated(game.getBoard());
    }

    public boolean checkValidated(Board board) {
        return true;
    }

    /**
     * @return a String with the number of points, the color of the bamboo and the number of stack(s) and bamboo section
     */
    @Override
    public String toString() {
        return "Gardener Objective : " + points + "points,"
                                       + color.toString() + "bambou,"
                                       + numberOfBambooStack + " bamboo stack(s),"
                                       + numberOfBambooSection + "bamboo sections per stack";
    }
}
