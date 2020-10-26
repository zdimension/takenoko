package fr.unice.polytech.ps5.takenoko.et2;

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
        this.color = color;
    }

    public Color getColor()
    {
        return color;
    }
}
