package fr.unice.polytech.ps5.takenoko.et2.gameplay;

import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerException;
import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import fr.unice.polytech.ps5.takenoko.et2.enums.Weather;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.Matches;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ProcessTurnTest
{
    private Game game;


    private DecisionMaker mockDecisionMaker1;
    private DecisionMaker mockDecisionMaker2;
    private Player p1;
    private Player p2;


    @BeforeEach
    void init() throws IllegalAccessException
    {
        mockDecisionMaker1 = mock(DecisionMaker.class);
        mockDecisionMaker2 = mock(DecisionMaker.class);
    }

    @Test
    void testQuestionMarkAndRainStandartUsageEverythingOk() throws DecisionMakerException, IllegalAccessException
    {
        game = new Game(new Random(999999999));
        p1 = game.addPlayer(p -> mockDecisionMaker1);
        p2 = game.addPlayer(p -> mockDecisionMaker2);

        game.isFirstRound = false;
        var t1 = new LandTile(Color.YELLOW);
        game.getBoard().addTile(t1 , new TilePosition(0, 1));
        List<Weather> weatherList = new ArrayList<>(Arrays.asList(Weather.values()));
        weatherList.remove(Weather.QUESTION_MARK);

        when(p1.getDecisionMaker().chooseWeather(weatherList)).thenReturn(Weather.RAIN);
        when(p1.getDecisionMaker().chooseTileToAddBamboo(game.getBoard().getBambooableTiles())).thenReturn(t1);

        List<GameAction> gameActionList1;
        List<GameAction> gameActionList2;
        List<GameAction> gameActionList3;
        gameActionList1 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList1.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList1.remove(GameAction.PLACE_IRRIGATION);
        gameActionList1.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList2.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList2.remove(GameAction.PLACE_IRRIGATION);
        gameActionList2.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2.remove(GameAction.DRAW_OBJECTIVE);
        gameActionList3 = new ArrayList<>(new ArrayList<>(GameAction.getUnlimitedActions()));
        gameActionList3.add(null);

        when(p1.getDecisionMaker().chooseAction(gameActionList1)).thenReturn(GameAction.DRAW_OBJECTIVE);
        when(p1.getDecisionMaker().chooseAction(gameActionList2)).thenReturn(GameAction.PICK_IRRIGATION);
        when(p1.getDecisionMaker().chooseAction(gameActionList3)).thenReturn(null);

        Class<?> cLass = PlotObjective.class;
        Mockito.<Class<?>>when(p1.getDecisionMaker().chooseDeck(game.gameData.objectiveDecks
            .entrySet()
            .stream()
            .filter(e -> !e.getValue().isEmpty())
            .map(Map.Entry::getKey)
            .collect(Collectors.toUnmodifiableList())
        )).thenReturn(cLass);

        assertTrue(game.processTurn(p1));

        var board = new Board();
        var t2 = new LandTile(Color.YELLOW);
        board.addTile(t2,  new TilePosition(0, 1));
        t2.growBambooSection();

        assertEquals(board, game.getBoard());
    }

    @Test
    void testRainEveryThingOk() throws DecisionMakerException, IllegalAccessException
    {
        game = new Game(new Random(999999992));
        p1 = game.addPlayer(p -> mockDecisionMaker1);
        p2 = game.addPlayer(p -> mockDecisionMaker2);

        game.isFirstRound = false;
        var t1 = new LandTile(Color.YELLOW);
        game.getBoard().addTile(t1 , new TilePosition(0, 1));

        when(p1.getDecisionMaker().chooseTileToAddBamboo(game.getBoard().getBambooableTiles())).thenReturn(t1);

        List<GameAction> gameActionList1;
        List<GameAction> gameActionList2;
        List<GameAction> gameActionList3;
        gameActionList1 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList1.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList1.remove(GameAction.PLACE_IRRIGATION);
        gameActionList1.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList2.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList2.remove(GameAction.PLACE_IRRIGATION);
        gameActionList2.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2.remove(GameAction.DRAW_OBJECTIVE);
        gameActionList3 = new ArrayList<>(new ArrayList<>(GameAction.getUnlimitedActions()));
        gameActionList3.add(null);

        when(p1.getDecisionMaker().chooseAction(gameActionList1)).thenReturn(GameAction.DRAW_OBJECTIVE);
        when(p1.getDecisionMaker().chooseAction(gameActionList2)).thenReturn(GameAction.PICK_IRRIGATION);
        when(p1.getDecisionMaker().chooseAction(gameActionList3)).thenReturn(null);

        Class<?> cLass = PlotObjective.class;
        Mockito.<Class<?>>when(p1.getDecisionMaker().chooseDeck(game.gameData.objectiveDecks
            .entrySet()
            .stream()
            .filter(e -> !e.getValue().isEmpty())
            .map(Map.Entry::getKey)
            .collect(Collectors.toUnmodifiableList())
        )).thenReturn(cLass);

        assertTrue(game.processTurn(p1));

        var board = new Board();
        var t2 = new LandTile(Color.YELLOW);
        board.addTile(t2,  new TilePosition(0, 1));
        t2.growBambooSection();

        assertEquals(board, game.getBoard());
    }

    @Test
    void testRainButNoBambooableTilesSoNothingHappends() throws IllegalAccessException, DecisionMakerException
    {
        game = new Game(new Random(999999992));
        p1 = game.addPlayer(p -> mockDecisionMaker1);
        p2 = game.addPlayer(p -> mockDecisionMaker2);

        game.isFirstRound = false;
        var t1 = new LandTile(Color.YELLOW);
        var t2 = new LandTile(Color.GREEN);
        var t3 = new LandTile(Color.PINK);
        game.getBoard().addTile(t1 , new TilePosition(0, 1));
        game.getBoard().addTile(t2 , new TilePosition(-1, 1));
        game.getBoard().addTile(t3 , new TilePosition(-1, 2));
        t1.growBambooSection();
        t1.growBambooSection();
        t1.growBambooSection();
        t2.growBambooSection();
        t2.growBambooSection();
        t2.growBambooSection();

        List<GameAction> gameActionList1;
        List<GameAction> gameActionList2;
        List<GameAction> gameActionList3;
        gameActionList1 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList1.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList1.remove(GameAction.PLACE_IRRIGATION);
        gameActionList1.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList2.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList2.remove(GameAction.PLACE_IRRIGATION);
        gameActionList2.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2.remove(GameAction.DRAW_OBJECTIVE);
        gameActionList3 = new ArrayList<>(new ArrayList<>(GameAction.getUnlimitedActions()));
        gameActionList3.add(null);

        when(p1.getDecisionMaker().chooseAction(gameActionList1)).thenReturn(GameAction.DRAW_OBJECTIVE);
        when(p1.getDecisionMaker().chooseAction(gameActionList2)).thenReturn(GameAction.PICK_IRRIGATION);
        when(p1.getDecisionMaker().chooseAction(gameActionList3)).thenReturn(null);

        Class<?> cLass = PlotObjective.class;
        Mockito.<Class<?>>when(p1.getDecisionMaker().chooseDeck(game.gameData.objectiveDecks
            .entrySet()
            .stream()
            .filter(e -> !e.getValue().isEmpty())
            .map(Map.Entry::getKey)
            .collect(Collectors.toUnmodifiableList())
        )).thenReturn(cLass);

        assertTrue(game.processTurn(p1));

        var board = new Board();
        var t4 = new LandTile(Color.YELLOW);
        var t5 = new LandTile(Color.GREEN);
        var t6 = new LandTile(Color.PINK);
        board.addTile(t4,  new TilePosition(0, 1));
        board.addTile(t5 , new TilePosition(-1, 1));
        board.addTile(t6 , new TilePosition(-1, 2));
        t4.growBambooSection();
        t4.growBambooSection();
        t4.growBambooSection();
        t5.growBambooSection();
        t5.growBambooSection();
        t5.growBambooSection();

        var truc = board.equals(game.getBoard());

        assertEquals(board, game.getBoard());
    }

    @Test
    void testRainButDecisionMakerChoosesNull() throws IllegalAccessException, DecisionMakerException
    {
        game = new Game(new Random(999999992));
        p1 = game.addPlayer(p -> mockDecisionMaker1);
        p2 = game.addPlayer(p -> mockDecisionMaker2);

        game.isFirstRound = false;
        var t1 = new LandTile(Color.YELLOW);
        game.getBoard().addTile(t1 , new TilePosition(0, 1));

        when(p1.getDecisionMaker().chooseTileToAddBamboo(game.getBoard().getBambooableTiles())).thenReturn(null);

        List<GameAction> gameActionList1;
        List<GameAction> gameActionList2;
        List<GameAction> gameActionList3;
        gameActionList1 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList1.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList1.remove(GameAction.PLACE_IRRIGATION);
        gameActionList1.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList2.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList2.remove(GameAction.PLACE_IRRIGATION);
        gameActionList2.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2.remove(GameAction.DRAW_OBJECTIVE);
        gameActionList3 = new ArrayList<>(new ArrayList<>(GameAction.getUnlimitedActions()));
        gameActionList3.add(null);

        when(p1.getDecisionMaker().chooseAction(gameActionList1)).thenReturn(GameAction.DRAW_OBJECTIVE);
        when(p1.getDecisionMaker().chooseAction(gameActionList2)).thenReturn(GameAction.PICK_IRRIGATION);
        when(p1.getDecisionMaker().chooseAction(gameActionList3)).thenReturn(null);

        Class<?> cLass = PlotObjective.class;
        Mockito.<Class<?>>when(p1.getDecisionMaker().chooseDeck(game.gameData.objectiveDecks
            .entrySet()
            .stream()
            .filter(e -> !e.getValue().isEmpty())
            .map(Map.Entry::getKey)
            .collect(Collectors.toUnmodifiableList())
        )).thenReturn(cLass);

        assertTrue(game.processTurn(p1));

        var board = new Board();
        var t2 = new LandTile(Color.YELLOW);
        board.addTile(t2,  new TilePosition(0, 1));
        var truc = board.equals(game.getBoard());

        assertEquals(board, game.getBoard());
    }

    @Test
    void checkRainButDecisionsMakerReturnsInvalidTilesAndItActsLikeNull() throws DecisionMakerException, IllegalAccessException
    {
        game = new Game(new Random(999999992));
        p1 = game.addPlayer(p -> mockDecisionMaker1);
        p2 = game.addPlayer(p -> mockDecisionMaker2);

        game.isFirstRound = false;
        var t1 = new LandTile(Color.YELLOW);
        game.getBoard().addTile(t1 , new TilePosition(0, 1));

        when(p1.getDecisionMaker().chooseTileToAddBamboo(game.getBoard().getBambooableTiles())).thenReturn(new LandTile(Color.YELLOW));

        List<GameAction> gameActionList1;
        List<GameAction> gameActionList2;
        List<GameAction> gameActionList3;
        gameActionList1 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList1.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList1.remove(GameAction.PLACE_IRRIGATION);
        gameActionList1.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList2.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList2.remove(GameAction.PLACE_IRRIGATION);
        gameActionList2.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2.remove(GameAction.DRAW_OBJECTIVE);
        gameActionList3 = new ArrayList<>(new ArrayList<>(GameAction.getUnlimitedActions()));
        gameActionList3.add(null);

        when(p1.getDecisionMaker().chooseAction(gameActionList1)).thenReturn(GameAction.DRAW_OBJECTIVE);
        when(p1.getDecisionMaker().chooseAction(gameActionList2)).thenReturn(GameAction.PICK_IRRIGATION);
        when(p1.getDecisionMaker().chooseAction(gameActionList3)).thenReturn(null);

        Class<?> cLass = PlotObjective.class;
        Mockito.<Class<?>>when(p1.getDecisionMaker().chooseDeck(game.gameData.objectiveDecks
            .entrySet()
            .stream()
            .filter(e -> !e.getValue().isEmpty())
            .map(Map.Entry::getKey)
            .collect(Collectors.toUnmodifiableList())
        )).thenReturn(cLass);

        assertTrue(game.processTurn(p1));

        var board = new Board();
        var t2 = new LandTile(Color.YELLOW);
        board.addTile(t2,  new TilePosition(0, 1));
        var truc = board.equals(game.getBoard());

        assertEquals(board, game.getBoard());
    }

    @Test
    void testStormPandaEatsOneOfThreeBamboo() throws IllegalAccessException, DecisionMakerException
    {
        game = new Game(new Random(999999990));
        p1 = game.addPlayer(p -> mockDecisionMaker1);
        p2 = game.addPlayer(p -> mockDecisionMaker2);

        game.isFirstRound = false;
        var t1 = new LandTile(Color.YELLOW);
        var t2 = new LandTile(Color.YELLOW);
        game.getBoard().addTile(t1 , new TilePosition(0, 1));
        game.getBoard().addTile(t2 , new TilePosition(-1, 0));
        t2.growBambooSection();
        t2.growBambooSection();


        var PandaPositionAvailableList = new ArrayList<TilePosition>()
        {{
            addAll(game.getBoard().getTiles().keySet());
        }};

        when(p1.getDecisionMaker().choosePandaTarget(PandaPositionAvailableList, true)).thenReturn(t2.getPosition().get());

        List<GameAction> gameActionList1;
        List<GameAction> gameActionList2;
        List<GameAction> gameActionList3;
        gameActionList1 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList1.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList1.remove(GameAction.PLACE_IRRIGATION);
        gameActionList1.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList2.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList2.remove(GameAction.PLACE_IRRIGATION);
        gameActionList2.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2.remove(GameAction.DRAW_OBJECTIVE);
        gameActionList3 = new ArrayList<>(new ArrayList<>(GameAction.getUnlimitedActions()));
        gameActionList3.add(null);

        when(p1.getDecisionMaker().chooseAction(gameActionList1)).thenReturn(GameAction.DRAW_OBJECTIVE);
        when(p1.getDecisionMaker().chooseAction(gameActionList2)).thenReturn(GameAction.PICK_IRRIGATION);
        when(p1.getDecisionMaker().chooseAction(gameActionList3)).thenReturn(null);

        Class<?> cLass = PlotObjective.class;
        Mockito.<Class<?>>when(p1.getDecisionMaker().chooseDeck(game.gameData.objectiveDecks
            .entrySet()
            .stream()
            .filter(e -> !e.getValue().isEmpty())
            .map(Map.Entry::getKey)
            .collect(Collectors.toUnmodifiableList())
        )).thenReturn(cLass);

        assertTrue(game.processTurn(p1));

        var board = new Board();
        var t3 = new LandTile(Color.YELLOW);
        var t4 = new LandTile(Color.YELLOW);
        board.addTile(t3,  new TilePosition(0, 1));
        board.addTile(t4,  new TilePosition(-1, 0));
        t4.growBambooSection();

        assertEquals(board, game.getBoard());
        assertEquals(1, p1.getBambooSectionReserve().get(Color.YELLOW));

    }

    @Test
    void testStormPandaEatsOneofOneBamboo() throws DecisionMakerException, IllegalAccessException
    {
        game = new Game(new Random(999999990));
        p1 = game.addPlayer(p -> mockDecisionMaker1);
        p2 = game.addPlayer(p -> mockDecisionMaker2);

        game.isFirstRound = false;
        var t1 = new LandTile(Color.YELLOW);
        var t2 = new LandTile(Color.YELLOW);
        game.getBoard().addTile(t1 , new TilePosition(0, 1));
        game.getBoard().addTile(t2 , new TilePosition(-1, 0));



        var PandaPositionAvailableList = new ArrayList<TilePosition>()
        {{
            addAll(game.getBoard().getTiles().keySet());
        }};

        when(p1.getDecisionMaker().choosePandaTarget(PandaPositionAvailableList, true)).thenReturn(t2.getPosition().get());

        List<GameAction> gameActionList1;
        List<GameAction> gameActionList2;
        List<GameAction> gameActionList3;
        gameActionList1 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList1.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList1.remove(GameAction.PLACE_IRRIGATION);
        gameActionList1.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList2.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList2.remove(GameAction.PLACE_IRRIGATION);
        gameActionList2.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2.remove(GameAction.DRAW_OBJECTIVE);
        gameActionList3 = new ArrayList<>(new ArrayList<>(GameAction.getUnlimitedActions()));
        gameActionList3.add(null);

        when(p1.getDecisionMaker().chooseAction(gameActionList1)).thenReturn(GameAction.DRAW_OBJECTIVE);
        when(p1.getDecisionMaker().chooseAction(gameActionList2)).thenReturn(GameAction.PICK_IRRIGATION);
        when(p1.getDecisionMaker().chooseAction(gameActionList3)).thenReturn(null);

        Class<?> cLass = PlotObjective.class;
        Mockito.<Class<?>>when(p1.getDecisionMaker().chooseDeck(game.gameData.objectiveDecks
            .entrySet()
            .stream()
            .filter(e -> !e.getValue().isEmpty())
            .map(Map.Entry::getKey)
            .collect(Collectors.toUnmodifiableList())
        )).thenReturn(cLass);

        assertTrue(game.processTurn(p1));

        var board = new Board();
        var t3 = new LandTile(Color.YELLOW);
        var t4 = new LandTile(Color.YELLOW);
        board.addTile(t3,  new TilePosition(0, 1));
        board.addTile(t4,  new TilePosition(-1, 0));
        t4.cutBambooSection();

        assertEquals(board, game.getBoard());
        assertEquals(1, p1.getBambooSectionReserve().get(Color.YELLOW));
    }

    @Test
    void testStormPandaEatsOneOfZeroBamboo() throws IllegalAccessException, DecisionMakerException
    {
        game = new Game(new Random(999999990));
        p1 = game.addPlayer(p -> mockDecisionMaker1);
        p2 = game.addPlayer(p -> mockDecisionMaker2);

        game.isFirstRound = false;
        var t1 = new LandTile(Color.YELLOW);
        var t2 = new LandTile(Color.YELLOW);
        game.getBoard().addTile(t1 , new TilePosition(0, 1));
        game.getBoard().addTile(t2 , new TilePosition(-1, 0));
        t2.cutBambooSection();


        var PandaPositionAvailableList = new ArrayList<TilePosition>()
        {{
            addAll(game.getBoard().getTiles().keySet());
        }};

        when(p1.getDecisionMaker().choosePandaTarget(PandaPositionAvailableList, true)).thenReturn(t2.getPosition().get());

        List<GameAction> gameActionList1;
        List<GameAction> gameActionList2;
        List<GameAction> gameActionList3;
        gameActionList1 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList1.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList1.remove(GameAction.PLACE_IRRIGATION);
        gameActionList1.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList2.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList2.remove(GameAction.PLACE_IRRIGATION);
        gameActionList2.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2.remove(GameAction.DRAW_OBJECTIVE);
        gameActionList3 = new ArrayList<>(new ArrayList<>(GameAction.getUnlimitedActions()));
        gameActionList3.add(null);

        when(p1.getDecisionMaker().chooseAction(gameActionList1)).thenReturn(GameAction.DRAW_OBJECTIVE);
        when(p1.getDecisionMaker().chooseAction(gameActionList2)).thenReturn(GameAction.PICK_IRRIGATION);
        when(p1.getDecisionMaker().chooseAction(gameActionList3)).thenReturn(null);

        Class<?> cLass = PlotObjective.class;
        Mockito.<Class<?>>when(p1.getDecisionMaker().chooseDeck(game.gameData.objectiveDecks
            .entrySet()
            .stream()
            .filter(e -> !e.getValue().isEmpty())
            .map(Map.Entry::getKey)
            .collect(Collectors.toUnmodifiableList())
        )).thenReturn(cLass);

        assertTrue(game.processTurn(p1));

        var board = new Board();
        var t3 = new LandTile(Color.YELLOW);
        var t4 = new LandTile(Color.YELLOW);
        board.addTile(t3,  new TilePosition(0, 1));
        board.addTile(t4,  new TilePosition(-1, 0));
        t4.cutBambooSection();

        assertEquals(board, game.getBoard());
        assertEquals(0, p1.getBambooSectionReserve().get(Color.YELLOW));
    }

    @Test
    void testStormPandaMovesToPond() throws DecisionMakerException, IllegalAccessException
    {
        game = new Game(new Random(999999990));
        p1 = game.addPlayer(p -> mockDecisionMaker1);
        p2 = game.addPlayer(p -> mockDecisionMaker2);

        game.isFirstRound = false;
        var t1 = new LandTile(Color.YELLOW);
        var t2 = new LandTile(Color.YELLOW);
        game.getBoard().addTile(t1 , new TilePosition(0, 1));
        game.getBoard().addTile(t2 , new TilePosition(-1, 0));
        t2.cutBambooSection();


        var PandaPositionAvailableList = new ArrayList<TilePosition>()
        {{
            addAll(game.getBoard().getTiles().keySet());
        }};

        when(p1.getDecisionMaker().choosePandaTarget(PandaPositionAvailableList, true)).thenReturn(t2.getPosition().get(), game.getBoard().getCenter().getPosition().get());

        List<GameAction> gameActionList1;
        List<GameAction> gameActionList2;
        List<GameAction> gameActionList3;
        List<GameAction> gameActionList4;
        List<GameAction> gameActionList5;
        gameActionList1 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList1.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList1.remove(GameAction.PLACE_IRRIGATION);
        gameActionList1.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList2.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList2.remove(GameAction.PLACE_IRRIGATION);
        gameActionList2.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2.remove(GameAction.DRAW_OBJECTIVE);
        gameActionList3 = new ArrayList<>(new ArrayList<>(GameAction.getUnlimitedActions()));
        gameActionList3.add(null);
        gameActionList4 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList4.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList4.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList5 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList5.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList5.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList5.remove(GameAction.DRAW_OBJECTIVE);

        when(p1.getDecisionMaker().chooseAction(gameActionList1)).thenReturn(GameAction.DRAW_OBJECTIVE);
        when(p1.getDecisionMaker().chooseAction(gameActionList2)).thenReturn(GameAction.PICK_IRRIGATION);
        when(p1.getDecisionMaker().chooseAction(gameActionList4)).thenReturn(GameAction.DRAW_OBJECTIVE);
        when(p1.getDecisionMaker().chooseAction(gameActionList5)).thenReturn(GameAction.PICK_IRRIGATION);
        when(p1.getDecisionMaker().chooseAction(gameActionList3)).thenReturn(null, null);

        Class<?> cLass = PlotObjective.class;
        Mockito.<Class<?>>when(p1.getDecisionMaker().chooseDeck(game.gameData.objectiveDecks
            .entrySet()
            .stream()
            .filter(e -> !e.getValue().isEmpty())
            .map(Map.Entry::getKey)
            .collect(Collectors.toUnmodifiableList())
        )).thenReturn(cLass, cLass);

        assertTrue(game.processTurn(p1));

        List<Weather> weatherList = new ArrayList<>(Arrays.asList(Weather.values()));
        weatherList.remove(Weather.QUESTION_MARK);
        when(p1.getDecisionMaker().chooseWeather(weatherList)).thenReturn(Weather.STORM);
        when(p1.getDecisionMaker().chooseTileToAddBamboo(game.getBoard().getBambooableTiles())).thenReturn(t1);

        assertTrue(game.processTurn(p1));

        var board = new Board();
        var t3 = new LandTile(Color.YELLOW);
        var t4 = new LandTile(Color.YELLOW);
        board.addTile(t3,  new TilePosition(0, 1));
        board.addTile(t4,  new TilePosition(-1, 0));
        t4.cutBambooSection();

        assertEquals(board, game.getBoard());
        assertEquals(0, p1.getBambooSectionReserve().get(Color.YELLOW));
    }

    @Test
    void testSunCallsChooseActionThrice() throws DecisionMakerException, IllegalAccessException
    {
        game = new Game(new Random(999999997));
        p1 = game.addPlayer(p -> mockDecisionMaker1);
        p2 = game.addPlayer(p -> mockDecisionMaker2);

        game.isFirstRound = false;
        var t1 = new LandTile(Color.YELLOW);
        var t2 = new LandTile(Color.YELLOW);
        game.getBoard().addTile(t1 , new TilePosition(0, 1));
        game.getBoard().addTile(t2 , new TilePosition(-1, 0));
        t2.cutBambooSection();


        var PandaPositionAvailableList = new ArrayList<TilePosition>()
        {{
            addAll(game.getBoard().getTiles().keySet());
        }};

        when(p1.getDecisionMaker().choosePandaTarget(PandaPositionAvailableList, true)).thenReturn(t2.getPosition().get(), game.getBoard().getCenter().getPosition().get());

        List<GameAction> gameActionList1;
        List<GameAction> gameActionList2;
        List<GameAction> gameActionList3;
        List<GameAction> gameActionList4;

        gameActionList1 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList1.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList1.remove(GameAction.PLACE_IRRIGATION);
        gameActionList1.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList2.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList2.remove(GameAction.PLACE_IRRIGATION);
        gameActionList2.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList2.remove(GameAction.DRAW_OBJECTIVE);
        gameActionList4 = new ArrayList<>(new ArrayList<>(GameAction.getUnlimitedActions()));
        gameActionList4.add(null);
        gameActionList3 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList3.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList3.remove(GameAction.PLACE_IMPROVEMENT);
        gameActionList3.remove(GameAction.DRAW_OBJECTIVE);
        gameActionList3.remove(GameAction.PICK_IRRIGATION);


        when(p1.getDecisionMaker().chooseAction(gameActionList1)).thenReturn(GameAction.DRAW_OBJECTIVE);
        when(p1.getDecisionMaker().chooseAction(gameActionList2)).thenReturn(GameAction.PICK_IRRIGATION);
        when(p1.getDecisionMaker().chooseAction(gameActionList3)).thenReturn(GameAction.MOVE_GARDENER);
        when(p1.getDecisionMaker().chooseAction(gameActionList4)).thenReturn(null);
        when(p1.getDecisionMaker().chooseGardenerTarget(List.of(t1.getPosition().get(), t2.getPosition().get()))).thenReturn(t1.getPosition().get());

        Class<?> cLass = PlotObjective.class;
        Mockito.<Class<?>>when(p1.getDecisionMaker().chooseDeck(game.gameData.objectiveDecks
            .entrySet()
            .stream()
            .filter(e -> !e.getValue().isEmpty())
            .map(Map.Entry::getKey)
            .collect(Collectors.toUnmodifiableList())
        )).thenReturn(cLass, cLass);

        assertTrue(game.processTurn(p1));

        verify(p1.getDecisionMaker(), times(4)).chooseAction(anyList());
    }
}