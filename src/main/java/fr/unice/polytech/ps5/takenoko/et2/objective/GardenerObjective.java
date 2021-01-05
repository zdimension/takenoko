package fr.unice.polytech.ps5.takenoko.et2.objective;

import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTileImprovement;
import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Player;

import java.util.Objects;
import java.util.Set;

/**
 * The class representing the gardener {@link fr.unice.polytech.ps5.takenoko.et2.objective.Objective}. In order to validate this objective, the specified configuration
 * has to be on the {@link fr.unice.polytech.ps5.takenoko.et2.board.Board}. Three kinds of configuration exist :
 * <ul>
 *     <li>a bamboo of 4 sections with a specific improvement
 *     <li>a bamboo of 4 sections without improvement
 *     <li>a group of several bamboo of 3 sections without any improvement constraints
 * </ul>
 */
public class GardenerObjective extends Objective
{
    private final Color color;
    private final int numberOfBambooStack;
    private final int numberOfBambooSection;
    private final LandTileImprovement landTileImprovement;

    /**
     * Construtor of a Gardener Objective
     *
     * @param points  Points of the Objective
     * @param color   Color of the bamboo's stacks and sections
     * @param stack   number of stacks of bamboo
     * @param section number of bamboo section on each stack
     */
    public GardenerObjective(int points, Color color, int stack, int section)
    {
        super(points);
        this.color = Objects.requireNonNull(color, "color must not be null");
        this.numberOfBambooStack = stack;
        this.numberOfBambooSection = section;
        this.landTileImprovement = null;
    }

    /**
     * Construtor of a Gardener Objective with a specific LandTileImprovement, especially for the one stack objective
     *
     * @param points  Points of the Objective
     * @param color   Color of the bamboo's stacks and sections
     * @param stack   number of stacks of bamboo
     * @param section number of bamboo section on each stack
     * @param imp     landTileImprovment requiered (only with one bambooStack)
     */
    public GardenerObjective(int points, Color color, int stack, int section, LandTileImprovement imp)
    {
        super(points);
        this.color = Objects.requireNonNull(color, "color must not be null");
        this.numberOfBambooStack = stack;
        this.numberOfBambooSection = section;
        this.landTileImprovement = imp;
    }

    /**
     * Check if the objective is validated with the given board
     *
     * @param board The board to check
     * @param player the player who have this objective in hand
     * @return true if the Objective is validated in the Game, false otherwise
     */
    @Override
    public boolean checkValidated(Board board, Player player)
    {
        Objects.requireNonNull(board, "board must not be null");
        Objects.requireNonNull(player, "player must not be null");

        return checkValidated(board);
        /*Set<LandTile> landTileList = board.getLandTiles();

        int bambooStackSize;
        int countBambooStack = 0;
        Color bambooColor;
        LandTileImprovement improvement;
        for (LandTile tile : landTileList)
        {
            bambooColor = tile.getColor();
            bambooStackSize = tile.getBambooSize();
            improvement = tile.getLandTileImprovement();
            if (bambooStackSize == this.numberOfBambooSection && bambooColor == this.color)
            {
                if (this.numberOfBambooStack == 1){
                    if (improvement == this.landTileImprovement)
                    {
                        countBambooStack++;
                    }
                }
                else
                {
                    countBambooStack++;
                }
            }
        }

        return this.numberOfBambooStack <= countBambooStack;*/
    }

    /**
     * Check if the objective is validated with the given board
     *
     * @param board The board to check
     * @return true if the Objective is validated in the Game, false otherwise
     */
    public boolean checkValidated(Board board)
    {
        Set<LandTile> landTileList = board.getLandTiles();
        int bambooStackSize;
        int countBambooStack = 0;
        Color bambooColor;
        LandTileImprovement improvement;
        for (LandTile tile : landTileList)
        {
            bambooColor = tile.getColor();
            bambooStackSize = tile.getBambooSize();
            improvement = tile.getLandTileImprovement();
            if (bambooStackSize == this.numberOfBambooSection && bambooColor == this.color)
            {
                if (this.numberOfBambooStack == 1)
                {
                    if (improvement == this.landTileImprovement)
                    {
                        countBambooStack++;
                    }
                }
                else
                {
                    countBambooStack++;
                }
            }
        }

        return this.numberOfBambooStack <= countBambooStack;
    }

    /**
     * @return a String with the number of points, the color of the bamboo and the number of stack(s) and bamboo section
     */
    @Override
    public String toString()
    {
        String message = "Gardener Objective : " + points + " points, "
            + color.toString() + " bamboo, "
            + numberOfBambooStack + " bamboo stack(s), "
            + numberOfBambooSection + " bamboo sections per stack, ";
        if(this.landTileImprovement!=null) {
            message += landTileImprovement.toString() + " land tile improvement";
        }
        return message;
    }
}
