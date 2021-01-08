package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.commandline.DecisionMakerHandler;
import fr.unice.polytech.ps5.takenoko.et2.commandline.LogLevelHandler;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Game;
import picocli.CommandLine;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.IntStream;

@CommandLine.Command(
    name = "takenoko",
    mixinStandardHelpOptions = true,
    abbreviateSynopsis = true,
    requiredOptionMarker = '*',
    sortOptions = false,
    usageHelpWidth = 120,
    version = "TakenokoET2 0.6")
public class TakenokoRunner implements Runnable
{
    private static final Logger LOGGER = Logger.getLogger(TakenokoRunner.class.getSimpleName());
    private static final Random RNG = new SecureRandom(); // new Random();
    @CommandLine.Parameters(
        paramLabel = "TYPE",
        index = "0",
        description = { "List of bots to use.", "A value of 0 for the 'level' parameter usually indicates a level equivalent to that of the random bot.", "Valid values: ${COMPLETION-CANDIDATES}" },
        completionCandidates = DecisionMakerHandler.class,
        converter = DecisionMakerHandler.class,
        arity = "2..4")
    private DecisionMakerBuilder[] players;
    @CommandLine.Option(
        required = true,
        names = { "-n", "--num-games" },
        description = "Number of games to run.")
    private Integer numGames;
    @CommandLine.Option(
        required = true,
        names = { "-l", "--log-level" },
        description = { "Logging level (default: ${DEFAULT-VALUE}).", "Valid values: ${COMPLETION-CANDIDATES}" },
        defaultValue = "SEVERE",
        completionCandidates = LogLevelHandler.class,
        converter = LogLevelHandler.class
    )
    private Level logLevel;
    @CommandLine.Option(
        names = { "-s", "--sequential" },
        description = "Run the games sequentially (default: parallel).")
    private boolean sequential;
    @CommandLine.Option(
        names = { "-S", "--seed" },
        description = "Custom seed to use for the RNG.")
    private long seed;
    @CommandLine.Option(
        names = { "-i", "--ignore-threshold" },
        description = {
            "Ignore objective threshold (default: ${DEFAULT-VALUE}).",
            "Games ending because of turn count limit or deadlocks will still designate winners based on their score, even if it doesn't reach the win threshold."
        },
        defaultValue = "false")
    private boolean ignoreThreshold;
    @CommandLine.Option(
        names = { "-q", "--quiet" },
        description = { "Only display the output table."},
        defaultValue = "false")
    private boolean quiet;
    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    public static void main(String[] args)
    {
        // console log format
        System.setProperty("java.util.logging.SimpleFormatter.format",
            "%1$tF %1$tT %4$s %3$s : %5$s%6$s%n");

        var cmd = new CommandLine(new TakenokoRunner())
            .setCaseInsensitiveEnumValuesAllowed(true);
        if (args.length == 0)
        {
            cmd.execute("-n", "1000", "minmax(1)", "minmax(0)", "plot(3)", "random", "-q");
            System.out.println();
            cmd.execute("-n", "1000", "minmax(1)", "minmax(1)", "minmax(1)", "minmax(1)", "-q");
        }
        else
        {
            cmd.execute(args);
        }
    }

    public void run()
    {
        try
        {
            updateLogLevel();

            if (numGames < 1)
            {
                throw new IllegalArgumentException("The number of games must be positive");
            }

            var botNames = spec.positionalParameters().get(0).originalStringValues();

            if (!quiet)
            {
                System.out.printf("Running %d games %s with bots: %s. Please wait...%n", numGames, sequential ? "sequentially" : "in parallel", botNames);
            }

            var freq = Arrays.stream(players).map(p -> new AtomicInteger()).toArray(AtomicInteger[]::new);
            var score = Arrays.stream(players).map(p -> new AtomicInteger()).toArray(AtomicInteger[]::new);
            final var N = numGames;
            final var progWidth = 50;
            AtomicInteger Nempty = new AtomicInteger();
            AtomicInteger Nlimit = new AtomicInteger();
            AtomicInteger Nfinished = new AtomicInteger();
            var start = Instant.now();
            var stream = IntStream.range(0, N);
            if (!sequential)
            {
                stream = stream.parallel();
            }

            stream.forEach(i ->
            {
                try
                {
                    Random rng = spec.optionsMap().get("--seed").stringValues().isEmpty()
                        ? RNG
                        : new Random(seed);
                    var game = new Game(rng);

                    for (DecisionMakerBuilder player : players)
                    {
                        game.addPlayer(player);
                    }
                    LOGGER.info("start " + i);
                    var res = game.gameProcessing(ignoreThreshold);
                    LOGGER.info("end " + i);

                    var count = Nfinished.incrementAndGet();

                    var width = count * progWidth / N;

                    if (!quiet)
                    {
                        System.out.print("\r[" + "*".repeat(width) + " ".repeat(progWidth - width) + "] " + (count * 100 / N) + "%");
                    }

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

                            var gp = game.getPlayers();
                            for (int i1 = 0; i1 < players.length; i1++)
                            {
                                score[i1].addAndGet(gp.get(i1).countPoints());
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            });
            var duration = Duration.between(start, Instant.now());

            if (!quiet)
            {
                System.out.println();
                System.out.printf("Total time elapsed: %d.%03ds (%.2f games/sec)%n",
                    duration.getSeconds(),
                    duration.getNano() / 1000000,
                    N * 1000000000d / duration.toNanos());
                System.out.println();
            }
            System.out.println(" Bot type   |  % wins | # wins | Avg. score");
            for (int i = 0; i < players.length; i++)
            {
                System.out.printf(" %-10s | %6.2f%% | %6d |    %4.1f%n", botNames.get(i), freq[i].get() * 100d / N, freq[i].get(), score[i].get() / (double)N);
            }
            if (!quiet)
            {
                System.out.println();
                if (ignoreThreshold)
                {
                    System.out.println("The objective threshold was ignored (--ignore-threshold). The above statistics ignore turn count limits and deadlocks.");
                }
                else
                {
                    System.out.println("Percentage of games");
                    System.out.printf("- that reached max turn count limit: %.2f%%%n", Nlimit.get() * 100.0 / N);
                    System.out.printf("- that were deadlocked: %.2f%%%n", Nempty.get() * 100.0 / N);
                }
            }
        }
        catch (Throwable e)
        {
            System.err.println("An error occured during the processing: " + e.getMessage());
        }
    }

    private void updateLogLevel()
    {
        var root = LogManager.getLogManager().getLogger("");
        root.setLevel(logLevel);
        Arrays.stream(root.getHandlers()).forEach(h -> h.setLevel(logLevel));
    }
}
