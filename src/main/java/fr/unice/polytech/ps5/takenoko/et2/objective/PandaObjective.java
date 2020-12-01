package fr.unice.polytech.ps5.takenoko.et2.objective;

import fr.unice.polytech.ps5.takenoko.et2.Color;
import fr.unice.polytech.ps5.takenoko.et2.Game;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;

import java.util.ArrayList;
import java.util.List;

/**
 * The class representing the gardener objective
 */
public class PandaObjective extends Objective
{

    private final List<Color> listColors = new ArrayList<>();
    private final int numberOfBambooSection;

    /**
     * Constructor
     *
     * @param points                points of the Objective
     * @param listColors            a list of bamboo section's color eg [PINK, YELLOW, GREEN]
     * @param numberOfBambooSection number of bamboo section
     */
    public PandaObjective(int points, List<Color> listColors, int numberOfBambooSection)
    {
        super(points);
        this.numberOfBambooSection = numberOfBambooSection;
        if (listColors.isEmpty())
        {
            throw new IllegalArgumentException("Invalid Objective");
        }
        this.listColors.addAll(listColors);

    }

    @Override
    public boolean checkValidated(Game game)
    {
        return false;
    }

    @Override
    public boolean checkValidated(Board board)
    {
        return false;
    }

    @Override
    public String toString()
    {
        StringBuilder message = new StringBuilder("PandaObjective : Nombre de points de l'objectif : " + points
            + "couleurs des sections de bambous : ");
            for (Color color : listColors) {
                message.append(color.toString());
            }
            message.append("nombre de sections de bambou : ").append(numberOfBambooSection);
        return message.toString();
    }
}
