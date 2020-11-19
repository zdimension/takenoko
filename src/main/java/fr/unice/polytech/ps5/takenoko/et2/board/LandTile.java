package fr.unice.polytech.ps5.takenoko.et2.board;

import fr.unice.polytech.ps5.takenoko.et2.BambooSection;
import fr.unice.polytech.ps5.takenoko.et2.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Land plot tile.
 */
public class LandTile extends Tile implements Cloneable
{
    /**
     * Color of the land.
     */
    private final Color color;
    private final List<BambooSection> bamboo;
    private static int maxBambooSize = 4;

    public LandTile(Color color)
    {
        this.color = Objects.requireNonNull(color, "color must not be null");
        bamboo = new ArrayList<>();
    }

    public Color getColor()
    {
        return color;
    }

    /**
     * @param bambooSection to add to Tile
     * @return if success or failure to add bambooSection to tile
     * @throws Exception
     */
    public boolean growBambooSection(BambooSection bambooSection) throws IllegalArgumentException
    {
        if (bamboo.size() == maxBambooSize || !isIrrigated())
        {
            return false;
        }

        if (bambooSection == null)
        {
            throw new IllegalArgumentException("bamboo section must not be null");
        }

        if (bambooSection.getColor() != this.color)
        {
            throw new IllegalArgumentException("bamboo and tile should have the same color");
        }

        bamboo.add(bambooSection);
        return true;
    }

    public int getBambooSize()
    {
        return bamboo.size();
    }

    /**
     * Check if the Tile is irrigated
     *
     * @return true if the Tile is irrigated, false otherwise
     */
    public boolean isIrrigated()
    {
        for (Edge edge : edges)
        {
            if (edge == null)
            {
                continue;
            }
            if (edge.isIrrigated() || (edge.getOther(this) instanceof PondTile))
            {
                return true;
            }
        }
        return false;
    }

    public List<BambooSection> getBamboo()
    {
        return bamboo;
    }

    public Object clone()
    {
        LandTile o = new LandTile(color);
        o.bamboo.clear();
        for (BambooSection bambooSection : bamboo)
        {
            o.bamboo.add(new BambooSection(bambooSection.getColor()));
        }
        return (Object) o;
    }

    /**
     * @return a String describing the LandTile
     */
    @Override
    public String toString()
    {
        return "[Land tile, " + this.color + ", " + isIrrigated() + ", " + bamboo + "]";
    }
}
