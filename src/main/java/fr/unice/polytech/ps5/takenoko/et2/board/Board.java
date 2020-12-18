package fr.unice.polytech.ps5.takenoko.et2.board;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Main game board. Contains PondTile as in the middle.
 */
public class Board implements Cloneable
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
    private final List<TilePosition> orderAdd = new ArrayList<>();

    /**
     * Initializes a Board instance.
     */
    public Board()
    {
        this.center = new PondTile();
        this.center.setPosition(TilePosition.ZERO);
        this.tileCache = new HashMap<>();
        this.tileCache.put(TilePosition.ZERO, center);
        for (int i = 0; i < 6; i++)
        {
            center.edges[i] = new Edge(center);
        }
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
        Objects.requireNonNull(pos, "tile position must not be null");
        return tileCache.getOrDefault(pos, null);
    }

    /**
     * @param pos tile position
     * @return a stream containing the neighboring tile positions
     */
    public Stream<TilePosition> getNeighboringPositions(TilePosition pos)
    {
        Objects.requireNonNull(pos, "tile position must not be null");
        return IntStream.range(-1, 2)
            .mapToObj(dx ->
                IntStream.range(-1, 2)
                    .filter(dy -> dx != dy)
                    .mapToObj(dy -> pos.add(new TilePosition(dx, dy)))
            )
            .flatMap(s -> s);
    }

    /**
     * @return a stream containing all valid empty positions where a drawn tile can be put
     */
    public Stream<TilePosition> getValidEmptyPositions()
    {
        return tileCache.keySet()
            .stream()
            .flatMap(t ->
                getNeighboringPositions(t)
                    .filter(this::isValid));
    }

    /**
     * @param pos tile position
     * @return a stream containing the neighboring tiles
     */
    private Stream<Tile> getNeighbors(TilePosition pos)
    {
        Objects.requireNonNull(pos, "tile position must not be null");
        return getNeighboringPositions(pos)
            .map(this::findTile)
            .filter(Objects::nonNull);
    }

    /**
     * @param pos desired position
     * @return whether it is possible or not to place a tile at the specified position
     */
    private boolean isValid(TilePosition pos)
    {
        Objects.requireNonNull(pos, "tile position must not be null");
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
        return addTileInternal(tile, pos, true);
    }

    private boolean addTileInternal(LandTile tile, TilePosition pos, boolean irrigate)
    {
        Objects.requireNonNull(tile, "tile must not be null");
        Objects.requireNonNull(pos, "tile position must not be null");
        if (!isValid(pos))
        {
            return false;
        }

        tile.setPosition(pos);
        tileCache.put(pos, tile);

        for (var i = 0; i < 6; i++)
        {
            var nb = findTile(pos.add(edgeNumbers.get(i)));
            if (nb != null)
            {
                var edge = nb.getEdge(i + 3);
                edge.setTile(tile);
                tile.edges[i] = edge;
                if (nb instanceof PondTile)
                {
                    edge.irrigated = true;
                }
            }
            else
            {
                tile.edges[i] = new Edge(tile);
            }
        }

        if (irrigate)
        {
            tile.growBambooSection();
        }

        orderAdd.add(pos);
        return true;
    }

    public Edge getEdgeBetweenTwoTiles(Tile t1, Tile t2)
    {
        for (int i = 0; i < 6; i++)
        {
            Edge e = t1.getEdge(i);
            if (e.getOther(t1) == t2)
            {
                return e;
            }
        }
        return null;
    }

    /**
     * @return stream of irrigated tiles on board
     */
    public Map<TilePosition, LandTile> getIrrigatedTiles()
    {
        var ret = tileCache.entrySet().stream().filter(map -> map.getValue() instanceof LandTile);
        return ret.filter(map -> ((LandTile) map.getValue()).isIrrigated()).collect(Collectors.toMap(Map.Entry::getKey, e -> (LandTile) e.getValue()));
    }

    public Stream<LandTile> getLandTilesWithoutImprovement()
    {
        return tileCache.values().stream()
            .filter(LandTile.class::isInstance).map(x -> (LandTile) x)
            .filter(LandTile::canSetImprovement);
    }

    /**
     * Get all the Land Tiles of the Board
     *
     * @return a Set of all the Land Tiles
     */
    public Set<LandTile> getLandTiles()
    {
        return tileCache.values().stream().filter(tile -> tile instanceof LandTile).map(tile -> (LandTile) tile).collect(Collectors.toSet());
    }

    /**
     * @return a String of the Board
     */
    @Override
    public String toString()
    {
        final var template = new char[][]
            {
                "   _ _   ".toCharArray(),
                " /     \\ ".toCharArray(),
                "/       \\".toCharArray(),
                "\\       /".toCharArray(),
                " \\ _ _ / ".toCharArray(),
            };
        int minX = 0, minY = 0, maxX = 0, maxY = 0;
        for (TilePosition pos : tileCache.keySet())
        {
            if (pos.getX() > maxX)
            {
                maxX = pos.getX();
            }
            if (pos.getX() < minX)
            {
                minX = pos.getX();
            }
            if (pos.getY() > maxY)
            {
                maxY = pos.getY();
            }
            if (pos.getY() < minY)
            {
                minY = pos.getY();
            }
        }
        final int hexWidth = 9;
        final int hexHeight = 5;
        final int hexSide = 2;
        var width = maxX - minX + 1;
        var height = maxY - minY + 1;
        var charsX = (hexWidth - hexSide) * width + 2;
        var charsY = (width - 1) * hexHeight / 2 + height * hexHeight;
        var lines = new char[charsY][charsX];
        for (char[] line : lines)
        {
            Arrays.fill(line, ' ');
        }
        for (TilePosition pos : tileCache.keySet())
        {
            var fixX = pos.getX() - minX;
            var fixY = pos.getY() - minY;
            var coordX = fixX * (hexWidth - hexSide);
            var coordY = fixX * (hexHeight / 2) + fixY * (hexHeight - 1);
            for (int i = 0; i < template.length; i++)
            {
                for (var x = 0; x < 9; x++)
                {
                    var temp = template[i][x];
                    if (temp == ' ')
                    {
                        continue;
                    }
                    lines[coordY + i][coordX + x] = temp;
                }
            }
            var posStr = String.format("%d,%d", pos.getX(), pos.getY()).toCharArray();
            System.arraycopy(posStr, 0, lines[coordY + 2], coordX + 1, posStr.length);
            var tile = tileCache.get(pos);
            if (tile instanceof LandTile)
            {
                posStr = String.valueOf(((LandTile) tile).getBambooSize()).toCharArray();
                System.arraycopy(posStr, 0, lines[coordY + 1], coordX + 3, posStr.length);
            }
            if (tile instanceof PondTile)
            {
                posStr = "POND".toCharArray();
            }
            else if (tile instanceof LandTile)
            {
                posStr = ((LandTile) tile).getColor().toString().toCharArray();
            }
            System.arraycopy(posStr, 0, lines[coordY + 3], coordX + 1, posStr.length);
        }
        return Arrays.stream(lines).map(String::new).collect(Collectors.joining("\n"));
    }

    /**
     * Clone the current Board
     *
     * @return the new Board, as an Object
     */
    public Object clone()
    {
        for (int i = 0; i < 6; i++)
        {
            if (!center.getEdge(i).irrigated)
            {
                throw new IllegalArgumentException("All PondTile's Edges must be irrigated");
            }
        }
        Board o = new Board();
        if (tileCache.size() != orderAdd.size() + 1)
        {
            throw new IllegalArgumentException("Board.clone(): tileCache.size() != orderAdd.size()");
        }
        try
        {
            for (TilePosition tilePosition : orderAdd)
            {
                Tile oldTile = tileCache.get(tilePosition);
                if (!(oldTile instanceof LandTile))
                {
                    continue;
                }
                LandTile newLandTile = (LandTile) ((LandTile) oldTile).clone();
                if (!o.addTileInternal(newLandTile, tilePosition, false))
                {
                    throw new IllegalArgumentException("Board.clone(): irrigation problem");
                }
                for (int i = 0; i < 6; i++)
                {
                    if (oldTile.getEdge(i).isIrrigated())
                    {
                        newLandTile.getEdge(i).irrigated = true;
                    }
                    else
                    {
                        newLandTile.getEdge(i).irrigated = false;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return o;
    }

    /**
     * @return list of tiles of the board that can grow bamboo on it.
     */
    public List<LandTile> getBambooableTiles()
    {
        return getLandTiles()
            .stream()
            .filter(LandTile::canGrowBamboo)
            .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(tileCache, board.tileCache) && Objects.equals(orderAdd, board.orderAdd);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(center, tileCache, orderAdd);
    }
}