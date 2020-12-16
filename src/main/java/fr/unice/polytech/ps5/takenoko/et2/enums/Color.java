package fr.unice.polytech.ps5.takenoko.et2.enums;

/**
 * Color that applies to the LandTile
 * <ul>
 *     <li>GREEN
 *     <li>YELLOW
 *     <li>PINK
 * </ul>
 */
public enum Color
{
    /**
     * GREEN color
     */
    GREEN
        {
            @Override
            public String toString()
            {
                return "Green";
            }
        },
    /**
     * YELLOW color
     */
    YELLOW
        {
            @Override
            public String toString()
            {
                return "Yellow";
            }
        },
    /**
     * PINK color
     */
    PINK
        {
            @Override
            public String toString()
            {
                return "Pink";
            }
        }
}
