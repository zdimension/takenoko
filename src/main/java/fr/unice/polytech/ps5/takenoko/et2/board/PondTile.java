package fr.unice.polytech.ps5.takenoko.et2.board;

/**
 * Pond tile. This tile can only be present once on the game board, at the center.
 */
public class PondTile extends Tile
{
    public PondTile()
    {
        for (int i = 0; i < this.edges.length; i++)
        {
            this.edges[i] = new Edge(this);
        }
    }

    /**
     * @return the String of the PondTile
     */
    @Override
    public String toString() {
        String pondTileString = "Pond Tile : ";
        for (Edge edge : edges) {
            pondTileString += edge.toString() + ",";
        }
        return pondTileString;
    }
}
