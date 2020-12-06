package fr.unice.polytech.ps5.takenoko.et2.commandline;

import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.MinMaxBot;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.RandomBot;
import picocli.CommandLine;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class DecisionMakerConverter implements CommandLine.ITypeConverter<DecisionMakerBuilder>
{
    private static final Map<String, Class<? extends DecisionMaker>> types = Map.of(
        "random", RandomBot.class,
        "minmax", MinMaxBot.class
    );
    private static final Pattern namePattern = Pattern.compile("^(\\w+)(?:\\(([^,]+(?:,[^,]+)*)?\\))?$");

    private Method getBuilder(Class<? extends DecisionMaker> cl)
    {
        return Arrays.stream(cl.getMethods()).filter(m -> m.getName().equals("getBuilder")).findFirst().get();
    }

    @Override
    public DecisionMakerBuilder convert(String s) throws IllegalArgumentException
    {
        s = s.replace(" ", "");
        var matcher = namePattern.matcher(s);
        if (!matcher.find())
        {
            throw new IllegalArgumentException();
        }
        var name = matcher.group(1);
        var clazz = Objects.requireNonNull(types.getOrDefault(name, null));
        var meth = getBuilder(clazz);
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
                throw new IllegalArgumentException();
            }
            return (DecisionMakerBuilder) meth.invoke(null, IntStream.range(0, params.length).mapToObj(i -> Integer.parseInt(groupstr[i])).toArray());
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException();
        }
    }
}
