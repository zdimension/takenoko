package fr.unice.polytech.ps5.takenoko.et2.gameplay;

import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTileImprovement;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerException;
import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import fr.unice.polytech.ps5.takenoko.et2.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class drawAndAddTileTest
{
    private Game game;
    private DecisionMaker mockDecisionMaker1;
    private DecisionMaker mockDecisionMaker2;
    private Player p1;

    @BeforeEach
    void init()
    {
        mockDecisionMaker1 = mock(DecisionMaker.class);
        mockDecisionMaker2 = mock(DecisionMaker.class);
    }

    void initGameAndPlayersWithSeed(long seed, boolean isFirstRound) throws IllegalAccessException
    {
        game = new Game(new Random(seed));
        p1 = game.addPlayer(p -> mockDecisionMaker1);
        Player p2 = game.addPlayer(p -> mockDecisionMaker2);
        game.isFirstRound = isFirstRound;
    }

    @Test
    void testPlacetileNexttoPondOk() throws IllegalAccessException, DecisionMakerException
    {
        initGameAndPlayersWithSeed(999999988, false);

        List<GameAction> gameActionList1;
        List<GameAction> gameActionList2;
        List<GameAction> gameActionList3;

        gameActionList1 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList1.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList1.remove(GameAction.PLACE_IRRIGATION);
        gameActionList1.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList1.remove(GameAction.MOVE_GARDENER);
        gameActionList1.remove(GameAction.MOVE_PANDA);
        gameActionList2 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList2.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList2.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2.remove(GameAction.PLACE_IRRIGATION);
        gameActionList3 = new ArrayList<>(new ArrayList<>(GameAction.getUnlimitedActions()));
        gameActionList3.add(null);
        gameActionList3.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList3.remove(GameAction.COMPLETE_OBJECTIVE);

        List<LandTile> drawnTiles2 = List.of(
            new LandTile(Color.YELLOW, LandTileImprovement.FERTILIZER),
            new LandTile(Color.GREEN, LandTileImprovement.FERTILIZER),
            new LandTile(Color.PINK)
        );
        List<TilePosition> validPos1 = Arrays.asList(
            new TilePosition(-1, 0),
            new TilePosition(-1, 1),
            new TilePosition(0, -1),
            new TilePosition(0, 1),
            new TilePosition(1, -1),
            new TilePosition(1, 0)
        );

        List<TilePosition> validPos2 = Arrays.asList(
            new TilePosition(-1, 0),
            new TilePosition(-1, 1),
            new TilePosition(0, -1),
            new TilePosition(0, 1),
            new TilePosition(1, -1)
        );

        List<LandTile> drawnTiles1 = List.of(
            new LandTile(Color.YELLOW, LandTileImprovement.FERTILIZER),
            new LandTile(Color.GREEN),
            new LandTile(Color.GREEN, LandTileImprovement.FERTILIZER)
        );

        validPos1.sort(TilePosition.storageComparer);
        validPos2.sort(TilePosition.storageComparer);

        when(p1.getDecisionMaker().chooseAction(gameActionList1)).thenReturn(GameAction.DRAW_TILE);
        when(p1.getDecisionMaker().chooseAction(gameActionList2)).thenReturn(GameAction.DRAW_TILE);
        when(p1.getDecisionMaker().chooseAction(gameActionList3)).thenReturn(null);
        when(p1.getDecisionMaker().chooseTile(Collections.unmodifiableList(drawnTiles1), validPos1)).thenReturn(Pair.of(new LandTile(Color.GREEN), new TilePosition(1, 0)));
        when(p1.getDecisionMaker().chooseTile(Collections.unmodifiableList(drawnTiles2), validPos2)).thenReturn(Pair.of(new LandTile(Color.YELLOW, LandTileImprovement.FERTILIZER), new TilePosition(1, -1)));
        Collections.shuffle(game.gameData.tileDeck, game.getRandom());
        assertTrue(game.processTurn(p1));

        assertEquals(25, game.gameData.tileDeck.size());

        var board = new Board();
        var t3 = new LandTile(Color.GREEN);
        var t4 = new LandTile(Color.YELLOW, LandTileImprovement.FERTILIZER);
        board.addTile(t3, new TilePosition(1, 0));
        board.addTile(t4, new TilePosition(1, -1));

        assertEquals(board, game.getBoard());

    }

    @Test
    void testPlaceTileNextToTwoTilesButNotPond() throws IllegalAccessException, DecisionMakerException
    {
        initGameAndPlayersWithSeed(999999988, true);

        var t1 = new LandTile(Color.YELLOW);
        var t2 = new LandTile(Color.YELLOW);
        game.getBoard().addTile(t1, new TilePosition(0, 1));
        game.getBoard().addTile(t2, new TilePosition(1, 0));

        List<GameAction> gameActionList1;
        List<GameAction> gameActionList2;
        List<GameAction> gameActionList3;

        gameActionList1 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList1.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList1.remove(GameAction.PLACE_IRRIGATION);
        gameActionList1.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList2.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList2.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2.remove(GameAction.DRAW_TILE);
        gameActionList2.remove(GameAction.PLACE_IRRIGATION);
        gameActionList3 = new ArrayList<>(new ArrayList<>(GameAction.getUnlimitedActions()));
        gameActionList3.add(null);
        gameActionList3.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList3.remove(GameAction.COMPLETE_OBJECTIVE);


        List<TilePosition> validPos1 = Arrays.asList(
            new TilePosition(-1, 0),
            new TilePosition(-1, 1),
            new TilePosition(0, -1),
            new TilePosition(1, -1),
            new TilePosition(1, 1)
        );

        List<LandTile> drawnTiles1 = List.of(
            new LandTile(Color.YELLOW, LandTileImprovement.FERTILIZER),
            new LandTile(Color.GREEN),
            new LandTile(Color.GREEN, LandTileImprovement.FERTILIZER)
        );

        validPos1.sort(TilePosition.storageComparer);


        when(p1.getDecisionMaker().chooseAction(gameActionList1)).thenReturn(GameAction.DRAW_TILE);
        when(p1.getDecisionMaker().chooseAction(gameActionList2)).thenReturn(GameAction.PICK_IRRIGATION);
        when(p1.getDecisionMaker().chooseAction(gameActionList3)).thenReturn(null);
        when(p1.getDecisionMaker().chooseTile(Collections.unmodifiableList(drawnTiles1), validPos1)).thenReturn(Pair.of(new LandTile(Color.GREEN), new TilePosition(1, 1)));
        Collections.shuffle(game.gameData.tileDeck, game.getRandom());
        assertTrue(game.processTurn(p1));

        assertEquals(26, game.gameData.tileDeck.size());

        var board = new Board();
        var t3 = new LandTile(Color.YELLOW);
        var t4 = new LandTile(Color.YELLOW);
        var t5 = new LandTile(Color.GREEN);
        board.addTile(t3, new TilePosition(0, 1));
        board.addTile(t4, new TilePosition(1, 0));
        board.addTile(t5, new TilePosition(1, 1));

        assertEquals(board, game.getBoard());

    }

    @Test
    void testPlaceTileInvalidPosition() throws IllegalAccessException
    {
        initGameAndPlayersWithSeed(999999988, true);

        var t1 = new LandTile(Color.YELLOW);
        game.getBoard().addTile(t1, new TilePosition(0, 1));

        List<GameAction> gameActionList1;

        gameActionList1 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList1.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList1.remove(GameAction.PLACE_IRRIGATION);
        gameActionList1.remove(GameAction.PLACE_IMPROVEMENT);

        List<TilePosition> validPos1 = Arrays.asList(
            new TilePosition(-1, 0),
            new TilePosition(-1, 1),
            new TilePosition(0, -1),
            new TilePosition(1, -1),
            new TilePosition(1, 0)
        );

        List<LandTile> drawnTiles1 = List.of(
            new LandTile(Color.YELLOW, LandTileImprovement.FERTILIZER),
            new LandTile(Color.GREEN),
            new LandTile(Color.GREEN, LandTileImprovement.FERTILIZER)
        );

        validPos1.sort(TilePosition.storageComparer);

        when(p1.getDecisionMaker().chooseAction(gameActionList1)).thenReturn(GameAction.DRAW_TILE);
        when(p1.getDecisionMaker().chooseTile(Collections.unmodifiableList(drawnTiles1), validPos1)).thenReturn(Pair.of(new LandTile(Color.GREEN), new TilePosition(1, 1)));
        Collections.shuffle(game.gameData.tileDeck, game.getRandom());
        assertThrows(IllegalArgumentException.class, () -> game.processTurn(p1));
    }

    @Test
    void testPlaceTileInvalidTile() throws IllegalAccessException
    {
        initGameAndPlayersWithSeed(999999988, true);

        var t1 = new LandTile(Color.YELLOW);
        game.getBoard().addTile(t1, new TilePosition(0, 1));

        List<GameAction> gameActionList1;

        gameActionList1 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList1.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList1.remove(GameAction.PLACE_IRRIGATION);
        gameActionList1.remove(GameAction.PLACE_IMPROVEMENT);

        List<TilePosition> validPos1 = Arrays.asList(
            new TilePosition(-1, 0),
            new TilePosition(-1, 1),
            new TilePosition(0, -1),
            new TilePosition(1, -1),
            new TilePosition(1, 0)
        );

        List<LandTile> drawnTiles1 = List.of(
            new LandTile(Color.YELLOW, LandTileImprovement.FERTILIZER),
            new LandTile(Color.GREEN),
            new LandTile(Color.GREEN, LandTileImprovement.FERTILIZER)
        );

        validPos1.sort(TilePosition.storageComparer);

        when(p1.getDecisionMaker().chooseAction(gameActionList1)).thenReturn(GameAction.DRAW_TILE);
        when(p1.getDecisionMaker().chooseTile(Collections.unmodifiableList(drawnTiles1), validPos1)).thenReturn(Pair.of(new LandTile(Color.PINK), new TilePosition(0, 1)));
        Collections.shuffle(game.gameData.tileDeck, game.getRandom());
        assertThrows(IllegalArgumentException.class, () -> game.processTurn(p1));
    }
}