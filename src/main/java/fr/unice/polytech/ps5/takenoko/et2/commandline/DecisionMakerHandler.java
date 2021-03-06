package fr.unice.polytech.ps5.takenoko.et2.commandline;

import fr.unice.polytech.ps5.takenoko.et2.commandline.annotations.Bot;
import fr.unice.polytech.ps5.takenoko.et2.commandline.annotations.BotParameter;
import fr.unice.polytech.ps5.takenoko.et2.commandline.exceptions.*;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import org.reflections.Reflections;
import picocli.CommandLine;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Handles the command-line parsing and completion for bot names and parameters
 */
public class DecisionMakerHandler extends ArrayList<String> implements CommandLine.ITypeConverter<DecisionMakerBuilder>
{
    private static final Map<String, Method> types = new HashMap<>();
    private static final List<String> botDisplayNames = new ArrayList<>();
    private static final Pattern namePattern = Pattern.compile("^(\\w+)(?:\\(([^,]+(?:,[^,]+)*)?\\))?$");

    static
    {
        var bots = new Reflections("fr.unice.polytech.ps5.takenoko.et2")
            .getTypesAnnotatedWith(Bot.class);
        for (Class<?> bot : bots)
        {
            //noinspection unchecked
            registerBot(bot.getAnnotation(Bot.class).key(), (Class<? extends DecisionMaker>) bot);
        }
    }

    DecisionMakerHandler()
    {
        super(botDisplayNames);
    }

    private static void registerBot(String name, Class<? extends DecisionMaker> clazz)
    {
        var meth = getBuilder(clazz);
        types.put(name, meth);
        if (meth.getParameterCount() != 0)
        {
            name += "(" + Arrays.stream(meth.getParameters()).map(parameter ->
            {
                var pname = parameter.getName();
                try
                {
                    var field = clazz.getDeclaredField(pname);
                    var annot = field.getAnnotation(BotParameter.class);
                    if (annot != null)
                    {
                        if (annot.lowerBound() != -1)
                        {
                            pname = annot.lowerBound() + "<=" + pname;
                        }
                        if (annot.upperBound() != -1)
                        {
                            pname += "<=" + annot.upperBound();
                        }
                    }
                }
                catch (NoSuchFieldException ignored)
                {
                }
                return pname;
            }).collect(Collectors.joining(", ")) + ")";
        }
        botDisplayNames.add(name);
    }

    private static Method getBuilder(Class<? extends DecisionMaker> cl)
    {
        return Arrays.stream(cl.getMethods()).filter(m -> m.getName().equals("getBuilder")).findFirst()
            .orElseThrow(() -> new MissingBuilderException(cl));
    }

    @Override
    public DecisionMakerBuilder convert(String s) throws IllegalArgumentException
    {
        s = s.replace(" ", "");
        var matcher = namePattern.matcher(s);
        if (!matcher.find())
        {
            throw new InvalidBotTypeSyntaxException();
        }
        var name = matcher.group(1);
        var meth = types.getOrDefault(name, null);
        if (meth == null)
        {
            throw new InvalidBotTypeException(name);
        }
        var params = meth.getParameters();
        try
        {
            var paramsGroup = matcher.group(2);
            String[] groupstr;
            if (paramsGroup == null)
            {
                groupstr = new String[0];
            }
            else
            {
                groupstr = paramsGroup.split(",");
            }
            if (groupstr.length != params.length)
            {
                throw new InvalidBotParameterCountException(params.length, groupstr.length);
            }
            return (DecisionMakerBuilder) meth.invoke(null, IntStream.range(0, params.length).mapToObj(i ->
            {
                try
                {
                    return Integer.parseInt(groupstr[i]);
                }
                catch (Exception e)
                {
                    throw new InvalidBotParameterException(e);
                }
            }).toArray());
        }
        catch (IllegalArgumentException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(e);
        }
    }
}