package fr.unice.polytech.ps5.takenoko.et2.gameplay;

import fr.unice.polytech.ps5.takenoko.et2.board.Edge;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTileImprovement;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;
import fr.unice.polytech.ps5.takenoko.et2.objective.PandaObjective;

import java.util.*;

/**
 * The class representing the player's belongings and actions upon those belongings.
 */
public class Player
{
    private static final int HAND_SIZE = 5;
    private final List<Objective> hand = new ArrayList<>(HAND_SIZE);
    private final List<Objective> objectivesCompleted = new ArrayList<>(HAND_SIZE);
    private final Game game;
    private final DecisionMaker decisionMaker;
    private final List<LandTileImprovement> chipReserve = new ArrayList<>();
    private boolean hasTriggeredEmperor;
    private int nbIrrigationsInStock = 0;
    private final Map<Color, Integer> bambooSectionReserve= new HashMap<>();


    /**
     * Constructor of this Player
     */
    public Player(Game game, DecisionMakerBuilder builder)
    {
        Objects.requireNonNull(builder, "builder must not be null");
        this.game = Objects.requireNonNull(game, "game must not be null");
        this.decisionMaker = builder.build(this);
        this.hasTriggeredEmperor = false;
        bambooSectionReserve.put(Color.GREEN, 0);
        bambooSectionReserve.put(Color.YELLOW, 0);
        bambooSectionReserve.put(Color.PINK, 0);
    }

    /**
     * @return the number of objectives this player has completed throughout the game
     */
    public int completedObjectivesCount()
    {
        return this.objectivesCompleted.size();
    }

    /**
     * Counts the total amount of points this player has earned completing objectives.
     *
     * @return total points of completed objectives
     */
    public int countPoints()
    {
        return objectivesCompleted
            .stream()
            .mapToInt(Objective::getPoints).sum() + (hasTriggeredEmperor ? 2 : 0);
    }

    /**
     * @return the amount of points earned from {@link fr.unice.polytech.ps5.takenoko.et2.objective.PandaObjective}
     */
    public int countPointsPanda()
    {
        return objectivesCompleted
            .stream()
            .filter(obj -> obj instanceof PandaObjective)
            .mapToInt(Objective::getPoints).sum();
    }

    /**
     * Adds one objective to this player hand
     *
     * @param objective a plot, gardener or panda objective
     */
    public void addObjective(Objective objective)
    {
        this.hand.add(Objects.requireNonNull(objective, "objective must not be null"));
    }

    /**
     * Moves an objective from the hand of this player to their objectivesCompleted storage.
     * Must be called when an objective was just completed.
     *
     * @param objective to move from hand to objectivesCompleted
     */
    public void moveObjectiveToComplete(Objective objective)
    {
        Objects.requireNonNull(objective, "objective must not be null");
        if (!hand.contains(objective))
        {
            throw new IllegalArgumentException("Hand of player does not contain given objective");
        }
        objectivesCompleted.add(objective);
        hand.remove(objective);
        objective.postValidation(this);
    }

    /**
     * Gets read-only Hand of objectives
     *
     * @return List of objectives
     */
    public List<Objective> getHand()
    {
        return Collections.unmodifiableList(hand);
    }

    /**
     * Checks if this player's hand of Objectives is full
     *
     * @return true if this player can pick up another Objective, false otherwise
     */
    public boolean isHandFull()
    {
        return hand.size() == HAND_SIZE;
    }

    /**
     * @return associated {@link fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker}
     */
    public DecisionMaker getDecisionMaker()
    {
        return decisionMaker;
    }

    /**
     * @return associated Game
     */
    public Game getGame()
    {
        return game;
    }

    /**
     * @return if this player has triggered the emperor
     */
    public boolean isHasTriggeredEmperor()
    {
        return hasTriggeredEmperor;
    }

    /**
     * Triggers the emperor.
     * Sets hasTriggeredEmperor to true
     */
    public void triggerEmperor()
    {
        this.hasTriggeredEmperor = true;
    }

