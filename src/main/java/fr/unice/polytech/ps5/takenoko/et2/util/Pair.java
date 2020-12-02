package fr.unice.polytech.ps5.takenoko.et2.util;

/**
 * Generic Pair class
 */
public class Pair<T1, T2>
{
    public final T1 first;
    public final T2 second;

    private Pair(T1 first, T2 second)
    {
        this.first = first;
        this.second = second;
    }

    public static <T1, T2> Pair<T1, T2> of(T1 first, T2 second)
    {
        return new Pair<>(first, second);
    }

    private static int hashCode(Object o)
    {
        return o == null ? 0 : o.hashCode();
    }

    @Override
    public int hashCode()
    {
        return 31 * hashCode(first) + hashCode(second);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Pair))
        {
            return false;
        }
        if (this == obj)
        {
            return true;
        }
        var pair = (Pair) obj;
        return equals(first, pair.first)
            && equals(second, pair.second);
    }

    private boolean equals(Object o1, Object o2)
    {
        return o1 == null ? o2 == null : (o1 == o2 || o1.equals(o2));
    }

    @Override
    public String toString()
    {
        return "(" + first + ", " + second + ')';
    }

    public T1 getFirst()
    {
        return first;
    }

    public T2 getSecond()
    {
        return second;
    }
}