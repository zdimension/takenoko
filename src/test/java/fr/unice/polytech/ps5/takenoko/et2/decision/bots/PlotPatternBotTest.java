package fr.unice.polytech.ps5.takenoko.et2.decision.bots;

import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Game;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

class PlotPatternBotTest
{
    private PlotPatternBot patternBot;
    private Game game;
    private Player player;
    private Board board;

    @BeforeEach
    void initBot()
    {
        try
        {
            game = spy(new Game());
            game.addPlayer(PlotPatternBot.getBuilder(1));
            game.addPlayer(PlotPatternBot.getBuilder(1));
            Optional<Player> p = game.getPlayers().stream().findAny();
            if (p.isEmpty())
            {
                fail();
            }
            patternBot = spy((PlotPatternBot) p.get().getDecisionMaker());
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
            fail();
        }
        player = spy(patternBot.getPlayer());
        board = game.getBoard();
    }

    @Test
    void chooseActionTest() {
        List<GameAction> gameActionChoises1 = new ArrayList<>(List.of(GameAction.DRAW_TILE, GameAction.COMPLETE_OBJECTIVE, GameAction.DRAW_OBJECTIVE));
        List<GameAction> gameActionChoises2 = new ArrayList<>(List.of(GameAction.DRAW_TILE, GameAction.MOVE_PANDA, GameAction.DRAW_OBJECTIVE));

        assertEquals(GameAction.COMPLETE_OBJECTIVE, patternBot.chooseAction(gameActionChoises1));
        assertEquals(GameAction.DRAW_OBJECTIVE, patternBot.chooseAction(gameActionChoises2));
    }

}