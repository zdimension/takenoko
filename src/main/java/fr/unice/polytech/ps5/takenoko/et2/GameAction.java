package fr.unice.polytech.ps5.takenoko.et2;

public enum GameAction
{
    DRAW_TILE,
    DRAW_OBJECTIVE,
    COMPLETE_OBJECTIVE(true),
    PICK_IRRIGATION,
    PLACE_IRRIGATION(true),
    MOVE_GARDENER,
    MOVE_PANDA,
    PLACE_IMPROVEMENT(true);

    private final boolean unlimited;

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
