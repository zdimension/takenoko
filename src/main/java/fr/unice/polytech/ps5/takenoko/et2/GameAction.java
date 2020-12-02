package fr.unice.polytech.ps5.takenoko.et2;

/**
 * Actions DecisionMaker can perform in his turn during the DecisionMaker phase. Constants may
 * have a parameter to indicate weather an action is unlimited or not.
 */
public enum GameAction
{
    DRAW_TILE,
    DRAW_OBJECTIVE,
    COMPLETE_OBJECTIVE(true),
    PICK_IRRIGATION,
    PLACE_IRRIGATION(true),
    MOVE_GARDENER,
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
