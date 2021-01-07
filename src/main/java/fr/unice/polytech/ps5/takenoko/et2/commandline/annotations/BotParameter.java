package fr.unice.polytech.ps5.takenoko.et2.commandline.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BotParameter
{
    int lowerBound() default -1;
    int upperBound() default -1;
}
