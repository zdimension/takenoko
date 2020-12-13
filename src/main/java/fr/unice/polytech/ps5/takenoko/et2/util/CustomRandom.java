package fr.unice.polytech.ps5.takenoko.et2.util;

import java.util.Random;

public class CustomRandom
{
    public Random rnd;

    public CustomRandom()
    {
        rnd = new Random();
    }

    public CustomRandom(long seed)
    {
        rnd = new Random(seed);
    }
}
