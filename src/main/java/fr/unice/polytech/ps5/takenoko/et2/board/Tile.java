package fr.unice.polytech.ps5.takenoko.et2.board;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Base tile class. Mother of {@link fr.unice.polytech.ps5.takenoko.et2.board.LandTile} and {@link fr.unice.polytech.ps5.takenoko.et2.board.PondTile}
 */
public abstract class Tile
{
    final Edge[] edges = new Edge[6];
    TilePosition position = null;

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

    /**
     * Get all the Edges of the Tile
     *
     * @return A Stream for all the Edges (6)
     */
    public Stream<Edge> getEdges()
    {
        return Arrays.stream(edges).filter(Objects::nonNull);
    }

    /**
     * Get the Tile's position, if known
     *
     * @return the Tile position, in the Board
     */
    public Optional<TilePosition> getPosition()
    {
        return Optional.ofNullable(position);
    }

    /**
     * Set the position of the Tile (doesn't change the real position, only useful for getPosition)
     *
     * @param position The position of the Tile
     */
    public void setPosition(TilePosition position)
    {
        this.position = position;
    }

    /**
     * @return a String describing the Tile
     */
    public abstract String toString();

}
