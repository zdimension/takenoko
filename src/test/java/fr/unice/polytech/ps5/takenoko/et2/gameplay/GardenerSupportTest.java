package fr.unice.polytech.ps5.takenoko.et2.gameplay;

import fr.unice.polytech.ps5.takenoko.et2.GameData;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class GardenerSupportTest
{
    private Game game;
    private Board board;
    private List<TilePosition> boardTilePosition = new ArrayList<>();

    @BeforeEach
    void init() {
        game = new Game(new GameData());
        board = game.getBoard();

        LandTile l1 = spy(new LandTile(Color.GREEN));
        LandTile l2 = spy(new LandTile(Color.GREEN, LandTileImprovement.FERTILIZER));
        LandTile l3 = new LandTile(Color.GREEN);
        LandTile l4 = new LandTile(Color.GREEN);
        LandTile l5 = spy(new LandTile(Color.GREEN));
        LandTile l6 = spy(new LandTile(Color.GREEN, LandTileImprovement.WATERSHED));
        LandTile l7 = spy(new LandTile(Color.YELLOW));
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

        board.addTileInternal(l1, t1, true);
        board.addTileInternal(l2, t2, true);
        board.addTileInternal(l3, t3, true);
        board.addTileInternal(l4, t4, true);
        board.addTileInternal(l5, t5, false );
        board.addTileInternal(l6, t6, true);
        board.addTileInternal(l7, t7, false);
        board.addTileInternal(l8, t8, false);
        board.addTileInternal(l9, t9, false);
        board.addTileInternal(l10, t10, false);
        board.addTileInternal(l11, t11, false);
        board.addTileInternal(l12, t12, false);
    }

    @Test
    void moveGardenerUnvalidTargetTest(){
        DecisionMaker mockDecisionMaker = Mockito.mock(DecisionMaker.class);
        Player mockPlayer = Mockito.mock(Player.class);
        TilePosition falsePosition1 = new TilePosition(0,1);
        TilePosition falsePosition2 = new TilePosition(0,2);

        when(mockPlayer.getDecisionMaker()).thenReturn(mockDecisionMaker);
        when(mockDecisionMaker.chooseGardenerTarget(anyList())).thenReturn(falsePosition1, falsePosition2);
        game.moveGardener(mockPlayer);
        game.moveGardener(mockPlayer);
        verify(mockDecisionMaker, times(2)).chooseGardenerTarget(anyList());
        verify(mockPlayer, times(2)).getDecisionMaker();
        verifyNoMoreInteractions(mockDecisionMaker);
        verifyNoMoreInteractions(mockPlayer);
    }

    @Test
    void moveGardenerNormalAndOnPondTile() {
        DecisionMaker mockDecisionMaker = Mockito.mock(DecisionMaker.class);
        Player mockPlayer = Mockito.mock(Player.class);
        TilePosition position = new TilePosition(0,1);
        LandTile gardenerGoal = (LandTile) board.getTiles().get(position);
        LandTile l1 = (LandTile) board.findTile(boardTilePosition.get(0));
        LandTile l2 = (LandTile) board.findTile(boardTilePosition.get(1));
        LandTile l5 = (LandTile) board.findTile(boardTilePosition.get(4));
        LandTile l6 = (LandTile) board.findTile(boardTilePosition.get(5));
        LandTile l7 = (LandTile) board.findTile(boardTilePosition.get(6));

        when(mockPlayer.getDecisionMaker()).thenReturn(mockDecisionMaker);
        when(mockDecisionMaker.chooseGardenerTarget(anyList())).thenReturn(position, TilePosition.ZERO);
        game.moveGardener(mockPlayer);
        game.moveGardener(mockPlayer);
        verify(mockDecisionMaker, times(2)).chooseGardenerTarget(anyList());
        verify(mockPlayer, times(2)).getDecisionMaker();
        verify(l1, times(1)).growBambooSection();
        verify(l2, times(1)).growBambooSection();
        verify(l5, times(0)).growBambooSection();
        verify(l6, times(1)).growBambooSection();
        verify(l7, times(0)).growBambooSection();
        verifyNoMoreInteractions(mockDecisionMaker);
        verifyNoMoreInteractions(mockPlayer);
    }
}
