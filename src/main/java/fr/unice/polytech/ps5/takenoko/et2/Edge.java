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
     * Sets the second tile
     * @param tile
     * @return true if success, false if failure
     */
    public boolean setTile(Tile tile){
        if(tiles[1] != null)
            return false;

        //TODO si c'est pas a cote ca echoue

        tiles[1] = tile;
        return true;
    }
}
