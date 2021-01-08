package fr.unice.polytech.ps5.takenoko.et2.gameplay;

import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTileImprovement;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PandaSupportTest
{
    private final List<TilePosition> boardTilePosition = new ArrayList<>();
    private Game game;
    private Board board;

    @BeforeEach
    void init()
    {
        game = new Game(new GameData());
        board = game.getBoard();

        LandTile l1 = spy(new LandTile(Color.GREEN));
        LandTile l2 = new LandTile(Color.GREEN);
        LandTile l3 = new LandTile(Color.GREEN);
        LandTile l4 = new LandTile(Color.GREEN);
        LandTile l5 = new LandTile(Color.GREEN);
        LandTile l6 = spy(new LandTile(Color.GREEN, LandTileImprovement.ENCLOSURE));
        LandTile l7 = new LandTile(Color.GREEN);
        LandTile l8 = new LandTile(Color.GREEN);
        LandTile l9 = new LandTile(Color.GREEN);
        LandTile l10 = spy(new LandTile(Color.GREEN));
        LandTile l11 = new LandTile(Color.GREEN);
        LandTile l12 = new LandTile(Color.GREEN);

        TilePosition t1 = new TilePosition(1, 0);
        TilePosition t2 = new TilePosition(1, -1);
        TilePosition t3 = new TilePosition(0, -1);
        TilePosition t4 = new TilePosition(-1, 0);
        TilePosition t5 = new TilePosition(2, -1);
        TilePosition t6 = new TilePosition(2, 0);
        TilePosition t7 = new TilePosition(1, 1);
        TilePosition t8 = new TilePosition(2, 1);
        TilePosition t9 = new TilePosition(1, 2);
        TilePosition t10 = new TilePosition(0, 2);
        TilePosition t11 = new TilePosition(1, -2);
        TilePosition t12 = new TilePosition(0, -2);

        boardTilePosition.add(t1);
        boardTilePosition.add(t2);
        boardTilePosition.add(t3);
        boardTilePosition.add(t4);
        boardTilePosition.add(t5);
        boardTilePosition.add(t6);
        boardTilePosition.add(t7);
        boardTilePosition.add(t8);
        boardTilePosition.add(t9);
        boardTilePosition.add(t10);
        boardTilePosition.add(t11);
        boardTilePosition.add(t12);

        board.addTile(l1, t1);
        board.addTile(l2, t2);
        board.addTile(l3, t3);
        board.addTile(l4, t4);
        board.addTile(l5, t5);
        board.addTile(l6, t6);
        board.addTile(l7, t7);
        board.addTile(l8, t8);
        board.addTile(l9, t9);
        board.addTile(l10, t10);
        board.addTile(l11, t11);
        board.addTile(l12, t12);
    }

    @Test
    void getValidPandaTargetsTest()
    {
        Set<TilePosition> expectedPandaTargets1 = Set.of(boardTilePosition.get(0),
            boardTilePosition.get(1),
            boardTilePosition.get(2),
            boardTilePosition.get(3),
            boardTilePosition.get(5),
            boardTilePosition.get(11));
        Set<TilePosition> expectedPandaTargets2 = Set.of(TilePosition.ZERO,
            boardTilePosition.get(1),
            boardTilePosition.get(3),
            boardTilePosition.get(4),
            boardTilePosition.get(5),
            boardTilePosition.get(6),
            boardTilePosition.get(8),
            boardTilePosition.get(10));
        TilePosition pandaPosition1 = TilePosition.ZERO;
        TilePosition pandaPosition2 = new TilePosition(1, 0);
        Set<TilePosition> pandaTargets1 = game.getValidTargets(pandaPosition1).collect(Collectors.toSet());
        Set<TilePosition> pandaTargets2 = game.getValidTargets(pandaPosition2).collect(Collectors.toSet());

        assertEquals(expectedPandaTargets1, pandaTargets1);
        assertEquals(expectedPandaTargets2, pandaTargets2);
    }

    @Test
    void movePandaUnvalidTargetTest()
    {
        DecisionMaker mockDecisionMaker = Mockito.mock(DecisionMaker.class);
        Player mockPlayer = Mockito.mock(Player.class);
        TilePosition falsePosition1 = new TilePosition(0, 1);
        TilePosition falsePosition2 = new TilePosition(0, 2);

        when(mockPlayer.getDecisionMaker()).thenReturn(mockDecisionMaker);
        when(mockDecisionMaker.choosePandaTarget(anyList(), anyBoolean())).thenReturn(falsePosition1, falsePosition2);
        assertThrows(IllegalArgumentException.class, () -> game.movePanda(mockPlayer));
        assertThrows(IllegalArgumentException.class, () -> game.movePanda(mockPlayer));
        verify(mockDecisionMaker, times(2)).choosePandaTarget(anyList(), anyBoolean());
        verify(mockPlayer, times(2)).getDecisionMaker();
        verifyNoMoreInteractions(mockDecisionMaker);
        verifyNoMoreInteractions(mockPlayer);
    }

    private void movePandaBasis(ArrayList<TilePosition> pandaTargets, ArrayList<LandTile> pandaTileTargets, ArrayList<Integer> invocations, int numberPandaMove, boolean storm)
    {
        DecisionMaker mockDecisionMaker = mock(DecisionMaker.class);
        Player mockPlayer = mock(Player.class);

        when(mockPlayer.getDecisionMaker()).thenReturn(mockDecisionMaker);
        when(mockDecisionMaker.choosePandaTarget(anyList(), anyBoolean())).thenReturn(pandaTargets.get(0));
        if (pandaTargets.size() != 1)
        {
            when(mockDecisionMaker.choosePandaTarget(anyList(), anyBoolean())).thenReturn(pandaTargets.get(0), pandaTargets.get(1), pandaTargets.get(1));
        }
        for (int i = 0; i < numberPandaMove; i++)
        {
            game.movePanda(mockPlayer, storm);
        }
        assertEquals(pandaTargets.get(pandaTargets.size() - 1), game.getPandaPosition());
        verify(mockDecisionMaker, times(invocations.get(0))).choosePandaTarget(anyList(), anyBoolean());
        verify(mockPlayer, times(invocations.get(1))).getDecisionMaker();
        verify(mockPlayer, times(invocations.get(2))).addBambooSection(Color.GREEN);
        verify(pandaTileTargets.get(0), times(invocations.get(3))).cutBambooSection();
        if (pandaTileTargets.size() != 1)
        {
            verify(pandaTileTargets.get(1), times(invocations.get(4))).cutBambooSection();
        }

        verifyNoMoreInteractions(mockDecisionMaker);
        verifyNoMoreInteractions(mockPlayer);
    }

    @Test
    void movePandaWithNoStormTest()
    {
        TilePosition position = new TilePosition(1, 0);
        LandTile pandaGoal = (LandTile) (board.getTiles().get(position));

        ArrayList<TilePosition> pandaPositionTargets = new ArrayList<>(List.of(position));
        ArrayList<LandTile> pandaTileTargets = new ArrayList<>(List.of(pandaGoal));
        ArrayList<Integer> invocations = new ArrayList<>(List.of(1, 1, 1, 1));

        movePandaBasis(pandaPositionTargets, pandaTileTargets, invocations, 1, false);
    }

    @Test
    void movePandaWithStormTest()
    {
        TilePosition position1 = new TilePosition(0, 2);
        TilePosition position2 = new TilePosition(1, 0);
        LandTile pandaGoal1 = (LandTile) (board.getTiles().get(position1));
        LandTile pandaGoal2 = (LandTile) (board.getTiles().get(position2));

        ArrayList<TilePosition> pandaPositionTargets = new ArrayList<>(List.of(position1, position2));
        ArrayList<LandTile> pandaTileTargets = new ArrayList<>(List.of(pandaGoal1, pandaGoal2));
        ArrayList<Integer> invocations = new ArrayList<>(List.of(3, 3, 1, 1, 1));

        movePandaBasis(pandaPositionTargets, pandaTileTargets, invocations, 3, true);
    }

    @Test
    void movePandaOnEnclosureTileTest()
    {
        TilePosition position = new TilePosition(2, 0);
        LandTile pandaGoal = (LandTile) (board.getTiles().get(position));

        ArrayList<TilePosition> pandaPositionTargets = new ArrayList<>(List.of(position));
        ArrayList<LandTile> pandaTileTargets = new ArrayList<>(List.of(pandaGoal));
        ArrayList<Integer> invocations = new ArrayList<>(List.of(1, 1, 0, 0));

        movePandaBasis(pandaPositionTargets, pandaTileTargets, invocations, 1, false);
    }

    @Test
    void movePandaOnPondTileTest()
    {
        DecisionMaker mockDecisionMaker = mock(DecisionMaker.class);
        Player mockPlayer = mock(Player.class);
        TilePosition position = new TilePosition(2, 0);
        LandTile pandaGoal = (LandTile) (board.getTiles().get(position));

        when(mockPlayer.getDecisionMaker()).thenReturn(mockDecisionMaker);
        when(mockDecisionMaker.choosePandaTarget(anyList(), anyBoolean())).thenReturn(position, TilePosition.ZERO);
        game.movePanda(mockPlayer);
        assertEquals(position, game.getPandaPosition());
        game.movePanda(mockPlayer);
        assertEquals(TilePosition.ZERO, game.getPandaPosition());
        verify(mockDecisionMaker, times(2)).choosePandaTarget(anyList(), anyBoolean());
        verify(mockPlayer, times(2)).getDecisionMaker();
        verify(mockPlayer, times(0)).addBambooSection(Color.GREEN);
        verify(pandaGoal, times(0)).cutBambooSection();
        verifyNoMoreInteractions(mockDecisionMaker);
        verifyNoMoreInteractions(mockPlayer);
    }
}
