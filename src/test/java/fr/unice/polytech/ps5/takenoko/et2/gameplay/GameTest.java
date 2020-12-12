package fr.unice.polytech.ps5.takenoko.et2.gameplay;

import fr.unice.polytech.ps5.takenoko.et2.GameData;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.RandomBot;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GameTest
{

    @Test
    void gameConstructorEmptyObjectiveDeck()
    {
        assertThrows(IllegalArgumentException.class, () -> new Game(new HashMap<>(), GameData.getStandardLandTiles()));
    }

    @Test
    void gameContructorEmptyTileDeck()
    {
        assertThrows(IllegalArgumentException.class, () -> new Game(GameData.getStandardObjectives(), new ArrayList<>()));
    }

    @Test
    void gameGameProcessing0Players()
    {
        var game = GameData.getStandardGame();

        assertThrows(IllegalArgumentException.class, game::gameProcessing);
    }

    @Test
    void gameGameProcessing1Players() throws Exception
    {
        var players = List.<DecisionMakerBuilder>of(
            RandomBot::new
        );

        var game = GameData.getStandardGame();

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

        var game = GameData.getStandardGame();

        for (DecisionMakerBuilder player : players)
        {
            game.addPlayer(player);
        }

        assertThrows(IllegalAccessException.class, () -> game.addPlayer(RandomBot::new));
    }
}