package fr.unice.polytech.ps5.takenoko.et2.gameplay;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Actions DecisionMaker can perform in his turn during the DecisionMaker phase. Constants may
 * have a parameter to indicate weather an action is unlimited or not. When an action is unlimited,
 * it doesn't count as a limited action, so it can be performed before, between and/or after
 * limited actions (2 for each turn most of the time).
 */
public enum GameAction
{
    /**
     * When the action is DRAW_TILE, DecisionMaker must choose one of three tiles an dplace it on
     * the board on a TilePosition of his choosing
     */
    DRAW_TILE,
    /**
     * When the action is DRAW_OBJECTIVE, DecisionMaker chooses one deck to pick an objective from.
     */
    DRAW_OBJECTIVE,
    /**
     * When the action is COMPLETE_OBJECTIVE, DecisionMaker chooses an objective to complete. This
     * action is unlimited.
     */
    COMPLETE_OBJECTIVE(true),
    /**
     * When the action is PICK_IRRIGATION, DecisionMaker takes an irrigation from the reserve
     * and stores it.
     */
    PICK_IRRIGATION,
    /**
     * When the action is PLACE_IRRIGATION, DecisionMaker puts an irrigation on the board. This
     * action is unlimited.
     */
    PLACE_IRRIGATION(true),
    /**
     * When the action is MOVE_GARDENER, DecisionMaker choose a TilePosition where to move the
     * gardener in a sraight line. He grows bamboo on the plot where he lands and all the adjacent
     * plots of the same color.
     */
    MOVE_GARDENER,
    /**
     * When the action is MOVE_PANDA, DecisionMaker choose a TilePosition where to move the
     * panda in a sraight line. He eats a bamboo section on the plot he lands on. That bamboo
     * section is stored by the DecisionMaker who moved the panda. The ENCLOSURE LandTileImprovement
     * prevents the panda to eat any bamboo section on the LandTile where the LandTileImprovement
     * is set.
     */
    MOVE_PANDA,
    /**
     * When the action is PLACE_IMPROVEMENT, DecisionMaker chooses a tile and a Improvement chip of
     * his belongings, to add the improvement to the tile. The tile must have no improvement or
     * bamboo yet. This action is unlimited.
     */
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
