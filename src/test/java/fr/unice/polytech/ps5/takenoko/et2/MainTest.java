package fr.unice.polytech.ps5.takenoko.et2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest
{

    String varToBeInitInSetup;

    @BeforeEach
    void setUp()
    {
        varToBeInitInSetup = "Hello World!";
    }

    @Test
    void helloTest()
    {
        assertEquals(varToBeInitInSetup, Main.hello());
    }
}