package fr.unice.polytech.ps5.takenoko.et2;

import java.util.Objects;

public class BambooSection
{
    private final Color color;

    public BambooSection(Color color)
    {
        this.color = Objects.requireNonNull(color, "color must not be null");
    }

    public Color getColor()
    {
        return color;
    }

    public String toString()
    {
        return "Bamboo: " + color;
    }
}
