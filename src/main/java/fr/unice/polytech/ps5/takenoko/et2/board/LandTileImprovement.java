package fr.unice.polytech.ps5.takenoko.et2.board;

/**
 * List of possible improvements for {@link fr.unice.polytech.ps5.takenoko.et2.board.LandTile}
 *
 * <ul>
 *     <li>{@link fr.unice.polytech.ps5.takenoko.et2.board.LandTileImprovement#ENCLOSURE}
 *     <li>{@link fr.unice.polytech.ps5.takenoko.et2.board.LandTileImprovement#FERTILIZER}
 *     <li>{@link fr.unice.polytech.ps5.takenoko.et2.board.LandTileImprovement#WATERSHED}
 * </ul>
 */
public enum LandTileImprovement
{
    /**
     * Protects the bamboo of the tile. The panda can move across and stop there but won't be able to eat any bamboo there
     */
    ENCLOSURE
        {
            @Override
            public String toString()
            {
                return "Enclosure";
            }
        },
    /**
     * Increases the growth of the bamboo of the tile. Each time the bamboo grows, two sections are added
     * instead of one
     */
    FERTILIZER
        {
            @Override
            public String toString()
            {
                return "Fertilizer";
            }
        },
    /**
     * Irrigates the tile. Cannot be used in an irrigation system
     */
    WATERSHED
        {
            @Override
            public String toString()
            {
                return "Watershed";
            }
        }
}
