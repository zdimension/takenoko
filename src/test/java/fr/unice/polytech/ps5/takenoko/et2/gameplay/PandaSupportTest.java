package fr.unice.polytech.ps5.takenoko.et2.gameplay;

import fr.unice.polytech.ps5.takenoko.et2.GameData;
import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class PandaSupportTest
{
    private Game game;
    private Board spyBoard;
    private List<TilePosition> boardTilePosition = new ArrayList<>();

    @BeforeEach
    void init() {
        game = new Game(new GameData());
        spyBoard = spy(game.getBoard());

        LandTile l1 = new LandTile(Color.GREEN);
        LandTile l2 = new LandTile(Color.GREEN);
        LandTile l3 = new LandTile(Color.GREEN);
        LandTile l4 = new LandTile(Color.GREEN);
        LandTile l5 = new LandTile(Color.GREEN);
        LandTile l6 = new LandTile(Color.GREEN);
        LandTile l7 = new LandTile(Color.GREEN);
        LandTile l8 = new LandTile(Color.GREEN);
        LandTile l9 = new LandTile(Color.GREEN);
        LandTile l10 = new LandTile(Color.GREEN);
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

        spyBoard.addTile(l1, t1);
        spyBoard.addTile(l2, t2);
        spyBoard.addTile(l3, t3);
        spyBoard.addTile(l4, t4);
        spyBoard.addTile(l5, t5);
        spyBoard.addTile(l6, t6);
        spyBoard.addTile(l7, t7);
        spyBoard.addTile(l8, t8);
        spyBoard.addTile(l9, t9);
        spyBoard.addTile(l10, t10);
        spyBoard.addTile(l11, t11);
        spyBoard.addTile(l12, t12);
    }

    @Test
    void getValidPandaTargetsTest() {
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
    void movePandaUnvalidTargetTest(){
        DecisionMaker mockDecisionMaker = Mockito.mock(DecisionMaker.class);
        Player mockPlayer = Mockito.mock(Player.class);
        TilePosition falsePosition1 = new TilePosition(0,1);
        TilePosition falsePosition2 = new TilePosition(0,2);

        when(mockPlayer.getDecisionMaker()).thenReturn(mockDecisionMaker);
        when(mockDecisionMaker.choosePandaTarget(anyList())).thenReturn(falsePosition1, falsePosition2);
        game.movePanda(mockPlayer);
        game.movePanda(mockPlayer);
        verify(mockDecisionMaker, times(2)).choosePandaTarget(anyList());
        verify(mockPlayer, times(2)).getDecisionMaker();
        verifyNoMoreInteractions(mockDecisionMaker);
        verifyNoMoreInteractions(mockPlayer);
    }


}
