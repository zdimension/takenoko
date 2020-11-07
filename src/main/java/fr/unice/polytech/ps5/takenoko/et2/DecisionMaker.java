package fr.unice.polytech.ps5.takenoko.et2;

public abstract class DecisionMaker
{
    protected Player player;

    public DecisionMaker(Player player)
    {
        this.player = player;
    }

    protected Board getBoard()
    {
        return player.getGame().getBoard();
    }

    //abstract GameAction chooseAction();
}
