package fr.unice.polytech.ps5.takenoko.et2.board;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Base tile class.
 */
public abstract class Tile
{
    protected final Edge[] edges = new Edge[6];

    private static int fixEdgeNum(int num)
    {
        num %= 6;
        if (num < 0)
        {
            num += 6;
        }
        return num;
    }

    /**
     * @param num edge index
     * @return the edge corresponding to the specified number
     */
    public Edge getEdge(int num)
    {
        return edges[fixEdgeNum(num)];
    }

    public Stream<Edge> getEdges()
    {
        return Arrays.stream(edges).filter(Objects::nonNull);
    }

    /**
     * @return a String describing the Tile
     */
    public abstract String toString();

}
