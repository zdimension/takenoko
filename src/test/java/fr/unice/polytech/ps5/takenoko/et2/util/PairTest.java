package fr.unice.polytech.ps5.takenoko.et2.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class PairTest
{
    @Test
    void getTest()
    {
        Object o1 = new Object();
        Object o2 = new Object();
        Pair<Object, Object> p = Pair.of(o1, o2);
        assertSame(p.getFirst(), o1);
        assertSame(p.getSecond(), o2);
    }
}
