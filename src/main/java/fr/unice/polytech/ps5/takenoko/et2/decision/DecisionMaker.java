package fr.unice.polytech.ps5.takenoko.et2.decision;

import fr.unice.polytech.ps5.takenoko.et2.board.*;
import fr.unice.polytech.ps5.takenoko.et2.enums.Weather;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Player;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.util.Pair;

import java.util.List;
import java.util.Objects;

/**
 * Class making decisions over the course of the game.
 *
 * All the fonctions are called by Game when the 'human player'
 * is expected to act in the 'real' game.
 *
 * This class is separated from the Player class because a living brain and
 * its belongings are not one unbreakable entity (Cf. 'Brain in a vat' from René
 * Descartes and Gilbert Harman).
 */
public abstract class DecisionMaker
{
    /**
     * Player on which DecisionMaker acts
     */
    protected final Player player;

    /**
     * Class constructor
     *
     * @param player player the DecisionMaker will be bound to
     */
    public DecisionMaker(Player player)
    {
        this.player = Objects.requireNonNull(player, "player must not be null");
    }

    /**
     * Player getter
     *
     * @return The player associated with the DecisionMaker
     */
    public Player getPlayer()
    {
        return player;
    }

    /**
     * Get the board of the game of the player
     *
     * @return board
     */
    protected Board getBoard()
    {
        return player.getGame().getBoard();
    }

    protected <T> T randomElement(List<T> list)
    {
        return list.get(player.getGame().getRandom().nextInt(list.size()));
    }

    /**Chooses one action out of allowed actions to perform during the turn
     *
     * @param base
     * @return chosen action
     */
    public abstract GameAction chooseAction(List<GameAction> base);

    /**Chooses one deck from which to draw an objective
     *
     * @return class of the deck chosen
     */
    public abstract Class<? extends Objective> chooseDeck(List<Class<? extends Objective>> available);

    /**Chooses one from three LandTiles and a position on the board to put it
     *
     * @param drawnTiles to choose from
     * @return chosen tile
     */
    public abstract Pair<LandTile, TilePosition> chooseTile(List<LandTile> drawnTiles, List<TilePosition> validPos); //drawnTiles.size() = 3

    /**Chooses an objective to complete among objectives in the player's hand
     *
     * @return Objective to complete
     */
    public abstract Objective chooseObjectiveToComplete(List<Objective> validObjectives);

    /**Chooses an edge on the board on which to put an irrigation
     *
     * @return An Edge, the position of the irrigation
     */
    public abstract Edge chooseIrrigationPosition(List<Edge> irrigableEdges);

    /**
     * @return The desired landing position of the gardener
     */
    public abstract TilePosition chooseGardenerTarget(List<TilePosition> valid);

    /**Chooses one weather to happen during the turn among allowed weathers
     *
     * @param weatherList to choose from
     * @return chosen weather
     */
    public abstract Weather chooseWeather(List<Weather> weatherList);

    /**
     * @return The desired landing position of the panda
     */
    public abstract TilePosition choosePandaTarget(List<TilePosition> valid, boolean isStorm);

    /**
     * When it rains, chooses a tile on which to add bamboo
     *
     * @param listIrrigatedTiles to choose from to add bamboo
     * @return a tile and its position to add a BambooSection on it
     */
    public abstract LandTile chooseTileToAddBamboo(List<LandTile> listIrrigatedTiles);

    /**
     * When it's cloudy, picks a LandTileImprovement
     *
     * @param listLandTileImprovements a list containing LandTilesImprovements in deck
     * @return The chosen LandTileImprovement
     */
    public abstract LandTileImprovement chooseLandTileImprovement(List<LandTileImprovement> listLandTileImprovements);

    /**
     * Chooses which LandTileImprovement he can place and where
     *
     * @param vacantLandTile        LandTile without improvement
     * @param availableImprovements improvements owned by the player
     * @return a Pair of LandTile and LandTileImprovement
     */
    public abstract Pair<LandTile, LandTileImprovement> chooseImprovementAndLandTile(List<LandTile> vacantLandTile, List<LandTileImprovement> availableImprovements);
}