    /**
     * Picks an irrigation from the game. Removes one irrigation in the game's stock and adds it to
     * this player's stock
     */
    public void pickIrrigation()
    {
        nbIrrigationsInStock++;
    }

    /**
     * Gets the number of irrigations in this player's stock
     *
     * @return the number of the Player's irrigations
     */
    public int getNbIrrigationsInStock()
    {
        return nbIrrigationsInStock;
    }

    /**
     * Irrigates a specific Edge using an available irrigation in this player's stock
     *
     * @param edge the Edge to be irrigated
     * @return true if the Edge could be irrigated, false otherwise
     */
    public boolean irrigateEdge(Edge edge)
    {
        Objects.requireNonNull(edge, "edge must not be null");
        if (nbIrrigationsInStock <= 0)
        {
            return false;
        }
        if (edge.isIrrigated())
        {
            return false;
        }
        LandTile[] tilesToGrowBamboo = edge.addIrrigation();
        nbIrrigationsInStock--;
        if (tilesToGrowBamboo == null)
        {
            return true;
        }
        for (LandTile landTile : tilesToGrowBamboo)
        {
            try
            {
                game.addBambooSectionToTile(landTile);
            }
            catch (Exception e)
            {
                //Do nothing
            }
        }
        return true;
    }

    /**
     * Adds a LandTileImprovement to this player's reserve (called after a cloud)
     *
     * @param landTileImprovement the chip to add
     */
    public void addChip(LandTileImprovement landTileImprovement)
    {
        Objects.requireNonNull(landTileImprovement, "landTileImprovement must not be null");
        chipReserve.add(landTileImprovement);
    }

    /**
     * Gets a List containing this player's LandTileImprovements
     *
     * @return the List
     */
    public List<LandTileImprovement> getChipReserve()
    {
        return chipReserve;
    }


    /**
     * Adds a bamboo section to this player's reserve (called after moving the panda)
     *
     * @param color the Color of the bamboo section to add to this player hand
     */
    public void addBambooSection(Color color)
    {
        Objects.requireNonNull(color, "color must not be null");
        bambooSectionReserve.put(color, bambooSectionReserve.get(color) + 1);
    }

    /**
     * Removes the bamboo sections of this player's reserve (called after {@link fr.unice.polytech.ps5.takenoko.et2.objective.PandaObjective} validated)
     *
     * @param bambooSectionList Map of Color and number of the bamboo sections eg [GREEN, 1, PINK, 2]
     */
    public void removeBambooSection(Map<Color, Integer> bambooSectionList)
    {
        Objects.requireNonNull(bambooSectionList, "bambooSectionList must not be null");
        if (bambooSectionList.isEmpty())
        {
            throw new IllegalArgumentException("bambooList must not be empty");
        }

        for(Color color : bambooSectionList.keySet()) {
            Integer newValue = this.bambooSectionReserve.get(color) - bambooSectionList.get(color);
            bambooSectionReserve.put(color, newValue);
        }

    }

    /**
     * Gets a List containing this player's BambooSection
     *
     * @return the List
     */
    public Map<Color, Integer> getBambooSectionReserve()
    {
        return bambooSectionReserve;
    }

    /**
     * Gets number of bamboos owned by this player
     *
     * @return the sum
     */
    public int getBambooSum()
    {
        int total = 0;
        for (Map.Entry<Color, Integer> entry : bambooSectionReserve.entrySet())
        {
            total += entry.getValue();
        }
        return total;
    }

    /**
     * @return a String describing this player
     */
    @Override
    public String toString()
    {
        StringBuilder playerString = new StringBuilder("Game : " + game.toString() +
            "\nDecisionMaker : " + decisionMaker.toString() +
            "Triggered Emperor : " + hasTriggeredEmperor +
            "Ojectives in Hand : ");
        for (Objective objective : hand)
        {
            playerString.append(objective.toString()).append("\n");
        }
        playerString.append("Objectives completed :");
        for (Objective objective : objectivesCompleted)
        {
            playerString.append(objective.toString()).append("\n");
        }
        return playerString.toString();
    }
}