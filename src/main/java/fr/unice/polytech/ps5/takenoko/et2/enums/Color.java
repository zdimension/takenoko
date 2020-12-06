package fr.unice.polytech.ps5.takenoko.et2.enums;

/**
 * Color that applies to the LandTile
 */
public enum Color
{
    GREEN
        {
            @Override
            public String toString()
            {
                return "Green";
            }
        },
    YELLOW
        {
            @Override
            public String toString()
            {
                return "Yellow";
            }
        },
    PINK
        {
            @Override
            public String toString()
            {
                return "Pink";
            }
        }
}
