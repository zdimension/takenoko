package fr.unice.polytech.ps5.takenoko.et2.decision;

import fr.unice.polytech.ps5.takenoko.et2.decision.bots.RandomBot;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Game;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

class DecisionMakerTest
{

    private RandomBot bot;

    @BeforeEach
    void initBot()
    {
        try
        {
            Game game = spy(new Game());
            game.addPlayer(RandomBot.getBuilder());
            game.addPlayer(RandomBot.getBuilder());
            Optional<Player> p = game.getPlayers().findAny();
            if (p.isEmpty())
            {
                fail();
            }
            bot = spy((RandomBot) p.get().getDecisionMaker());
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testRandomElementArrayListIntegers()
    {
        var set = new HashSet<Integer>();
        var rnd = new Random();
        while (set.size() < 100)
        {
            set.add(rnd.nextInt());
        }
        var list = new ArrayList<>(set);
        for (int i = 0; i < 1000; i++)
        {
            assertTrue(set.contains(bot.randomElement(list)));
        }
    }

    @Test
    void testRandomElementThrowsWhenEmptyListProvided()
    {
        var list = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> bot.randomElement(list));
    }
}