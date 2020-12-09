package fr.unice.polytech.ps5.takenoko.et2.board;

/**
 * List of possible improvements for LandTile
 */
public enum LandTileImprovement
{
    ENCLOSURE
        {
            @Override
            public String toString()
            {
                return "Enclosure";
            }
        },
    FERTILIZER
        {
            @Override
            public String toString()
            {
                return "Fertilizer";
            }
        },
    WATERSHED
        {
            @Override
            public String toString()
            {
                return "Watershed";
            }
        }
}
