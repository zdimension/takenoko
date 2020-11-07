package fr.unice.polytech.ps5.takenoko.et2;

public enum GameAction
{
    DRAW_PLOT,
    DRAW_OBJECTIVE,
    COMPLETE_OBJECTIVE(true);

    boolean unlimited;

    GameAction(boolean unlimited)
    {
        this.unlimited = unlimited;
    }

    GameAction()
    {
        this(false);
    }

    public boolean isUnlimited()
    {
        return unlimited;
    }
}
