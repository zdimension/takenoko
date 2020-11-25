package fr.unice.polytech.ps5.takenoko.et2.decision;

import fr.unice.polytech.ps5.takenoko.et2.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import fr.unice.polytech.ps5.takenoko.et2.board.Edge;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.util.Pair;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DecisionMakerDebugger extends DecisionMaker
{
    private final Scanner sc;

    /**
     * Class constructor
     *
     * @param player
     */
    public DecisionMakerDebugger(Player player)
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
