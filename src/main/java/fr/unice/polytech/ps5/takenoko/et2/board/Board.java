package fr.unice.polytech.ps5.takenoko.et2.board;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Main game board.
 */
public class Board
{
    /**
     * Mapping LUT between edge numbers and generator vectors
     */
    public static final List<TilePosition> edgeNumbers = Collections.unmodifiableList(List.of(
        new TilePosition(0, 1),
        new TilePosition(1, 0),
        new TilePosition(1, -1),
        new TilePosition(0, -1),
        new TilePosition(-1, 0),
        new TilePosition(-1, 1)
    ));
    private final PondTile center;
    private final Map<TilePosition, Tile> tileCache;

    /**
     * Initializes a Board instance.
     */
    public Board()
    {
        this.center = new PondTile();
        this.tileCache = new HashMap<>();
        this.tileCache.put(TilePosition.ZERO, center);
    }

    /**
     * @return the center (pond) tile of the board
     */
    public PondTile getCenter()
    {
        return center;
    }

    /**
     * @return the map associating vectors with tiles
     */
    public Map<TilePosition, Tile> getTiles()
    {
        return Collections.unmodifiableMap(tileCache);
    }

    /**
     * @param pos tile position
     * @return the tile at the specified position
     */
    public Tile findTile(TilePosition pos)
    {
        return tileCache.getOrDefault(pos, null);
    }

    /**
     * @param pos tile position
     * @return a stream containing the neighboring tile positions
     */
    public Stream<TilePosition> getNeighboringPositions(TilePosition pos)
    {
        return IntStream.range(-1, 2)
            .mapToObj(dx ->
                IntStream.range(-1, 2)
                    .filter(dy -> dx != dy)
                    .mapToObj(dy -> new TilePosition(dx, dy))
            )
            .flatMap(s -> s);
    }

    /**
     * @param pos tile position
     * @return a stream containing the neighboring tiles
     */
    private Stream<Tile> getNeighbors(TilePosition pos)
    {
        return getNeighboringPositions(pos)
            .map(this::findTile)
            .filter(Objects::nonNull);
    }

    /**
     * @param pos desired position
     * @return whether it is possible or not to place a tile at the specified position
     */
    public boolean isValid(TilePosition pos)
    {
        if (this.tileCache.containsKey(pos))
        {
            return false;
        }

        int count = 0;

        for (Iterator<Tile> it = getNeighbors(pos).iterator(); it.hasNext(); )
        {
            Tile p = it.next();

            // we can put a plot next to the pond
            if (p == center) return true;

            // or next to two existing plots
            if (++count == 2) return true;
        }

        return false;
    }

    /**
     * @param tile to add
     * @param pos  of the tile to add
     * @return true if the tile was added, false if it failed
     */
    public boolean addTile(LandTile tile, TilePosition pos)
    {
        if (!isValid(pos))
        {
            return false;
        }

        tileCache.put(pos, tile);

        for (var i = 0; i < 6; i++)
        {
            var nb = findTile(pos.add(edgeNumbers.get(i)));
            if (nb != null)
            {
                var edge = nb.getEdge(i + 3);
                edge.setTile(tile);
                tile.edges[i] = edge;
            }
            else
            {
                tile.edges[i] = new Edge(tile);
            }
        }

        return true;
    }
}
