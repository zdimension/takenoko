package fr.unice.polytech.ps5.takenoko.et2.objective;

import fr.unice.polytech.ps5.takenoko.et2.Game;

/**
 * An abstract class for the objectives
 */
public abstract class Objective
{
    protected int points;

    /**
     * Constructor
     *
     * @param points How many points is worth the objective
     */
    public Objective(int points)
    {
        this.points = points;
    }

    /**
     * Get points for the objective
     *
     * @return number of points
     */
    public int getPoints()
    {
        return this.points;
    }

    /**
     * Change points for the objective
     *
     * @param points Objective's points
     */
    public void setPoints(int points)
    {
        this.points = points;
    }

    /**
     * Check if the objective is validated with the given Game
     *
     * @param game The game to check
     * @return true if the Objective is validated in the Game, false otherwise
     */
    public abstract boolean checkValidated(Game game);

    /**
     * @return a String describing the Objective
     */
    @Override
    public abstract String toString();
}
