package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.RandomBot;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Main
{
    private static final Logger LOGGER = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String... args)
    {
        // console log format
        System.setProperty("java.util.logging.SimpleFormatter.format",
            "%1$tF %1$tT %4$s %3$s : %5$s%6$s%n");

        // only show warnings
        var level = Level.SEVERE;
        var root = LogManager.getLogManager().getLogger("");
        root.setLevel(level);
        Arrays.stream(root.getHandlers()).forEach(h -> h.setLevel(level));

        var players = List.<DecisionMakerBuilder>of(
            //MinMaxBot::new,
            //MinMaxBot.getBuilder(2),
            //MinMaxBot.getBuilder(2),
            //MinMaxBot.getBuilder(2),
            RandomBot::new,
            //RandomBot::new,
            //RandomBot::new,
            RandomBot::new
        );
        var freq = players.stream().map(p -> new AtomicInteger()).toArray(AtomicInteger[]::new);
        final var N = 10000;
        AtomicInteger Nempty = new AtomicInteger();
        var start = Instant.now();
        IntStream.range(0, N).parallel().mapToObj(i ->
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
                var res = game.gameProcessing();
                LOGGER.info("end " + i);
                return res;
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }).forEach(res ->
        {
            if (res.isEmpty())
            {
                Nempty.getAndIncrement();
            }
            else
            {
                for (Integer re : res)
                {
                    freq[re].getAndIncrement();
                }
            }
        });

        var duration = Duration.between(start, Instant.now());
        System.out.printf("Total: %d.%03ds (%.2f games/sec)%n",
            duration.getSeconds(),
            duration.getNano() / 1000000,
            N * 1000000000d / duration.toNanos());
        System.out.println(Arrays.toString(Arrays.stream(freq).mapToInt(AtomicInteger::get).asDoubleStream().map(d -> d / N).toArray()));
        System.out.printf("%.2f%% dead-ends%n", Nempty.get() * 100.0 / N);
    }
}
