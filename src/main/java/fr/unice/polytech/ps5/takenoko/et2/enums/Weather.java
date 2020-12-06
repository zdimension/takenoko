package fr.unice.polytech.ps5.takenoko.et2.enums;

/**
 * Weather conditions occuring during a turn. Constants may have a parameter to indicate wheather
 * the weather condition involve DecisionMaker to perform an action instantly or not.
 * All the processing is performed in Game.
 */
public enum Weather
{
    /**
     * When the Sun shines, DecisionMaker gains an additional action to perform during his turn. This
     * additional action faces the same constraints the ordinary actions do.
     */
    SUN("Sun"),
    /**
     * When it rains, DecisionMaker can choose to make bamboo grow. Oridnary constraints are applied
     * to this growth.
     */
    RAIN("Rain", true),
    /**
     * When the wind blows, DecisionMaker can perform two identical actions during the turn if he
     * pleases.
     */
    WIND("Wind"),
    /**
     * When the storm bursts, DecisionMaker can choose to put the panda on a tile of his choice so
     * it eats bamboo on the tile it lands on.
     */
    STORM("Storm", true),
    /**
     * When clouds overcast the sky, DecisionMaker draws an improvement chip from the reserve. If
     * there is no chip left, DecisionMaker chooses another weather condition.
     */
    CLOUDS("Clouds", true),
    /**
     * DecisionMaker chooses another weather condition.
     */
    QUESTION_MARK("Question Mark");

    private final String name;
    private final boolean directAction;

    Weather(String name, boolean directAction)
    {
        this.name = name;
        this.directAction = directAction;
    }

    Weather(String name)
    {
        this(name, false);
    }

    public boolean isDirectAction()
    {
        return directAction;
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
