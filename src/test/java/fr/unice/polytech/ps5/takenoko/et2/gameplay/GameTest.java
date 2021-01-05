package fr.unice.polytech.ps5.takenoko.et2.gameplay;

import fr.unice.polytech.ps5.takenoko.et2.GameData;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.Edge;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.RandomBot;
import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import fr.unice.polytech.ps5.takenoko.et2.objective.GardenerObjective;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PandaObjective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GameTest
{

    @Test
    void gameGameProcessing0Players()
    {
        var game = new Game();

        assertThrows(IllegalArgumentException.class, game::gameProcessing);
    }

    @Test
    void gameGameProcessing1Players() throws Exception
    {
        var players = List.<DecisionMakerBuilder>of(
            RandomBot::new
        );

        var game = new Game();

        for (DecisionMakerBuilder player : players)
        {
            game.addPlayer(player);
        }

        assertThrows(IllegalArgumentException.class, game::gameProcessing);
    }

    @Test
    void gameAddPlayer5Players() throws Exception
    {
        var players = List.<DecisionMakerBuilder>of(
            RandomBot::new,
            RandomBot::new,
            RandomBot::new,
            RandomBot::new
        );

        var game = new Game();

        for (DecisionMakerBuilder player : players)
        {
            game.addPlayer(player);
        }

        assertThrows(IllegalAccessException.class, () -> game.addPlayer(RandomBot::new));
    }

    @Test
    void findCompletableObjectivesTest() {
        var game = new Game(new GameData());
        Board board = game.getBoard();
        Player mockPlayer = Mockito.mock(Player.class);
        List<Objective> playerHand = new ArrayList<>();
        List<Objective> objectivesListExpected = new ArrayList<>();

        PandaObjective mockPandaObjective = Mockito.mock(PandaObjective.class);
        GardenerObjective mockGardenerObjective = Mockito.mock(GardenerObjective.class);
        PlotObjective mockPlotObjective = Mockito.mock(PlotObjective.class);

        playerHand.add(mockPandaObjective);
        playerHand.add(mockGardenerObjective);
        playerHand.add(mockPlotObjective);

        objectivesListExpected.add(mockPandaObjective);
        objectivesListExpected.add(mockGardenerObjective);

        when(mockPlayer.getHand()).thenReturn(playerHand);
        when(mockGardenerObjective.checkValidated(board,mockPlayer)).thenReturn(true);
        when(mockPandaObjective.checkValidated(board,mockPlayer)).thenReturn(true);
        when(mockPlotObjective.checkValidated(board,mockPlayer)).thenReturn(false);

        List<Objective> playerCompleteObjective = game.findCompletableObjectives(mockPlayer).collect(Collectors.toList());
        assertEquals(objectivesListExpected, playerCompleteObjective);

        verify(mockPlayer, times(1)).getHand();
        verify(mockPandaObjective, times(1)).checkValidated(board, mockPlayer);
        verify(mockPlotObjective, times(1)).checkValidated(board, mockPlayer);
        verify(mockGardenerObjective, times(1)).checkValidated(board, mockPlayer);

        verifyNoMoreInteractions(mockPlayer);
        verifyNoMoreInteractions(mockPandaObjective);
        verifyNoMoreInteractions(mockPlotObjective);
        verifyNoMoreInteractions(mockGardenerObjective);
    }

    @Test
    void completeObjectiveNullPlayerTest() {
        var game = new Game(new GameData());
        assertThrows(NullPointerException.class, () -> game.completeObjective(null));
    }

    private void completeObjectiveTestsBasis(Objective objectiveChoosen, boolean isChoosenObjectiveValid, boolean triggerEmperor, List<Integer> nbInteractions) throws IllegalAccessException
    {
        Game game = new Game(new GameData());
        Board board = game.getBoard();
        DecisionMaker mockDecisionMaker1 = mock(DecisionMaker.class);
        DecisionMaker mockDecisionMaker2 = mock(DecisionMaker.class);
        Player spyPlayer1 = spy(game.addPlayer(p -> mockDecisionMaker1));
        Player spyPlayer2 = spy(game.addPlayer(p -> mockDecisionMaker2));
        PandaObjective mockPandaObjective = mock(PandaObjective.class);
        GardenerObjective mockGardenerObjective = mock(GardenerObjective.class);
        PlotObjective mockPlotObjective = mock(PlotObjective.class);
        PandaObjective cast = Mockito.mock(PandaObjective.class);

        spyPlayer1.addObjective(mockPandaObjective);
        spyPlayer1.addObjective(mockGardenerObjective);
        spyPlayer1.addObjective(mockPlotObjective);
        spyPlayer1.addObjective(objectiveChoosen);

        if(triggerEmperor) {
            for(int i=0; i<8; i++) {
                Objective mockObjectiveCompleted = Mockito.mock(Objective.class);
                spyPlayer1.addObjective(mockObjectiveCompleted);
                spyPlayer1.moveObjectiveToComplete(mockObjectiveCompleted);
            }
        }
        if (objectiveChoosen instanceof PandaObjective) {
            cast = (PandaObjective)objectiveChoosen;
            when(cast.getBambooSectionList()).thenReturn(Map.of(Color.GREEN, 2));
        }
        when(mockDecisionMaker1.chooseObjectiveToComplete(anyList())).thenReturn(objectiveChoosen);
        when(mockGardenerObjective.checkValidated(board,spyPlayer1)).thenReturn(true);
        when(mockPandaObjective.checkValidated(board,spyPlayer1)).thenReturn(true);
        when(mockPlotObjective.checkValidated(board,spyPlayer1)).thenReturn(false);
        when(objectiveChoosen.checkValidated(board,spyPlayer1)).thenReturn(isChoosenObjectiveValid);

        game.completeObjective(spyPlayer1);

        verify(spyPlayer1, times(nbInteractions.get(0))).getHand();
        verify(spyPlayer1, times(nbInteractions.get(1))).addObjective(any(Objective.class));
        verify(spyPlayer1, times(nbInteractions.get(2))).getDecisionMaker();
        verify(spyPlayer1, times(nbInteractions.get(3))).moveObjectiveToComplete(any(Objective.class));
        verify(spyPlayer1, times(nbInteractions.get(4))).completedObjectivesCount();
        verify(spyPlayer1, times(nbInteractions.get(5))).triggerEmperor();
        verify(mockDecisionMaker1, times(nbInteractions.get(6))).chooseObjectiveToComplete(anyList());
        verify(mockPandaObjective, times(nbInteractions.get(7))).checkValidated(board, spyPlayer1);
        verify(mockPlotObjective, times(nbInteractions.get(8))).checkValidated(board, spyPlayer1);
        verify(mockGardenerObjective, times(nbInteractions.get(9))).checkValidated(board, spyPlayer1);
        verify(objectiveChoosen, times(nbInteractions.get(10))).checkValidated(board, spyPlayer1);
        if (objectiveChoosen instanceof PandaObjective)
        {
            verify(spyPlayer1, times(nbInteractions.get(11))).removeBambooSection(anyMap());
            verify(cast, times(nbInteractions.get(12))).getBambooSectionList();
        }

        verifyNoMoreInteractions(mockDecisionMaker1);
        verifyNoMoreInteractions(mockGardenerObjective);
        verifyNoMoreInteractions(mockPandaObjective);
        verifyNoMoreInteractions(mockPlotObjective);
        verifyNoMoreInteractions(spyPlayer1);
        verifyNoMoreInteractions(spyPlayer2);
    }

    @Test
    void completeObjectiveNotCompleteObjectiveSelectedTest() throws IllegalAccessException
    {
        PandaObjective mockPandaObjective = Mockito.mock(PandaObjective.class);
        List<Integer> listOfInteractions = List.of(1,4,1,0,0,0,1,1,1,1,1,0,0);
        completeObjectiveTestsBasis(mockPandaObjective, false, false, listOfInteractions);
    }

    @Test
    void completeObjectivePandaObjectiveSelectedTest() throws IllegalAccessException
    {
        PandaObjective mockPandaObjective = Mockito.mock(PandaObjective.class);
        List<Integer> listOfInteractions = List.of(1,4,1,1,2,0,1,1,1,1,1,1,1);
        completeObjectiveTestsBasis(mockPandaObjective, true, false, listOfInteractions);
    }

    @Test
    void completeObjectiveNotPandaObjectiveSelectedTest() throws IllegalAccessException
    {
        GardenerObjective mockGardenerObjective = Mockito.mock(GardenerObjective.class);
        List<Integer> listOfInteractions = List.of(1,4,1,1,2,0,1,1,1,1,1,0,0);
        completeObjectiveTestsBasis(mockGardenerObjective, true, false, listOfInteractions);
    }

    @Test
    void completeObjectiveAndTriggerEmperorTest() throws IllegalAccessException
    {
        GardenerObjective mockGardenerObjective = Mockito.mock(GardenerObjective.class);
        List<Integer> listOfInteractions = List.of(1,12,1,9,2,1,1,1,1,1,1,0,0);
        completeObjectiveTestsBasis(mockGardenerObjective, true, true, listOfInteractions);
    }

    @Test
    void placeIrrigationTest() {
        Game game = new Game(new GameData());
        Board board = game.getBoard();
        LandTile l1 = new LandTile(Color.GREEN);
        board.addTile(l1 , new TilePosition(1,0));
        Player mockPlayer = Mockito.mock(Player.class);
        DecisionMaker mockDecisionMaker = Mockito.mock(DecisionMaker.class);

        Edge choosenEdge1 = l1.getEdge(0);
        Edge choosenEdge2 = l1.getEdge(5);

        when(mockPlayer.getDecisionMaker()).thenReturn(mockDecisionMaker);
        when(mockDecisionMaker.chooseIrrigationPosition(anyList())).thenReturn(choosenEdge1, choosenEdge2);

        game.placeIrrigation(mockPlayer);
        game.placeIrrigation(mockPlayer);
        verify(mockPlayer, times(1)).irrigateEdge(any(Edge.class));
    }
}