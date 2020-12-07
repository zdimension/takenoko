package fr.unice.polytech.ps5.takenoko.et2.gameplay;

import fr.unice.polytech.ps5.takenoko.et2.board.Edge;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTileImprovement;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import fr.unice.polytech.ps5.takenoko.et2.objective.GardenerObjective;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PandaObjective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerTest
{
    Game mockGame;
    DecisionMakerBuilder mockBuilder;
    Player p;

    @BeforeEach
    void init()
    {
        mockGame = mock(Game.class);
        mockBuilder = mock(DecisionMakerBuilder.class);
        p =new Player(mockGame, mockBuilder);
    }

    @Test
    void addBambooSectionTest()
    {
        Map<Color, Integer> bambooReserveAfterAdd = Map.of(Color.GREEN,0, Color.YELLOW,1, Color.PINK,0);
        p.addBambooSection(Color.YELLOW);
        assertEquals(bambooReserveAfterAdd, p.getBambooSectionReserve());

        assertThrows(NullPointerException.class, () -> p.addBambooSection(null));
        verifyNoMoreInteractions(mockGame);
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
        assertThrows(NullPointerException.class, () -> p.removeBambooSection(null));

        verifyNoMoreInteractions(mockGame);
    }

    @Test
    void addObjectiveTest()
    {
        PandaObjective mockPandaObjective = mock(PandaObjective.class);
        List<Objective> handAfterAdd = new ArrayList<>(5);
        handAfterAdd.add(mockPandaObjective);
        p.addObjective(mockPandaObjective);
        assertEquals(handAfterAdd, p.getHand());

        assertThrows(NullPointerException.class, () -> p.addObjective(null));

        verifyNoMoreInteractions(mockGame);
    }

    @Test
    void moveObjectiveToCompleteTest()
    {
        Objective mockGardenerObjective = mock(GardenerObjective.class);
        assertEquals(0,p.completedObjectivesCount());
        assertEquals(0,p.getHand().size());
        assertThrows(IllegalArgumentException.class, () -> p.moveObjectiveToComplete(mockGardenerObjective));

        p.addObjective(mockGardenerObjective);

        assertEquals(0,p.completedObjectivesCount());
        assertEquals(1,p.getHand().size());

        p.moveObjectiveToComplete(mockGardenerObjective);

        assertEquals(1,p.completedObjectivesCount());
        assertEquals(0,p.getHand().size());
        assertThrows(NullPointerException.class, () -> p.moveObjectiveToComplete(null));

        verifyNoMoreInteractions(mockGame);
    }

    @Test
    void isHandFullTest()
    {
        Objective mockPandaObjective1 = mock(PandaObjective.class);
        Objective mockPandaObjective2 = mock(PandaObjective.class);
        Objective mockGardenerObjective1 = mock(GardenerObjective.class);
        Objective mockGardenerObjective2 = mock(GardenerObjective.class);
        Objective mockPlotObjective1 = mock(PlotObjective.class);
        Objective mockPlotObjective2 = mock(PlotObjective.class);

        p.addObjective(mockPandaObjective1);
        p.addObjective(mockPandaObjective2);
        assertFalse(p.isHandFull());

        p.addObjective(mockGardenerObjective1);
        p.addObjective(mockGardenerObjective2);
        p.addObjective(mockPlotObjective1);
        assertTrue(p.isHandFull());

        p.addObjective(mockPlotObjective2);
        assertFalse(p.isHandFull());

        verifyNoMoreInteractions(mockPandaObjective1);
        verifyNoMoreInteractions(mockPandaObjective2);
        verifyNoMoreInteractions(mockGardenerObjective1);
        verifyNoMoreInteractions(mockGardenerObjective2);
        verifyNoMoreInteractions(mockPlotObjective1);
        verifyNoMoreInteractions(mockPlotObjective2);
        verifyNoMoreInteractions(mockGame);
    }

    @Test
    void countPointsTest()
    {
        Objective mockPandaObjective = mock(PandaObjective.class);
        Objective mockGardenerObjective = mock(GardenerObjective.class);
        Objective mockPlotObjective = mock(PlotObjective.class);

        when(mockPandaObjective.getPoints()).thenReturn(3);
        when(mockGardenerObjective.getPoints()).thenReturn(3);
        when(mockPlotObjective.getPoints()).thenReturn(3);

        p.addObjective(mockPandaObjective);
        p.addObjective(mockGardenerObjective);
        p.addObjective(mockPlotObjective);
        p.moveObjectiveToComplete(mockPandaObjective);
        p.moveObjectiveToComplete(mockGardenerObjective);
        p.moveObjectiveToComplete(mockPlotObjective);

        assertEquals(9, p.countPoints());
        p.triggerEmperor();
        assertEquals(11,p.countPoints());

        verify(mockPandaObjective,times(2)).getPoints();
        verify(mockGardenerObjective,times(2)).getPoints();
        verify(mockPlotObjective,times(2)).getPoints();

        verifyNoMoreInteractions(mockPandaObjective);
        verifyNoMoreInteractions(mockGardenerObjective);
        verifyNoMoreInteractions(mockPlotObjective);
        verifyNoMoreInteractions(mockGame);
    }

    @Test
    void triggerEmperorTest()
    {
        assertFalse(p.isHasTriggeredEmperor());
        p.triggerEmperor();
        assertTrue(p.isHasTriggeredEmperor());

        verifyNoMoreInteractions(mockGame);
    }

    @Test
    void addChipTest()
    {
        LandTileImprovement chip = LandTileImprovement.ENCLOSURE;
        List<LandTileImprovement> chipReserveAfterAdd = new ArrayList<>();
        chipReserveAfterAdd.add(chip);
        p.addChip(chip);
        assertEquals(chipReserveAfterAdd, p.getChipReserve());

        assertThrows(NullPointerException.class, () -> p.addChip(null));

        verifyNoMoreInteractions(mockGame);
    }

    @Test
    void pickIrrigationTest()
    {
        assertEquals(0,p.getNbIrrigationsInStock());
        p.pickIrrigation();
        assertEquals(1,p.getNbIrrigationsInStock());
        verifyNoMoreInteractions(mockGame);
    }

    @Test
    void irrigateEdgeTest()
    {
        assertThrows(NullPointerException.class, () -> p.irrigateEdge(null));

        Edge mockEdge1 = mock(Edge.class);
        Edge mockEdge2 = mock(Edge.class);

        when(mockEdge1.isIrrigated()).thenReturn(true);
        when(mockEdge2.isIrrigated()).thenReturn(false);

        assertFalse(p.irrigateEdge(mockEdge1));

        p.pickIrrigation();

        assertFalse(p.irrigateEdge(mockEdge1));
        assertTrue(p.irrigateEdge(mockEdge2));

        verify(mockEdge1,times(1)).isIrrigated();
        verify(mockEdge1,times(1)).isIrrigated();

        verifyNoMoreInteractions(mockEdge1);
        //verifyNoMoreInteractions(mockEdge2);
        verifyNoMoreInteractions(mockGame);

    }


}