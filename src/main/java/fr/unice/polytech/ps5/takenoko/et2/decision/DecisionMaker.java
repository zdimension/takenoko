package fr.unice.polytech.ps5.takenoko.et2.decision;

import fr.unice.polytech.ps5.takenoko.et2.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import fr.unice.polytech.ps5.takenoko.et2.Weather;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.Edge;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class DecisionMaker
{
    protected final Player player;

    /**
     * Class constructor
     *
     * @param player player the DecisionMaker will be bound to
     */
    public DecisionMaker(Player player) { this.player = Objects.requireNonNull(player, "player must not be null"); }

    /**
     * Get the board of the game of the player
     *
     * @return board
     */
    protected Board getBoard()
    {
        return player.getGame().getBoard();
    }

    /**
     * @param base
     * @return chosen action
     */
    public abstract GameAction chooseAction(List<GameAction> base);

    /**
     * @return class of the deck chosen
     */
    public abstract Class<? extends Objective> chooseDeck(List<Class<? extends Objective>> available);

    /**
     * @param drawnTiles to choose from
     * @return chosen tile
     */
    public abstract Pair<LandTile, TilePosition> chooseTile(List<LandTile> drawnTiles, List<TilePosition> validPos); //drawnTiles.size() = 3

    /**
     * @return Objective to complete
     */
    public abstract Objective chooseObjectiveToComplete(List<Objective> validObjectives);

    /**
     * @return An Edge, the position of the irrigation
     */
    public abstract Edge chooseIrrigationPosition(List<Edge> irrigableEdges);

    /**
     * @return The desired landing position of the gardener
     */
    public abstract TilePosition chooseGardenerTarget(List<TilePosition> valid);

    /**
     *
     * @param weatherList to choose from
     * @return chosen weather
     */
    public abstract Weather chooseWeather(List<Weather> weatherList);

    /** When it rains
     *
     * @param listIrrigatedTiles
     * @return a tile and its position to add a BambooSection on it
     */
    //public abstract Pair<TilePosition, LandTile> chooseTileToAddBamboo(Map<TilePosition, LandTile> listIrrigatedTiles);
}
