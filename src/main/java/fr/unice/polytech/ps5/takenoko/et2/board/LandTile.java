package fr.unice.polytech.ps5.takenoko.et2.board;

import fr.unice.polytech.ps5.takenoko.et2.enums.Color;

import java.util.Objects;

/**
 * Land plot tile. Bamboo can grow on it if it is irrigated
 */
public class LandTile extends Tile implements Cloneable
{
    private static final int maxBambooSize = 4;
    private final Color color; // Color of the land
    private LandTileImprovement landTileImprovement = null;
    private int bambooCount = 0;

    /**
     * Constructor, generate a new LandTile with the given Color
     *
     * @param color Color of the LandTile
     */
    public LandTile(Color color)
    {
        this.color = Objects.requireNonNull(color, "color must not be null");
    }

    /**
     * Constructor, generate a new LandTile with the given Color and Improvement
     *
     * @param color               Color of the LandTile
     * @param landTileImprovement Improvement of the LandTile
     */
    public LandTile(Color color, LandTileImprovement landTileImprovement)
    {
        this(color);
        this.landTileImprovement = landTileImprovement;
    }

    /**
     * Get the LandTile's Color
     *
     * @return Color of the LandTile
     */
    public Color getColor()
    {
        return color;
    }

    /**
     * Get the LandTile's improvement
     *
     * @return The LandTileImprovement
     */
    public LandTileImprovement getLandTileImprovement()
    {
        return landTileImprovement;
    }

    /**
     * @return whether an improvement can be added to the tile
     */
    boolean canSetImprovement()
    {
        return bambooCount == 0 && landTileImprovement == null;
    }

    /**
     * Set the LandTileImprovement for this LandTile. Possible only if there is no bamboo and no other improvement
     *
     * @param landTileImprovement The improvement
     * @return true if the change is ok, false otherwise
     */
    public boolean setLandTileImprovement(LandTileImprovement landTileImprovement)
    {
        if (!canSetImprovement())
        {
            return false;
        }
        if (landTileImprovement == LandTileImprovement.WATERSHED && !isIrrigated())
        {
            growBambooSection();
        }
        this.landTileImprovement = landTileImprovement;
        return true;
    }

    /**
     * @return if success or failure to add bamboo section to tile
     */
    public boolean growBambooSection()
    {
        if (!canGrowBamboo())
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

    public boolean canGrowBamboo()
    {
        return bambooCount < maxBambooSize && isIrrigated();
    }

    /**
     * @return if success or failure to remove bamboo section to tile
     */
    public boolean cutBambooSection()
    {
        if (bambooCount == 0)
        {
            return false;
        }

        bambooCount--;

        return true;
    }

    /**
     * Get the size of th bamboo in this LandTile
     *
     * @return Size of the bamboo
     */
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

    /**
     * Clone the current LandTile into a new Object
     *
     * @return the new cloned Landtile, with an Object object
     */
    public Object clone()
    {
        LandTile o = new LandTile(color, landTileImprovement);
        o.bambooCount = bambooCount;
        return o;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof LandTile))
        {
            return false;
        }
        LandTile landTile = (LandTile) o;
        return (landTile.color == color && landTile.bambooCount == this.bambooCount && landTile.landTileImprovement == landTileImprovement && Objects.equals(landTile.position, this.position));
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(color, landTileImprovement, bambooCount, position);
    }

    @Override
    public String toString()
    {
        return "[Land tile, " + this.color + ", " + isIrrigated() + ", " + bambooCount + " sections, " + (landTileImprovement == null ? "No improvement" : landTileImprovement) + "]";
    }
}
