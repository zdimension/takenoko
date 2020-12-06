package fr.unice.polytech.ps5.takenoko.et2.commandline;

import picocli.CommandLine;

import java.util.logging.Level;

public class LogLevelConverter implements CommandLine.ITypeConverter<Level>
{
    @Override
    public Level convert(String s) throws IllegalArgumentException
    {
        return Level.parse(s);
    }
}
