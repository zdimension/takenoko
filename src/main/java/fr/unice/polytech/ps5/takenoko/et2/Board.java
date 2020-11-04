package fr.unice.polytech.ps5.takenoko.et2;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Main game board.
 */
public class Board
{
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
     * @return a stream containing the neighboring tiles
     */
    private Stream<Tile> getNeighbors(TilePosition pos)
    {
        return IntStream.range(-1, 2)
            .mapToObj(dx ->
                IntStream.range(-1, 2)
                    .filter(dy -> dx != dy)
                    .mapToObj(dy -> findTile(pos.add(new TilePosition(dx, dy))))
                    .filter(Objects::nonNull)
            )
            .flatMap(s -> s);
    }

    /**
     * @param pos desired position
     * @return whether it is possible or not to place a tile at the specified position
     */
    public boolean isValid(TilePosition pos)
    {
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
}
