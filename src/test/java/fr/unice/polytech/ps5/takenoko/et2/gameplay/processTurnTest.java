package fr.unice.polytech.ps5.takenoko.et2.gameplay;

import fr.unice.polytech.ps5.takenoko.et2.GameData;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class processTurnTest
{
    private Game game;
    private DecisionMakerBuilder mockBuilder1;
    private DecisionMakerBuilder mockBuilder2;


    @BeforeEach
    void init() throws IllegalAccessException
    {
        game = GameData.getStandardGame();

        mockBuilder1 = mock(DecisionMakerBuilder.class);
        mockBuilder2 = mock(DecisionMakerBuilder.class);

        game.addPlayer(mockBuilder1);
        game.addPlayer(mockBuilder2);
    }



}