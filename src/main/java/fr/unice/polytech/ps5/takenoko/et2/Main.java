package fr.unice.polytech.ps5.takenoko.et2;

import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.MinMaxBot;
import fr.unice.polytech.ps5.takenoko.et2.decision.bots.RandomBot;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Main
{
    private static final Logger LOGGER = Logger.getLogger(Main.class.getSimpleName());

    static Game getDefaultData() throws Exception
    {
        var land = new ArrayList<LandTile>();
        for (var i = 0; i < 11; i++)
        {
            land.add(new LandTile(Color.GREEN));
        }
        for (var i = 0; i < 9; i++)
        {
            land.add(new LandTile(Color.YELLOW));
        }
        for (var i = 0; i < 7; i++)
        {
            land.add(new LandTile(Color.PINK));
        }
        var objectives = new ArrayList<PlotObjective>();
        for (var x : Map.of(Color.GREEN, 2, Color.YELLOW, 3, Color.PINK, 4).entrySet())
        {
            var color = x.getKey();
            var score = x.getValue();
            objectives.add(new PlotObjective(score, Collections.nCopies(3, color), List.of(0, 2)));
            objectives.add(new PlotObjective(score, Collections.nCopies(3, color), List.of(0, 2)));
            objectives.add(new PlotObjective(score, Collections.nCopies(3, color), List.of(0, 2)));
            objectives.add(new PlotObjective(score + 1, Collections.nCopies(4, color), List.of(0, 2, 3)));
        }
        objectives.add(new PlotObjective(3, List.of(Color.GREEN, Color.GREEN, Color.YELLOW, Color.YELLOW), List.of(2, 3, 5)));
        objectives.add(new PlotObjective(4, List.of(Color.GREEN, Color.GREEN, Color.PINK, Color.PINK), List.of(2, 3, 5)));
        objectives.add(new PlotObjective(5, List.of(Color.PINK, Color.PINK, Color.YELLOW, Color.YELLOW), List.of(2, 3, 5)));
        return new Game(objectives, land);
    }

    public static void main(String... args)
    {
        // console log format
        System.setProperty("java.util.logging.SimpleFormatter.format",
            "%1$tF %1$tT %4$s %3$s : %5$s%6$s%n");

        // only show warnings
        Arrays.stream(LogManager.getLogManager().getLogger("").getHandlers())
            .forEach(h -> h.setLevel(Level.SEVERE));

        var players = List.<DecisionMakerBuilder>of(
            //MinMaxBot::new,
            MinMaxBot.getBuilder(2),
            //RandomBot::new,
            RandomBot::new
        );
        var freq = players.stream().map(p -> new AtomicInteger()).toArray(AtomicInteger[]::new);
        final var N = 10;
        AtomicInteger Nempty = new AtomicInteger();
        var start = Instant.now();
        IntStream.range(0, N).parallel().mapToObj(i ->
        {
            try
            {
                var game = getDefaultData();
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
