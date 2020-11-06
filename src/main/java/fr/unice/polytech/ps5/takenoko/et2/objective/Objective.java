package fr.unice.polytech.ps5.takenoko.et2.objective;

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
}
