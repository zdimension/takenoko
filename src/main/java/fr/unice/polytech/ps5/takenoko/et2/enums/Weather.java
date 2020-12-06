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
    SUN
        {
            @Override
            public String toString()
            {
                return "Sun";
            }
        },
    /**
     * When it rains, DecisionMaker can choose to make bamboo grow. Oridnary constraints are applied
     * to this growth.
     */
    RAIN(true)
        {
            @Override
            public String toString()
            {
                return "Rain";
            }
        },
    /**
     * When the wind blows, DecisionMaker can perform two identical actions during the turn if he
     * pleases.
     */
    WIND
        {

            @Override
            public String toString()
            {
                return "Wind";
            }
        },
    /**
     * When the storm bursts, DecisionMaker can choose to put the panda on a tile of his choice so
     * it eats bamboo on the tile it lands on.
     */
    STORM(true)
        {
            @Override
            public String toString()
            {
                return "Storm";
            }
        },
    /**
     * When clouds overcast the sky, DecisionMaker draws an improvement chip from the reserve. If
     * there is no chip left, DecisionMaker chooses another weather condition.
     */
    CLOUDS(true)
        {
            @Override
            public String toString()
            {
                return "Clouds";
            }
        },
    /**
     * DecisionMaker chooses another weather condition.
     */
    QUESTION_MARK
        {
            @Override
            public String toString()
            {
                return "Question Mark";
            }
        };

    private final boolean directAction;

    Weather(boolean directAction)
    {
        this.directAction = directAction;
    }

    Weather()
    {
        this(false);
    }

    public boolean isDirectAction()
    {
        return directAction;
    }


}
