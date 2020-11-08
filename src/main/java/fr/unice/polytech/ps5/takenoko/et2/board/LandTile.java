package fr.unice.polytech.ps5.takenoko.et2.board;

import fr.unice.polytech.ps5.takenoko.et2.Color;

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

    /**
     * @return a String describing the LandTile
     */
    @Override
    public String toString()
    {
        String landTileString = "Land Tile : " + this.color;
        for (Edge edge : edges)
        {
            landTileString += edge.toString() + ",";
        }
        return landTileString;
    }
}
