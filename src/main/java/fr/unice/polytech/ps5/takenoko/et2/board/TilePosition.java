package fr.unice.polytech.ps5.takenoko.et2.board;

import java.util.Comparator;
import java.util.Objects;

/**
 * 2D hexagonal-space vector
 */
public class TilePosition
{
    /**
     * The null position
     */
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
     * Position comparer. To be used only when deterministic storage order is needed.
     */
    public static final Comparator<TilePosition> storageComparer = Comparator
        .comparingInt(TilePosition::getX)
        .thenComparingInt(TilePosition::getY);

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

    private static int sign(int n)
    {
        return n < 0 ? -1 : 1;
    }

    /**
     * Get X coordinate of TilePosition
     *
     * @return The X coordinate
     */
    public int getX()
    {
        return x;
    }

    /**
     * Get Y coordinate of TilePosition
     *
     * @return The Y coordinate
     */
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

    /**
     * @param other subtrahend
     * @return the elementwise difference of the two vectors
     */
    public TilePosition sub(TilePosition other)
    {
        return new TilePosition(x - other.x, y - other.y);
    }

    /**
     * @return the basis for the specified vector, or null if the vector is a linear combination of multiples bases
     */
    public TilePosition getBasis()
    {
        if (x == 0)
        {
            return new TilePosition(0, sign(y));
        }
        if (y == 0)
        {
            return new TilePosition(sign(x), 0);
        }
        if (x == -y)
        {
            return new TilePosition(sign(x), sign(y));
        }

        return null;
    }

    /**
     * Check if the TilePosition is equal to another object
     *
     * @param other The other Object
     * @return true if the Object is an instance of TilePosition and the fieds are equals, false otherwise
     */
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

    /**
     * HashCode of the position
     *
     * @return Objects.hash(x, y)
     */
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
