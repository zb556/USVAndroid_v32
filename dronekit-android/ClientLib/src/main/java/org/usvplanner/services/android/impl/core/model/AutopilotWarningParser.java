package org.usvplanner.services.android.impl.core.model;

import org.usvplanner.services.android.impl.core.drone.autopilot.MavLinkDrone;

/**
 * Parse received autopilot warning messages.
 */
public interface AutopilotWarningParser {

    String getDefaultWarning();

    String parseWarning(MavLinkDrone drone, String warning);
}
