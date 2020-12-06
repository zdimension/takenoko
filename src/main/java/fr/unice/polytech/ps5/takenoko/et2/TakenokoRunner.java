package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.commandline.DecisionMakerConverter;
import fr.unice.polytech.ps5.takenoko.et2.commandline.LogLevelCandidates;
import fr.unice.polytech.ps5.takenoko.et2.commandline.LogLevelConverter;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Game;
import picocli.CommandLine;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.IntStream;

@CommandLine.Command(name = "takenoko", mixinStandardHelpOptions = true)
public class TakenokoRunner implements Runnable
{
    private static final Logger LOGGER = Logger.getLogger(TakenokoRunner.class.getSimpleName());

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

        new CommandLine(new TakenokoRunner())
            .setCaseInsensitiveEnumValuesAllowed(true)
            .execute(args);
    }

    public void run()
    {
        var root = LogManager.getLogManager().getLogger("");
        root.setLevel(logLevel);
        Arrays.stream(root.getHandlers()).forEach(h -> h.setLevel(logLevel));

        var botNames = spec.positionalParameters().get(0).originalStringValues();
        System.out.printf("Running %d games %s with bots: %s%n", numGames, sequential ? "sequentially" : "in parallel", botNames);

        var freq = Arrays.stream(players).map(p -> new AtomicInteger()).toArray(AtomicInteger[]::new);
        final var N = numGames;
        AtomicInteger Nempty = new AtomicInteger();
        AtomicInteger Nlimit = new AtomicInteger();
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
        System.out.println("Victory statistics:");
        for (int i = 0; i < players.length; i++)
        {
            System.out.printf("- #%d [%-10s] : %.2f%%%n", i + 1, botNames.get(i), freq[i].get() * 100d / N);
        }
        System.out.println("Percentage of games");
        System.out.printf("- that reached max turn count limit: %.2f%%%n", Nlimit.get() * 100.0 / N);
        System.out.printf("- that were deadlocked: %.2f%%%n", Nempty.get() * 100.0 / N);
    }
}
