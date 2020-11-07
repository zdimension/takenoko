package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DecisionMakerDebugger extends DecisionMaker
{
    private Scanner sc;

    public DecisionMakerDebugger(Player player)
    {
        super(player);
        sc = new Scanner(System.in);
    }

    @Override
    GameAction chooseAction()
    {
        return null; //TODO
    }

    public static DecisionMakerBuilder getBuilder()
    {
        return DecisionMakerDebugger::new;
    }

    LandTile chooseTile(ArrayList<LandTile> drawnTiles)
    {
        if (drawnTiles.size() != 3)
        {
            throw new IllegalArgumentException();
        }
        int input;

        do
        {
            System.out.println("Drawn tiles :");
            drawnTiles.forEach(r -> System.out.println(r));
            System.out.println("Choose one (0, 1 or 2) :");

            input = sc.nextInt();
        }
        while (input < 0 || input > 2);

        System.out.println(drawnTiles.get(input) + " chosen");
        return drawnTiles.get(input);
    }

    @Override
    TilePosition chooseTilePosition(LandTile tile)
    {
        int inputx;
        int inputy;
        TilePosition tilePosition;
        do
        {
            System.out.println("Board :");
            this.getBoard().getTiles().forEach((k, v) -> System.out.println(k + " : " + v));
            System.out.println("Choose x axis : ");
            inputx= sc.nextInt();
            System.out.println("Choose y axis : ");
            inputy= sc.nextInt();
            tilePosition = new TilePosition(inputx, inputy);
        }
        while (!this.getBoard().isValid(tilePosition));

        return tilePosition;
    }

    @Override
    Objective completeObjective()
    {
        int input;
        int numberCards = player.getHand().size();
        do
        {
            System.out.println("Your Objectives :");
            player.getHand().forEach(x -> System.out.println(x));
            System.out.println("Choose one (0, 1, 2...) :");
            input = sc.nextInt();
        }
        while (input < 0 || input > numberCards - 1);

        return player.getHand().get(input);
    }

    @Override
    boolean anyExtraAction()
    {
        return false; //TODO
    }

    @Override
    GameAction chooseExtraAction()
    {
        return null; //TODO
    }

    @Override
    Class<? extends Objective> chooseDeck()
    {
        return null; //TODO
    }

    @Override
    LandTile chooseTile(List<LandTile> drawnTiles)
    {
        return null; //TODO
    }
}
