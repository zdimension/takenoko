package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PandaObjective;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class PlayerTest
{
    Player p;

    @BeforeEach
    void init()
    {
        Game mockGame = mock(Game.class);
        DecisionMakerBuilder mockBuilder = mock(DecisionMakerBuilder.class);
        p =new Player(mockGame, mockBuilder);
    }

    @Test
    void addBambooSectionTest()
    {
        Map<Color, Integer> bambooReserveAfterAdd = Map.of(Color.GREEN,0, Color.YELLOW,1, Color.PINK,0);
        p.addBambooSection(Color.YELLOW);
        assertEquals(bambooReserveAfterAdd, p.getBambooSectionReserve());
        assertThrows(NullPointerException.class, () -> p.addBambooSection(null));
    }

    @Test
    void removeBambooSectionTest()
    {
        p.addBambooSection(Color.GREEN);
        p.addBambooSection(Color.YELLOW);
        p.addBambooSection(Color.PINK);

        Map<Color, Integer> bambooReserveAfterRemove = Map.of(Color.GREEN,0, Color.YELLOW,0, Color.PINK,0);
        Map<Color, Integer> bambooSectionToRemove = Map.of(Color.GREEN,1, Color.YELLOW,1, Color.PINK,1);
        p.removeBambooSection(bambooSectionToRemove);
        assertEquals(bambooReserveAfterRemove, p.getBambooSectionReserve());
        assertThrows(IllegalArgumentException.class, () -> p.removeBambooSection(new HashMap<>()));
    }

    @Test
    void addObjective()
    {
        PandaObjective mockPandaObjective = mock(PandaObjective.class);
        List<Objective> handAfterAdd = new ArrayList<>(5);
        handAfterAdd.add(mockPandaObjective);
        p.addObjective(mockPandaObjective);
        assertEquals(handAfterAdd, p.getHand());
        assertThrows(NullPointerException.class, () -> p.addObjective(null));
    }

}