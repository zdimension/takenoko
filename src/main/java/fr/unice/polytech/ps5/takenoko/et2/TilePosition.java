package fr.unice.polytech.ps5.takenoko.et2;

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
    TilePosition add(TilePosition other)
    {
        return new TilePosition(x + other.x, y + other.y);
    }

    /**
     * @param factor multiplication factor
     * @return scalar multiplication of the vector by the specifified factor
     */
    TilePosition mul(int factor)
    {
        return new TilePosition(x * factor, y * factor);
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

    public int getEdgeNum()
    {
        if (x == 0)
        {
            return (y == 1) ? 0 : 3;
        }
        if (y == 0)
        {
            return (x == 1) ? 1 : 4;
        }
        return (x > y) ? 2 : 5;
    }
}
