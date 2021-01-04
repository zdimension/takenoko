package fr.unice.polytech.ps5.takenoko.et2.objective;

import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Player;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * The class representing the panda objective
 */
public class PandaObjective extends Objective
{
    private final Map<Color, Integer> bambooSectionList;

    /**
     * Constructor
     *
     * @param points                 points of the Objective
     * @param bambooSectionList    a map of bamboo section color and the number of each one eg [YELLOW, 2]
     */
    public PandaObjective(int points, Map<Color,Integer> bambooSectionList)
    {
        super(points);
        if (bambooSectionList.isEmpty())
        {
            throw new IllegalArgumentException("Invalid Objective");
        }
        this.bambooSectionList = Collections.unmodifiableMap(bambooSectionList);
    }

    /**
     * Get the bamboo section of the objectif in order to remove it from the player reserve after validating it
     * @return a Map containing the color of the bamboo and the number of each of them
     */
    public Map<Color, Integer> getBambooSectionList() { return this.bambooSectionList; }

    /**
     * Check if the objective is validated. The player bamboo reserve contains the 
     * @param board the game board
     * @param player the player
     * @return true if the objective is validated false otherwise
     */
    @Override
    public boolean checkValidated(Board board, Player player)
    {
        Objects.requireNonNull(board, "board must not be null");
        Objects.requireNonNull(player, "player must not be null");
        Map<Color, Integer> playerReserve = player.getBambooSectionReserve();

        for (Color bambooColor : this.bambooSectionList.keySet())
        {
            Integer objectiveBamboo = this.bambooSectionList.get(bambooColor);
            Integer playerBamboo = playerReserve.get(bambooColor);
            if (playerBamboo < objectiveBamboo)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the objective is validated with given Map. Useful for the bots
     *
     * @param playerReserve Map for the player's bamboo reserve
     * @return true if validated with given parameters, false otherwise
     */
    public boolean checkValidated(Map<Color, Integer> playerReserve)
    {
        for (Color bambooColor : this.bambooSectionList.keySet())
        {
            Integer objectiveBamboo = this.bambooSectionList.get(bambooColor);
            Integer playerBamboo = playerReserve.get(bambooColor);
            if (playerBamboo < objectiveBamboo)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Describe the Panda Objective, gives it number of points, the number of each bamboo section color
     *
     * @return a String with the number of points and the number of each bamboo section color
     */
    @Override
    public String toString()
    {
        StringBuilder message = new StringBuilder("PandaObjective : Nombre de points de l'objectif : " + points);
        for (Color bambooColor : this.bambooSectionList.keySet())
        {
            Integer numberOfEachColor = this.bambooSectionList.get(bambooColor);
            if (numberOfEachColor!=0){
                message.append(bambooColor.toString());
                message.append(":"+ numberOfEachColor);
            }
        }
        return message.toString();
    }
}
