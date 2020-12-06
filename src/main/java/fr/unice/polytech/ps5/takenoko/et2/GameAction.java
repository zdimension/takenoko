package fr.unice.polytech.ps5.takenoko.et2;

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

    public static List<GameAction> getUnlimitedActions()
    {
        return Arrays.stream(values())
            .filter(GameAction::isUnlimited)
            .collect(Collectors.toUnmodifiableList());
    }

    public boolean isUnlimited()
    {
        return unlimited;
    }
}
