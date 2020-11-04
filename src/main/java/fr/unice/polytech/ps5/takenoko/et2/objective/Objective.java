package fr.unice.polytech.ps5.takenoko.et2.objective;

public abstract class Objective
{
    protected int points;

    public Objective(int points)
    {
        this.points = points;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }
}
