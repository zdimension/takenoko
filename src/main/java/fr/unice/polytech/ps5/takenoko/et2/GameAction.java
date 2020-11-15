package fr.unice.polytech.ps5.takenoko.et2;

import java.util.Objects;

public enum GameAction
{
    DRAW_TILE,
    DRAW_OBJECTIVE,
    COMPLETE_OBJECTIVE(true),
    PICK_IRRIGATION,
    PLACE_IRRIGATION(true);

    boolean unlimited;

    GameAction(boolean unlimited)
    {
        this.unlimited = Objects.requireNonNull(unlimited, "unlimited must not be null");
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
