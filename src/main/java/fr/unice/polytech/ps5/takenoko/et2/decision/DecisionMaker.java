package fr.unice.polytech.ps5.takenoko.et2.decision;

import fr.unice.polytech.ps5.takenoko.et2.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;

import java.util.List;

public abstract class DecisionMaker
{
    protected Player player;

    /**Class constructor
     *
     * @param player
     */
    public DecisionMaker(Player player)
    {
        this.player = player;
    }

    /**Get the board of the game of the player
     *
     * @return board
     */
    protected Board getBoard()
    {
        return player.getGame().getBoard();
    }

    /**
     * @return chosen action
     * @param base
     */
    public abstract GameAction chooseAction(List<GameAction> base);

    /**
     * @return class of the deck chosen
     */
    public abstract Class<? extends Objective> chooseDeck();

    /**
     * @param drawnTiles to choose from
     * @return chosen tile
     */
    public abstract LandTile chooseTile(List<LandTile> drawnTiles); //drawnTiles.size() = 3

    /**
     *
     * @param validPos
     * @param tile to put on the board
     * @return desired position of tile
     */
    public abstract TilePosition chooseTilePosition(List<TilePosition> validPos, LandTile tile);

    /**
     * @return Objective to complete
     */
    public abstract Objective chooseObjectiveToComplete(List<Objective> validObjectives);

}
