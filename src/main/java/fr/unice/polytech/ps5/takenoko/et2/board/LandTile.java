package fr.unice.polytech.ps5.takenoko.et2.board;

import fr.unice.polytech.ps5.takenoko.et2.enums.Color;

import java.util.Objects;

/**
 * Land plot tile.
 */
public class LandTile extends Tile implements Cloneable
{
    private static final int maxBambooSize = 4;
    /**
     * Color of the land.
     */
    private final Color color;
    private LandTileImprovement landTileImprovement = null;
    private int bambooCount = 0;

    public LandTile(Color color)
    {
        this.color = Objects.requireNonNull(color, "color must not be null");
    }

    public LandTile(Color color, LandTileImprovement landTileImprovement)
    {
        this(color);
        this.landTileImprovement = landTileImprovement;
    }

    public Color getColor()
    {
        return color;
    }

    public LandTileImprovement getLandTileImprovement()
    {
        return landTileImprovement;
    }

    public boolean setLandTileImprovement(LandTileImprovement landTileImprovement)
    {
        if (bambooCount != 0)
        {
            return false;
        }
        this.landTileImprovement = landTileImprovement;
        return true;
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

        if (landTileImprovement == LandTileImprovement.FERTILIZER && (bambooCount < maxBambooSize))
        {
            bambooCount++;
        }

        return true;
    }

    /**
     * @return if success or failure to remove bamboo section to tile
     */
    public boolean cutBambooSection()
    {
        if (bambooCount == 00)
        {
            return false;
        }

        bambooCount--;

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
        if (landTileImprovement == LandTileImprovement.WATERSHED)
        {
            return true;
        }
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
        LandTile o = new LandTile(color, landTileImprovement);
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
        return (landTile.color == color && landTile.bambooCount == this.bambooCount && landTile.landTileImprovement == landTileImprovement);
    }

    /**
     * @return a String describing the LandTile
     */
    @Override
    public String toString()
    {
        return "[Land tile, " + this.color + ", " + isIrrigated() + ", " + bambooCount + " sections, " + (landTileImprovement == null ? "No improvement" : landTileImprovement) + "]";
    }
}
