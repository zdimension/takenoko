package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;

import java.util.ArrayList;
import java.util.List;

public abstract class DecisionMaker
{
    protected Player player;

    public DecisionMaker(Player player)
    {
        this.player = player;
    }

    protected Board getBoard()
    {
        return player.getGame().getBoard();
    }

    /**
     *
     * @return chosen action
     */
    abstract GameAction chooseAction();

    /**
     *
     * @return true if DecisionMaker wants to do an extra action, false otherwise
     */
    abstract boolean anyExtraAction();

    /**
     *
     * @return chosen extra action
     */
    abstract GameAction chooseExtraAction();

    /**
     *
     * @return class of the deck chosen
     */
    abstract Class<? extends Objective> chooseDeck();

    /**
     *
     * @param drawnTiles to choose from
     * @return chosen tile
     */
    abstract LandTile chooseTile(List<LandTile> drawnTiles); //drawnTiles.size() = 3

    /**
     *
     * @param tile to put on the board
     * @return desired position of tile
     */
    abstract TilePosition chooseTilePosition(LandTile tile);

    /**
     *
     * @return Objective to complete
     */
    abstract Objective completeObjective();

}
