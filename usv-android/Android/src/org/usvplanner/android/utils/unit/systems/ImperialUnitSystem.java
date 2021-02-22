package org.usvplanner.android.utils.unit.systems;

import org.usvplanner.android.utils.unit.providers.area.AreaUnitProvider;
import org.usvplanner.android.utils.unit.providers.area.ImperialAreaUnitProvider;
import org.usvplanner.android.utils.unit.providers.length.ImperialLengthUnitProvider;
import org.usvplanner.android.utils.unit.providers.length.LengthUnitProvider;
import org.usvplanner.android.utils.unit.providers.speed.ImperialSpeedUnitProvider;
import org.usvplanner.android.utils.unit.providers.speed.SpeedUnitProvider;

/**
 * Created by Fredia Huya-Kouadio on 1/20/15.
 */
public class ImperialUnitSystem implements UnitSystem{

    private static final LengthUnitProvider lengthUnitProvider = new ImperialLengthUnitProvider();
    private static final AreaUnitProvider areaUnitProvider = new ImperialAreaUnitProvider();
    private static final SpeedUnitProvider speedUnitProvider = new ImperialSpeedUnitProvider();

    @Override
    public LengthUnitProvider getLengthUnitProvider() {
        return lengthUnitProvider;
    }

    @Override
    public AreaUnitProvider getAreaUnitProvider() {
        return areaUnitProvider;
    }

    @Override
    public SpeedUnitProvider getSpeedUnitProvider() {
        return speedUnitProvider;
    }

    @Override
    public int getType() {
        return IMPERIAL;
    }
}
