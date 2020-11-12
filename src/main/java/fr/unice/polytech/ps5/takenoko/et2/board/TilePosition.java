package fr.unice.polytech.ps5.takenoko.et2.board;

import java.util.Objects;

/**
 * 2D hexagonal-space vector
 */
public class TilePosition
{
    public static final TilePosition ZERO = new TilePosition(0, 0);

    /**
     * X coordinate
     */
    private final int x;
    /**
     * Y coordinate
     */
    private final int y;

    /**
     * Instanciates a new vector
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public TilePosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Position comparer. To be used only when deterministic storage order is needed.
     *
     * @param a first vector
     * @param b second vector
     * @return sign of comparison
     */
    static int storageComparer(TilePosition a, TilePosition b)
    {
        var xc = Integer.compare(a.x, b.x);
        if (xc != 0)
        {
            return xc;
        }
        return Integer.compare(a.y, b.y);
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    /**
     * @param other addend
     * @return the elementwise sum of the two vectors
     */
    public TilePosition add(TilePosition other)
    {
        return new TilePosition(x + other.x, y + other.y);
    }

    @Override
    public boolean equals(Object other)
    {
        if (other instanceof TilePosition)
        {
            TilePosition pos = (TilePosition) other;
            return pos.x == x && pos.y == y;
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }

    /**
     * @return a String of the position of the tile
     */
    @Override
    public String toString()
    {
        return "[Position (" + x + ", " + y + ")]";
    }
}
