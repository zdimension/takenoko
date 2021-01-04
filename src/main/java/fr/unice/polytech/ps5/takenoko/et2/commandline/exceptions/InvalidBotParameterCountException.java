package fr.unice.polytech.ps5.takenoko.et2.commandline.exceptions;

public class InvalidBotParameterCountException extends IllegalArgumentException
{
    public InvalidBotParameterCountException(int expected, int got)
    {
        super(String.format("Invalid parameter count (expected %d, got %d). Maybe your shell is stripping off parentheses?", expected, got));
    }
}
