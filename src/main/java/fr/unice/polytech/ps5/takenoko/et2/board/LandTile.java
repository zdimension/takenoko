package fr.unice.polytech.ps5.takenoko.et2.board;

import fr.unice.polytech.ps5.takenoko.et2.Color;

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
    private int bambooCount = 0;
    private static final int maxBambooSize = 4;

    public LandTile(Color color)
    {
        this.color = Objects.requireNonNull(color, "color must not be null");
    }

    public Color getColor()
    {
        return color;
    }

    /**
     * @return if success or failure to add bamboo section to tile
     */
    public boolean growBambooSection()
    {
        if (bambooCount == maxBambooSize || !isIrrigated())
        {
            return false;
        }

        bambooCount++;

        return true;
    }

    public int getBambooSize()
    {
        return bambooCount;
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

    public Object clone()
    {
        LandTile o = new LandTile(color);
        o.bambooCount = bambooCount;
        return o;
    }

    public boolean equals(Object o)
    {
        if (!(o instanceof LandTile))
        {
            return false;
        }
        LandTile landTile = (LandTile) o;
        return (landTile.color == color && landTile.bambooCount == this.bambooCount);// todo
    }

    /**
     * @return a String describing the LandTile
     */
    @Override
    public String toString()
    {
        return "[Land tile, " + this.color + ", " + isIrrigated() + ", " + bambooCount + " sections]";
    }
}
