package fr.unice.polytech.ps5.takenoko.et2.decision;

import fr.unice.polytech.ps5.takenoko.et2.gameplay.Player;

public interface DecisionMakerBuilder
{
    DecisionMaker build(Player player);
}
