package fr.unice.polytech.ps5.takenoko.et2.decision;

import fr.unice.polytech.ps5.takenoko.et2.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import fr.unice.polytech.ps5.takenoko.et2.Weather;
import fr.unice.polytech.ps5.takenoko.et2.board.Edge;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTileImprovement;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.util.Pair;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Debugging class initially created when no bot had been born yet to check wheather the
 * interaction architecture between DecisionMaker and Game was viable. Can be used to test any
 * other class if the bots are not up to date
 */
public class CLIDecisionMaker extends DecisionMaker
{
    private final Scanner sc;

    /**
     * Class constructor
     *
     * @param player
     */
    public CLIDecisionMaker(Player player)
    {
        super(player);
        sc = new Scanner(System.in);
    }

    private <T> T chooseItem(List<T> items)
    {
        Objects.requireNonNull(items, "list must not be null");

        int input;
        do
        {
            System.out.println("Available choices :");
            for (int i = 0; i < items.size(); i++)
            {
                System.out.printf("%d - %s%n", i, items.get(i).toString());
            }
            System.out.println("Choose one (0, 1, 2...) :");
            input = sc.nextInt();
        }
        while (input < 0 || input >= items.size());

        return items.get(input);
    }

    @Override
    public GameAction chooseAction(List<GameAction> base)
    {
        return chooseItem(base);
    }

    @Override
    public Pair<LandTile, TilePosition> chooseTile(List<LandTile> drawnTiles, List<TilePosition> validPos)
    {
        return Pair.of(chooseItem(drawnTiles), readPosition(validPos));
    }

    @Override
    public Objective chooseObjectiveToComplete(List<Objective> validObjectives)
    {
        return chooseItem(validObjectives);
    }

    @Override
    public Edge chooseIrrigationPosition(List<Edge> irrigableEdges)
    {
        return chooseItem(irrigableEdges);
    }

    @Override
    public Class<? extends Objective> chooseDeck(List<Class<? extends Objective>> available)
    {
        return chooseItem(available);
    }

    @Override
    public TilePosition chooseGardenerTarget(List<TilePosition> validPos)
    {
        return readPosition(validPos);
    }

    @Override
    public Weather chooseWeather(List<Weather> weatherList)
    {
        return chooseItem(weatherList);
    }

    @Override
    public TilePosition choosePandaTarget(List<TilePosition> valid)
    {
        return valid.get(0);
    }

    @Override
    public LandTile chooseTileToAddBamboo(List<LandTile> listIrrigatedTiles)
    {
        return chooseItem(listIrrigatedTiles);
    }

    @Override
    public LandTileImprovement chooseLandTileImprovement(List<LandTileImprovement> listLandTileImprovements)
    {
        return listLandTileImprovements.get(0); // TODO
    }

    @Override
    public Pair<LandTile, LandTileImprovement> chooseImprovementAndLandTile(List<LandTile> vacantLandTile, List<LandTileImprovement> availableImprovements)
    {
        return Pair.of(vacantLandTile.get(0), availableImprovements.get(0)); // TODO
    }

    private TilePosition readPosition(List<TilePosition> validPos)
    {
        Objects.requireNonNull(validPos, "validPos must not be null");

        var validX = validPos.stream().map(TilePosition::getX).collect(Collectors.toList());

        int inputx;
        int inputy;
        TilePosition tilePosition;
        System.out.println("Board :");
        this.getBoard().getTiles().forEach((k, v) -> System.out.println(k + " : " + v));
        do
        {
            System.out.println("Choose x axis : ");
            inputx = sc.nextInt();
        }
        while (!validX.contains(inputx));

        do
        {
            System.out.println("Choose y axis : ");
            inputy = sc.nextInt();
            tilePosition = new TilePosition(inputx, inputy);
        }
        while (!validPos.contains(tilePosition));
        return tilePosition;
    }
}
