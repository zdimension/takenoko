package fr.unice.polytech.ps5.takenoko.et2.gameplay;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Get list of all unlimited GameActions
     *
     * @return The list of unlimited GameAction
     */
    public static List<GameAction> getUnlimitedActions()
    {
        return Arrays.stream(values())
            .filter(GameAction::isUnlimited)
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Check if a GameAction is unlimited
     *
     * @return true if unlimited, false otherwise
     */
    public boolean isUnlimited()
    {
        return unlimited;
    }
}
