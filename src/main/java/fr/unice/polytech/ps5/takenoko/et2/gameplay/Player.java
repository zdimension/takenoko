package fr.unice.polytech.ps5.takenoko.et2.gameplay;

import fr.unice.polytech.ps5.takenoko.et2.board.Edge;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTile;
import fr.unice.polytech.ps5.takenoko.et2.board.LandTileImprovement;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMaker;
import fr.unice.polytech.ps5.takenoko.et2.decision.DecisionMakerBuilder;
import fr.unice.polytech.ps5.takenoko.et2.enums.Color;
import fr.unice.polytech.ps5.takenoko.et2.objective.Objective;

import java.util.*;

/**
 * The class representing the player.
 */
public class Player
{
    public static final int HAND_SIZE = 5;
    private final List<Objective> hand = new ArrayList<>(HAND_SIZE);
    private final List<Objective> objectivesCompleted = new ArrayList<>(HAND_SIZE);
    private final Game game;
    private final DecisionMaker decisionMaker;
    private final List<LandTileImprovement> chipReserve = new ArrayList<>();
    private boolean hasTriggeredEmperor;
    private int nbIrrigationsInStock = 0;
    private final Map<Color, Integer> bambooSectionReserve= new HashMap<>();


    /**
     * Constructor of the Player
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
     * @return the number of objectives the player has completed during the game
     */
    public int completedObjectivesCount()
    {
        return this.objectivesCompleted.size();
    }

    /**
     * Counts the total amount of points the player has earned completing objectives.
     *
     * @return total points of completed objectives
     */
    public int countPoints()
    {
        int totalPoint = 0;
        for (Objective objective : this.objectivesCompleted)
        {
            totalPoint += objective.getPoints();
        }
        if (hasTriggeredEmperor)
        {
            totalPoint += 2;
        }
        return totalPoint;
    }

    /**
     * Add one objective to the player hand
     *
     * @param objective a plot, gardener or panda objective
     */
    public void addObjective(Objective objective)
    {
        this.hand.add(Objects.requireNonNull(objective, "objective must not be null"));
    }

    /**
     * Moves an objective from the hand of the player to their objectivesCompleted storage.
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
    }

    /**
     * Get read-only Hand of objectives
     *
     * @return List of objectives
     */
    public List<Objective> getHand()
    {
        return Collections.unmodifiableList(hand);
    }

    /**
     * Check if the player's hand of Objectives is full
     *
     * @return true if the Player can pick up another Objective, false otherwise
     */
    public boolean isHandFull()
    {
        return hand.size() == HAND_SIZE;
    }

    /**
     * @return associated DecisionMaker
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
     * @return if player has triggered the emperor
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
     * Pick an irrigation from the game. Remove one irrigation in the game's deck and add it to the player's stock
     */
    public void pickIrrigation()
    {
        nbIrrigationsInStock++;
    }

    /**
     * Get the number of irrigations in the Player's stock
     *
     * @return The number of the Player's irrigations
     */
    public int getNbIrrigationsInStock()
    {
        return nbIrrigationsInStock;
    }

    /**
     * Irrigate a specific Edge
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
     * Add a LandTileImprovement to the player's reserve (called after a cloud)
     *
     * @param landTileImprovement The chip to add
     */
    public void addChip(LandTileImprovement landTileImprovement)
    {
        Objects.requireNonNull(landTileImprovement, "landTileImprovement must not be null");
        chipReserve.add(landTileImprovement);
    }

    /**
     * Get a List containing the player's LandTileImprovements
     *
     * @return the List
     */
    public List<LandTileImprovement> getChipReserve()
    {
        return chipReserve;
    }


    /**
     * Add a bamboo section to the player's reserve (called after moving the panda)
     *
     * @param color the Color of the bamboo section to add to the player hand
     */
    public void addBambooSection(Color color)
    {
        Objects.requireNonNull(color, "color must not be null");
        bambooSectionReserve.put(color, bambooSectionReserve.get(color) + 1);
    }

    /**
     * Remove the bamboo sections of the player's reserve (called after PandaObjective validated)
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
     * Get a List containing the player's BambooSection
     *
     * @return the List
     */
    public Map<Color, Integer> getBambooSectionReserve()
    {
        return bambooSectionReserve;
    }


    /**
     * @return a String describing the player
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