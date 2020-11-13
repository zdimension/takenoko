package fr.unice.polytech.ps5.takenoko.et2.board;

import fr.unice.polytech.ps5.takenoko.et2.Color;

import java.util.Objects;

/**
 * Land plot tile.
 */
public class LandTile extends Tile
{
    /**
     * Color of the land.
     */
    private final Color color;

    public LandTile(Color color)
    {
        this.color = Objects.requireNonNull(color, "color must not be null");
    }

    public Color getColor()
    {
        return color;
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
            Tile neighbour = edge.getOther(this);
            if (edge.isIrrigated() || (neighbour != null && (neighbour instanceof PondTile)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @return a String describing the LandTile
     */
    @Override
    public String toString()
    {
        return "[Land tile, " + this.color + "]";
    }
}
