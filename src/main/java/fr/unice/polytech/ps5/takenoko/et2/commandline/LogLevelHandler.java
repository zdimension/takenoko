package fr.unice.polytech.ps5.takenoko.et2.commandline;

import picocli.CommandLine;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Handles the command-line parsing and completion for log levels (-l parameter)
 */
public class LogLevelHandler extends ArrayList<String> implements CommandLine.ITypeConverter<Level>
{
    LogLevelHandler()
    {
        super(Arrays.stream(Level.class.getDeclaredFields()).filter(f ->
            Modifier.isStatic(f.getModifiers()) &&
                Modifier.isFinal(f.getModifiers()) &&
                f.getType() == Level.class).map(Field::getName).collect(Collectors.toList()));
    }

    @Override
    public Level convert(String s) throws IllegalArgumentException
    {
        return Level.parse(s.toUpperCase());
    }
}
