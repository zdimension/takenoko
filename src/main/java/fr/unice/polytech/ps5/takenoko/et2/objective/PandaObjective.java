package fr.unice.polytech.ps5.takenoko.et2.objective;

import fr.unice.polytech.ps5.takenoko.et2.Color;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;

import java.util.*;

/**
 * The class representing the gardener objective
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

    @Override
    public boolean checkValidated(Board board, Player player)
    {
        Objects.requireNonNull(board, "board must not be null");
        Objects.requireNonNull(player, "player must not be null");
        Map<Color, Integer> playerReserve = player.getBambooSectionReserve();

        for (Color bambooColor : this.bambooSectionList.keySet()) {
            Integer objectiveBamboo = this.bambooSectionList.get(bambooColor);
            Integer playerBamboo = playerReserve.get(bambooColor);
            if(playerBamboo<objectiveBamboo) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder message = new StringBuilder("PandaObjective : Nombre de points de l'objectif : " + points);
        for (Color bambooColor : this.bambooSectionList.keySet()) {
            Integer numberOfEachColor = this.bambooSectionList.get(bambooColor);
            if (numberOfEachColor!=0){
                message.append(bambooColor.toString());
                message.append(":"+ numberOfEachColor);
            }
        }
        return message.toString();
    }
}
