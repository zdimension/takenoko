package fr.unice.polytech.ps5.takenoko.et2.commandline.exceptions;

public class InvalidBotTypeException extends IllegalArgumentException
{
    public InvalidBotTypeException(String botName)
    {
        super(String.format("Invalid bot type: %s", botName));
    }
}
