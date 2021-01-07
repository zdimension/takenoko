package fr.unice.polytech.ps5.takenoko.et2.decision.bots;

import fr.unice.polytech.ps5.takenoko.et2.board.Board;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.TilePosition;
import fr.unice.polytech.ps5.takenoko.et2.commandline.annotations.Bot;
import fr.unice.polytech.ps5.takenoko.et2.commandline.annotations.BotParameter;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.GameAction;
import fr.unice.polytech.ps5.takenoko.et2.gameplay.Player;
import fr.unice.polytech.ps5.takenoko.et2.objective.PlotObjective;
import fr.unice.polytech.ps5.takenoko.et2.util.Pair;

import java.util.List;
import java.util.stream.Collectors;

@Bot(key = "plot")
public class PlotPatternBot extends RandomBot
{
    @BotParameter(lowerBound = 0, upperBound = 3)
    private final int level;

    private PlotPatternBot(Player player, int level)
    {
        super(player);
        this.level = level;
    }

    public static DecisionMakerBuilder getBuilder(int level)
    {
        return player -> new PlotPatternBot(player, level);
    }

    @Override
    public GameAction chooseAction(List<GameAction> base)
    {
        if (level != 0 && level != 2)
        {
            if (base.contains(GameAction.COMPLETE_OBJECTIVE))
            {
                return GameAction.COMPLETE_OBJECTIVE;
            }

            if (base.contains(GameAction.DRAW_OBJECTIVE))
            {
                return GameAction.DRAW_OBJECTIVE;
            }
        }

        return super.chooseAction(base);
    }

    private List<PlotObjective> getPlotObjectives()
    {
        return player.getHand()
            .stream()
            .filter(PlotObjective.class::isInstance)
            .map(o -> (PlotObjective) o)
            .collect(Collectors.toList());
    }

    @Override
    public Pair<LandTile, TilePosition> chooseTile(List<LandTile> drawnTiles, List<TilePosition> validPos)
    {
        if (level >= 2)
        {
            var objs = getPlotObjectives();
            if (!objs.isEmpty())
            {
                LandTile maxTile = null;
                TilePosition maxPos = null;
                int maxScore = 0;
                for (LandTile tile : drawnTiles)
                {
                    for (TilePosition pos : validPos)
                    {
                        var b = (Board) getBoard().clone();
                        if (b.addTile(tile, pos))
                        {
                            for (PlotObjective obj : objs)
                            {
                                if (obj.checkValidated(b))
                                {
                                    if (obj.getPoints() > maxScore)
                                    {
                                        maxTile = tile;
                                        maxPos = pos;
                                        maxScore = obj.getPoints();
                                    }
                                }
                            }
                        }
                        else
                        {
                            throw new IllegalStateException();
                        }
                    }
                }

                if (maxScore > 0)
                {
                    return Pair.of(maxTile, maxPos);
                }
            }
        }

        return super.chooseTile(drawnTiles, validPos);
    }
}
