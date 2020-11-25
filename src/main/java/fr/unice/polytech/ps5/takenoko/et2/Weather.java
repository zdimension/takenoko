package fr.unice.polytech.ps5.takenoko.et2;

public enum Weather
{
    SUN
        {
            @Override
            public String toString()
            {
                return "Sun";
            }
        },
    RAIN(true)
        {
            @Override
            public String toString()
            {
                return "Rain";
            }
        },
    WIND
        {
            @Override
            public String toString()
            {
                return "Wind";
            }
        },
    STORM(true)
        {
            @Override
            public String toString()
            {
                return "Storm";
            }
        },
    CLOUDS(true)
        {
            @Override
            public String toString()
            {
                return "Clouds";
            }
        },
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
