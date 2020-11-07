package fr.unice.polytech.ps5.takenoko.et2;

public class DecisionMakerDebugger extends DecisionMaker
{
    public DecisionMakerDebugger(Player player)
    {
        super(player);
    }

    //@Override
    //GameAction chooseAction()
    //{
    //    return null;
    //}

    public static DecisionMakerBuilder getBuilder()
    {
        return DecisionMakerDebugger::new;
    }
}
