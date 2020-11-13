package fr.unice.polytech.ps5.takenoko.et2.decision;

import java.util.Objects;

public class DecisionMakerException extends Exception
{
    public DecisionMakerException(String message)
    {
        super(Objects.requireNonNull(message, "message must not be null"));
    }
}
