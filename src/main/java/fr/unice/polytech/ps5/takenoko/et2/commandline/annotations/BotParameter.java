package fr.unice.polytech.ps5.takenoko.et2.commandline.annotations;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BotParameter
{
    int lowerBound() default -1;
    int upperBound() default -1;
}
