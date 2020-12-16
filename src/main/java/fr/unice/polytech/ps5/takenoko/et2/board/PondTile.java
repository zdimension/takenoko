package fr.unice.polytech.ps5.takenoko.et2.board;

import java.util.Objects;

/**
 * Pond tile. This tile can only be present once on the game board, at the center.
 */
public class PondTile extends Tile
{
    /**
     * Constructor. Should be called once
     */
    public PondTile()
    {
        for (int i = 0; i < this.edges.length; i++)
        {
            this.edges[i] = new Edge(this);
        }
    }

    @Override
    public String toString()
    {
        return "[Pond tile]";
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof PondTile && Objects.equals(position, ((PondTile) obj).position);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.position);
    }
}
