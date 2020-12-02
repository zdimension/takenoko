package fr.unice.polytech.ps5.takenoko.et2.decision.bots;

import fr.unice.polytech.ps5.takenoko.et2.Game;
import fr.unice.polytech.ps5.takenoko.et2.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.GameData;
import fr.unice.polytech.ps5.takenoko.et2.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MinMaxBotTest
{
    MinMaxBot bot;
    Game game;

    @BeforeEach
    void initBot()
    {
        try
        {
            game = new Game(GameData.getStandardObjectives(), GameData.getLandTiles(11, 9, 7));
            game.addPlayer(MinMaxBot.getBuilder(1));
            game.addPlayer(MinMaxBot.getBuilder(1));
            Optional<Player> p = game.getPlayers().findAny();
            if (p.isEmpty())
            {
                fail();
            }
            bot = (MinMaxBot) p.get().getDecisionMaker();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void chooseActionTest()
    {
        List<GameAction> possibleActions = Arrays.asList(GameAction.DRAW_TILE);
        assertTrue(possibleActions.contains(bot.chooseAction(possibleActions)));

        // I consider than using a loop here is frowned upon, but it seems to be preferable
        possibleActions = new ArrayList<>();
        for (GameAction gameAction : GameAction.values())
        {
            possibleActions.add(gameAction);
            assertTrue(possibleActions.contains(bot.chooseAction(possibleActions)));
        }
    }
}
