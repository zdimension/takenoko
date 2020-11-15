package fr.unice.polytech.ps5.takenoko.et2.board;

import java.util.Objects;

/**
 * Edge between two tiles.
 */
public class Edge
{
    private final Tile[] tiles = new Tile[2];
    private boolean irrigated = false;

    /**
     * Creates an edge from the specified tile.
     *
     * @param tile base tile
     */
    public Edge(Tile tile)
    {
        Objects.requireNonNull(tile, "tile position must not be null");
        tiles[0] = tile;
        tiles[1] = null;
    }

    /**
     * @param index tile number (0 or 1)
     * @return the tile corresponding to the specified number
     */
    public Tile getTile(int index) { return tiles[index]; }

    /**
     * @param first tile on the one side
     * @return the tile on the other side of the edge
     */
    public Tile getOther(Tile first)
    {
        Objects.requireNonNull(first, "first must not be null");
        if (tiles[0] == first)
        {
            return tiles[1];
        }
        return tiles[0];
    }

    /**
     * @param tile tile to add to the edge
     * @return true if success, false if failure
     */
    public boolean setTile(Tile tile)
    {
        Objects.requireNonNull(tile, "tile must not be null");
        if (tiles[1] != null)
        {
            return false;
        }

        tiles[1] = tile;
        return true;
    }

    /**
     * Add an irrigation to the edge. There is only one irrigation per edge
     *
     * @return an Array of LandTile, with first-irrigated LandTiles
     */
    public LandTile[] addIrrigation()
    {
        if (irrigated)
        {
            return null;
        }
        int sizeReturn = 0;
        boolean firstOk = false; // Berk
        for (int i = 0; i < tiles.length; i++) // Add the bamboo
        {
            if (!(tiles[i] instanceof LandTile))
            {
                continue;
            }
            LandTile landTile = (LandTile) tiles[i];
            if (landTile.isIrrigated())
            {
                continue;
            }
            sizeReturn++;
            firstOk = (i == 0);
        }
        irrigated = true;
        LandTile[] arrayReturn = new LandTile[sizeReturn];
        switch (sizeReturn)
        {
            case 1:
                if (firstOk)
                {
                    arrayReturn[0] = (LandTile) tiles[0];
                }
                else
                {
                    arrayReturn[0] = (LandTile) tiles[1];
                }
                break;
            case 2:
                arrayReturn[0] = (LandTile) tiles[0];
                arrayReturn[1] = (LandTile) tiles[1];
                break;
        }
        return arrayReturn;
    }

    /**
     * Check if there is an irrigation on the Edge
     *
     * @return true if there is an irrigation, false otherwise
     */
    public boolean isIrrigated()
    {
        return irrigated;
    }

    /**
     * @return a String describing the edge
     */
    @Override
    public String toString()
    {
        return "[Edge, 1=" + tiles[0].toString() + ", 2=" + tiles[1].toString() + "]";
    }
}
