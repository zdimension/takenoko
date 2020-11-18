package fr.unice.polytech.ps5.takenoko.et2.objective;

import fr.unice.polytech.ps5.takenoko.et2.Color;
import fr.unice.polytech.ps5.takenoko.et2.Game;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.Tile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The class representing the gardener objective
 */
public class GardenerObjective extends Objective {
    private final Color color;
    private final int numberOfBambooStack;
    private final int numberOfBambooSection;

    /**
     * Construtor of a Gardener Objective
     *
     * @param points Points of the Objective
     * @param color Color of the bamboo's stacks and sections
     * @param stack number of stacks of bamboo
     * @param section number of bamboo section on each stack
     */
    public GardenerObjective(int points, Color color, int stack, int section) {
        super(points);
        this.color = Objects.requireNonNull(color, "color must not be null");
        this.numberOfBambooStack = stack;
        this.numberOfBambooSection = section;
    }

    @Override
    public boolean checkValidated(Game game)
    {
        return checkValidated(game.getBoard());
    }

    /**
     * Check if the objective is validated with the given board
     *
     * @param board The board to check
     * @return true if the Objective is validated in the Game, false otherwise
     */
    public boolean checkValidated(Board board) {
        Objects.requireNonNull(board, "board must not be null");

        Map<TilePosition, Tile> listTiles = board.getTiles();
        Set<LandTile> landTileList = listTiles.values()
            .stream()
            .filter(tile -> tile instanceof LandTile)
            .map(tile -> (LandTile)tile)
            .collect(Collectors.toSet());

        int bambooStackSize;
        int countBambooStack = 0;
        Color bambooColor;
        for (LandTile tile : landTileList) {
            bambooColor = tile.getColor();
            bambooStackSize = tile.getBambooSize();
            if (bambooStackSize == this.numberOfBambooSection && bambooColor.equals(this.color)) {
                countBambooStack++;
            }
            if (this.numberOfBambooStack == countBambooStack) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return a String with the number of points, the color of the bamboo and the number of stack(s) and bamboo section
     */
    @Override
    public String toString() {
        return "Gardener Objective : " + points + "points,"
                                       + color.toString() + "bambou,"
                                       + numberOfBambooStack + " bamboo stack(s),"
                                       + numberOfBambooSection + "bamboo sections per stack";
    }
}
