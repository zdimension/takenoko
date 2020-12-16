package fr.unice.polytech.ps5.takenoko.et2.enums;

/**
 * Weather conditions occuring during a turn. directAction indicates wheather
 * the weather conditions involve DecisionMaker to perform an action instantly or not.
 * All the processing is performed in Game.
 *
 * <table style="width:70%">
 *     <thead>
 *         <tr>
 *           <th style="text-align:right;width:60%;padding-right:7px">Value</th>
 *           <th style="text-align:left;width:40%;padding-right:7px">directAction</th>
 *         </tr>
 *     </thead>
 *     <tbody>
 *         <tr>
 *             <td style="text-align:right;padding-right:7px">{@link fr.unice.polytech.ps5.takenoko.et2.enums.Weather#SUN}</td>
 *         </tr>
 *         <tr>
 *             <td style="text-align:right;padding-right:7px">{@link fr.unice.polytech.ps5.takenoko.et2.enums.Weather#RAIN}</td>
 *             <td style="padding-left:7px">✔</td>
 *         </tr>
 *         <tr>
 *             <td style="text-align:right;padding-right:7px">{@link fr.unice.polytech.ps5.takenoko.et2.enums.Weather#WIND}</td>
 *         </tr>
 *         <tr>
 *             <td style="text-align:right;padding-right:7px">{@link fr.unice.polytech.ps5.takenoko.et2.enums.Weather#STORM}</td>
 *             <td style="padding-left:7px">✔</td>
 *         </tr>
 *         <tr>
 *             <td style="text-align:right;padding-right:7px">{@link fr.unice.polytech.ps5.takenoko.et2.enums.Weather#CLOUDS}</td>
 *             <td style="padding-left:7px">✔</td>
 *         </tr>
 *         <tr>
 *             <td style="text-align:right;padding-right:7px">{@link fr.unice.polytech.ps5.takenoko.et2.enums.Weather#QUESTION_MARK}</td>
 *         </tr>
 *     </tbody>
 * </table>
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

    /**
     * Name of the Weahter.
     */
    private final String name;
    /**
     * Defines if an action associated with the weather has to be performed in the Weather Phase.
     * (doesn't apply to QUESTION_MARK)
     */
    private final boolean directAction;

    /**
     * Main constructor.
     * @param name of the Weather
     * @param directAction of the Weather
     */
    Weather(String name, boolean directAction)
    {
        this.name = name;
        this.directAction = directAction;
    }

    /**
     * Constructor overload. directAction set to false.
     * @param name of the Weather
     */
    Weather(String name)
    {
        this(name, false);
    }

    /**
     * Check if the weather is a direct action
     *
     * @return true if direct action, false otherwise
     */
    public boolean isDirectAction()
    {
        return directAction;
    }

    /**
     * toString() for Weather
     *
     * @return A String representing the Weather, eg "Clouds"
     */
    @Override
    public String toString()
    {
        return this.name;
    }
}
