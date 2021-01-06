package fr.unice.polytech.ps5.takenoko.et2.commandline.exceptions;

import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;

public class MissingBuilderException extends IllegalArgumentException
{
    public MissingBuilderException(Class<? extends DecisionMaker> botName)
    {
        super(String.format("Missing builder in class: %s", botName.toString()));
    }
}
