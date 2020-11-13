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
     * @return a String describing the LandTile
     */
    @Override
    public String toString()
    {
        return "[Land tile, " + this.color + "]";
    }
}
