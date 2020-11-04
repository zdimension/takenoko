package fr.unice.polytech.ps5.takenoko.et2.objective;

public abstract class Objective
{
    protected int points;

    public Objective()
    {
        //
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }
}
