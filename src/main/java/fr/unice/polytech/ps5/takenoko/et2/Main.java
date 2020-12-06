package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.MinMaxBot;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.RandomBot;
import picocli.CommandLine;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@CommandLine.Command(name = "takenoko", mixinStandardHelpOptions = true)
public class Main implements Runnable
{
    private static final Logger LOGGER = Logger.getLogger(Main.class.getSimpleName());

    @CommandLine.Parameters(
        paramLabel = "botType",
        index = "0",
        description = "List of bots to use",
        converter = DecisionMakerConverter.class,
        arity = "2..4")
    private DecisionMakerBuilder[] players;

    @CommandLine.Option(
        required = true,
        names = { "-n", "--num-games" },
        description = "Number of games to run")
    private Integer numGames;

    @CommandLine.Option(
        required = true,
        names = { "-l", "--log-level" },
        description = { "Logging level (default: ${DEFAULT-VALUE}).", "Valid values: ${COMPLETION-CANDIDATES}" },
        defaultValue = "SEVERE",
        completionCandidates = LogLevelCandidates.class,
        converter = LogLevelConverter.class
    )
    private Level logLevel;

    @CommandLine.Option(
        names = { "-s", "--sequential" },
        description = "Run the games sequentially (default: parallel).")
    private boolean sequential;

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    public static void main(String... args)
    {
        // console log format
        System.setProperty("java.util.logging.SimpleFormatter.format",
            "%1$tF %1$tT %4$s %3$s : %5$s%6$s%n");

        new CommandLine(new Main())
            .setCaseInsensitiveEnumValuesAllowed(true)
            .execute(args);
    }

    public void run()
    {
        var root = LogManager.getLogManager().getLogger("");
        root.setLevel(logLevel);
        Arrays.stream(root.getHandlers()).forEach(h -> h.setLevel(logLevel));

        var freq = Arrays.stream(players).map(p -> new AtomicInteger()).toArray(AtomicInteger[]::new);
        final var N = numGames;
        AtomicInteger Nempty = new AtomicInteger();
        AtomicInteger Nlimit = new AtomicInteger();
        var start = Instant.now();
        var stream =IntStream.range(0, N);
        if (!sequential)
            stream = stream.parallel();
        stream.forEach(i ->
        {
            try
            {
                //var game = GameData.getStandardGame();
                var game = new Game(GameData.getStandardObjectives(), GameData.getLandTiles(11, 9, 7));
                for (DecisionMakerBuilder player : players)
                {
                    game.addPlayer(player);
                }
                LOGGER.info("start " + i);
                var res = game.gameProcessing(true);
                LOGGER.info("end " + i);

                if (res == null)
                {
                    Nempty.getAndIncrement();
                }
                else if (res.isEmpty())
                {
                    Nlimit.getAndIncrement();
                }
                else
                {
                    for (Integer re : res)
                    {
                        freq[re].getAndIncrement();
                    }
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });

        var duration = Duration.between(start, Instant.now());
        System.out.printf("Total time elapsed: %d.%03ds (%.2f games/sec)%n",
            duration.getSeconds(),
            duration.getNano() / 1000000,
            N * 1000000000d / duration.toNanos());
        System.out.println("Percentage of games");
        System.out.println("- won per player: " +
            Arrays.stream(freq).mapToInt(AtomicInteger::get).mapToObj(d -> String.format("%.2f%%", (double) d * 100 / N)).collect(Collectors.joining(" ; ")));
        System.out.printf("- that reached max turn count limit: %.2f%%%n", Nlimit.get() * 100.0 / N);
        System.out.printf("- that were deadlocked: %.2f%%%n", Nempty.get() * 100.0 / N);
    }

    private static class DecisionMakerConverter implements CommandLine.ITypeConverter<DecisionMakerBuilder>
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
            var clazz =  Objects.requireNonNull(types.getOrDefault(name, null));
            var meth = getBuilder(clazz);
            var params = meth.getParameters();
            try
            {
                var paramsGroup = matcher.group(2);
                String[] groupstr;
                if (paramsGroup == null)
                    groupstr = new String[0];
                else
                    groupstr = paramsGroup.split(",");
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

    private static class LogLevelCandidates extends ArrayList<String>
    {
        LogLevelCandidates()
        {
            super(Arrays.stream(Level.class.getDeclaredFields()).filter(f ->
                Modifier.isStatic(f.getModifiers()) &&
                    Modifier.isFinal(f.getModifiers()) &&
                    f.getType() == Level.class).map(Field::getName).collect(Collectors.toList()));
        }
    }

    private static class LogLevelConverter implements CommandLine.ITypeConverter<Level>
    {
        @Override
        public Level convert(String s) throws IllegalArgumentException
        {
            return Level.parse(s);
        }
    }
}
