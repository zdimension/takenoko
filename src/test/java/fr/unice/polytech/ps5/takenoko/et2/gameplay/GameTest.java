package fr.unice.polytech.ps5.takenoko.et2.gameplay;

import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.RandomBot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
}