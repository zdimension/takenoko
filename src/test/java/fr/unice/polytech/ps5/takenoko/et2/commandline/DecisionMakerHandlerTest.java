package fr.unice.polytech.ps5.takenoko.et2.commandline;

import fr.unice.polytech.ps5.takenoko.et2.commandline.exceptions.InvalidBotParameterCountException;
import fr.unice.polytech.ps5.takenoko.et2.commandline.exceptions.InvalidBotParameterException;
import fr.unice.polytech.ps5.takenoko.et2.commandline.exceptions.InvalidBotTypeException;
import fr.unice.polytech.ps5.takenoko.et2.commandline.exceptions.InvalidBotTypeSyntaxException;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.MinMaxBot;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.RandomBot;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Game;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DecisionMakerHandlerTest
{
    private final DecisionMakerHandler dmh = new DecisionMakerHandler();
    private final Game game = new Game();

    private DecisionMaker getDM(String arg)
    {
        return new Player(game, dmh.convert(arg)).getDecisionMaker();
    }

    @Test
    void convert()
    {
        assertTrue(getDM("random") instanceof RandomBot);

        var mm = getDM("minmax(123)");
        assertTrue(mm instanceof MinMaxBot && ((MinMaxBot)mm).getDepth() == 123);

        assertThrows(InvalidBotTypeSyntaxException.class, () -> getDM("minmax("));
        assertThrows(InvalidBotTypeSyntaxException.class, () -> getDM("minmax)"));
        assertThrows(InvalidBotParameterException.class, () -> getDM("minmax(()"));
        assertThrows(InvalidBotParameterException.class, () -> getDM("minmax(abc)"));

        assertThrows(InvalidBotParameterCountException.class, () -> getDM("minmax(1,2,3,4,5,6)"));
        assertThrows(InvalidBotTypeException.class, () -> getDM("invalid(1,2,3,4,5,6)"));
    }
}