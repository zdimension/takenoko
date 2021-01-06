package fr.unice.polytech.ps5.takenoko.et2.decision.bots;

import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTileImprovement;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import fr.unice.polytech.ps5.takenoko.et2.enums.Weather;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Game;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Player;
import fr.unice.polytech.ps5.takenoko.et2.objective.GardenerObjective;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PandaObjective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;
import fr.unice.polytech.ps5.takenoko.et2.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

class MinMaxBotTest
{
    private MinMaxBot bot;
    private Game game;
    private Player player;
    private Board board;

    @BeforeEach
    void initBot()
    {
        try
        {
            game = spy(new Game());
            game.addPlayer(MinMaxBot.getBuilder(1));
            game.addPlayer(MinMaxBot.getBuilder(1));
            Optional<Player> p = game.getPlayers().findAny();
            if (p.isEmpty())
            {
                fail();
            }
            bot = spy((MinMaxBot) p.get().getDecisionMaker());
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
            fail();
        }
        player = spy(bot.getPlayer());
        board = game.getBoard();
    }

    @Test
    void chooseActionTest()
    {
        List<GameAction> possibleActions = Collections.singletonList(GameAction.DRAW_TILE);
        assertTrue(possibleActions.contains(bot.chooseAction(possibleActions)));

        // I consider than using a loop here is frowned upon, but it seems to be preferable
        possibleActions = new ArrayList<>();
        for (GameAction gameAction : GameAction.values())
        {
            possibleActions.add(gameAction);
            assertTrue(possibleActions.contains(bot.chooseAction(possibleActions)));
        }
    }

    @Test
    void chooseWeatherTest()
    {
        List<Weather> possibleWeathers = new ArrayList<>();
        for (Weather weather : Weather.values())
        {
            possibleWeathers.add(weather);
            assertTrue(possibleWeathers.contains(bot.chooseWeather(possibleWeathers)));
        }
    }

    @Test
    void chooseLandTileImprovementTest()
    {
        List<LandTileImprovement> possibleLandTileImprovements = new ArrayList<>();
        for (LandTileImprovement landTileImprovement : LandTileImprovement.values())
        {
            possibleLandTileImprovements.add(landTileImprovement);
            assertTrue(possibleLandTileImprovements.contains(bot.chooseLandTileImprovement(possibleLandTileImprovements)));
        }
    }

    @Test
    void decisionTileTest()
    {
        assertTrue(board.addTile(new LandTile(Color.YELLOW), new TilePosition(1, 0)));
        assertTrue(board.addTile(new LandTile(Color.GREEN), new TilePosition(1, -1)));
        PlotObjective plotObjective = spy(new PlotObjective(4, Arrays.asList(Color.YELLOW, Color.GREEN, Color.PINK), Arrays.asList(3, 4)));
        player.addObjective(plotObjective);
        LandTile goodTile = new LandTile(Color.PINK);
        TilePosition goodPosition = new TilePosition(0, -1);
        List<LandTile> listTiles = new ArrayList<>(Arrays.asList(new LandTile(Color.YELLOW), new LandTile(Color.GREEN), goodTile));
        List<TilePosition> listPositions = new ArrayList<>(Arrays.asList(new TilePosition(-1, 0), goodPosition, new TilePosition(-1, 1), new TilePosition(0, 1)));
        assertEquals(player.getHand(), new ArrayList<>(Collections.singletonList(plotObjective)));
        assertEquals(bot.chooseTile(listTiles, listPositions), Pair.of(goodTile, goodPosition));
    }

    @Test
    void decisionGardenerTest()
    {
        GardenerObjective gardenerObjective = new GardenerObjective(4, Color.GREEN, 1, 3, LandTileImprovement.WATERSHED);
        LandTile goodTile = new LandTile(Color.GREEN, LandTileImprovement.WATERSHED);
        TilePosition goodPosition = new TilePosition(1, -1);
        assertEquals(goodTile.getLandTileImprovement(), LandTileImprovement.WATERSHED);
        assertTrue(board.addTile(new LandTile(Color.YELLOW), new TilePosition(1, 0)));
        assertTrue(board.addTile(goodTile, goodPosition));
        assertTrue(board.addTile(new LandTile(Color.PINK), new TilePosition(0, -1)));
        assertTrue(goodTile.growBambooSection());
        player.addObjective(gardenerObjective);
        assertEquals(bot.chooseGardenerTarget(Arrays.asList(new TilePosition(1, 0), goodPosition, new TilePosition(0, -1))), goodPosition);
        assertThrows(IllegalArgumentException.class, () -> bot.chooseGardenerTarget(new ArrayList<>()));

        bot.setLastActionChosen(GameAction.MOVE_GARDENER);
        assertEquals(bot.chooseGardenerTarget(Arrays.asList(new TilePosition(1, 0), goodPosition, new TilePosition(0, -1))), goodPosition);

    }

    @Test
    void decisionPandaTest()
    {
        Map<Color, Integer> pandaObjectiveMap = new HashMap<>();
        pandaObjectiveMap.put(Color.GREEN, 2);
        pandaObjectiveMap.put(Color.PINK, 2);
        pandaObjectiveMap.put(Color.YELLOW, 2);
        player.addBambooSection(Color.GREEN);
        player.addBambooSection(Color.GREEN);
        player.addBambooSection(Color.PINK);
        player.addBambooSection(Color.PINK);
        player.addBambooSection(Color.YELLOW);
        PandaObjective pandaObjective = new PandaObjective(4, pandaObjectiveMap);
        LandTile t1 = new LandTile(Color.GREEN);
        LandTile t2 = new LandTile(Color.YELLOW);
        LandTile t3 = new LandTile(Color.PINK);
        TilePosition goodPosition = new TilePosition(1, -1);
        assertTrue(board.addTile(t1, new TilePosition(1, 0)));
        assertTrue(board.addTile(t2, goodPosition));
        assertTrue(board.addTile(t3, new TilePosition(-1, 0)));
        assertTrue(t1.growBambooSection());
        assertTrue(t2.growBambooSection());
        assertTrue(t3.growBambooSection());
        player.addObjective(pandaObjective);
        assertEquals(bot.choosePandaTarget(Arrays.asList(new TilePosition(1, 0), goodPosition, new TilePosition(-1, 0)), false), goodPosition);
    }

    @Test
    void decisionChooseObjectiveTest()
    {
        PandaObjective po = new PandaObjective(5, new HashMap<>()
        {{
            put(Color.YELLOW, 2);
        }});
        PlotObjective plo = new PlotObjective(3, Collections.singletonList(Color.PINK), Collections.emptyList());
        List<Objective> listObj = new ArrayList<>(Arrays.asList(new GardenerObjective(4, Color.PINK, 2, 2), po, plo));
        assertEquals(bot.chooseObjectiveToComplete(listObj), po);
    }

    @Test
    void decisionChooseActionTest()
    {
        // Not fixed yet
    }
}
