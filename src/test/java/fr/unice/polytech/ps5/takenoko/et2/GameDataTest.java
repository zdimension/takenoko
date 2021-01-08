package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.GameData;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameDataTest
{
    @Test
    void getStandardLandTiles()
    {
        assertEquals(Map.of(
            Color.GREEN, 11,
            Color.YELLOW, 9,
            Color.PINK, 7
            ), GameData.getStandardLandTiles()
                .stream().collect(Collectors.groupingBy(LandTile::getColor))
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, t -> t.getValue().size()))
        );
    }

    @Test
    void getStandardObjectives()
    {
        assertArrayEquals(new int[] { 15, 15, 15 },
            GameData.getStandardObjectives().values().stream().mapToInt(List::size).toArray());
    }
}