package fr.unice.polytech.ps5.takenoko.et2;

/**
 * Edge between two tiles.
 */
public class Edge
{
    private final Tile[] tiles = new Tile[2];

    /**
     * Creates an edge from the specified tile.
     *
     * @param tile base tile
     */
    public Edge(Tile tile)
    {
        tiles[0] = tile;
        tiles[1] = null;
    }

    /**
     * @param index tile number (0 or 1)
     * @return the tile corresponding to the specified number
     */
    public Tile getTile(int index)
    {
        return tiles[index];
    }

    /**
     * @param first tile on the one side
     * @return the tile on the other side of the edge
     */
    public Tile getOther(Tile first)
    {
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
        if (tiles[1] != null)
        {
            return false;
        }

        tiles[1] = tile;
        return true;
    }
}
