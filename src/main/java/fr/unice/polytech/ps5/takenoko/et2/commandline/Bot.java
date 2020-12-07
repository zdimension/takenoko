package fr.unice.polytech.ps5.takenoko.et2.commandline;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Bot
{
    String key();
}
