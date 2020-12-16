package fr.unice.polytech.ps5.takenoko.et2.gameplay;

import fr.unice.polytech.ps5.takenoko.et2.GameData;
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
import org.mockito.Mockito;


import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class processTurnTest
{
    private Game game;


    DecisionMaker mockDecisionMaker1;
    DecisionMaker mockDecisionMaker2;
    Player p1;
    Player p2;


    @BeforeEach
    void init() throws IllegalAccessException
    {
        game = GameData.getStandardGame(new Random(999999999));

        mockDecisionMaker1 = mock(DecisionMaker.class);
        mockDecisionMaker2 = mock(DecisionMaker.class);
        p1 = game.addPlayer(p -> mockDecisionMaker1);
        p2 = game.addPlayer(p -> mockDecisionMaker2);

    }

    @Test
    void checkstandardRain() throws DecisionMakerException
    {
        game.isFirstRound = false;
        var t1 = new LandTile(Color.YELLOW);
        game.getBoard().addTile(t1 , new TilePosition(0, 1));
        List<Weather> weatherList = new ArrayList<>(Arrays.asList(Weather.values()));
        weatherList.remove(Weather.QUESTION_MARK);
        weatherList.remove(Weather.CLOUDS);

        when(p1.getDecisionMaker().chooseWeather(weatherList)).thenReturn(Weather.RAIN);
        when(p1.getDecisionMaker().chooseTileToAddBamboo(game.getBoard().getBambooableTiles())).thenReturn(t1);

        List<GameAction> gameActionList1;
        List<GameAction> gameActionList2;
        List<GameAction> gameActionList3;
        gameActionList1 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList1.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList1.remove(GameAction.PLACE_IRRIGATION);
        gameActionList1.remove(GameAction.PLACE_IMPROVEMENT); //TODO remove this line when chipReserve intanciated
        gameActionList2 = new ArrayList<>(new ArrayList<>(Arrays.asList(GameAction.values())));
        gameActionList2.remove(GameAction.COMPLETE_OBJECTIVE);
        gameActionList2.remove(GameAction.PLACE_IRRIGATION);
        gameActionList2.remove(GameAction.PLACE_IMPROVEMENT); //TODO remove this line when chipReserve intanciated
        gameActionList2.remove(GameAction.DRAW_OBJECTIVE);
        gameActionList3 = new ArrayList<>(new ArrayList<>(GameAction.getUnlimitedActions()));
        gameActionList3.add(null);

        when(p1.getDecisionMaker().chooseAction(gameActionList1)).thenReturn(GameAction.DRAW_OBJECTIVE);
        when(p1.getDecisionMaker().chooseAction(gameActionList2)).thenReturn(GameAction.PICK_IRRIGATION);
        when(p1.getDecisionMaker().chooseAction(gameActionList3)).thenReturn(null);

        Class<?> cLass = PlotObjective.class;
        Mockito.<Class<?>>when(p1.getDecisionMaker().chooseDeck(game.objectiveDecks
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
        var truc = board.equals(game.getBoard());

        assertEquals(board, game.getBoard());

    }
}