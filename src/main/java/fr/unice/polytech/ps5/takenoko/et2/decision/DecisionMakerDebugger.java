package fr.unice.polytech.ps5.takenoko.et2.decision;

import fr.unice.polytech.ps5.takenoko.et2.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import fr.unice.polytech.ps5.takenoko.et2.board.Edge;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;
import fr.unice.polytech.ps5.takenoko.et2.util.Pair;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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

    @Override
    public GameAction chooseAction(List<GameAction> base)
    {
        Objects.requireNonNull(base, "base must not be null");
        int input;
        do
        {
            System.out.println("Possible actions :");
            for (GameAction action : base)
            {
                System.out.println(action);
            }
            System.out.println("Choose one (0, 1, 2...) :");

            input = sc.nextInt();
        }
        while (input < 0 || input > base.size() - 1);

        return base.get(input);
    }

    @Override
    public Pair<LandTile, TilePosition> chooseTile(List<LandTile> drawnTiles, List<TilePosition> validPos)
    {
        Objects.requireNonNull(validPos, "validPos must not be null");
        Objects.requireNonNull(drawnTiles, "drawnTiles must not be null");
        if (drawnTiles.size() != 3)
        {
            throw new IllegalArgumentException();
        }
        int input;

        do
        {
            System.out.println("Drawn tiles :");
            drawnTiles.forEach(System.out::println);
            System.out.println("Choose one (0, 1 or 2) :");

            input = sc.nextInt();
        }
        while (input < 0 || input > 2);

        System.out.println(drawnTiles.get(input) + " chosen");
        return Pair.of(drawnTiles.get(input), readPosition(validPos));
    }

    @Override
    public Objective chooseObjectiveToComplete(List<Objective> validObjectives)
    {
        Objects.requireNonNull(validObjectives, "validObjectives must not be null");
        int input;
        int numberCards = validObjectives.size();
        do
        {
            System.out.println("Your Objectives :");
            validObjectives.forEach(System.out::println);
            System.out.println("Choose one (0, 1, 2...) :");
            input = sc.nextInt();
        }
        while (input < 0 || input > numberCards - 1);

        return validObjectives.get(input);
    }

    @Override
    public Edge chooseIrrigationPosition(List<Edge> irrigableEdges)
    {
        Objects.requireNonNull(irrigableEdges, "irrigableEdges must not be null");
        int input;
        int numberCards = irrigableEdges.size();
        do
        {
            System.out.println("Irrigable edges :");
            irrigableEdges.forEach(System.out::println);
            System.out.println("Choose one (0, 1, 2...) :");
            input = sc.nextInt();
        }
        while (input < 0 || input > numberCards - 1);

        return irrigableEdges.get(input);
    }

    @Override
    public Class<? extends Objective> chooseDeck(List<Class<? extends Objective>> available)
    {
        int input;
        Class<? extends Objective> clazz;
        do
        {
            System.out.println("Available decks :");
            System.out.println("0 : ObjectivePlot");
            System.out.println("Choose one (0, 1, 2...) :");
            input = sc.nextInt();
            if (input == 1)
            {
                clazz = PlotObjective.class;
                break;
            }
        }
        while (true);

        return clazz;
    }

    @Override
    public TilePosition chooseGardenerTarget(List<TilePosition> validPos)
    {
        Objects.requireNonNull(validPos, "validPos must not be null");

        return readPosition(validPos);
    }

    private TilePosition readPosition(List<TilePosition> validPos)
    {
        int inputx;
        int inputy;
        TilePosition tilePosition;
        do
        {
            System.out.println("Board :");
            this.getBoard().getTiles().forEach((k, v) -> System.out.println(k + " : " + v));
            System.out.println("Choose x axis : ");
            inputx = sc.nextInt();
            System.out.println("Choose y axis : ");
            inputy = sc.nextInt();
            tilePosition = new TilePosition(inputx, inputy);
        }
        while (!validPos.contains(tilePosition));
        return tilePosition;
    }
}
