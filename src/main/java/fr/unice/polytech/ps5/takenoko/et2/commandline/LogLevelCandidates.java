package fr.unice.polytech.ps5.takenoko.et2.commandline;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class LogLevelCandidates extends ArrayList<String>
{
    LogLevelCandidates()
    {
        super(Arrays.stream(Level.class.getDeclaredFields()).filter(f ->
            Modifier.isStatic(f.getModifiers()) &&
                Modifier.isFinal(f.getModifiers()) &&
                f.getType() == Level.class).map(Field::getName).collect(Collectors.toList()));
    }
}
